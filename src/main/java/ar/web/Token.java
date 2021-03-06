package ar.web;

import java.util.Base64;

import dev.paseto.jpaseto.Paseto;
import dev.paseto.jpaseto.PasetoException;
import dev.paseto.jpaseto.Pasetos;
import dev.paseto.jpaseto.lang.Keys;

public class Token {

  private Paseto paseto;
  private String pasetoToken;

  public Token(String pasetoToken) {
    if (pasetoToken == null)
      throw new UnnauthorizedException("token cannot be null or empty");
    this.pasetoToken = pasetoToken;
  }

  public Token verify(String base64Secret) {
    try {
      this.paseto = Pasetos.parserBuilder()
          .setSharedSecret(Keys.secretKey(Base64.getDecoder().decode(base64Secret))).build()
          .parse(this.pasetoToken);
      return this;
    } catch (PasetoException ex) {
      throw new UnnauthorizedException(ex);
    }
  }

  String roles() {
    if (this.paseto != null)
      return this.paseto.getClaims().get("roles", String.class);
    
    throw new UnnauthorizedException("There is no token created yet");
  }

  Integer userId() {
    if (this.paseto != null)
      return this.paseto.getClaims().get("id", Integer.class);
    
    throw new UnnauthorizedException("There is no token created yet");
  }

}
