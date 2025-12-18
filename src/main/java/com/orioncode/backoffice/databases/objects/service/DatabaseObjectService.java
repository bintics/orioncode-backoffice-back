package com.orioncode.backoffice.databases.objects.service;

import com.orioncode.backoffice.databases.objects.dto.CreateDatabaseObjectRequestDTO;
import com.orioncode.backoffice.databases.objects.dto.DatabaseObjectRequestDTO;
import com.orioncode.backoffice.databases.objects.dto.DatabaseObjectResponse;
import com.orioncode.backoffice.databases.objects.dto.DatabaseObjectSearchResponseDTO;
import com.orioncode.backoffice.databases.objects.entity.DatabaseObject;
import com.orioncode.backoffice.databases.objects.repository.DatabaseObjectRepository;
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
public class DatabaseObjectService {

    private final DatabaseObjectRepository databaseObjectRepository;
    private final CriterialParser criterialParser;

    @Transactional(readOnly = true)
    public DatabaseObjectResponse getById(String id) {
        DatabaseObject databaseObject = databaseObjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objeto de base de datos no encontrado con ID: " + id));
        return convertToDTO(databaseObject);
    }

    @Transactional
    public DatabaseObjectResponse create(CreateDatabaseObjectRequestDTO requestDTO) {
        DatabaseObject databaseObject = new DatabaseObject();
        databaseObject.setId(requestDTO.getId());
        databaseObject.setName(requestDTO.getName());
        databaseObject.setType(requestDTO.getType());
        databaseObject.setSchemaId(requestDTO.getSchemaId());
        databaseObject.setOwner(requestDTO.getOwner());
        databaseObject.setDescription(requestDTO.getDescription());
        databaseObject.setRowCount(requestDTO.getRowCount());
        databaseObject.setSizeInKB(requestDTO.getSizeInKB());
        databaseObject.setCreatedAt(LocalDateTime.now());
        databaseObject.setUpdatedAt(LocalDateTime.now());

        DatabaseObject savedObject = databaseObjectRepository.save(databaseObject);
        return convertToDTO(savedObject);
    }

    @Transactional
    public DatabaseObjectResponse update(String id, DatabaseObjectRequestDTO requestDTO) {
        DatabaseObject databaseObject = databaseObjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objeto de base de datos no encontrado con ID: " + id));

        databaseObject.setName(requestDTO.getName());
        databaseObject.setType(requestDTO.getType());
        databaseObject.setSchemaId(requestDTO.getSchemaId());
        databaseObject.setOwner(requestDTO.getOwner());
        databaseObject.setDescription(requestDTO.getDescription());
        databaseObject.setRowCount(requestDTO.getRowCount());
        databaseObject.setSizeInKB(requestDTO.getSizeInKB());
        databaseObject.setUpdatedAt(LocalDateTime.now());

        DatabaseObject updatedObject = databaseObjectRepository.save(databaseObject);
        return convertToDTO(updatedObject);
    }

    @Transactional
    public void delete(String id) {
        if (!databaseObjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Objeto de base de datos no encontrado con ID: " + id);
        }
        databaseObjectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<DatabaseObjectSearchResponseDTO> search(
            String filter, String search, Pageable pageable) {

        Specification<DatabaseObject> spec = Specification.where(null);

        if (filter != null && !filter.trim().isEmpty() && search != null && !search.trim().isEmpty()) {
            spec = this.criterialParser.parse(filter, search);
        } else if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("type")), searchPattern),
                    cb.like(cb.lower(root.get("schemaId")), searchPattern),
                    cb.like(cb.lower(root.get("owner")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern)
            ));
        }

        Page<DatabaseObject> page = databaseObjectRepository.findAll(spec, pageable);

        List<DatabaseObjectSearchResponseDTO> data = page.getContent().stream()
                .map(this::convertToSearchDTO)
                .collect(Collectors.toList());

        PaginationMetadata pagination = new PaginationMetadata(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        SearchMetadata metadata = new SearchMetadata(
                List.of("name", "type", "schemaId", "owner", "description")
        );

        return new PageResponse<>(data, pagination, metadata);
    }

    private DatabaseObjectResponse convertToDTO(DatabaseObject databaseObject) {
        return new DatabaseObjectResponse(
                databaseObject.getId(),
                databaseObject.getName(),
                databaseObject.getType(),
                databaseObject.getSchemaId(),
                databaseObject.getOwner(),
                databaseObject.getDescription(),
                databaseObject.getRowCount(),
                databaseObject.getSizeInKB(),
                databaseObject.getCreatedAt(),
                databaseObject.getUpdatedAt()
        );
    }

    private DatabaseObjectSearchResponseDTO convertToSearchDTO(DatabaseObject databaseObject) {
        return new DatabaseObjectSearchResponseDTO(
                databaseObject.getId(),
                databaseObject.getName(),
                databaseObject.getType(),
                databaseObject.getSchemaId(),
                databaseObject.getOwner(),
                databaseObject.getDescription(),
                databaseObject.getRowCount(),
                databaseObject.getSizeInKB(),
                databaseObject.getCreatedAt(),
                databaseObject.getUpdatedAt()
        );
    }
}

