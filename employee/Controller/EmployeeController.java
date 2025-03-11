package com.example.employee.Controller;

import com.example.employee.Api.ApiResponse;
import com.example.employee.model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    ArrayList<Employee>employees=new ArrayList<>();


    @GetMapping("/get")
    public ArrayList<Employee> getEmployee(){
        return employees;
    }
@PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Employee added successfully"));
    }
    @PutMapping("/update/{index}")
    public ResponseEntity updateEmployee(@PathVariable int index, @RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        employees.set(index, employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee updated successfully"));
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity deleteEmployee(@PathVariable int index) {
        if (index > employees.size()) {
            return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));

        }
        employees.remove(index);
        return ResponseEntity.status(200).body(new ApiResponse("Employee deleted"));
    }
    @GetMapping("/search/{position}")
    public ResponseEntity searcEmployee(@PathVariable String position){

        for(Employee employee:employees ){
            if(employee.getPosition().equals(position)){
                return ResponseEntity.status(200).body(employee);
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Not found"));
    }
    @GetMapping("/get/{age}")

    public ResponseEntity getEmployeeByAgeRange(@RequestParam int minAge,@RequestParam int maxAge){
        ArrayList<Employee> filteredEmployees = new ArrayList<>();
        for(Employee employee : employees){
            if(employee.getAge()>=minAge && employee.getAge()<=maxAge){
                filteredEmployees.add(employee);
                return ResponseEntity.status(200).body(employee);
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("Not found"));

    }
    @PostMapping("/apply/{id}")

    public ResponseEntity<ApiResponse> applyForAnnualLeave(@PathVariable Long id, @RequestParam int days) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                if (employee.getAnnualLeave() >= days) {
                    employee.setAnnualLeave(employee.getAnnualLeave() - days);
                    return ResponseEntity.status(200).body(new ApiResponse("Leave applied successfully. Remaining annual leave: " + employee.getAnnualLeave()));

                }
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("Not enough annual leave balance"));
    }
    @GetMapping("/no-annual-leave")
    public ResponseEntity getEmployeesWithNoAnnualLeave() {
        ArrayList<Employee> noLeaveEmployees = new ArrayList<>();

        for (Employee employee : employees) {
            if (employee.getAnnualLeave() == 0) {
                noLeaveEmployees.add(employee);
            }
        }
        return ResponseEntity.status(200).body(noLeaveEmployees);

    }
    @PostMapping("/promote/{id}")
    public ResponseEntity promoteEmployee(@PathVariable String id, @RequestParam Long requesterId) {
        Employee requester = null;
        Employee employeeToPromote = null;

        for (Employee employee : employees) {
            if (employee.getId().equals(requesterId) && "supervisor".equalsIgnoreCase(employee.getPosition())) {
                requester = employee;
            }
            if (employee.getId().equals(id)) {
                employeeToPromote = employee;
            }
        }

        if (requester == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Requester must be a supervisor"));
        }
        if (employeeToPromote == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
        }
        if (employeeToPromote.getAge() < 30) {
            return ResponseEntity.status(400).body(new ApiResponse("Employee must be at least 30 years old to be promoted"));
        }
        if (employeeToPromote.isOnLeave()) {
            return ResponseEntity.status(400).body(new ApiResponse("Employee cannot be promoted while on leave"));
        }

        employeeToPromote.setPosition("supervisor");
        return ResponseEntity.status(200).body(new ApiResponse("Employee promoted successfully"));
    }

}
