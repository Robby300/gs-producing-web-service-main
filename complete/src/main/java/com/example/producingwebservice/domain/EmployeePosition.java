package com.example.producingwebservice.domain;

public enum EmployeePosition {

    DIRECTOR("директор",150_000, 200_000),
    MANAGER("менеджер",100_000, 150_000),
    WORKER("рабочий", 50_000, 100_000);

    private final int lowSalary;
    private final int highSalary;
    private final String ruName;

    EmployeePosition(String ruName, int lowSalary, int highSalary) {
        this.ruName = ruName;
        this.lowSalary = lowSalary;
        this.highSalary = highSalary;
    }

    public String value() {
        return name();
    }

    public boolean isValidSalary(int salary) {
        return salary >= lowSalary && salary < highSalary;
    }

    public String getNotValidMessage(int salary) {
        return new String("у "+ ruName + " зп должна быть в диапазоне от "
                + lowSalary + " до " + highSalary + ", в запросе прислали " + salary);
    }
}

