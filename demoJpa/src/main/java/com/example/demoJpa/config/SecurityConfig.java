        package com.example.demoJpa.config;

        import com.example.demoJpa.repository.UserRepository;
        import org.apache.catalina.filters.CorsFilter;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.authentication.AuthenticationManager;
        import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
        import org.springframework.security.config.Customizer;
        import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
        import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
        import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.config.http.SessionCreationPolicy;
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
        import org.springframework.security.web.SecurityFilterChain;
        import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
        import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
        import org.springframework.web.cors.CorsConfiguration;
        import org.springframework.web.cors.CorsConfigurationSource;
        import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

        import java.util.Arrays;

        @Configuration
        @EnableMethodSecurity(prePostEnabled = true)
        @EnableWebSecurity
        public class SecurityConfig {

            @Autowired
            private CustomUserDetailsService customUserDetailsService;

            private final UserRepository userRepository;
            private final JwtTokenFilter jwtTokenFilter;

            public SecurityConfig(UserRepository userRepository, JwtTokenFilter jwtTokenFilter) {
                this.userRepository = userRepository;
                this.jwtTokenFilter = jwtTokenFilter;
            }

            @Bean
            public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
            }

            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                        .authorizeRequests(authorize -> authorize
                                .requestMatchers("/users/login", "/users/register","/api/roles/**").permitAll()
                                .anyRequest().authenticated())
                        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(authenticationProvider())
                        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                        .httpBasic(Customizer.withDefaults())
                        .cors(cors -> cors.configurationSource(corsConfigurationSource()));
                return http.build();
            }

            @Bean
            public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(customUserDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                System.out.println("okefggth");
                return authProvider;
            }

            @Bean
            public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
                return authConfiguration.getAuthenticationManager();
            }
            @Bean
            CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("*")); // Adjust this to your needs
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setExposedHeaders(Arrays.asList("Authorization"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
            }
        }
