package com.portfolio.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;

@SpringBootTest
class RouteLocatorTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void routesAreRegistered() {
        Set<String> ids = routeLocator.getRoutes()
            .map(route -> route.getId())
            .collect(Collectors.toSet())
            .block();

        assertThat(ids).containsExactlyInAnyOrder("catalog", "users", "orders");
    }
}
