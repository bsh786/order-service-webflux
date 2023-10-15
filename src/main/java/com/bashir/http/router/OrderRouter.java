package com.bashir.http.router;

import com.bashir.http.handler.OrderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class OrderRouter {
    @Autowired
    OrderHandler orderHandler;

    @Bean
    public RouterFunction<ServerResponse> getOrderRouter() {
        return RouterFunctions.route()
                .POST("/order", orderHandler::createOrder)
                .GET("/order", orderHandler::getAllOrders)
                .build();
    }


}
