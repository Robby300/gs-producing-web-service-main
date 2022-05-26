package com.example.producingwebservice.domain;

import java.text.MessageFormat;

public enum EmployeePosition {

    DIRECTOR(150_000, 200_000),
    MANAGER(100_000, 150_000),
    WORKER( 50_000, 100_000);
    public final int lowSalary;
    public final int highSalary;


    EmployeePosition(int lowSalary, int highSalary) {
        this.lowSalary = lowSalary;
        this.highSalary = highSalary;
    }

    public String value() {
        return name();
    }

    public boolean isValidSalary(int salary) {
        return salary >= lowSalary && salary < highSalary;
    }
}

