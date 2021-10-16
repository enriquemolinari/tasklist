package ar.web;

import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class WebAPI {

  private int webPort;

  public WebAPI(int webPort) {
    this.webPort = webPort;
  }

  public void start() {
    Javalin app = Javalin.create().start(this.webPort);
    app.get("/tasks", tasks());

    app.exception(RuntimeException.class, (e, ctx) -> {
      ctx.status(401);
      ctx.json(Map.of("result", "error", "message", e.getMessage()));
      // log error in a stream...
    });

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.json(Map.of("result", "error", "message", "Ups... algo se rompió.: " + e.getMessage()));
      // log error in a stream...
    });
  }

  private Handler tasks() {
    return ctx -> {
//      LoginForm form = ctx.bodyAsClass(LoginForm.class);
//
//      String token = userAuth.authenticate(form.getUser(), form.getPass())
//          .orElseThrow(() -> new RuntimeException("Invalid username or password"));

      // TODO: add secure for PROD
      // ctx.res.setHeader("Set-Cookie", "token=" + token + ";" + "HttpOnly;
      // SameSite=strict");

      String token = ctx.cookie("token");

      try {
        Algorithm algorithm = Algorithm.HMAC256("secret3");
        JWTVerifier verifier = JWT.require(algorithm).build(); 
        DecodedJWT jwt = verifier.verify(token);
        System.out.println(jwt.getClaim("roles"));
      } catch (JWTVerificationException exception) {
        throw exception;
      }

      System.out.println(token);
      ctx.json(Map.of("result", "success"));
    };
  }
}
