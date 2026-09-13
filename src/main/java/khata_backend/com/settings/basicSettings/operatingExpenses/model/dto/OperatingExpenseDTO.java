package khata_backend.com.settings.basicSettings.operatingExpenses.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class OperatingExpenseDTO {

    private UUID id;

    @NotBlank(message = "Expense name cannot be blank.")
    @Size(min = 3, max = 100, message = "Expense name must be between 3-100 characters.")
    private String expenseName;

    @NotNull(message = "Amount per piece cannot be null.")
    @DecimalMin(value = "0.01", message = "Amount per piece must be greater than zero.")
    private BigDecimal amountPerPiece;
}
