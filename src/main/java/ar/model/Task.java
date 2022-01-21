package ar.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private boolean done = false;
  private LocalDateTime creationDate;
  private LocalDateTime expireDate;
  private String syncId;
  
  private enum TaskStatus {
    DANGER, WARNING, FINE, FUTURE;
  }

  @ManyToOne(cascade = CascadeType.ALL)
  private Creator creator;
  private String taskText;
  
  public Task(Long id, LocalDateTime creationDate, String expirationDate, Creator creator, String text, String syncId) {
    this(creationDate, expirationDate, creator, text, syncId);
    this.id = id;
  }

  public Task(LocalDateTime creationDate, String expirationDate, Creator creator, String text, String syncId) {
    TaskErrors errors = new TaskErrors(creationDate, text, expirationDate);
    errors.throwOnError();

    this.creationDate = creationDate;
    this.expireDate = new DateTimeFormatted(expirationDate).toLocalDateTime();
    this.creator = creator;
    this.taskText = text;
    this.syncId = syncId;
  }

  public void done() {
    this.done = true;
  }

  public void inProgress() {
    this.done = false;
  }

  public Long creatorId() {
    return this.creator.id();
  }

  public TaskStatus status() {
    long days = ChronoUnit.DAYS.between(this.creationDate, this.expireDate);

    if (days <= 2) {
      return TaskStatus.DANGER;
    }
    if (days > 2 && days <= 5) {
      return TaskStatus.WARNING;
    }
    if (days > 5 && days <= 15) {
      return TaskStatus.FINE;
    }

    return TaskStatus.FUTURE;
  }

  public Map<String, Object> toMap() {
    var m = Map.of("id", this.id, "creationDate", new DateTimeFormatted(this.creationDate).toString(),
        "expirationDate", new DateTimeFormatted(this.expireDate).toString(), "text", this.taskText,
        "done", this.done, "creator", this.creator.toMap(), "status", this.status(), "syncId", this.syncId);

    return m;
  }

  // just for hibernate...
  public Task() {

  }

  private Long getId() {
    return id;
  }

  private void setId(Long id) {
    this.id = id;
  }

  private String getSyncId() {
    return syncId;
  }

  private void setSyncId(String syncId) {
    this.syncId = syncId;
  }

  private boolean isDone() {
    return done;
  }

  private void setDone(boolean done) {
    this.done = done;
  }

  private LocalDateTime getCreationDate() {
    return creationDate;
  }

  private void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  private LocalDateTime getExpireDate() {
    return expireDate;
  }

  private void setExpireDate(LocalDateTime expireDate) {
    this.expireDate = expireDate;
  }

  private Creator getCreator() {
    return creator;
  }

  private void setCreator(Creator creator) {
    this.creator = creator;
  }

  private String getTaskText() {
    return taskText;
  }

  private void setTaskText(String taskText) {
    this.taskText = taskText;
  }
}
