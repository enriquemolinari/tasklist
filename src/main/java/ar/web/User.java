package ar.web;

import java.util.Set;

public class User {

  private static final String JWT_SECRET = "secret";
  private String token;
  private Set requiredRoles;

  public User(String token, Set routeRoles) {
    this.token = token;
    this.requiredRoles = routeRoles;
  }

  public boolean checkAccess() {
    Token token = new Token(this.token).verify(JWT_SECRET);

    String userRoles = token.roles();
    return requiredRoles.contains(Role.valueOf(userRoles));
  }
}