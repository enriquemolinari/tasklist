package ar.web;

public class TaskDto {

  private String idTask;
  private String expirationDate;
  private String taskText;
  
  public String getExpirationDate() {
    return expirationDate;
  }
  
  public void setExpirationDate(String expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getTaskText() {
    return taskText;
  }

  public void setTaskText(String taskText) {
    this.taskText = taskText;
  }

  public String getIdTask() {
    return idTask;
  }

  public void setIdTask(String idTask) {
    this.idTask = idTask;
  }
}
