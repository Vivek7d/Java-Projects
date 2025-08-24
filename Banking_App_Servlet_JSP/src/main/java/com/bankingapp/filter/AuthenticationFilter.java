package com.bankingapp.filter;

import com.bankingapp.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

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
		String aiServletURI = httpRequest.getContextPath() + "/admin/ai";

		boolean loggedIn = (session != null && session.getAttribute("user") != null);
		User user = loggedIn ? (User) session.getAttribute("user") : null;

		boolean isLoginRequest = requestURI.equals(loginURI);
		boolean isAuthRequest = requestURI.startsWith(authServletURI);
		boolean isAiRequest = requestURI.startsWith(aiServletURI);

		
		if (isLoginRequest || isAuthRequest || loggedIn) {
			if (isAiRequest && (user == null || !"Admin".equalsIgnoreCase(user.getRole()))) {
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

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
