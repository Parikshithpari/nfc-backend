package com.example.demo.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class CORSConfig 
{
	@Bean
	public CorsConfigurationSource corsConfigurationSource() 
	{
		CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(
	     List.of("https://gconnectt.com/salon")
	    		//List.of("http://127.0.0.1:5500")
	    );
	    configuration.setAllowedMethods(
	      List.of("GET", "POST", "OPTIONS", "PUT", "DELETE")
	    );
	    configuration.setAllowedHeaders(List.of("Authorization",
	    		"Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers")
	    		);
	    configuration.setMaxAge(3600L);
	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
	@Configuration
	public class WebConfig implements WebMvcConfigurer 
	{
	    @Override
	    public void addCorsMappings(CorsRegistry registry) 
	    {
	        registry.addMapping("/**")
	        		.allowedOrigins("https://gconnectt.com/salon")
	        		//.allowedOrigins("http://127.0.0.1:5500")
	                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
	                .allowedHeaders("*")
	                .allowCredentials(true);
	    }
	}
}
