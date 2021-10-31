package ar.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class Token {

  private DecodedJWT jwt;
  private String token;

  public Token(String token) {
    this.token = token;
  }

  public Token verify(String secret) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm).build();
      this.jwt = verifier.verify(this.token);
      return this;
    } catch (JWTVerificationException e) {
      throw new UnnauthorizedException(e);
    }
  }

  private DecodedJWT decode() {
    if (this.jwt != null)
      return this.jwt;
    
    try {
      this.jwt = JWT.decode(token);
      return this.jwt;
    } catch (JWTDecodeException e) {
      throw new UnnauthorizedException(e);
    }
  }

  public String roles() {
    return this.decode().getClaim("roles").asString();
  }

  public Integer userId() {
    return this.decode().getClaim("id").asInt();
  }

}
