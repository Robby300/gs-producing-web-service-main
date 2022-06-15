package com.example.producingwebservice.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class MessageService {
    public static final Locale russian = new Locale("ru", "RU");

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