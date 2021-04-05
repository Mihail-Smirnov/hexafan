package com.msmir.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Value("${upload.path}")
  private String uploadPath;

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("index");
    registry.addViewController("/login").setViewName("login");
    registry.addViewController("/game").setViewName("game");
    registry.addViewController("/registration").setViewName("registration");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/css/**", "/js/**", "/lib/**", "/img/**", "/uploads/**").
        addResourceLocations("classpath:/static/css/", "classpath:/static/js/",
            "classpath:/static/lib/", "classpath:/static/img/", "file:" + uploadPath + "/");
  }
}
