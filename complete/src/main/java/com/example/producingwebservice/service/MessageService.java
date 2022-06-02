package com.example.producingwebservice.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;

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
        return getMessage(className + "." + a);
    }
}