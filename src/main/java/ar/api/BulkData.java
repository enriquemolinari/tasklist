package ar.api;

public class BulkData implements Comparable<BulkData> {
  private String expirationDate;
  private String taskText;
  private String syncId;
  private String done;
  private Long queuedTime;
  private String operation;
  
  public BulkData(String expirationDate, String taskText, String syncId, String done, Long queuedTime, String operation) {
    this.expirationDate = expirationDate;
    this.taskText = taskText;
    this.syncId = syncId;
    this.done = done;
    this.queuedTime = queuedTime;
    this.operation = operation;
  }
  
  public String expirationDate() {
    return this.expirationDate;
  }

  public String text() {
    return this.taskText;
  }
  
  public String syncId() {
    return this.syncId;
  }
  
  public Boolean done() {
    return Boolean.valueOf(this.done);
  }
  
  public Long queuedTime() {
    return this.queuedTime;
  }

  public String operation() {
    return this.operation;
  }

  @Override
  public int compareTo(BulkData data) {
    return this.queuedTime().compareTo(data.queuedTime());
  }
}
