package ru.godl1ght.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "department", indexes = {
        @Index(name = "department_name_idx", columnList = "name")
})
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "employees")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 2, max = 30, message = "Название должно быть от 2 до 30 символов")
    @NotEmpty(message = "Название обязательно для заполнения")
    private String name;

    @NotEmpty(message = "Задача обязательна для заполнения")
    @Size(min = 5, max = 50, message = "Задача должна быть от 5 до 50 символов")
    @Column(name = "task", nullable = false)
    private String task;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Employee> employees = new ArrayList<>();
}
