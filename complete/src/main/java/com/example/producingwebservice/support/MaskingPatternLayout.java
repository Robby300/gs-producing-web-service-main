package com.example.producingwebservice.support;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Component
public class MaskingPatternLayout extends PatternLayout {

    private static final String PASSWORD_MASK_PATTERN = "password=^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    private static final String SALARY_MASK_PATTERN = "salary=(\\d+)";
    private static final String DELIMITER = "|";
    private static final int FIRST_MATCHER_INDEX = 1;
    public static final char ASTERISK = '*';
    private final Pattern multilinePattern;

    public MaskingPatternLayout() {
        List<String> maskPatterns = List.of(PASSWORD_MASK_PATTERN, SALARY_MASK_PATTERN);
        multilinePattern = Pattern.compile(String.join(DELIMITER, maskPatterns), Pattern.MULTILINE);
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        return maskMessage(super.doLayout(event));
    }

    private void maskGroup(StringBuilder maskBuilder, Matcher matcher, int group) {
        if (matcher.group(group) != null) {
            IntStream.range(matcher.start(group), matcher.end(group))
                    .forEach(i -> maskBuilder.setCharAt(i, ASTERISK));
        }
    }

    private String maskMessage(String message) {
        if (multilinePattern == null) {
            return message;
        }
        StringBuilder maskBuilder = new StringBuilder(message);
        Matcher matcher = multilinePattern.matcher(maskBuilder);
        while (matcher.find()) {
            IntStream.rangeClosed(FIRST_MATCHER_INDEX, matcher.groupCount())
                    .forEach(group -> maskGroup(maskBuilder, matcher, group));
        }
        return maskBuilder.toString();
    }
}