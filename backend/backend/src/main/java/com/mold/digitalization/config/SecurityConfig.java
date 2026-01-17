package com.mold.digitalization.config;

import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private DevTokenAuthenticationFilter devTokenAuthenticationFilter;

    @Value("${app.security.dev.bypass.processStateMachine:false}")
    private boolean devBypassProcessStateMachine;

    @Value("${app.security.dev.bypass.storageRecords:false}")
    private boolean devBypassStorageRecords;

    @Value("${app.security.dev.bypass.initialParameters:false}")
    private boolean devBypassInitialParameters;
    
    @Value("${app.security.dev.bypass.consumables:false}")
    private boolean devBypassConsumables;

    @Value("${app.security.dev.bypass.tuningRecords:false}")
    private boolean devBypassTuningRecords;

    @Value("${app.security.dev.bypass.roles:false}")
    private boolean devBypassRoles;
    
    @Value("${app.security.dev.bypass.departments:false}")
    private boolean devBypassDepartments;
    
    private boolean devBypassUsers = true;

    @Value("${app.security.dev.token.enabled:false}")
    private boolean devTokenEnabled;

    @Value("${app.cors.allowed-origins:}")
    private String corsAllowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(registry -> {
            registry.requestMatchers(
                    "/v1/api/auth/login",
                    "/v1/api/auth/refresh-token",
                    "/auth/login",
                    "/auth/refresh-token",
                    "/api/auth/login",
                    "/api/auth/refresh-token",
                    "/api/departments/**",
                    "/api/users/**",
                    "/api/roles/**",
                    "/api/user-roles/**",
                    "/api/health",
                    "/api/dev-config/**",
                    "/actuator/health",
                    "/actuator/info",
                    "/temp/**",
                    "/v1/api/auth/forgot-password",
                    "/v1/api/auth/validate-reset-code/**",
                    "/v1/api/auth/reset-password-by-code",
                    "/api/process-preset/**",
                    "/api/process-preset"
            ).permitAll();

            if (devBypassProcessStateMachine) {
                registry.requestMatchers(HttpMethod.GET, "/api/process-state-machine/**").permitAll();
            }

            if (devBypassStorageRecords) {
                registry.requestMatchers(HttpMethod.GET, "/api/mold/storage/**").permitAll();
            }

            if (devBypassInitialParameters) {
                registry.requestMatchers(HttpMethod.GET, "/api/mold-initial-parameters/**").permitAll();
            }

            if (devBypassConsumables) {
                registry.requestMatchers(HttpMethod.GET, "/api/consumables/**").permitAll();
            }

            if (devBypassRoles) {
                registry.requestMatchers(HttpMethod.GET, "/api/roles/**").permitAll();
            }

            if (devBypassDepartments) {
                registry.requestMatchers(HttpMethod.GET, "/api/departments/**").permitAll();
            }

            if (devBypassTuningRecords) {
                registry.requestMatchers(HttpMethod.GET, "/api/tuning-records/**").permitAll();
            }

            if (devBypassUsers) {
                registry.requestMatchers(HttpMethod.GET, "/api/users", "/api/users/**").permitAll();
            }
            
            if (devBypassRoles) {
                registry.requestMatchers(HttpMethod.GET, "/api/user-roles/**").permitAll();
            }

            String[] writeRoles = devTokenEnabled
                    ? new String[]{"ADMIN","SUPER_ADMIN","DEV"}
                    : new String[]{"ADMIN","SUPER_ADMIN"};
            
            registry.requestMatchers(HttpMethod.POST,
                    "/api/molds/**",
                    "/api/mold-archive/**",
                    "/api/mold-types/**",
                    "/api/mold-statuses/**",
                    "/api/mold-initial-parameters/**",
                    "/api/products/**",
                    "/api/departments/**"
            ).hasAnyRole(writeRoles);
            registry.requestMatchers(HttpMethod.PUT,
                    "/api/molds/**",
                    "/api/mold-archive/**",
                    "/api/mold-types/**",
                    "/api/mold-statuses/**",
                    "/api/mold-initial-parameters/**",
                    "/api/products/**",
                    "/api/departments/**"
            ).hasAnyRole(writeRoles);
            registry.requestMatchers(HttpMethod.DELETE,
                    "/api/molds/**",
                    "/api/mold-archive/**",
                    "/api/mold-types/**",
                    "/api/mold-statuses/**",
                    "/api/mold-initial-parameters/**",
                    "/api/products/**",
                    "/api/departments/**"
            ).hasAnyRole(writeRoles);
            registry.requestMatchers(HttpMethod.POST,
                    "/api/users/**",
                    "/api/roles/**",
                    "/api/user-roles/**"
            ).hasAnyRole(writeRoles);
            registry.requestMatchers(HttpMethod.PUT,
                    "/api/users/**",
                    "/api/roles/**",
                    "/api/user-roles/**"
            ).hasAnyRole(writeRoles);
            registry.requestMatchers(HttpMethod.DELETE,
                    "/api/users/**",
                    "/api/roles/**",
                    "/api/user-roles/**"
            ).hasAnyRole(writeRoles);

            registry.requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs", "/webjars/**", "/doc.html", "/favicon.ico", "/v3/api-docs/**", "/knife4j/**").permitAll();

            registry.anyRequest().authenticated();
        });

        http.addFilterBefore(devTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(handling -> handling
            .authenticationEntryPoint((request, response, authException) -> {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\": 401, \"message\": \"未授权访问\"}");
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"code\": 403, \"message\": \"拒绝访问\"}");
            }));
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        if (corsAllowedOrigins != null && !corsAllowedOrigins.trim().isEmpty()) {
            String[] origins = corsAllowedOrigins.split(",");
            for (String origin : origins) {
                String o = origin.trim();
                if (!o.isEmpty()) {
                    configuration.addAllowedOrigin(o);
                }
            }
        } else {
            configuration.addAllowedOrigin("http://localhost:5172");
            configuration.addAllowedOrigin("http://127.0.0.1:5172");
            configuration.addAllowedOrigin("http://localhost:5173");
            configuration.addAllowedOrigin("http://127.0.0.1:5173");
            configuration.addAllowedOrigin("http://localhost:5174");
            configuration.addAllowedOrigin("http://127.0.0.1:5174");
            configuration.addAllowedOrigin("http://localhost:5175");
            configuration.addAllowedOrigin("http://127.0.0.1:5175");
            configuration.addAllowedOrigin("http://localhost:4173");
            configuration.addAllowedOrigin("http://127.0.0.1:4173");
        }
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
