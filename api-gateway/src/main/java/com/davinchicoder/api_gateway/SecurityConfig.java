package com.davinchicoder.api_gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final KeycloakJwtConverter keycloakJwtConverter;

    private final Map<String, Map<HttpMethod, String>> permissions = Map.of(
            "/api/v1/employees/**", Map.of(
                    HttpMethod.GET, "employee.read",
                    HttpMethod.POST, "employee.write",
                    HttpMethod.PUT, "employee.write",
                    HttpMethod.DELETE, "employee.write"
            ),
            "/api/v1/departments/**", Map.of(
                    HttpMethod.GET, "department.read",
                    HttpMethod.POST, "department.write",
                    HttpMethod.PUT, "department.write",
                    HttpMethod.DELETE, "department.write"
            )
    );

    public SecurityConfig(KeycloakJwtConverter keycloakJwtConverter) {
        this.keycloakJwtConverter = keycloakJwtConverter;
    }

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter.toReactive())))
                .authorizeExchange(exchange -> {

                    exchange.pathMatchers("/actuator/health/**").permitAll();

                    permissions.forEach((path, methods) ->
                            methods.forEach((method, authority) ->
                                    exchange.pathMatchers(method, path).hasAuthority(authority)
                            )
                    );

                    exchange.pathMatchers("/api/**").authenticated();

                    exchange.anyExchange().permitAll();
                });

        return http.build();
    }

    @Bean
    public GlobalFilter jwtForwardFilter() {
        return (exchange, chain) -> {
            var token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null) {
                exchange = exchange.mutate()
                        .request(r -> r.header("Authorization", token))
                        .build();
            }
            return chain.filter(exchange);
        };
    }
}
