package com.codesoft.edu.controller;

import com.codesoft.edu.dto.TaskDto;
import com.codesoft.edu.model.Priority;
import com.codesoft.edu.model.Task;
import com.codesoft.edu.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    public TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/create/todos/{todo_id}")
    public String createForm(@PathVariable("todo_id") Long todoId, Model model) {
        model.addAttribute("taskDto", new TaskDto());
        model.addAttribute("todo_id", todoId);
        model.addAttribute("priorities", Priority.values());
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    public String createTask(@ModelAttribute TaskDto taskDto, @PathVariable("todo_id") Long todoId) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        taskRepository.save(task);
        return "redirect:/todos/all/users/" + todoId;
    }

    @GetMapping("/{task_id}/update/todos/{todo_id}")
    public String updateForm(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId, Model model) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task id is not correct"));
        model.addAttribute("task", task);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("todo_id", todoId);
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    public String updateTask(@ModelAttribute Task task, @PathVariable("todo_id") Long todoId) {
        taskRepository.save(task);
        return "redirect:/todos/all/users/" + todoId;
    }

    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    public String deleteTask(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId) {
        taskRepository.deleteById(taskId);
        return "redirect:/todos/all/users/" + todoId;
    }
}
