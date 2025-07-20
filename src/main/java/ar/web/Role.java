package ar.web;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
  SIMPLE, ADMIN;
}
