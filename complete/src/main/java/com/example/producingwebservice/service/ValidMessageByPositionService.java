package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.text.MessageFormat;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public class ValidMessageByPositionService {
    public static Locale russian = new Locale("ru", "RU");

    private final ResourceBundleMessageSource messageSource;


    //todo Общие замечание
    // у тебя данный сервис делает две разные логики. 1 достает из ResourceBundle, а другая делает какую то логику валидации
    // необходимо разделить логику
    public String getMessage(String code) {
        try {
            return messageSource.getMessage(code, null, russian);
        } catch (Exception e) {
            return code;
        }
    }

    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, russian);
    }

    public String getMessage(@NonNull Enum a) {
        String className = a.getClass().getName();
        return getMessage(className + "." + a);
    }

    public String getNotValidSalaryMessage(Employee employee) { //todo кириллица в коде.
        return MessageFormat.format("У позиции {0} зп должна быть в диапазоне от {1} до {2}, в запросе прислали {3}",
                getMessage(employee.getEmployeePosition()),
                employee.getEmployeePosition().lowSalary,
                employee.getEmployeePosition().highSalary,
                employee.getSalary());
    }

    public String getNotValidCountsOfTasksMessage(Employee employee) { //todo кириллица в коде.
        return MessageFormat.format("У позиции {0} количество задач должно быть не более {1}",
                getMessage(employee.getEmployeePosition()),
                employee.getEmployeePosition().maxTasks);
    }
}