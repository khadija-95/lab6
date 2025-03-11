package com.example.employee.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
    @NotNull(message = "ID cannot be null")
    @Size(min = 3)
    private String id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 4, message = "Name must be more than 4 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with '05' and have exactly 10 digits")
    private String phoneNumber;

    @NotNull(message = "Age cannot be null")
    @Min(value = 26, message = "Age must be more than 25")
    private Integer age;

    @NotNull(message = "Position cannot be null")
    @Pattern(regexp = "supervisor|coordinator", message = "Position must be either 'supervisor' or 'coordinator'")
    private String position;

    private boolean onLeave = false;

    @NotNull(message = "Hire date cannot be null")
    @PastOrPresent(message = "Hire date must be in the present or past")
    private LocalDate hireDate;

    @NotNull(message = "Annual leave cannot be null")
    @Positive(message = "Annual leave must be a positive number")
    private Integer annualLeave;
}

