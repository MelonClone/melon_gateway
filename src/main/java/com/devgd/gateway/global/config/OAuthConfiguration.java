package com.devgd.gateway.global.config;

import com.devgd.gateway.domain.model.Role;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;


/**
 * The Class OAuthConfiguration that sets up the OAuth2 single sign on
 * configuration and the web security associated with it.
 */
@Configuration
@RequiredArgsConstructor
public class OAuthConfiguration extends WebSecurityConfigurerAdapter  {
	
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void configure(WebSecurity web) throws Exception {
		//Allow access to all static resources without authentication
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
	}

	/**
	 * Define the security that applies to the proxy
	*/
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
		.authorizeRequests()
			.antMatchers("/v1/admin/user/**",
						"/v1/produce/{\\d+}/**")
						.hasRole(Role.ADMIN.name())
			.antMatchers("/v1/artist_manage/{\\d+}/**")
						.hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())
			.antMatchers(HttpMethod.POST, "/v1/album/**")
						.hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())
			.antMatchers(HttpMethod.PUT, "/v1/album/**")
						.hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())
			.antMatchers(HttpMethod.DELETE, "/v1/album/**")
						.hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())
			.antMatchers("/v1/artist/{\\d+}/**",
						"/v1/playlist/{\\d+}/**",
						"/v1/album/{\\d+}/**",
						"/v1/music/{\\w+}/**")
						.hasAnyRole(Role.MEMBER.name(), Role.ARTIST.name(), Role.ADMIN.name())
			.antMatchers("/v1/search/{\\d+}/**",
						"/v1/user/{\\d+}/**")
						.permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin().disable()
		.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
	}
}
