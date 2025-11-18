package co.ucentral.gateway.filter;

import co.ucentral.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class CustomFilterFactory extends AbstractGatewayFilterFactory<CustomFilterFactory.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public CustomFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(CustomFilterFactory.Config config) {
        return (exchange, chain) -> {

//            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
//                return chain.filter(exchange);
//            }

            String headerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
            String path =  exchange.getRequest().getPath().value();
            String method = exchange.getRequest().getMethod().toString();
            System.out.println(path + " " +  method);

            if(path.equals("/api/v1/users/create") || path.equals("/api/v1/user/auth/login")){
                return chain.filter(exchange);
            }

            if(headerToken == null){
                return unauthorized(exchange);
            }

            String accessToken = headerToken.substring(7);

            if(!jwtUtil.isTokenValid(accessToken)){
//                System.out.println("token no valido");
                return unauthorized(exchange);
            }

            if (jwtUtil.isTokenValid(accessToken)){
                return chain.filter(exchange);
            }

            return unauthorized(exchange);

        };
    }

    public Mono<Void> unauthorized(ServerWebExchange exchange){
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        String error = "unauthorized";
        DataBuffer buffer = response.bufferFactory().wrap(error.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));

    }


    public static class Config {}


}
