package khata_backend.com.settings.basicSettings.operatingExpenses.service.impl;

import khata_backend.com.exception.DuplicateResourceException;
import khata_backend.com.exception.ResourceNotFoundException;
import khata_backend.com.settings.basicSettings.operatingExpenses.mapper.OperatingExpenseMapper;
import khata_backend.com.settings.basicSettings.operatingExpenses.model.dto.OperatingExpenseDTO;
import khata_backend.com.settings.basicSettings.operatingExpenses.model.entity.OperatingExpense;
import khata_backend.com.settings.basicSettings.operatingExpenses.repository.OperatingExpenseRepo;
import khata_backend.com.settings.basicSettings.operatingExpenses.service.OperatingExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OperatingExpenseServiceImpl implements OperatingExpenseService {

	private final OperatingExpenseRepo operatingExpenseRepo;
	private final OperatingExpenseMapper operatingExpenseMapper;

	@Override
	public OperatingExpenseDTO createOperatingExpense(OperatingExpenseDTO operatingExpenseDTO) {
		validateExpenseNameUniqueness(operatingExpenseDTO.getExpenseName(), null);

		OperatingExpense operatingExpense = operatingExpenseMapper.toEntity(operatingExpenseDTO);
		OperatingExpense savedExpense = operatingExpenseRepo.save(operatingExpense);
		log.info("[SERVICE] createOperatingExpense - expense persisted with id: {} and name: {}",
				savedExpense.getId(), savedExpense.getExpenseName());
		return operatingExpenseMapper.toDTO(savedExpense);
	}

	@Override
	public OperatingExpenseDTO updateOperatingExpense(
			OperatingExpenseDTO operatingExpenseDTO, UUID operatingExpenseId) {
		OperatingExpense existingExpense = findOperatingExpenseById(operatingExpenseId);
		validateExpenseNameUniqueness(operatingExpenseDTO.getExpenseName(), operatingExpenseId);

		existingExpense.setExpenseName(operatingExpenseDTO.getExpenseName());
		existingExpense.setAmountPerPiece(operatingExpenseDTO.getAmountPerPiece());

		OperatingExpense updatedExpense = operatingExpenseRepo.save(existingExpense);
		log.info("[SERVICE] updateOperatingExpense - expense updated with id: {}", operatingExpenseId);
		return operatingExpenseMapper.toDTO(updatedExpense);
	}

	@Override
	@Transactional(readOnly = true)
	public OperatingExpenseDTO getOperatingExpenseById(UUID operatingExpenseId) {
		OperatingExpense operatingExpense = findOperatingExpenseById(operatingExpenseId);
		log.debug("[SERVICE] getOperatingExpenseById - found expense name: {} for id: {}",
				operatingExpense.getExpenseName(), operatingExpenseId);
		return operatingExpenseMapper.toDTO(operatingExpense);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OperatingExpenseDTO> getOperatingExpenses(Pageable pageable) {
		Page<OperatingExpense> expenses = operatingExpenseRepo.findAll(pageable);
		log.debug("[SERVICE] getOperatingExpenses - retrieved {} operating expense(s)",
				expenses.getTotalElements());
		return expenses.map(operatingExpenseMapper::toDTO);
	}

	@Override
	public void deleteOperatingExpense(UUID operatingExpenseId) {
		OperatingExpense operatingExpense = findOperatingExpenseById(operatingExpenseId);
		operatingExpenseRepo.delete(operatingExpense);
		log.info("[SERVICE] deleteOperatingExpense - expense deleted with id: {} and name: {}",
				operatingExpenseId, operatingExpense.getExpenseName());
	}

	private void validateExpenseNameUniqueness(String expenseName, UUID excludedOperatingExpenseId) {
		boolean exists = excludedOperatingExpenseId == null
				? operatingExpenseRepo.existsByExpenseName(expenseName)
				: operatingExpenseRepo.existsByExpenseNameAndIdNot(expenseName, excludedOperatingExpenseId);

		if (exists) {
			log.warn("[SERVICE] Duplicate operating expense name detected: {} (excluded id: {})",
					expenseName, excludedOperatingExpenseId);
			throw new DuplicateResourceException("Operating expense already exists with name: " + expenseName);
		}
	}

	private OperatingExpense findOperatingExpenseById(UUID operatingExpenseId) {
		return operatingExpenseRepo.findById(operatingExpenseId)
				.orElseThrow(() -> {
					log.error("[SERVICE] Operating expense not found with id: {}", operatingExpenseId);
					return new ResourceNotFoundException(
							"Operating expense not found with ID: " + operatingExpenseId);
				});
	}
}
