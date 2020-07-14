package com.devgd.gateway.global.config;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JwtAuthenticationConfig {

	@Value("${jwt.secret}")
	private String secret;

	// @Value("${shuaicj.security.jwt.url:/login}")
	// private String url;

	// @Value("${shuaicj.security.jwt.header:Authorization}")
	private String header = "Authorization";

	// @Value("${shuaicj.security.jwt.prefix:Bearer}")
	private String prefix = "Bearer";

	// @Value("${shuaicj.security.jwt.expiration:#{24*60*60}}")
	private long expiration = 30 * 60 * 1000L; // 토큰 유효시간 30분
}