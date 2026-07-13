package com.orioncode.frontoffice.projects.service;

import com.orioncode.frontoffice.projects.dto.CreateProjectRequestDTO;
import com.orioncode.shared.criteria.CriterialParser;
import com.orioncode.shared.exception.ResourceNotFoundException;
import com.orioncode.shared.dto.PageResponse;
import com.orioncode.shared.dto.PaginationMetadata;
import com.orioncode.shared.dto.SearchMetadata;
import com.orioncode.frontoffice.projects.dto.ProjectRequestDTO;
import com.orioncode.frontoffice.projects.dto.ProjectResponse;
import com.orioncode.frontoffice.projects.dto.ProjectSearchResponseDTO;
import com.orioncode.frontoffice.projects.entity.Project;
import com.orioncode.frontoffice.projects.repository.ProjectRepository;
import com.orioncode.frontoffice.projects.event.ProjectCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CriterialParser criterialParser;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));
        return convertToDTO(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByOwnerId(String ownerId) {
        return projectRepository.findByOwnerId(ownerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponse createProject(CreateProjectRequestDTO requestDTO) {
        Project project = new Project();
        project.setId(requestDTO.getId());
        project.setName(requestDTO.getName());
        project.setDescription(requestDTO.getDescription());
        // Nuevo estatus agnóstico para integración externa
        project.setStatus("CREATING_REPOSITORY");
        project.setOwnerId(requestDTO.getOwnerId());
        project.setTypeId(requestDTO.getTypeId());

        Project savedProject = projectRepository.save(project);
        // Publicar evento para listeners
        eventPublisher.publishEvent(new ProjectCreatedEvent(this, savedProject));
        return convertToDTO(savedProject);
    }

    @Transactional
    public ProjectResponse updateProject(String id, ProjectRequestDTO requestDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));

        project.setName(requestDTO.getName());
        project.setDescription(requestDTO.getDescription());
        project.setStatus(requestDTO.getStatus());
        project.setTypeId(requestDTO.getTypeId());
        project.setOwnerId(requestDTO.getOwnerId());

        Project updatedProject = projectRepository.save(project);
        return convertToDTO(updatedProject);
    }

    @Transactional
    public void deleteProject(String id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proyecto no encontrado con ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProjectSearchResponseDTO> searchProjects(
            String filter, String search, Pageable pageable) {

        Specification<Project> spec = Specification.where(null);

        if (filter != null && !filter.trim().isEmpty() && search != null && !search.trim().isEmpty()) {
            spec = this.criterialParser.parse(filter, search);
        } else if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern),
                    cb.like(cb.lower(root.get("status")), searchPattern),
                    cb.like(cb.lower(root.get("typeId")), searchPattern),
                    cb.like(cb.lower(root.get("ownerId")), searchPattern)
            ));
        }

        Page<Project> page = projectRepository.findAll(spec, pageable);

        List<ProjectSearchResponseDTO> data = page.getContent().stream()
                .map(this::convertToSearchDTO)
                .collect(Collectors.toList());

        PaginationMetadata pagination = new PaginationMetadata(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        SearchMetadata metadata = new SearchMetadata(
                List.of("name", "description", "status", "typeId", "ownerId")
        );

        return new PageResponse<>(data, pagination, metadata);
    }

    private ProjectResponse convertToDTO(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getTypeId(),
                project.getOwnerId(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }

    private ProjectSearchResponseDTO convertToSearchDTO(Project project) {
        return new ProjectSearchResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getTypeId(),
                project.getOwnerId(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
