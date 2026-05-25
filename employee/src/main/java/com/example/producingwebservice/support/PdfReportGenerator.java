package com.example.producingwebservice.support;

import com.example.producingwebservice.model.EmployeeDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.lowagie.text.Element.ALIGN_CENTER;

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

	private PdfReportGenerator() {}

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
			log.error("Error generating PDF", e);
		} finally {
			document.close();
		}
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	private static boolean employeeHasTasks(EmployeeDto employeeDto) {
		return !employeeDto.getTasks().isEmpty();
	}

	private static List getListOfEmployeeTasks(EmployeeDto employeeDto) {
		List employeeTasks = new List();
		employeeTasks.setListSymbol(new Chunk(TASK));
		employeeTasks.setNumbered(true);
		employeeDto.getTasks().stream().map(Object::toString).forEach(employeeTasks::add);
		return employeeTasks;
	}

	private static Paragraph getParagraph(String content, int fontSize) {
		Font fontTitle = new Font(Font.HELVETICA);
		fontTitle.setSize(fontSize);
		Paragraph paragraph = new Paragraph(content, fontTitle);
		paragraph.setAlignment(ALIGN_CENTER);
		return paragraph;
	}

	private static String getDownloadDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
	}

	private static List getListOfEmployeeFields(EmployeeDto employeeDto) {
		List listOfEmployeeFields = new List();
		listOfEmployeeFields.setListSymbol(new Chunk(UUID));
		listOfEmployeeFields.add(employeeDto.getUuid());

		listOfEmployeeFields.setListSymbol(new Chunk(NAME));
		listOfEmployeeFields.add(employeeDto.getName());

		listOfEmployeeFields.setListSymbol(new Chunk(POSITION));
		listOfEmployeeFields.add(employeeDto.getPosition().name());

		listOfEmployeeFields.setListSymbol(new Chunk(SALARY));
		listOfEmployeeFields.add(employeeDto.getSalary());
		return listOfEmployeeFields;
	}
}
