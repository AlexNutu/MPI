package com.alenut.mpi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


//
//@Configuration
//public class ResourceConfig extends WebMvcConfigurerAdapter {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**")
//                .addResourceLocations("file:ext-resources/")
//                .setCachePeriod(0);
//    }
//}

//@Configuration
//public abstract class AdditionalResourceWebConfiguration implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
//    }
//}

// After that you need add / before images in URL like this: <img th:src="@{'/images/' + ${post.imageName}}"/>
//@Configuration
//public class ResourceConfig extends WebMvcConfigurerAdapter {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
//    }
//}