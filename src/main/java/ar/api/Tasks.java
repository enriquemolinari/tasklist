package ar.api;

import java.util.List;

import ar.model.Task;

public interface Tasks {

  List<Task> list(String idCreator);
  
}
