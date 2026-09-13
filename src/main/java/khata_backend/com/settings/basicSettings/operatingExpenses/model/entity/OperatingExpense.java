package khata_backend.com.settings.basicSettings.operatingExpenses.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import khata_backend.com.common.model.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;


@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "operating_expense")
public class OperatingExpense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "expense_name", length = 100, unique = true)
    private String expenseName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amountPerPiece;

}
