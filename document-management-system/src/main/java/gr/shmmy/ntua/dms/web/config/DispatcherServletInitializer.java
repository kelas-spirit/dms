package gr.shmmy.ntua.dms.web.config;



import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
	  System.out.println("geia sou dispetcherServlet :)");

          return null;
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
	  System.out.println("geia sou dispetcherServlet :)2");

          return new Class<?>[] { WebConfig.class };
  }

  @Override
  protected String[] getServletMappings() {
	  System.out.println("geia sou dispetcherServlet :)3");

          return new String[] { "/" };
  }

  @Override
  protected void customizeRegistration(Dynamic registration) {
	  System.out.println("geia sou dispetcherServlet :)4");

          registration.setInitParameter("dispatchOptionsRequest", "true");
  }

}