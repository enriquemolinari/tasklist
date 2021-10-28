package ar.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.api.Tasks;
import ar.model.Task;
import io.javalin.Javalin;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Handler;

enum Role implements RouteRole {
  SIMPLE, ADMIN;
}

public class WebAPI {

  private int webPort;
  private Tasks tasks;

  public WebAPI(int webPort, Tasks tasks) {
    this.webPort = webPort;
    this.tasks = tasks;
  }

  public void start() {
    Javalin app = Javalin.create(config -> {
      config.accessManager((handler, ctx, routeRoles) -> {
        
        Token token = new Token("secret", ctx.cookie("token"));
        token.verify();

        // token from user same as idCreator queryParam
        String idCreator = ctx.queryParam("idCreator");
        if (idCreator != null && !idCreator.equals(token.userId().toString())) {
          ctx.status(401).json(Map.of("result", "error", "message", "Unnathorized"));
        }
        
        String userRoles = token.roles();
        if (!routeRoles.contains(Role.valueOf(userRoles))) {
          ctx.status(401).json(Map.of("result", "error", "message", "Unnathorized"));
        }

        handler.handle(ctx);
      });
    }).start(this.webPort);

    app.get("/tasks", tasks(), Role.SIMPLE);

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
      String idCreator = ctx.queryParam("idCreator");

      List<Task> tasks = this.tasks.list(idCreator);

      List<Map<String, Object>> tasksToJson = tasks.stream().map((t) -> {
        return t.toMap();
      }).collect(Collectors.toList());

      ctx.json(Map.of("result", "success", "tasks", tasksToJson));
    };
  }
}
