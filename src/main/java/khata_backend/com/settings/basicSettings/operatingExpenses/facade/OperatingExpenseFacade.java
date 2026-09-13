package khata_backend.com.settings.basicSettings.operatingExpenses.facade;

import khata_backend.com.common.BaseResponse;
import khata_backend.com.exception.InvalidDataException;
import khata_backend.com.settings.basicSettings.operatingExpenses.model.dto.OperatingExpenseDTO;
import khata_backend.com.settings.basicSettings.operatingExpenses.service.OperatingExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OperatingExpenseFacade {

    private final OperatingExpenseService operatingExpenseService;

    public BaseResponse<OperatingExpenseDTO> createOperatingExpense(OperatingExpenseDTO operatingExpenseDTO) {
        requirePayload(operatingExpenseDTO);

        OperatingExpenseDTO created = operatingExpenseService.createOperatingExpense(operatingExpenseDTO);
        return BaseResponse.<OperatingExpenseDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("Operating expense created successfully")
                .data(created)
                .build();
    }

    public BaseResponse<OperatingExpenseDTO> updateOperatingExpense(
            OperatingExpenseDTO operatingExpenseDTO, UUID operatingExpenseId) {
        requireValidId(operatingExpenseId);
        requirePayload(operatingExpenseDTO);

        OperatingExpenseDTO updated = operatingExpenseService.updateOperatingExpense(
                operatingExpenseDTO, operatingExpenseId);
        return BaseResponse.<OperatingExpenseDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Operating expense updated successfully")
                .data(updated)
                .build();
    }

    public BaseResponse<OperatingExpenseDTO> getOperatingExpenseById(UUID operatingExpenseId) {
        requireValidId(operatingExpenseId);

        return BaseResponse.<OperatingExpenseDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Operating expense fetched successfully")
                .data(operatingExpenseService.getOperatingExpenseById(operatingExpenseId))
                .build();
    }

    public BaseResponse<Page<OperatingExpenseDTO>> getOperatingExpenses(Pageable pageable) {
        if (pageable == null) {
            throw new InvalidDataException("Pageable data cannot be null");
        }

        Page<OperatingExpenseDTO> expenses = operatingExpenseService.getOperatingExpenses(pageable);
        return BaseResponse.<Page<OperatingExpenseDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Operating expenses fetched successfully")
                .data(expenses)
                .build();
    }

    public BaseResponse<Void> deleteOperatingExpense(UUID operatingExpenseId) {
        requireValidId(operatingExpenseId);

        operatingExpenseService.deleteOperatingExpense(operatingExpenseId);
        return BaseResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Operating expense deleted successfully")
                .build();
    }

    private void requirePayload(OperatingExpenseDTO operatingExpenseDTO) {
        if (operatingExpenseDTO == null) {
            throw new InvalidDataException("Operating expense data cannot be null");
        }
    }

    private void requireValidId(UUID operatingExpenseId) {
        if (operatingExpenseId == null) {
            throw new InvalidDataException("Operating expense ID cannot be null");
        }
    }
}
