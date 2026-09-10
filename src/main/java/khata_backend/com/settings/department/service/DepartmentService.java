package khata_backend.com.settings.department.service;

import khata_backend.com.settings.department.model.dto.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DepartmentService {

    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, UUID departmentId);

    DepartmentDTO getDepartmentById(UUID departmentId);

    Page<DepartmentDTO> getDepartments(Pageable pageable);

    void deleteDepartment(UUID departmentId);
}
