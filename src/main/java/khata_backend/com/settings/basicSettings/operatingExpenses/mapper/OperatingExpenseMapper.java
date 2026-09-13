package khata_backend.com.settings.basicSettings.operatingExpenses.mapper;

import khata_backend.com.settings.basicSettings.operatingExpenses.model.dto.OperatingExpenseDTO;
import khata_backend.com.settings.basicSettings.operatingExpenses.model.entity.OperatingExpense;
import org.springframework.stereotype.Component;

@Component
public class OperatingExpenseMapper {

    public OperatingExpense toEntity(OperatingExpenseDTO operatingExpenseDTO) {
        if (operatingExpenseDTO == null) {
            return null;
        }

        OperatingExpense operatingExpense = new OperatingExpense();
        operatingExpense.setExpenseName(operatingExpenseDTO.getExpenseName());
        operatingExpense.setAmountPerPiece(operatingExpenseDTO.getAmountPerPiece());
        return operatingExpense;
    }

    public OperatingExpenseDTO toDTO(OperatingExpense operatingExpense) {
        if (operatingExpense == null) {
            return null;
        }

        OperatingExpenseDTO operatingExpenseDTO = new OperatingExpenseDTO();
        operatingExpenseDTO.setId(operatingExpense.getId());
        operatingExpenseDTO.setExpenseName(operatingExpense.getExpenseName());
        operatingExpenseDTO.setAmountPerPiece(operatingExpense.getAmountPerPiece());
        return operatingExpenseDTO;
    }
}
