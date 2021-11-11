package ru.levelup.battleship.web_socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/room");
        config.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

//    @Bean
//    public MappingJackson2MessageConverter mappingJackson2MessageConverter(ObjectMapper objectMapper) {
//        MappingJackson2MessageConverter jacksonMessageConverter = new MappingJackson2MessageConverter();
//        jacksonMessageConverter.setObjectMapper(objectMapper);
//        jacksonMessageConverter.setSerializedPayloadClass(String.class);
//        jacksonMessageConverter.setStrictContentTypeMatch(true);
//        return jacksonMessageConverter;
//    }
}