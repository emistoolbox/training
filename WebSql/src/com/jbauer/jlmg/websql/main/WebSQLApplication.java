package com.jbauer.jlmg.websql.main;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import com.jbauer.jlmg.websql.service.ApiSQLServiceImpl;




public class WebSQLApplication extends javax.ws.rs.core.Application {
    
    private Set<Object> singletons = new HashSet<Object>();
    private CorsFilter corsFilter;
    
    public WebSQLApplication(@Context ServletContext servletContext) {
        corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");        
        corsFilter.setAllowedHeaders("Authorization, Content-Type");
    }

    @Override
    public Set<Object> getSingletons() {
        singletons.add(corsFilter);
        singletons.add(new ApiSQLServiceImpl());
        return singletons;
    } 
    
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet();
/*        resources.add(JacksonConfig.class);
        resources.add(SecurityInterceptor.class);
        
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
*/
        return resources;
    }
   
}