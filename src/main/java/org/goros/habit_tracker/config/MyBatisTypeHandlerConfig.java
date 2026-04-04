package org.goros.habit_tracker.config;

import org.apache.ibatis.type.TypeHandlerRegistry;
import org.goros.habit_tracker.repository.typehandler.UUIDTypeHandler;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MyBatisTypeHandlerConfig {

    @Bean
    public ConfigurationCustomizer typeHandlerCustomizer() {
        return configuration -> {
            TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
            registry.register(UUID.class, UUIDTypeHandler.class);
        };
    }
}
