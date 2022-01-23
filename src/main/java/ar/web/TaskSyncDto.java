package ar.web;

public class TaskSyncDto {

  private String expirationDate;
  private String text;
  private String syncId;
  private String done;  
  private String queuedTime;
  private String op;
  private String id;
  
  public String getExpirationDate() {
    return expirationDate;
  }
  
  public void setExpirationDate(String expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getText() {
    return text;
  }

  public void setText(String taskText) {
    this.text = taskText;
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

  public String getQueuedTime() {
    return queuedTime;
  }

  public void setQueuedTime(String queuedTime) {
    this.queuedTime = queuedTime;
  }

  public String getOp() {
    return op;
  }

  public void setOp(String op) {
    this.op = op;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
}
