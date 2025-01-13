package ru.godl1ght.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "employee", indexes = {
        @Index(name = "employee_name_idx", columnList = "name")
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "department")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @NotEmpty(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @JsonBackReference
    private Department department;

}
