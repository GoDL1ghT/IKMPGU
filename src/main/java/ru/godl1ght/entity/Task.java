package ru.godl1ght.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "task")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "employee")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Название задачи обязательно")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    private String title;

    @NotEmpty(message = "Описание задачи обязательно")
    @Size(min = 10, max = 500, message = "Описание должно быть от 10 до 500 символов")
    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.NEW;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;
}