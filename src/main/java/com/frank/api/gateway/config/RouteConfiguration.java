package com.frank.api.gateway.config;

import com.frank.api.gateway.filter.InitFilter;
import com.frank.api.gateway.filter.ReqValidCheckFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator apiServiceRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
            .route(r -> r.path("/apiservice/**")
                         .filters(f -> f.stripPrefix(1)
                                        .filters(new InitFilter(),new ReqValidCheckFilter()))
                         .uri("http://localhost:20000")
                         .order(0)
                         .id("api")
            )
            .build();
    }

}
