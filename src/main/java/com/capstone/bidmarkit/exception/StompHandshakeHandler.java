package com.capstone.bidmarkit.exception;

import com.capstone.bidmarkit.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandshakeHandler implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                throw new AccessDeniedException("token is null or not started with prefix");
            }

            String token = authorizationHeader.substring(TOKEN_PREFIX.length());

            if (!tokenProvider.validToken(token)) {
                throw new AccessDeniedException("Invalid token");
            }
        }
        return message;
    }
}

