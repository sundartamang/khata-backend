package khata_backend.com.settings.department.controller;

import jakarta.validation.Valid;
import khata_backend.com.common.BaseResponse;
import khata_backend.com.common.ResponseBuilder;
import khata_backend.com.settings.department.facade.DepartmentFacade;
import khata_backend.com.settings.department.model.dto.DepartmentDTO;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/department")
@AllArgsConstructor
public class DepartmentController {

	private final DepartmentFacade departmentFacade;

	@PostMapping
	public ResponseEntity<BaseResponse<DepartmentDTO>> createDepartment(
			@Valid @RequestBody DepartmentDTO departmentDTO) {
		return ResponseBuilder.build(departmentFacade.createDepartment(departmentDTO));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BaseResponse<DepartmentDTO>> getDepartmentById(
			@PathVariable("id") UUID departmentId) {
		return ResponseBuilder.build(departmentFacade.getDepartmentById(departmentId));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<Page<DepartmentDTO>>> getDepartments(Pageable pageable) {
		return ResponseBuilder.build(departmentFacade.getDepartments(pageable));
	}

	@PutMapping("/{id}")
	public ResponseEntity<BaseResponse<DepartmentDTO>> updateDepartment(
			@Valid @RequestBody DepartmentDTO departmentDTO,
			@PathVariable("id") UUID departmentId) {
		return ResponseBuilder.build(departmentFacade.updateDepartment(departmentDTO, departmentId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<BaseResponse<Void>> deleteDepartment(
			@PathVariable("id") UUID departmentId) {
		return ResponseBuilder.build(departmentFacade.deleteDepartment(departmentId));
	}
}
