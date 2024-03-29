        package com.example.demoJpa.config;

        import com.example.demoJpa.repository.UserRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.http.HttpMethod;
        import org.springframework.security.authentication.AuthenticationManager;
        import org.springframework.security.config.Customizer;
        import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
        import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
        import org.springframework.security.web.SecurityFilterChain;
        import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

        @Configuration
            @EnableMethodSecurity(prePostEnabled = true)
            @EnableWebSecurity
            public class SecurityConfig {
                @Autowired
                private CustomUserDetailsService customUserDetailsService;

                private final UserRepository userRepository;

                public SecurityConfig(UserRepository userRepository) {
                    this.userRepository = userRepository;
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
                                    // .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ROLE_ADMIN_READ", "ROLE_USER_READ")
                                    .anyRequest().authenticated()
                            )
                            .httpBasic(Customizer.withDefaults());
                    return http.build();
                }


                /*         @Bean
                public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http.csrf(AbstractHttpConfigurer::disable)
                            .authorizeRequests(authorize -> {
                                authorize
                                        .antMatchers("/register", "/login").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/users").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/users").hasAnyAuthority(ADMIN_CREATE.name())
                                        .requestMatchers(HttpMethod.PUT, "/users").hasAnyAuthority(ADMIN_UPDATE.name())
                                        .requestMatchers(HttpMethod.DELETE, "/users").hasAnyAuthority(MANAGER_DELETE.name())
                                        .anyRequest().authenticated();
                            })
                            .formLogin(formLogin -> formLogin
                                    .loginPage("/login")
                                    .defaultSuccessURL("/", true)
                                    .permitAll())
                            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessURL("/login").permitAll())
                            .httpBasic(Customizer.withDefaults());
                    return http.build();
                }*/
                @Bean
                public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
                    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
                    authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
                    return authenticationManagerBuilder.build();
                }
            }
