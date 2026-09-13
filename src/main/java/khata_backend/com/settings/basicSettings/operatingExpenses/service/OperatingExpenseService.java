package khata_backend.com.settings.basicSettings.operatingExpenses.service;

import khata_backend.com.settings.basicSettings.operatingExpenses.model.dto.OperatingExpenseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OperatingExpenseService {

	OperatingExpenseDTO createOperatingExpense(OperatingExpenseDTO operatingExpenseDTO);

	OperatingExpenseDTO updateOperatingExpense(OperatingExpenseDTO operatingExpenseDTO, UUID operatingExpenseId);

	OperatingExpenseDTO getOperatingExpenseById(UUID operatingExpenseId);

	Page<OperatingExpenseDTO> getOperatingExpenses(Pageable pageable);

	void deleteOperatingExpense(UUID operatingExpenseId);
}
