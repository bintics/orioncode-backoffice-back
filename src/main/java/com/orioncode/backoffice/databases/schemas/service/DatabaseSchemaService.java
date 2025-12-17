package com.orioncode.backoffice.databases.schemas.service;

import com.orioncode.backoffice.databases.schemas.dto.CreateDatabaseSchemaRequestDTO;
import com.orioncode.backoffice.databases.schemas.dto.DatabaseSchemaRequestDTO;
import com.orioncode.backoffice.databases.schemas.dto.DatabaseSchemaResponse;
import com.orioncode.backoffice.databases.schemas.dto.DatabaseSchemaSearchResponseDTO;
import com.orioncode.backoffice.databases.schemas.entity.DatabaseSchema;
import com.orioncode.backoffice.databases.schemas.repository.DatabaseSchemaRepository;
import com.orioncode.shared.criteria.CriterialParser;
import com.orioncode.shared.dto.PageResponse;
import com.orioncode.shared.dto.PaginationMetadata;
import com.orioncode.shared.dto.SearchMetadata;
import com.orioncode.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatabaseSchemaService {

    private final DatabaseSchemaRepository databaseSchemaRepository;
    private final CriterialParser criterialParser;

    @Transactional(readOnly = true)
    public DatabaseSchemaResponse getById(String id) {
        DatabaseSchema schema = databaseSchemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schema de base de datos no encontrado con ID: " + id));
        return convertToDTO(schema);
    }

    @Transactional
    public DatabaseSchemaResponse create(CreateDatabaseSchemaRequestDTO requestDTO) {
        DatabaseSchema schema = new DatabaseSchema();
        schema.setId(requestDTO.getId());
        schema.setName(requestDTO.getName());
        schema.setServerId(requestDTO.getServerId());
        schema.setDescription(requestDTO.getDescription());
        schema.setOwner(requestDTO.getOwner());
        schema.setTablesCount(requestDTO.getTablesCount());
        schema.setViewsCount(requestDTO.getViewsCount());
        schema.setProceduresCount(requestDTO.getProceduresCount());
        schema.setFunctionsCount(requestDTO.getFunctionsCount());
        schema.setLastModified(requestDTO.getLastModified());
        schema.setSizeInMB(requestDTO.getSizeInMB());
        schema.setCreatedAt(LocalDateTime.now());
        schema.setUpdatedAt(LocalDateTime.now());

        DatabaseSchema savedSchema = databaseSchemaRepository.save(schema);
        return convertToDTO(savedSchema);
    }

    @Transactional
    public DatabaseSchemaResponse update(String id, DatabaseSchemaRequestDTO requestDTO) {
        DatabaseSchema schema = databaseSchemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schema de base de datos no encontrado con ID: " + id));

        schema.setName(requestDTO.getName());
        schema.setServerId(requestDTO.getServerId());
        schema.setDescription(requestDTO.getDescription());
        schema.setOwner(requestDTO.getOwner());
        schema.setTablesCount(requestDTO.getTablesCount());
        schema.setViewsCount(requestDTO.getViewsCount());
        schema.setProceduresCount(requestDTO.getProceduresCount());
        schema.setFunctionsCount(requestDTO.getFunctionsCount());
        schema.setLastModified(requestDTO.getLastModified());
        schema.setSizeInMB(requestDTO.getSizeInMB());
        schema.setUpdatedAt(LocalDateTime.now());

        DatabaseSchema updatedSchema = databaseSchemaRepository.save(schema);
        return convertToDTO(updatedSchema);
    }

    @Transactional
    public void delete(String id) {
        if (!databaseSchemaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schema de base de datos no encontrado con ID: " + id);
        }
        databaseSchemaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<DatabaseSchemaSearchResponseDTO> search(
            String filter, String search, Pageable pageable) {

        Specification<DatabaseSchema> spec = Specification.where(null);

        if (filter != null && !filter.trim().isEmpty() && search != null && !search.trim().isEmpty()) {
            spec = this.criterialParser.parse(filter, search);
        } else if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("serverId")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern),
                    cb.like(cb.lower(root.get("owner")), searchPattern)
            ));
        }

        Page<DatabaseSchema> page = databaseSchemaRepository.findAll(spec, pageable);

        List<DatabaseSchemaSearchResponseDTO> data = page.getContent().stream()
                .map(this::convertToSearchDTO)
                .collect(Collectors.toList());

        PaginationMetadata pagination = new PaginationMetadata(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        SearchMetadata metadata = new SearchMetadata(
                List.of("name", "serverId", "description", "owner")
        );

        return new PageResponse<>(data, pagination, metadata);
    }

    private DatabaseSchemaResponse convertToDTO(DatabaseSchema schema) {
        return new DatabaseSchemaResponse(
                schema.getId(),
                schema.getName(),
                schema.getServerId(),
                schema.getDescription(),
                schema.getOwner(),
                schema.getTablesCount(),
                schema.getViewsCount(),
                schema.getProceduresCount(),
                schema.getFunctionsCount(),
                schema.getLastModified(),
                schema.getSizeInMB(),
                schema.getCreatedAt(),
                schema.getUpdatedAt()
        );
    }

    private DatabaseSchemaSearchResponseDTO convertToSearchDTO(DatabaseSchema schema) {
        return new DatabaseSchemaSearchResponseDTO(
                schema.getId(),
                schema.getName(),
                schema.getServerId(),
                schema.getDescription(),
                schema.getOwner(),
                schema.getTablesCount(),
                schema.getViewsCount(),
                schema.getProceduresCount(),
                schema.getFunctionsCount(),
                schema.getLastModified(),
                schema.getSizeInMB(),
                schema.getCreatedAt(),
                schema.getUpdatedAt()
        );
    }
}

