package com.example.producingwebservice.type;

public enum EmployeePosition {

    DIRECTOR(150_000, 200_000, 4),
    MANAGER(100_000, 150_000, 3),
    WORKER( 50_000, 100_000, 2);
    public final int lowSalary;
    public final int highSalary;
    public final int maxTasks;


    EmployeePosition(int lowSalary, int highSalary, int maxTasks) {
        this.lowSalary = lowSalary;
        this.highSalary = highSalary;
        this.maxTasks = maxTasks;
    }

    public boolean isValidSalary(int salary) {
        return salary >= lowSalary && salary < highSalary;
    }

    public boolean isValidCountOfTasks(int tasks) {
        return tasks <= maxTasks;
    }
}

