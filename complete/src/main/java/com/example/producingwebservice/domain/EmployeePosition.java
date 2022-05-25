package com.example.producingwebservice.domain;

public enum EmployeePosition {

    DIRECTOR("директор",150_000, 200_000), //todo кириллицу в коде оставлять плохо. Используй resourceBundle есть пример в fccr класс MessageService
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

    public String getNotValidMessage(int salary) { //todo кириллицу в коде оставлять плохо. Используй resourceBundle есть пример в fccr класс MessageService
        return "у позиции  \"" + ruName + "\" зп должна быть в диапазоне от " //todo если надо что то вставлять строку используй MessageFormat
                + lowSalary + " до " + highSalary + ", в запросе прислали " + salary;
    }
}

