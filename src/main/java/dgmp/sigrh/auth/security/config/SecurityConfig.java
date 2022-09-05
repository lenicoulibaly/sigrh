package dgmp.sigrh.auth.security.config;

import dgmp.sigrh.auth.security.filters.ContextFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig
{
    private final ContextFilter contextFilter;
    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception
    {
        return http
                .csrf().ignoringAntMatchers("/h2console/**")
                .and()
                .headers().frameOptions().disable()
                .and()
                .formLogin().loginPage("/login")

                .and()
                .addFilterAfter(contextFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .mvcMatchers("/login", "/h2console/**", "/h2console", "/resources/**", "/static/**").permitAll()
                .mvcMatchers("/sigrh/**").authenticated()
                //.anyRequest().authenticated()  /h2console
                .and()
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().mvcMatchers("/resources/**", "/static/**", "/h2console/**");
    }
}