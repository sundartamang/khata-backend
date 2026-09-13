package khata_backend.com.settings.basicSettings.operatingExpenses.controller;

import jakarta.validation.Valid;
import khata_backend.com.common.BaseResponse;
import khata_backend.com.common.ResponseBuilder;
import khata_backend.com.settings.basicSettings.operatingExpenses.facade.OperatingExpenseFacade;
import khata_backend.com.settings.basicSettings.operatingExpenses.model.dto.OperatingExpenseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/operating-expense")
@RequiredArgsConstructor
public class OperatingExpenseController {

    private final OperatingExpenseFacade operatingExpenseFacade;

    @PostMapping
    public ResponseEntity<BaseResponse<OperatingExpenseDTO>> createOperatingExpense(
            @Valid @RequestBody OperatingExpenseDTO operatingExpenseDTO) {
        return ResponseBuilder.build(operatingExpenseFacade.createOperatingExpense(operatingExpenseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OperatingExpenseDTO>> getOperatingExpenseById(
            @PathVariable UUID id) {
        return ResponseBuilder.build(operatingExpenseFacade.getOperatingExpenseById(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<OperatingExpenseDTO>>> getOperatingExpenses(Pageable pageable) {
        return ResponseBuilder.build(operatingExpenseFacade.getOperatingExpenses(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<OperatingExpenseDTO>> updateOperatingExpense(
            @Valid @RequestBody OperatingExpenseDTO operatingExpenseDTO,
            @PathVariable UUID id) {
        return ResponseBuilder.build(operatingExpenseFacade.updateOperatingExpense(operatingExpenseDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteOperatingExpense(@PathVariable UUID id) {
        return ResponseBuilder.build(operatingExpenseFacade.deleteOperatingExpense(id));
    }
}
