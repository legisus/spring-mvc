package com.codesoft.edu.controller;

import com.codesoft.edu.dto.ToDoDto;
import com.codesoft.edu.dto.ToDoTransformer;
import com.codesoft.edu.model.Task;
import com.codesoft.edu.model.ToDo;
import com.codesoft.edu.model.User;
import com.codesoft.edu.service.TaskService;
import com.codesoft.edu.service.ToDoService;
import com.codesoft.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/todos")
public class ToDoController {

    private ToDoService toDoService;
    private UserService userService;
    private TaskService taskService;

    @Autowired
    public ToDoController(ToDoService toDoService, UserService userService, TaskService taskService) {
        this.toDoService = toDoService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/create/users/{ownerId}")
    public String create(@PathVariable Long ownerId, Model model) {
        User owner = userService.readById(ownerId);
        ToDo todo = new ToDo();
        todo.setOwner(owner);
        if (todo.getTasks() == null) {
            todo.setTasks(new ArrayList<>());
        }
        if (todo.getCollaborators() == null) {
            todo.setCollaborators(new ArrayList<>());
        }
        List<User> users = userService.getAll();
        List<Task> tasks = taskService.getAll();
        model.addAttribute("allUsers", users);
        model.addAttribute("owner", owner);
        model.addAttribute("todo", todo);
        model.addAttribute("tasks", tasks);
        return "create-todo";
    }

    @PostMapping("/create/users/{ownerId}")
    public String create(@PathVariable Long ownerId, @Valid @ModelAttribute ToDoDto toDoDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<User> users = userService.getAll();
            List<Task> tasks = taskService.getAll();
            model.addAttribute("allUsers", users);
            model.addAttribute("owner", userService.readById(ownerId));
            model.addAttribute("tasks", tasks);
            return "create-todo";
        }

        User owner = userService.readById(ownerId);
        if (owner == null) {
            model.addAttribute("errorMessage", "Owner not found");
            return "create-todo";
        }

        ToDoDto updatedToDoDto = new ToDoDto();
        updatedToDoDto.setTitle(toDoDto.getTitle());
        updatedToDoDto.setOwner(owner);
        updatedToDoDto.setCollaboratorIds(toDoDto.getCollaboratorIds());
        updatedToDoDto.setTasks(toDoDto.getTasks());

        ToDo entity = ToDoTransformer.convertToEntity(updatedToDoDto);
        entity.setOwner(owner);

        List<User> collaborators = Optional.ofNullable(toDoDto.getCollaboratorIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(userService::readById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (toDoDto.getId() != null) {
            collaborators.removeIf(user -> user.getId().equals(owner.getId()));
        }

        entity.setCollaborators(collaborators);

        toDoService.create(entity);
        return "redirect:/todos/all/users/" + ownerId;

    }

    @GetMapping("/{id}/tasks")
    public String read(@PathVariable Long id, Model model) {
        ToDo todo = toDoService.readById(id);
        model.addAttribute("tasks", todo.getTasks());
        return "update-task";
    }

    @GetMapping("/{todo_id}/update/users/{owner_id}")
    public String updateForm(@PathVariable Long todo_id, @PathVariable Long owner_id, Model model) {

        ToDo todo = toDoService.readById(todo_id);
        User owner = userService.readById(owner_id);
        List<Task> tasks = taskService.getAll();

        if (!owner.equals(todo.getOwner())) {
            return "error";
        }

        List<User> users = userService.getAll();

        model.addAttribute("allUsers", users);
        model.addAttribute("owner", owner);
        model.addAttribute("todo", todo);
        model.addAttribute("tasks", tasks);
        return "update-todo";
    }

    @PostMapping("/{todo_id}/update/users/{owner_id}")
    public String update(@PathVariable Long todo_id, @PathVariable Long owner_id, @Valid @ModelAttribute ToDoDto todoUpdated, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allUsers", userService.getAll());
            model.addAttribute("tasks", taskService.getAll());
            return "update-todo";
        }

        User newOwner = userService.readById(todoUpdated.getOwner().getId());
        if (newOwner == null) {
            model.addAttribute("errorMessage", "Owner not found");
            return "update-todo";
        }

        ToDo entity = ToDoTransformer.convertToEntity(todoUpdated);
        entity.setOwner(newOwner);

        if(todoUpdated.getCollaborators() != null) {
            List<User> collaborators = todoUpdated.getCollaboratorIds().stream()
                    .map(userService::readById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            collaborators.removeIf(user -> user.getId().equals(newOwner.getId()));
            entity.setCollaborators(collaborators);
        } else {
            entity.setCollaborators(new ArrayList<>());
        }

        if (todoUpdated.getTaskIds() != null) {
            List<Task> tasks = todoUpdated.getTaskIds().stream()
                    .map(taskService::readById)
                    .filter(Objects::nonNull)
                    .peek(task -> task.setTodo(entity))
                    .collect(Collectors.toList());
            entity.setTasks(tasks);

            for (Task task : tasks) {
                taskService.update(task);
            }
        } else {
            entity.setTasks(new ArrayList<>());
        }

        toDoService.update(entity);

        return "redirect:/todos/all/users/" + owner_id;
    }

    @GetMapping("/{todo_id}/delete/users/{owner_id}")
    public String delete(@PathVariable Long todo_id, @PathVariable Long owner_id) {
        toDoService.delete(todo_id);
        return "redirect:/todos/all/users/" + owner_id;
    }

    @GetMapping("/all/users/{user_id}")
    public String getAll(@PathVariable Long user_id, Model model) {
        User user = userService.readById(user_id);
        model.addAttribute("user", user);
        model.addAttribute("todos", user.getMyTodos());
        model.addAttribute("collaborations", user.getOtherTodos());
        return "todos-user";
    }

    @GetMapping("/{id}/add")
    public String addCollaborator(@PathVariable Long id, @RequestParam Long collaboratorId, RedirectAttributes redirectAttributes) {
        ToDo todo = toDoService.readById(id);
        User collaborator = userService.readById(collaboratorId);
        todo.getCollaborators().add(collaborator);
        toDoService.update(todo);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/todos/{id}/tasks";
    }

    @GetMapping("/{id}/remove")
    public String removeCollaborator(@PathVariable Long id, @RequestParam Long collaboratorId, RedirectAttributes redirectAttributes) {
        ToDo todo = toDoService.readById(id);
        User collaborator = userService.readById(collaboratorId);
        todo.getCollaborators().remove(collaborator);
        toDoService.update(todo);
        userService.update(collaborator);
        redirectAttributes.addAttribute("user_id", todo.getOwner().getId());
        return "redirect:/todos/all/users/{user_id}";
    }

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("todos", toDoService.getAll());
        return "todo-lists";
    }
}
