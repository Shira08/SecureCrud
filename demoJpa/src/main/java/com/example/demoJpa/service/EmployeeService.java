package com.example.demoJpa.service;

import com.example.demoJpa.dto.EmployeeUpdateDto;
import com.example.demoJpa.entity.Employee;
import com.example.demoJpa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    //concept d'injection de dependance
    @Autowired
    private EmployeeRepository employeeRepository;

        public Employee saveEmployee(Employee employee) {
            Employee emp = new Employee();
            employee.setName(employee.getName());
            employee.setSalary(employee.getSalary());
            employee.setUser(employee.getUser());
            return employeeRepository.save(employee);
        }

    public Optional<Employee> updateEmployeeManager(Long id, Employee employeeDetails) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    //employee.setName(employeeDetails.getName());
                    employee.setSalary(employeeDetails.getSalary());
                    return employeeRepository.save(employee);
                });
    }
    public Optional<Employee> updateEmployeeInfo(Long id, EmployeeUpdateDto employeeUpdateDto) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(employeeUpdateDto.getName());
                    // Do not update the salary field here
                    return employeeRepository.save(employee);
                });
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> fetchAllEmployees() {
        List<Employee> allEmployees = employeeRepository.findAll();
        return allEmployees;
    }

    public String deleteEmployee(Long id) {
        if (employeeRepository.findById(id).isPresent()) {
            employeeRepository.deleteById(id);
            return "Employee deleted successfully";
        }
        return "No such employee in the database";
    }

}
