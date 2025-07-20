package ar.web;

import io.javalin.security.RouteRole;

import java.util.Set;

public class User {

  private String pasetobase64Token;
  private String base64Secret;

  public User(String token, String base64Secret) {
    this.pasetobase64Token = token;
    this.base64Secret = base64Secret;
  }

  public boolean checkAccess(Set<RouteRole> requiredRoles) {
    Token token = new Token(this.pasetobase64Token).verify(this.base64Secret);

    String userRoles = token.roles();
    return requiredRoles.contains(Role.valueOf(userRoles));
  }

  public String roles() {
    return new Token(this.pasetobase64Token).verify(this.base64Secret).roles();
  }

  public Integer userId() {
    return new Token(this.pasetobase64Token).verify(this.base64Secret).userId();
  }

}