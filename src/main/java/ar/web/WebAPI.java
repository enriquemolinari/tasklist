package ar.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.api.BulkData;
import ar.api.Tasks;
import ar.model.Task;
import ar.model.TaskException;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class WebAPI {
  private static final String ORIGIN = "https://web-epm.loca.lt";
  private static final String JSON_SUCCESS = "success";
  private static final String JSON_ERROR = "error";
  private static final String TASKS_ENDPOINT = "/tasks";
  private static final String JSON_RESULT = "result";
  private static final String JSON_MESSAGE = "message";
  private static final String TOKEN_COOKIE_NAME = "token";
  private int webPort;
  private Tasks tasks;
  private String base64Secret;
  private Boolean localTunnel;

  public WebAPI(int webPort, Boolean localTunnel, Tasks tasks, String base64Secret) {
    this.webPort = webPort;
    this.tasks = tasks;
    this.base64Secret = base64Secret;
    this.localTunnel = localTunnel;
  }

  public void start() {
    Javalin app = Javalin.create(config -> {
      if (localTunnel) {
        config.bundledPlugins.enableCors(cors -> {
          cors.addRule(it -> {
            it.allowHost(ORIGIN);
          });
        });
      }
    }).start(this.webPort);

    app.beforeMatched(ctx -> {
      var user = new User(ctx.cookie(TOKEN_COOKIE_NAME), base64Secret);
      if (!user.checkAccess(ctx.routeRoles())) {
        ctx.status(401).json(Map.of(JSON_RESULT, JSON_ERROR, JSON_MESSAGE, "Unnathorized"));
      }
    });

    app.get(TASKS_ENDPOINT, tasks(), Role.SIMPLE, Role.ADMIN);
    app.post(TASKS_ENDPOINT, addTasks(), Role.SIMPLE, Role.ADMIN);
    app.delete(TASKS_ENDPOINT, deleteTasks(), Role.SIMPLE, Role.ADMIN);
    app.put(TASKS_ENDPOINT + "/done", taskDone(), Role.SIMPLE, Role.ADMIN);
    app.put(TASKS_ENDPOINT + "/inprogress", taskInProgress(), Role.SIMPLE, Role.ADMIN);

    // sync endpoint
    app.post(TASKS_ENDPOINT + "/bulk", bulkUpdate(), Role.SIMPLE, Role.ADMIN);

    app.exception(UnnauthorizedException.class, (e, ctx) -> {
      ctx.status(401);
      ctx.json(Map.of(JSON_RESULT, JSON_ERROR, JSON_MESSAGE, e.getMessage()));
      // log error in a stream...
    });

    app.exception(TaskException.class,
        (e, ctx) -> ctx.json(Map.of(JSON_RESULT, JSON_ERROR, JSON_MESSAGE, e.toMap()))
    // log error in a stream...
    );

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.json(
          Map.of(JSON_RESULT, JSON_ERROR, JSON_MESSAGE, "Ups... algo se rompió.: " + e.getMessage()));
      // log error in a stream...
    });
  }

  private Handler deleteTasks() {
    return ctx -> {
      TaskDto dto = ctx.bodyAsClass(TaskDto.class);

      this.tasks.deleteTask(new User(ctx.cookie(TOKEN_COOKIE_NAME), base64Secret).userId().toString(),
          dto.getIdTask());
      ctx.json(Map.of(JSON_RESULT, JSON_SUCCESS));
    };
  }

  private Handler taskDone() {
    return ctx -> {
      TaskDto dto = ctx.bodyAsClass(TaskDto.class);

      this.tasks.done(new User(ctx.cookie(TOKEN_COOKIE_NAME), base64Secret).userId().toString(),
          dto.getIdTask());
      ctx.json(Map.of(JSON_RESULT, JSON_SUCCESS));
    };
  }

  private Handler bulkUpdate() {
    return ctx -> {
      TaskSyncDto[] dto = ctx.bodyAsClass(TaskSyncDto[].class);

      List<BulkData> bulk = Arrays.asList(dto).stream().map((e) -> {
        return new BulkData(e.getExpirationDate(), e.getText(), e.getSyncId(), e.getDone(),
            Long.valueOf(e.getQueuedTime()), e.getOp());
      }).collect(Collectors.toList());

      this.tasks.bulkUpdate(new User(ctx.cookie(TOKEN_COOKIE_NAME), base64Secret).userId().toString(),
          bulk);
      ctx.json(Map.of(JSON_RESULT, JSON_SUCCESS));
    };
  }

  private Handler taskInProgress() {
    return ctx -> {
      TaskDto dto = ctx.bodyAsClass(TaskDto.class);

      this.tasks.inProgress(new User(ctx.cookie(TOKEN_COOKIE_NAME), base64Secret).userId().toString(),
          dto.getIdTask());
      ctx.json(Map.of(JSON_RESULT, JSON_SUCCESS));
    };
  }

  private Handler addTasks() {
    return ctx -> {
      TaskDto dto = ctx.bodyAsClass(TaskDto.class);

      this.tasks.addTask(new User(ctx.cookie(TOKEN_COOKIE_NAME), base64Secret).userId().toString(),
          dto.getTaskText(), dto.getExpirationDate());
      ctx.json(Map.of(JSON_RESULT, JSON_SUCCESS));
    };
  }

  private Handler tasks() {
    return ctx -> {

      List<Task> tasksList = this.tasks
          .list(new User(ctx.cookie(TOKEN_COOKIE_NAME), base64Secret).userId().toString());

      List<Map<String, Object>> tasksToJson = tasksList.stream().map(t -> t.toMap())
          .collect(Collectors.toList());

      ctx.json(Map.of(JSON_RESULT, JSON_SUCCESS, "tasks", tasksToJson));
    };
  }
}
