package ar.web;

public class TaskSyncDto {

  private String expirationDate;
  private String taskText;
  private String syncId;
  private String done;
  
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
  
  public String getSyncId() {
    return syncId;
  }

  public void setSyncId(String syncId) {
    this.syncId = syncId;
  }

  public String getDone() {
    return done;
  }

  public void setDone(String done) {
    this.done = done;
  }
}
