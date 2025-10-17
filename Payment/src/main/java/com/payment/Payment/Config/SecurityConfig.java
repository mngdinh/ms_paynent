package com.payment.Payment.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(prePostEnabled = true)
@CrossOrigin(origins = {"http://localhost:5173", "exp://192.168.90.68:8081"})
public class SecurityConfig {

    @Value("${jwt.signerKey}") //Đọc từ file application.yaml
    private String signerKey;

    @Autowired
    @Lazy
    private CustomJwtDecoder customJwtDecoder;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
//                request.anyRequest().authenticated());
                request.anyRequest().permitAll());

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler));

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<GrantedAuthority> authorities = new ArrayList<>();

            // Lấy claim "role"
            String role = jwt.getClaimAsString("role");
            if (role != null && !role.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }

            System.out.println("✅ Extracted authorities from JWT: " + authorities);

            return authorities;
        });
        return converter;
    }

    //CORS handling
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173",
                "https://localhost:5173",
                "https://score-lens.vercel.app",
                "https://scorelens.onrender.com",
                "https://scorelens-gateway.onrender.com",
                "https://localhost:7174",
                "http://localhost:7174"
        ));

        // Thêm pattern cho mobile apps
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(
                "exp://*",           // Expo mobile apps
                "capacitor://*",     // Capacitor apps
                "ionic://*",         // Ionic apps
                "file://*",          // Local file protocol
                "*://192.168.*.*:*", // Local network IPs
                "*://10.*.*.*:*",    // Private network IPs
                "*://172.*.*.*:*"    // Private network IPs
        ));
        //corsConfiguration.addAllowedOriginPattern("*"); // mở rộng cho tất cả các port localhost
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource url = new UrlBasedCorsConfigurationSource();
        url.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(url);
    }

}
