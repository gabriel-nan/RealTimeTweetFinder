package com.gabinan.rttweetfinder;


import java.util.Arrays;
import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;




/**
 * Spring Boot FTW
 * 
 * This app connects to a twitter stream and returns real time tweets filtered by 
 * clients through a REST API.
 * 
 * @author Gabriel Nan
 */
@SpringBootApplication
@Log4j
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ApplicationContext ctx = SpringApplication.run(App.class, args);
        
        
        //Let's print out all the beans created by this context
        String[] beans = ctx.getBeanDefinitionNames();
        Arrays.sort(beans);
        
        myLogger.info("Initialized Beans: ");
        for(String bean : beans){
           myLogger.info(bean);
        }
        
        
    }
    
}
