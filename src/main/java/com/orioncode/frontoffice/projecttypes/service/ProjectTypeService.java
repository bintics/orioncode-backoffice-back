package com.orioncode.frontoffice.projecttypes.service;

import com.orioncode.shared.exception.ResourceNotFoundException;
import com.orioncode.shared.dto.PageResponse;
import com.orioncode.shared.dto.PaginationMetadata;
import com.orioncode.shared.dto.SearchMetadata;
import com.orioncode.frontoffice.projecttypes.dto.ProjectTypeRequestDTO;
import com.orioncode.frontoffice.projecttypes.dto.ProjectTypeResponseDTO;
import com.orioncode.frontoffice.projecttypes.dto.ProjectTypeSearchResponseDTO;
import com.orioncode.frontoffice.projecttypes.entity.ProjectType;
import com.orioncode.frontoffice.projecttypes.repository.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectTypeService {

    private final ProjectTypeRepository projectTypeRepository;

    @Transactional(readOnly = true)
    public List<ProjectTypeResponseDTO> getAllProjectTypes() {
        return projectTypeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectTypeResponseDTO getProjectTypeById(String id) {
        ProjectType projectType = projectTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de proyecto no encontrado con ID: " + id));
        return convertToDTO(projectType);
    }

    @Transactional(readOnly = true)
    public ProjectTypeResponseDTO getProjectTypeByName(String name) {
        ProjectType projectType = projectTypeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de proyecto no encontrado con nombre: " + name));
        return convertToDTO(projectType);
    }

    @Transactional
    public ProjectTypeResponseDTO createProjectType(ProjectTypeRequestDTO requestDTO) {
        // Validar que se proporcione un ID
        String projectTypeId = requestDTO.getId();
        if (projectTypeId == null || projectTypeId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del tipo de proyecto es requerido");
        }

        // Validar que no exista un tipo con el mismo ID
        if (projectTypeRepository.existsById(projectTypeId)) {
            throw new IllegalArgumentException("Ya existe un tipo de proyecto con el ID: " + projectTypeId);
        }

        // Validar que no exista un tipo con el mismo nombre
        if (projectTypeRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un tipo de proyecto con el nombre: " + requestDTO.getName());
        }

        ProjectType projectType = new ProjectType();
        projectType.setId(projectTypeId);
        projectType.setName(requestDTO.getName());
        projectType.setDescription(requestDTO.getDescription());

        ProjectType savedProjectType = projectTypeRepository.save(projectType);
        return convertToDTO(savedProjectType);
    }

    @Transactional
    public ProjectTypeResponseDTO updateProjectType(String id, ProjectTypeRequestDTO requestDTO) {
        ProjectType projectType = projectTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de proyecto no encontrado con ID: " + id));

        // Validar que no exista otro tipo con el mismo nombre
        if (!projectType.getName().equals(requestDTO.getName()) &&
            projectTypeRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un tipo de proyecto con el nombre: " + requestDTO.getName());
        }

        projectType.setName(requestDTO.getName());
        projectType.setDescription(requestDTO.getDescription());

        ProjectType updatedProjectType = projectTypeRepository.save(projectType);
        return convertToDTO(updatedProjectType);
    }

    @Transactional
    public void deleteProjectType(String id) {
        if (!projectTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de proyecto no encontrado con ID: " + id);
        }
        projectTypeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProjectTypeSearchResponseDTO> searchProjectTypes(
            String filter, String search, Pageable pageable) {

        Specification<ProjectType> spec = Specification.where(null);

        if (filter != null && !filter.trim().isEmpty() && search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> {
                switch (filter.toLowerCase()) {
                    case "name":
                        return cb.like(cb.lower(root.get("name")), searchPattern);
                    case "description":
                        return cb.like(cb.lower(root.get("description")), searchPattern);
                    default:
                        return cb.or(
                            cb.like(cb.lower(root.get("name")), searchPattern),
                            cb.like(cb.lower(root.get("description")), searchPattern)
                        );
                }
            });
        } else if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), searchPattern),
                cb.like(cb.lower(root.get("description")), searchPattern)
            ));
        }

        Page<ProjectType> page = projectTypeRepository.findAll(spec, pageable);

        List<ProjectTypeSearchResponseDTO> data = page.getContent().stream()
                .map(this::convertToSearchDTO)
                .collect(Collectors.toList());

        PaginationMetadata pagination = new PaginationMetadata(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        SearchMetadata metadata = new SearchMetadata(
                List.of("name", "description")
        );

        return new PageResponse<>(data, pagination, metadata);
    }

    private ProjectTypeResponseDTO convertToDTO(ProjectType projectType) {
        return new ProjectTypeResponseDTO(
                projectType.getId(),
                projectType.getName(),
                projectType.getDescription(),
                projectType.getCreatedAt(),
                projectType.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ProjectTypeResponseDTO> getProjectTypesByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<ProjectType> projectTypes = projectTypeRepository.findAllById(ids);
        return projectTypes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProjectTypeSearchResponseDTO convertToSearchDTO(ProjectType projectType) {
        return new ProjectTypeSearchResponseDTO(
                projectType.getId(),
                projectType.getName(),
                projectType.getDescription(),
                projectType.getCreatedAt(),
                projectType.getUpdatedAt()
        );
    }
}

