package com.bankingapp.filter;

import com.bankingapp.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// This filter is mapped to "/*", meaning it will intercept EVERY request to the application.
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);

		String requestURI = httpRequest.getRequestURI();
		String loginURI = httpRequest.getContextPath() + "/login.jsp";
		String authServletURI = httpRequest.getContextPath() + "/auth";
		// --- ADD a URI for our new AI servlet ---
		String aiServletURI = httpRequest.getContextPath() + "/admin/ai";

		boolean loggedIn = (session != null && session.getAttribute("user") != null);
		User user = loggedIn ? (User) session.getAttribute("user") : null;

		boolean isLoginRequest = requestURI.equals(loginURI);
		boolean isAuthRequest = requestURI.startsWith(authServletURI);
		// --- ADD a check for the AI request ---
		boolean isAiRequest = requestURI.startsWith(aiServletURI);

		// --- UPDATED LOGIC ---
		// Allow the request if:
		// 1. The user is trying to log in or out.
		// 2. The user is logged in.
		// 3. The request is for the AI servlet AND the logged-in user is an Admin.
		if (isLoginRequest || isAuthRequest || loggedIn) {
			if (isAiRequest && (user == null || !"Admin".equalsIgnoreCase(user.getRole()))) {
				// Special case: Block non-admins from accessing the AI servlet
				System.out.println("Non-admin access blocked for AI servlet: " + requestURI);
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
				return;
			}
			// User is allowed, so continue the request chain
			chain.doFilter(request, response);
		} else {
			// User is not logged in and trying to access a protected page. Redirect them.
			System.out.println("Unauthorized access blocked for: " + requestURI);
			httpResponse.sendRedirect(loginURI);
		}
	}

	// You can leave init() and destroy() methods empty for this implementation
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}