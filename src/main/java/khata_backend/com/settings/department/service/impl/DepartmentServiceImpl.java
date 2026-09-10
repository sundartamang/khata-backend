package khata_backend.com.settings.department.service.impl;

import khata_backend.com.exception.DuplicateResourceException;
import khata_backend.com.exception.ResourceNotFoundException;
import khata_backend.com.settings.department.mapper.DepartmentMapper;
import khata_backend.com.settings.department.model.dto.DepartmentDTO;
import khata_backend.com.settings.department.model.entity.Department;
import khata_backend.com.settings.department.repository.DepartmentRepo;
import khata_backend.com.settings.department.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        validateDepartmentCodeUniqueness(departmentDTO.getDepartmentCode(), null);

        Department department = departmentMapper.toEntity(departmentDTO);
        Department savedDepartment = departmentRepo.save(department);
        log.info("[SERVICE] createDepartment - department persisted with id: {} and code: {}",
                savedDepartment.getId(), savedDepartment.getDepartmentCode());
        return departmentMapper.toDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, UUID departmentId) {
        Department existingDepartment = findDepartmentById(departmentId);
        validateDepartmentCodeUniqueness(departmentDTO.getDepartmentCode(), departmentId);

        existingDepartment.setDepartmentCode(departmentDTO.getDepartmentCode());
        existingDepartment.setDepartmentName(departmentDTO.getDepartmentName());

        Department updatedDepartment = departmentRepo.save(existingDepartment);
        log.info("[SERVICE] updateDepartment - department updated with id: {}", departmentId);
        return departmentMapper.toDTO(updatedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(UUID departmentId) {
        Department department = findDepartmentById(departmentId);
        log.debug("[SERVICE] getDepartmentById - found department code: {} for id: {}",
                department.getDepartmentCode(), departmentId);
        return departmentMapper.toDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDTO> getDepartments(Pageable pageable) {
        Page<Department> departments = departmentRepo.findAll(pageable);
        log.debug("[SERVICE] getDepartments - retrieved {} department(s)", departments.getTotalElements());
        return departments.map(departmentMapper::toDTO);
    }

    @Override
    public void deleteDepartment(UUID departmentId) {
        Department department = findDepartmentById(departmentId);
        departmentRepo.delete(department);
        log.info("[SERVICE] deleteDepartment - department deleted with id: {} and code: {}",
                departmentId, department.getDepartmentCode());
    }

    private void validateDepartmentCodeUniqueness(String departmentCode, UUID excludedDepartmentId) {
        boolean exists = (excludedDepartmentId == null)
                ? departmentRepo.existsByDepartmentCode(departmentCode)
                : departmentRepo.existsByDepartmentCodeAndIdNot(departmentCode, excludedDepartmentId);

        if (exists) {
            log.warn("[SERVICE] Duplicate department code detected: {} (excluded id: {})",
                    departmentCode, excludedDepartmentId);
            throw new DuplicateResourceException("Department already exists with code: " + departmentCode);
        }
    }

    private Department findDepartmentById(UUID departmentId) {
        return departmentRepo.findById(departmentId)
                .orElseThrow(() -> {
                    log.error("[SERVICE] Department not found with id: {}", departmentId);
                    return new ResourceNotFoundException("Department not found with ID: " + departmentId);
                });
    }
}
