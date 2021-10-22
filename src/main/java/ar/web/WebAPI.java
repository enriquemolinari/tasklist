package ar.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import ar.api.Tasks;
import ar.model.Task;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class WebAPI {

  private int webPort;
  private Tasks tasks;
  
  public WebAPI(int webPort, Tasks tasks) {
    this.webPort = webPort;
    this.tasks = tasks;
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
    
    //Token cookie validation
    
//    app.before(ctx -> {
//    String token = ctx.cookie("token");
//
//    try {
//      Algorithm algorithm = Algorithm.HMAC256("secret");
//      JWTVerifier verifier = JWT.require(algorithm).build(); 
//      DecodedJWT jwt = verifier.verify(token);
//      System.out.println(jwt.getClaim("roles"));
//    } catch (JWTVerificationException exception) {
//      throw exception;
//    }
//    });

  }

  private Handler tasks() {
    return ctx -> {
      List<Task> tasks = this.tasks.list(ctx.queryParam("idCreator"));
      List<Map<String, Object>> a = tasks.stream().map((l) -> {
        return l.toMap();
      }).collect(Collectors.toList());
      
      ctx.json(Map.of("result", "success", "tasks", a));
    };
  }
}
