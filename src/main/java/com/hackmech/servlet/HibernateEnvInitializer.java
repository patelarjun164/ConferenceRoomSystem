package com.hackmech.servlet;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class HibernateEnvInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Set system properties from environment variables
        System.setProperty("AIVEN_USERNAME", System.getenv("AIVEN_USERNAME"));
        System.setProperty("AIVEN_PASSWORD", System.getenv("AIVEN_PASSWORD"));
    }
}
