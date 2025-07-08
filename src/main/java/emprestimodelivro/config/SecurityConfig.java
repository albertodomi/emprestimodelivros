package emprestimodelivro.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/index.html").permitAll()
                        .requestMatchers("/login", "/register", "/error", "/auth/login").permitAll()

                        // =========================
                        // ROTAS ADMINISTRATIVAS - SÓ ADMIN
                        // =========================
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/livros/novo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/livros").hasRole("ADMIN")
                        .requestMatchers("/livros/deletar/**").hasRole("ADMIN")
                        .requestMatchers("/livros/atualizar-situacao/**").hasRole("ADMIN")
                        .requestMatchers("/emprestimos/admin/**").hasRole("ADMIN")

                        // =========================
                        // ROTAS DE USUÁRIO COMUM - ADMIN OU CLIENT
                        // =========================
                        .requestMatchers("/usuarioComum/**").hasAnyRole("ADMIN", "CLIENT")
                        .requestMatchers("/emprestimos/solicitar").hasAnyRole("ADMIN", "CLIENT")
                        .requestMatchers("/emprestimos/usuarioComum/**").hasAnyRole("ADMIN", "CLIENT")
                        .requestMatchers("/emprestimos/devolverlivros").hasAnyRole("ADMIN", "CLIENT")
                        .requestMatchers("/livros/buscar").hasAnyRole("ADMIN", "CLIENT")
                        .requestMatchers("/dashboard").hasAnyRole("ADMIN", "CLIENT")
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                "SELECT email, senha, true as enabled FROM usuarios WHERE email = ?");
        manager.setAuthoritiesByUsernameQuery(
                "SELECT email, CONCAT('ROLE_', tipo_usuario) as authority FROM usuarios WHERE email = ?");

        return manager;
    }
}