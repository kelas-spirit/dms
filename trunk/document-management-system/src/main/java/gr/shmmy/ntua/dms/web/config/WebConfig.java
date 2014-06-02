package gr.shmmy.ntua.dms.web.config;


//package mk.hsilomedus.springsockets.config;

import gr.shmmy.ntua.dms.web.handler.ChatWebSocketHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

@Configuration
@EnableWebMvc
@EnableWebSocket
@ComponentScan(basePackages={"gr.shmmy.ntua.dms.web"})
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	  System.out.println("geia sou webconfig :)");

    registry.addHandler(chatWebSocketHandler(), "/chat").withSockJS();
  }
  
  @Bean
  public WebSocketHandler chatWebSocketHandler() {
	  System.out.println("geia sou webconfig 2 :)");

    return new PerConnectionWebSocketHandler(ChatWebSocketHandler.class);
  }


  // Allow serving HTML files through the default Servlet
  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	  System.out.println("geia sou webconfig 3 :P");

          configurer.enable();
  }
  
  @Bean
	public ViewResolver viewResolver() {
		//logger.log(Level.DEBUG, "setting up view resolver");
		//logger.log(Level.DEBUG, "");

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

}
