package khata_backend.com.settings.department.facade;

import khata_backend.com.common.BaseResponse;
import khata_backend.com.exception.InvalidDataException;
import khata_backend.com.settings.department.model.dto.DepartmentDTO;
import khata_backend.com.settings.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepartmentFacade {

	private final DepartmentService departmentService;

	public BaseResponse<DepartmentDTO> createDepartment(DepartmentDTO departmentDTO) {
		requirePayload(departmentDTO);

		DepartmentDTO created = departmentService.createDepartment(departmentDTO);
		return BaseResponse.<DepartmentDTO>builder()
				.code(HttpStatus.CREATED.value())
				.message("Department created successfully")
				.data(created)
				.build();
	}

	public BaseResponse<DepartmentDTO> updateDepartment(DepartmentDTO departmentDTO, UUID departmentId) {
		requireValidId(departmentId);
		requirePayload(departmentDTO);

		DepartmentDTO updated = departmentService.updateDepartment(departmentDTO, departmentId);
		return BaseResponse.<DepartmentDTO>builder()
				.code(HttpStatus.OK.value())
				.message("Department updated successfully")
				.data(updated)
				.build();
	}

	public BaseResponse<DepartmentDTO> getDepartmentById(UUID departmentId) {
		requireValidId(departmentId);

		DepartmentDTO department = departmentService.getDepartmentById(departmentId);
		return BaseResponse.<DepartmentDTO>builder()
				.code(HttpStatus.OK.value())
				.message("Department fetched successfully")
				.data(department)
				.build();
	}

	public BaseResponse<Page<DepartmentDTO>> getDepartments(org.springframework.data.domain.Pageable pageable) {
		if (pageable == null) {
			throw new InvalidDataException("Pageable data cannot be null");
		}

		Page<DepartmentDTO> departments = departmentService.getDepartments(pageable);
		return BaseResponse.<Page<DepartmentDTO>>builder()
				.code(HttpStatus.OK.value())
				.message("Departments fetched successfully")
				.data(departments)
				.build();
	}

	public BaseResponse<Void> deleteDepartment(UUID departmentId) {
		requireValidId(departmentId);

		departmentService.deleteDepartment(departmentId);
		return BaseResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.message("Department deleted successfully")
				.build();
	}

	private void requirePayload(DepartmentDTO departmentDTO) {
		if (departmentDTO == null) {
			throw new InvalidDataException("Department data cannot be null");
		}
	}

	private void requireValidId(UUID departmentId) {
		if (departmentId == null) {
			throw new InvalidDataException("Department ID cannot be null");
		}
	}
}
