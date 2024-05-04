package com.capstone.bidmarkit.config;

import com.capstone.bidmarkit.config.jwt.LoginFilter;
import com.capstone.bidmarkit.config.jwt.TokenAuthenticationFilter;
import com.capstone.bidmarkit.config.jwt.TokenProvider;
import com.capstone.bidmarkit.service.MemberDetailService;
import com.capstone.bidmarkit.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(auth -> auth.disable())
                .csrf(auth -> auth.disable())
                .formLogin(auth -> auth.disable())
                .httpBasic(auth -> auth.disable())
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/member", "/accessToken").permitAll()
//                        .requestMatchers("/test").hasRole("MEMBER")
//                        .anyRequest().authenticated())
                        .anyRequest().permitAll())
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), LoginFilter.class)
                .addFilterAt(
                        new LoginFilter(authenticationManager(authenticationConfiguration), tokenProvider, refreshTokenService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
