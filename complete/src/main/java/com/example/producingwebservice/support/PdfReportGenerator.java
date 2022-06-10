package com.example.producingwebservice.support;

import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.model.EmployeeDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.pdf.BaseFont.HELVETICA_BOLD;

@Slf4j
public class PdfReportGenerator {

    public static final String EMPLOYEE_QUESTIONNAIRE = "Employee questionnaire";
    public static final String EMPLOYEE_TASKS = "Employee tasks";
    public static final String TASK = "Task: ";
    public static final String UUID = "UUID: ";
    public static final String NAME = "Name: ";
    public static final String POSITION = "Position: ";
    public static final String SALARY = "Salary: ";
    public static final int BIG_FONT_SIZE = 16;
    public static final int MIDDLE_FONT_SIZE = 14;
    private static final String DATE_FORMAT_PATTERN = "HH:mm:ss dd.MM.yyyy";

    private PdfReportGenerator() {
    }

    public static ByteArrayInputStream getEmployeePdfReport(EmployeeDto employeeDto) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(getParagraph(EMPLOYEE_QUESTIONNAIRE, BIG_FONT_SIZE));
            document.add(getParagraph(getDownloadDate(), MIDDLE_FONT_SIZE));
            document.add(getListOfEmployeeFields(employeeDto));
            if (employeeHasTasks(employeeDto)) {
                document.add(getParagraph(EMPLOYEE_TASKS, MIDDLE_FONT_SIZE));
                document.add(getListOfEmployeeTasks(employeeDto));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static boolean employeeHasTasks(EmployeeDto employeeDto) {
        return !employeeDto.getTasks().isEmpty();
    }

    private static List getListOfEmployeeTasks(EmployeeDto employeeDto) {
        List list = new List();
        Set<Task> tasks = employeeDto.getTasks();
        list.setListSymbol(new Chunk(TASK));
        list.setNumbered(true);
        for (Task task : tasks) {
            list.add(task.toString());
        }
        return list;
    }

    private static Paragraph getParagraph(String content, int fontSize) {
        Font fontTitle = FontFactory.getFont(HELVETICA_BOLD);
        fontTitle.setSize(fontSize);
        Paragraph paragraph = new Paragraph(content, fontTitle);
        paragraph.setAlignment(ALIGN_CENTER);
        return paragraph;
    }

    private static String getDownloadDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
    }

    private static List getListOfEmployeeFields(EmployeeDto employeeDto) {
        List list = new List();
        list.setListSymbol(new Chunk(UUID));
        list.add(employeeDto.getUuid());

        list.setListSymbol(new Chunk(NAME));
        list.add(employeeDto.getName());

        list.setListSymbol(new Chunk(POSITION));
        list.add(employeeDto.getPosition().name());

        list.setListSymbol(new Chunk(SALARY));
        list.add(employeeDto.getSalary());
        return list;
    }
}
