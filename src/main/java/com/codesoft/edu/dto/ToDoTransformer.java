package com.codesoft.edu.dto;

import com.codesoft.edu.model.Task;
import com.codesoft.edu.model.ToDo;
import com.codesoft.edu.model.User;

import java.util.List;

public class ToDoTransformer {
    public static ToDoDto convertToDto(ToDo todo) {
        ToDoDto todoDto = new ToDoDto();
        todoDto.setId(todo.getId());
        todoDto.setTitle(todo.getTitle());
        todoDto.setCreatedAt(todo.getCreatedAt());
        todoDto.setOwner(todo.getOwner());
        todoDto.setTasks(todo.getTasks());
        todoDto.setCollaborators(todo.getCollaborators());
        return todoDto;
    }

    public static ToDo convertToEntity(ToDoDto todoDto, User owner, List<Task> tasks, List<User> collaborators) {
        ToDo todo = new ToDo();
        todo.setId(todoDto.getId());
        todo.setTitle(todoDto.getTitle());
        todo.setCreatedAt(todoDto.getCreatedAt());
        todo.setOwner(owner);
        todo.setTasks(tasks);
        todo.setCollaborators(collaborators);
        return todo;
    }

    public static ToDo convertToEntity(ToDoDto todoDto) {
        ToDo todo = new ToDo();
        if(todoDto.getId() != null) {
            todo.setId(todoDto.getId());
        }
        todo.setTitle(todoDto.getTitle());
        if(todoDto.getCreatedAt() == null) {
            todo.setCreatedAt(java.time.LocalDateTime.now());
        } else {
            todo.setCreatedAt(todoDto.getCreatedAt());
        }
        todo.setOwner(todoDto.getOwner());
        todo.setTasks(todoDto.getTasks());
        todo.setCollaborators(todoDto.getCollaborators());
        return todo;
    }
}
