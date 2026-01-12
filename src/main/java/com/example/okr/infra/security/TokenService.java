package com.example.okr.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.okr.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

//en esta clase generamos los servicios de jwt
@Service
public class TokenService {
    @Value("{api.security.secret}")
    private String apiSecret;

    public String generateToken(User user)
    {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("OKRAPI")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getUser_id())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error al generar el token", e);
        }
    }

    public String getSubject(String token) {
        if(token == null || token.isBlank()) {
            throw new RuntimeException("Token es nulo o vacío");
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    .withIssuer("OKRAPI")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException e) {
            System.out.println(e.toString());
        }
        if(verifier.getSubject()==null) {
            throw new RuntimeException("Token no contiene un sujeto válido");
        }

        return verifier.getSubject();
    }
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

 /**   public String[] decodeToken(String token) {
        DecodedJWT decodedJWT = null;
        System.out.println("Decoding token: " + token);
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            System.out.println("Using algorithm with secret: " + apiSecret);
            decodedJWT = JWT.require(algorithm)
                    .withIssuer("OKRAPI")
                    .build()
                    .verify((String)token);
            System.out.println("Token successfully decoded.");
        } catch (JWTVerificationException e) {
            System.out.println(e.toString());
        }

        String userId = decodedJWT.getClaim("id").toString();
        System.out.println("userId from token: " + decodedJWT.getClaim("id").toString());
        String username = decodedJWT.getSubject();

        return new String[]{userId, username};
    } **/

 //este metodo sirve para obtener el usuario y id del token del usuario
 public String[] decodeToken(String JWTtoken)
 {
     String[] userDecode = new String[2];

     String[] parts = JWTtoken.split("\\."); //

     Base64.Decoder decoder = Base64.getUrlDecoder();

     String payloadJson = new String(decoder.decode(parts[1]));


     String[] bodyParts = payloadJson.replace('}',' ').trim().split(",");

     userDecode[0]= returnUsername(bodyParts[0]);

     userDecode[1]= returnUserId(bodyParts[2]);

     return userDecode;
 }
    public String returnUsername(String correo)
    {
        String[] username = correo.split(":");
        return username[1];
    }
    public String returnUserId(String id)
    {
        String[] userId = id.split(":");
        return userId[1];
    }
}
