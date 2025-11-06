package co.ucentral.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String privateKey = "test_secret_key";
    private String userGenerator = "backend_user";


    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(this.privateKey))
                    .withIssuer(this.userGenerator)
                    .build()
                    .verify(token);
//            System.out.println("token en el gateway "+ token );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    public Date extractExpiration(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }


}
