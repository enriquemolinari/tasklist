package ar.web;

import io.javalin.core.security.RouteRole;

public enum Role implements RouteRole {
  SIMPLE, ADMIN;
}
