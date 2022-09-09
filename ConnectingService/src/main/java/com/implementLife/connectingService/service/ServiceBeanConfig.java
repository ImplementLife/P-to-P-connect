package com.implementLife.connectingService.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class ServiceBeanConfig {
    @Bean
    public RoomService getRoomService() {
        RoomService original = new InMemoryService();
        LogProxy handler = new LogProxy(original);
        return (RoomService) Proxy.newProxyInstance(
            RoomService.class.getClassLoader(),
            new Class[] { RoomService.class },
            handler);
    }
}
