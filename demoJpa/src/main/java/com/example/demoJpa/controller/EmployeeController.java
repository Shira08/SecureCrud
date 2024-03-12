package com.example.demoJpa.controller;

import com.example.demoJpa.dto.EmployeeUpdateDto;
import com.example.demoJpa.entity.Employee;
import com.example.demoJpa.repository.EmployeeRepository;
import com.example.demoJpa.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('employee:create')")
    public ResponseEntity<Object> saveEmployee(@RequestBody Employee employee) {
        try {
            Employee newEmployee = employeeService.saveEmployee(employee);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Employee created successfully");
            response.put("employee", newEmployee);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create employee: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<Object> getEmployeeById(@PathVariable long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);

        return employee.map(emp -> ResponseEntity.ok((Object) emp))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Employee with id " + id + " not found"));
    }

    @GetMapping("/all")
    //@PreAuthorize("hasAuthority('employee:read')")
    public List<Employee> getAllEmployees() {
        return employeeService.fetchAllEmployees();
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('employee:update')")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeUpdateDto employeeUpdateDto) {
        Optional<Employee> updateEmployee = employeeService.updateEmployeeInfo(id, employeeUpdateDto);

        if (updateEmployee.isPresent()) {
            return ResponseEntity.ok(updateEmployee.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with id " + id + " not found");
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('employee:delete')")
    public String deleteEmployee(@PathVariable("id") Long id) {
        return employeeService.deleteEmployee(id);
    }


}
