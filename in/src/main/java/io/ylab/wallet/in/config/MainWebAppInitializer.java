package io.ylab.wallet.in.config;

import io.ylab.wallet.in.filter.AuthenticationFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;

/**
 * Initializes application on server startup.
 */
public class MainWebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        var context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfig.class);
        servletContext.addListener(new ContextLoaderListener(context));
        var dispatcherServlet = new DispatcherServlet(context);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        var registration = servletContext.addServlet("mvc", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        var filterRegistration = servletContext.addFilter("authFilter", AuthenticationFilter.class);
        filterRegistration.addMappingForUrlPatterns(null, false, "/*");
    }
}
