package gpse.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.*;

import java.util.Arrays;

/**
 * SecurityConfig.
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableConfigurationProperties(SecurityConstants.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final SecurityConstants securityConstants;

    /**
     * Sets securityConstant and userDetailService.
     *
     * @param userDetailsService the userDetailsService
     * @param securityConstants  the securityConstants
     */
    @Autowired
    public SecurityConfig(final UserDetailsService userDetailsService, final SecurityConstants securityConstants) {
        super();
        this.userDetailsService = userDetailsService;
        this.securityConstants = securityConstants;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityConstants))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService, securityConstants))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Returns Source.
     *
     * @return source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "PATCH", "DELETE"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}

