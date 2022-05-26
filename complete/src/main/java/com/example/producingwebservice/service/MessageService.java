package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.EmployeePosition;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.text.MessageFormat;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public class MessageService {
    public static Locale russian = new Locale("ru", "RU");

    private final ResourceBundleMessageSource messageSource;

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
        return getMessage(className + "." + a.toString());
    }


    public String getNotValidSalaryMessage(@NonNull EmployeePosition position, int salary) { //todo кириллицу в коде оставлять плохо. Используй resourceBundle есть пример в fccr класс MessageService
        return MessageFormat.format("У позиции \"{0}\" зп должна быть в диапазоне от {1} до {2}, в запросе прислали {3}",
                getMessage(position),
                position.lowSalary,
                position.highSalary,
                salary);
    }
}