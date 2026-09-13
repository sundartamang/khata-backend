package khata_backend.com.settings.department.mapper;

import khata_backend.com.settings.department.model.dto.DepartmentDTO;
import khata_backend.com.settings.department.model.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentDTO departmentDTO){
        if(departmentDTO == null){
            return null;
        }
        Department department = new Department();
        department.setDepartmentCode(departmentDTO.getDepartmentCode());
        department.setDepartmentName(departmentDTO.getDepartmentName());
        return department;
    }

    public DepartmentDTO toDTO(Department department){
        if(department == null){
            return null;
        }
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setDepartmentCode(department.getDepartmentCode());
        departmentDTO.setDepartmentName(department.getDepartmentName());
        return departmentDTO;
    }
}
