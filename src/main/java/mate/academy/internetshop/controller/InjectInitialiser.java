package mate.academy.internetshop.controller;

import mate.academy.internetshop.lib.Injector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InjectInitialiser implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            Injector.injectDependency();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
