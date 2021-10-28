package ar.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class Token {

  private String secret;
  private DecodedJWT jwt;
  private String token;

  public Token(String secret, String token) {
    this.secret = secret;
    this.token = token;
  }

  public void verify() {
    try {
      Algorithm algorithm = Algorithm.HMAC256(this.secret);
      JWTVerifier verifier = JWT.require(algorithm).build();
      this.jwt = verifier.verify(this.token);
    } catch (JWTVerificationException e) {
      throw new RuntimeException(e);
    }
  }

  public String roles() {
    return jwt.getClaim("roles").asString();
  }

  public Integer userId() {
    return jwt.getClaim("id").asInt();
  }
    
}
