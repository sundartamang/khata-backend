package khata_backend.com.settings.department.repository;

import khata_backend.com.settings.department.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepo extends JpaRepository<Department, UUID> {
    boolean existsByDepartmentCode(String departmentCode);

    boolean existsByDepartmentCodeAndIdNot(String departmentCode, UUID departmentId);

    Optional<Department> findByDepartmentCode(String departmentCode);
}