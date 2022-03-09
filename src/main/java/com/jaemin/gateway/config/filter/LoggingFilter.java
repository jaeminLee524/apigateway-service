package com.jaemin.gateway.config.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(LoggingFilter.Config config) {
//         Custom Pre Filter
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("GlobalFilter baseMessage: {}", config.getBaseMessage());
//
//            if (config.isPreLogger()) {
//                log.info("GlobalFilter start: request id -> {}", request.getId());
//            }
//
//            // Custom Post Filter
//            // Mono : webflux(added after spring5) 비동기방식으로 단일값 전달할때 Mono타입으로 전달
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                if (config.isPostLogger()) {
//                    log.info("GlobalFilter End: response Code -> {}", response.getStatusCode());
//                }
//
//            }));
            GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {

                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();

                log.info("GlobalFilter baseMessage: {}", config.getBaseMessage());

                if (config.isPreLogger()) {
                    log.info("GlobalFilter start: request id -> {}", request.getId());
                }

                // Custom Post Filter
                // Mono : webflux(added after spring5) 비동기방식으로 단일값 전달할때 Mono타입으로 전달
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    if (config.isPostLogger()) {
                        log.info("GlobalFilter End: response Code -> {}", response.getStatusCode());
                    }
                }));
            }, Ordered.HIGHEST_PRECEDENCE); // 실행순서를 가장 높게

        return filter;
        }


    @Data
    public static class Config {
        // put the configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
