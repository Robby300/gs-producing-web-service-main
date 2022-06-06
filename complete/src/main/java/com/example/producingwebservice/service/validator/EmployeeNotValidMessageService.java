package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeNotValidMessageService {
    private final MessageService messageService;

    public String getNotValidSalaryMessage() {
        return messageService.getMessage("validation.salary.size");
    }

    public String getSalaryNotANumber() {
        return messageService.getMessage("validation.salary.not.number");
    }

    public String getNotValidSalaryByPositionMessage(Employee employee) {
        /*todo
            можно сделать одну строку в ResourceBundle:
               -> У позиции {0} ЗП должна быть в диапазоне от {1} до {2} в запросе прислали {3}
            и вызвать так
               -> MessageFormat.format(messageService.getMessage("название из bundle"),
                messageService.getMessage(employee.getPosition(),
                employee.getPosition().lowSalary,
                employee.getPosition().highSalary,
                employee.getSalary())
             таким образом, сокращается код и нет доп записей в bundle
                )
         */
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage(employee.getPosition())
                + messageService.getMessage("validation.income.should.be.in.range.of")
                + employee.getPosition().lowSalary
                + messageService.getMessage("validation.to")
                + employee.getPosition().highSalary
                + messageService.getMessage("validation.sent.in.request")
                + employee.getSalary();
    }

    public String getNotValidCountsOfTasksMessage(Employee employee) {
        //todo смотри туду выше
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage(employee.getPosition())
                + messageService.getMessage("validation.number.of.tasks.should.not.exceed")
                + employee.getPosition().maxTasks;
    }

    public String getNotNullMessage() {
        return messageService.getMessage("validation.not.null");
    }

    public String getNotValidNameLength() {
        return messageService.getMessage("validation.name.length");
    }
}
