package ru.godl1ght.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.godl1ght.entity.Department;
import ru.godl1ght.entity.Employee;
import ru.godl1ght.repositories.DepartmentRepository;
import ru.godl1ght.repositories.EmployeeRepository;

import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;


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
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeRepository.save(employee);
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
}

