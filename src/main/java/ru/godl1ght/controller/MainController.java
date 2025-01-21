package ru.godl1ght.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ru.godl1ght.entity.Department;
import ru.godl1ght.entity.Employee;
import ru.godl1ght.entity.Task;
import ru.godl1ght.entity.TaskStatus;
import ru.godl1ght.repositories.DepartmentRepository;
import ru.godl1ght.repositories.EmployeeRepository;
import ru.godl1ght.repositories.TaskRepository;

@Controller
@AllArgsConstructor
public class MainController {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;


    @GetMapping("/")
    public String index(Model model) {
        List<Department> departments = departmentRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Department::getId))
                .toList();

        List<Employee> employees = employeeRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Employee::getId))
                .toList();

        model.addAttribute("departments", departments);
        model.addAttribute("employees", employees);

        return "index";
    }

    @GetMapping("/addDepartment")
    public String showAddDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "addDepartment";
    }

    @PostMapping("/addDepartment")
    public String addDepartment(@Valid @ModelAttribute Department department, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "addDepartment";
        }

        departmentRepository.save(department);
        return "redirect:/";
    }

    @GetMapping("/addEmployee")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        return "addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(
            @ModelAttribute Employee employee,
            @RequestParam(value = "initialTask", required = false) String initialTask,
            @RequestParam(value = "taskDescription", required = false) String taskDescription) {
        employeeRepository.save(employee);

        if (initialTask != null && !initialTask.trim().isEmpty()) {
            Task task = new Task();
            task.setTitle(initialTask);
            task.setDescription(taskDescription != null ? taskDescription : "");
            task.setEmployee(employee);
            task.setStatus(TaskStatus.NEW);
            taskRepository.save(task);
        }

        return "redirect:/";
    }

    @GetMapping("/deleteDepartment/{id}")
    public String deleteDepartment(@PathVariable("id") Long id) {
        List<Employee> employees = employeeRepository.findAllByDepartmentId(id);
        employeeRepository.deleteAll(employees);
        departmentRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/editDepartment/{id}")
    public String showEditDepartmentForm(@PathVariable("id") Long id, Model model) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute("department", department);
        return "editDepartment";
    }

    @PostMapping("/editDepartment/{id}")
    public String updateDepartment(@PathVariable("id") Long id, @ModelAttribute Department department) {
        department.setId(id);
        departmentRepository.save(department);
        return "redirect:/";
    }

    @GetMapping("/editEmployee/{id}")
    public String showEditEmployeeForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentRepository.findAll());
        return "editEmployee";
    }

    @PostMapping("/editEmployee/{id}")
    public String updateEmployee(@PathVariable("id") Long id, @ModelAttribute Employee employee) {
        employee.setId(id);
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @GetMapping("/addTask/{employeeId}")
    public String showAddTaskForm(@PathVariable(value = "employeeId") Long employeeId, Model model) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeId));
        Task task = new Task();
        task.setEmployee(employee);
        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());
        return "addTask";
    }

    @PostMapping("/addTask/{employeeId}")
    public String addTask(
            @PathVariable(value = "employeeId") Long employeeId,
            @Valid @ModelAttribute Task task,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addTask";
        }
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeId));
        task.setEmployee(employee);
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/editTask/{id}")
    public String showEditTaskForm(@PathVariable(value = "id") Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());
        return "editTask";
    }

    @PostMapping("/editTask/{id}")
    public String updateTask(
            @PathVariable(value = "id") Long id,
            @Valid @ModelAttribute Task task,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editTask";
        }
        task.setId(id);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        task.setEmployee(existingTask.getEmployee());
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable(value = "id") Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }
}

