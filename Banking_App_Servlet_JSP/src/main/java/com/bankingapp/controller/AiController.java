package com.bankingapp.controller;

import com.bankingapp.model.User;
import com.bankingapp.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@WebServlet("/admin/ai")
public class AiController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();
    private final Gson gson = new Gson();
    private String apiKey;

    @Override
    public void init() throws ServletException {
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new ServletException("Cannot find config.properties file in WEB-INF");
            }
            prop.load(input);
            apiKey = prop.getProperty("GEMINI_API_KEY");
            if (apiKey == null || apiKey.trim().isEmpty()) {
                 throw new ServletException("GEMINI_API_KEY not set in config.properties");
            }
        } catch (IOException e) {
            throw new ServletException("Could not load API key from config.properties", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userMessage = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            List<User> allUsers = userService.getAllUsersForAiContext();
            String userDataAsCsv = formatUsersAsCsv(allUsers);

            String prompt = "You are 'Bank AI', a helpful assistant for a bank administrator. " +
                          "Analyze the following customer data, provided in CSV format, to answer the user's question. " +
                          "The columns are: ID, FirstName, LastName, Email, Role, AccountNumber, Balance, IsActive.\n\n" +
                          "--- CUSTOMER DATA ---\n" +
                          userDataAsCsv +
                          "\n--- END DATA ---\n\n" +
                          "User's Question: " + userMessage;

            String geminiResponse = callGeminiApi(prompt);

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(Map.of("reply", geminiResponse)));

        } catch (SQLException | InterruptedException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Error processing your request.")));
            e.printStackTrace();
        }
    }

    private String formatUsersAsCsv(List<User> users) {
        StringBuilder csv = new StringBuilder("ID,FirstName,LastName,Email,Role,AccountNumber,Balance,IsActive\n");
        for (User user : users) {
             csv.append(String.format("%d,%s,%s,%s,%s,%s,%.2f,%b\n",
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getAccountNumber() != null ? user.getAccountNumber() : "N/A",
                user.getBalance(),
                user.isActive()
            ));
        }
        return csv.toString();
    }

    private String callGeminiApi(String prompt) throws IOException, InterruptedException {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;
        
        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(textPart));
        Map<String, Object> requestBodyMap = Map.of("contents", List.of(content));
        String requestBody = gson.toJson(requestBodyMap);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        try {
            // Define a generic type for Gson to parse into a Map
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseMap = gson.fromJson(httpResponse.body(), type);

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return "The AI model returned no candidates.";
            }
            
            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            if (contentMap == null) {
                return "The AI response is missing the 'content' part.";
            }
            
            List<Map<String, String>> parts = (List<Map<String, String>>) contentMap.get("parts");
            if (parts == null || parts.isEmpty()) {
                return "The AI response is missing the 'parts' section.";
            }
            
            return parts.get(0).get("text");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to parse Gemini response: " + httpResponse.body());
            return "I couldn't process the response from the AI model.";
        }
    }
}