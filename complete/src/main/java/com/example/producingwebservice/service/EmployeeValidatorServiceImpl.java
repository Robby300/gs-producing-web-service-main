package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.type.EmployeePosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Service
@RequiredArgsConstructor
public class EmployeeValidatorServiceImpl implements EmployeeValidatorService {

    public static final String SEPARATOR = "; ";
    private final EmployeeNotValidMessageService employeeNotValidMessageService;
    private final MessageService messageService;

    @Override
    public boolean isValidInput(Employee employee) {
        if (isNotValidSalary(employee) || isNotValidCountOfTasks(employee)) {
            return false;
        }
        return getConstraintViolations(employee).isEmpty();
    }

    @Override
    public String getViolationsMessage(Employee employee) {
        StringBuilder violationsMessage = new StringBuilder();
        Set<ConstraintViolation<Employee>> employeeConstraintViolations = getConstraintViolations(employee);
        employeeConstraintViolations
                .forEach(violation -> violationsMessage.append(violation.getMessageTemplate())
                        .append(SEPARATOR));
        addNotValidSalaryMessage(employee, violationsMessage);
        addNotValidCountsOfTasksMessage(employee, violationsMessage);
        return violationsMessage.toString();
    }

    private Set<ConstraintViolation<Employee>> getConstraintViolations(Employee employee) {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(employee);
    }

    private boolean isNotValidCountOfTasks(Employee employee) {
        EmployeePosition employeePosition = employee.getEmployeePosition();
        return !employeePosition.isValidCountOfTasks(employee.getTasks().size());
    }

    private boolean isNotValidSalary(Employee employee) {
        return !employee.getEmployeePosition().isValidSalary(employee.getSalary());
    }

    private void addNotValidCountsOfTasksMessage(Employee employee, StringBuilder result) {
        if (isNotValidCountOfTasks(employee)) {
            result.append(employeeNotValidMessageService.getNotValidCountsOfTasksMessage(employee)).append(SEPARATOR);
        }
    }

    private void addNotValidSalaryMessage(Employee employee, StringBuilder result) {
        if (isNotValidSalary(employee)) {
            result.append(employeeNotValidMessageService.getNotValidSalaryMessage(employee)).append(SEPARATOR);
        }
    }

    public String getNotValidSalaryMessage(Employee employee) {
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage("validation.income.should.be.in.range.of")
                + employee.getEmployeePosition().lowSalary
                + messageService.getMessage("validation.to")
                + employee.getEmployeePosition().highSalary
                + messageService.getMessage("validation.sent.in.request")
                + employee.getSalary();
    }

    public String getNotValidCountsOfTasksMessage(Employee employee) {
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage(employee.getEmployeePosition())
                + messageService.getMessage("validation.number.of.tasks.should.not.exceed")
                + employee.getEmployeePosition().maxTasks;
    }


//    public Map<String, String> checkSalary(Position position, Long salary) {
//        Map<String, String> response = new HashMap<>();
//
//        if (salary != null && !isLegalSalary(position, salary)) {
//            String message = MessageFormat.format(
//                    resourceBundle.getString(SALARY_BUNDLE_KEY),
//                    SALARY,
//                    position.getSalaryMin(),
//                    position.getSalaryMax(),
//                    salary);
//            response.put(SALARY, message);
//        }
//        return response;
//    }
//
//    private boolean isLegalSalary(Position position, Long salary) {
//        return salary >= position.getSalaryMin() && salary <= position.getSalaryMax();
//    }
//
//    public Map<String, String> checkAge(Position position, Long age) {
//        Map<String, String> response = new HashMap<>();
//        if (age != null && age < position.getMinAge()) {
//            String message = MessageFormat.format(
//                    resourceBundle.getString(AGE_BUNDLE_KEY),
//                    AGE,
//                    position.getMinAge(),
//                    age);
//            response.put(AGE, message);
//        }
//        return response;
//    }
//
//    public List<String> checkRequiredFields(Employee employee) {
//        List<String> invalidFields = new ArrayList<>();
//        if (employee.getName() == null) {
//            invalidFields.add(NAME);
//        }
//        if (employee.getSurname() == null) {
//            invalidFields.add(SURNAME);
//        }
//        if (employee.getPosition() == null) {
//            invalidFields.add(POSITION);
//        }
//        if (employee.getAge() == null) {
//            invalidFields.add(AGE);
//        }
//        if (employee.getSalary() == null) {
//            invalidFields.add(SALARY);
//        }
//
//        Position position = getDefine(employee.getPosition());
//        if (position == SENIOR) {
//            invalidFields.addAll(requiredFieldsSenior(employee));
//        } else if (position == MANAGER) {
//            invalidFields.addAll(requiredFieldsManager(employee));
//        }
//        return invalidFields;
//    }
//
//    private List<String> requiredFieldsSenior(Employee employee) {
//        List<String> nullableFields = new ArrayList<>();
//        if (employee.getGrade() == null) {
//            nullableFields.add(GRADE);
//        }
//        if (employee.getDescription() == null) {
//            nullableFields.add(DESCRIPTION);
//        }
//        return nullableFields;
//    }
//
//    public Map<String, String> checkAdmissibleTaskCount(Position position, Long countTasks) {
//        Map<String, String> response = new HashMap<>();
//        if (countTasks > position.getCountTasksMax()) {
//            String message = MessageFormat.format(
//                    resourceBundle.getString(TASKS_BUNDLE_KEY),
//                    position.getPosition(),
//                    position.getCountTasksMax(),
//                    countTasks);
//            response.put(TASKS_UID, message);
//        }
//        return response;
//    }
//
//    private List<String> requiredFieldsManager(Employee employee) {
//        List<String> nullableFields = new ArrayList<>();
//        if (employee.getGrade() == null) {
//            nullableFields.add(GRADE);
//        }
//        return nullableFields;
//    }
}
