package com.gabinan.rttweetfinder.config;

import com.gabinan.rttweetfinder.subscribers.RxSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration to create the config and subscriber beans
 * at runtime.
 * 
 * @author Gabriel Nan
 */

@Configuration
public class BeanConfiguration {
    
    
    @Bean
    public GlobalConfig globalConfig(){
        return new GlobalConfig();
    }
    
    @Bean
    public RxSubscriber rxSubscriber(){
        return new RxSubscriber(globalConfig());
    }
    
}
