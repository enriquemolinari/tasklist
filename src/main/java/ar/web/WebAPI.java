package ar.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.api.Tasks;
import ar.model.Task;
import ar.model.TaskException;
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
    Javalin app = Javalin.create(config -> {
      config.accessManager((handler, ctx, routeRoles) -> {
        if (!new User(ctx.cookie("token"), routeRoles).checkAccess()) {
          ctx.status(401).json(Map.of("result", "error", "message", "Unnathorized"));
        }
        handler.handle(ctx);
      });
    }).start(this.webPort);

    app.get("/tasks", tasks(), Role.SIMPLE, Role.ADMIN);
    app.post("/tasks", addTasks(), Role.SIMPLE, Role.ADMIN);
    app.delete("/tasks", deleteTasks(), Role.SIMPLE, Role.ADMIN);
    app.put("/tasks/done", taskDone(), Role.SIMPLE, Role.ADMIN);
    app.put("/tasks/inprogress", taskInProgress(), Role.SIMPLE, Role.ADMIN);

    app.exception(UnnauthorizedException.class, (e, ctx) -> {
      ctx.status(401);
      ctx.json(Map.of("result", "error", "message", e.getMessage()));
      // log error in a stream...
    });

    app.exception(TaskException.class, (e, ctx) -> {
      ctx.json(Map.of("result", "error", "message", e.toMap()));
      // log error in a stream...
    });

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.json(Map.of("result", "error", "message", "Ups... algo se rompió.: " + e.getMessage()));
      // log error in a stream...
    });
  }

  private Handler deleteTasks() {
    return ctx -> {
      TaskDto dto = ctx.bodyAsClass(TaskDto.class);

      this.tasks.deleteTask(new Token(ctx.cookie("token")).userId().toString(), dto.getIdTask());
      ctx.json(Map.of("result", "success"));
    };
  }

  private Handler taskDone() {
    return ctx -> {
      TaskDto dto = ctx.bodyAsClass(TaskDto.class);

      this.tasks.done(new Token(ctx.cookie("token")).userId().toString(), dto.getIdTask());
      ctx.json(Map.of("result", "success"));
    };
  }

  private Handler taskInProgress() {
    return ctx -> {
      TaskDto dto = ctx.bodyAsClass(TaskDto.class);

      this.tasks.inProgress(new Token(ctx.cookie("token")).userId().toString(), dto.getIdTask());
      ctx.json(Map.of("result", "success"));
    };
  }

  private Handler addTasks() {
    return ctx -> {
      TaskDto dto = ctx.bodyAsClass(TaskDto.class);

      this.tasks.addTask(new Token(ctx.cookie("token")).userId().toString(), dto.getTaskText(),
          dto.getExpirationDate());
      ctx.json(Map.of("result", "success"));
    };
  }

  private Handler tasks() {
    return ctx -> {

      List<Task> tasks = this.tasks.list(new Token(ctx.cookie("token")).userId().toString());

      List<Map<String, Object>> tasksToJson = tasks.stream().map((t) -> {
        return t.toMap();
      }).collect(Collectors.toList());

      ctx.json(Map.of("result", "success", "tasks", tasksToJson));
    };
  }
}
