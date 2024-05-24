package com.codesoft.edu.dto;

import com.codesoft.edu.model.Task;
import com.codesoft.edu.model.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ToDoDto {

    private Long id;
    @NotBlank(message = "The 'title' cannot be empty")
    private String title;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
    private User owner;
    private List<Task> tasks;
    private List<User> collaborators;
    private List<Long> collaboratorIds;
    private List<Long> taskIds;


//    @Override
//    public String toString() {
//        return "ToDoDto{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", createdAt=" + createdAt +
//                ", owner=" + owner +
//                ", tasks=" + tasks +
//                ", collaborators=" + collaborators +
//                '}';
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoDto toDoDto = (ToDoDto) o;
        return id.equals(toDoDto.id) &&
                title.equals(toDoDto.title) &&
                createdAt.equals(toDoDto.createdAt) &&
                owner.equals(toDoDto.owner) &&
                tasks.equals(toDoDto.tasks) &&
                collaborators.equals(toDoDto.collaborators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createdAt, owner, tasks, collaborators);
    }

}
