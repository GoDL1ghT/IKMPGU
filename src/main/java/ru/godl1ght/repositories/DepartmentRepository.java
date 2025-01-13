package ru.godl1ght.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.godl1ght.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}