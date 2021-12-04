package ar.web;

import java.util.Set;

import io.javalin.core.security.RouteRole;

public class User {

  private static final String JWT_SECRET = "secret";
  private String jwtbase64Token;
  private Set<RouteRole> requiredRoles;

  public User(String token, Set<RouteRole> routeRoles) {
    this.jwtbase64Token = token;
    this.requiredRoles = routeRoles;
  }

  public boolean checkAccess() {
    Token token = new Token(this.jwtbase64Token).verify(JWT_SECRET);

    String userRoles = token.roles();
    return requiredRoles.contains(Role.valueOf(userRoles));
  }
}