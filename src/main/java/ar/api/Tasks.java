package ar.api;

import java.util.List;

import ar.model.Task;

public interface Tasks {

  List<Task> list(String idCreator);

  void deleteTask(String idCreator, String idTask);

  void done(String idCreator, String idTask);

  void inProgress(String idCreator, String idTask);

  void addTask(String idCreator, String taskText, String expirationDate);

  void bulkUpdate(String idCreator, List<BulkData> data);

}
