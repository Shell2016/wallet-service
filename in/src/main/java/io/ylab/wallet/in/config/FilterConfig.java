package io.ylab.wallet.in.config;

import io.ylab.wallet.domain.security.JwtHandler;
import io.ylab.wallet.in.filter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Filters configuration class.
 */
@Configuration
public class FilterConfig {
    /**
     * Registers custom AuthenticationFilter into filter chain.
     */
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistrationBean(JwtHandler jwtHandler) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter(jwtHandler));
        registrationBean.addUrlPatterns("/users/*");
        return registrationBean;
    }
}
