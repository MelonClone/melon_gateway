package com.devgd.gateway.global.config;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.devgd.gateway.domain.model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	@Autowired
	private JwtAuthenticationConfig jwtConfig;
	private Key secretKey;

	@Bean
	public JwtAuthenticationConfig jwtConfig() {
		return new JwtAuthenticationConfig();
	}

	// 객체 초기화, secretKey를 hmac sha 로 인코딩한다.
	@PostConstruct
	protected void init() {
		this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
	}
	
	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String token) {
		return new UsernamePasswordAuthenticationToken(this.getUserId(token), "", getAuthorities(getUserRole(token)));
	}

	public UserDetails parseUserDetail(String userRole) {
		List<GrantedAuthority> authorities = getAuthorities(userRole);
		
		return new User("", "", authorities);
	}

	public List<GrantedAuthority> getAuthorities(String... roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(Role.valueOf(role).getValue()));
		}

		return authorities;
	}

	public JwtParser getJwtParser() {
		return Jwts.parserBuilder().setSigningKey(secretKey).build();
	}

	// 토큰에서 USER ID 추출
	public String getUserId(final String token) {
		final String userId = getClaims(token).getSubject();
		return userId;
	}

	// 토큰에서 USER 정보 추출
	public Map<String, String> getUserInfo(final String token) {
		final Map<String, String> userInfo = (Map<String, String>) getClaims(token).get("info");
		return userInfo;
	}

	public String getUserRole(final String token) {
		final String userRole = (String) getClaims(token).get("roles");
		return userRole;
	}

	// Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
	public String resolveToken(final HttpServletRequest request) {
		if (request.getHeader("Authorization") != null) return request.getHeader("Authorization").substring("Bearer ".length());
		if (request.getHeader("X-AUTH-TOKEN") != null) return request.getHeader("X-AUTH-TOKEN");

		return "";
	}

	// 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(final String jwtToken) {
		try {
			return !getClaims(jwtToken).getExpiration().before(new Date());
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Claims getClaims(final String jwt) {
		try {
			return getJwtParser().parseClaimsJws(jwt).getBody();
		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
				| IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}