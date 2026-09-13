package khata_backend.com.settings.basicSettings.operatingExpenses.repository;

import khata_backend.com.settings.basicSettings.operatingExpenses.model.entity.OperatingExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OperatingExpenseRepo extends JpaRepository<OperatingExpense, UUID> {

    boolean existsByExpenseName(String expenseName);

    boolean existsByExpenseNameAndIdNot(String expenseName, UUID operatingExpenseId);
}
