package com.devgd.gateway.global.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		if (token != null && !token.equals("") && jwtTokenProvider.validateToken(token)) {
			SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
		}
		
		chain.doFilter(request, response);
	}
}