package com.orioncode.backoffice.databases.servers.service;

import com.orioncode.backoffice.databases.servers.dto.CreateDatabaseServerRequestDTO;
import com.orioncode.backoffice.databases.servers.dto.DatabaseServerRequestDTO;
import com.orioncode.backoffice.databases.servers.dto.DatabaseServerResponse;
import com.orioncode.backoffice.databases.servers.dto.DatabaseServerSearchResponseDTO;
import com.orioncode.backoffice.databases.servers.entity.DatabaseServer;
import com.orioncode.backoffice.databases.servers.repository.DatabaseServerRepository;
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
public class DatabaseServerService {

    private final DatabaseServerRepository databaseServerRepository;
    private final CriterialParser criterialParser;

    @Transactional(readOnly = true)
    public DatabaseServerResponse getById(String id) {
        DatabaseServer server = databaseServerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor de base de datos no encontrado con ID: " + id));
        return convertToDTO(server);
    }

    @Transactional
    public DatabaseServerResponse create(CreateDatabaseServerRequestDTO requestDTO) {
        DatabaseServer server = new DatabaseServer();
        server.setId(requestDTO.getId());
        server.setName(requestDTO.getName());
        server.setEngine(requestDTO.getEngine());
        server.setVersion(requestDTO.getVersion());
        server.setHost(requestDTO.getHost());
        server.setPort(requestDTO.getPort());
        server.setEnvironment(requestDTO.getEnvironment());
        server.setDescription(requestDTO.getDescription());
        server.setActive(true);
        server.setCreatedAt(LocalDateTime.now());
        server.setUpdatedAt(LocalDateTime.now());

        DatabaseServer savedServer = databaseServerRepository.save(server);
        return convertToDTO(savedServer);
    }

    @Transactional
    public DatabaseServerResponse update(String id, DatabaseServerRequestDTO requestDTO) {
        DatabaseServer server = databaseServerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor de base de datos no encontrado con ID: " + id));

        server.setName(requestDTO.getName());
        server.setEngine(requestDTO.getEngine());
        server.setVersion(requestDTO.getVersion());
        server.setHost(requestDTO.getHost());
        server.setPort(requestDTO.getPort());
        server.setEnvironment(requestDTO.getEnvironment());
        server.setDescription(requestDTO.getDescription());
        server.setActive(requestDTO.isActive());
        server.setUpdatedAt(LocalDateTime.now());

        DatabaseServer updatedServer = databaseServerRepository.save(server);
        return convertToDTO(updatedServer);
    }

    @Transactional
    public void delete(String id) {
        if (!databaseServerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Servidor de base de datos no encontrado con ID: " + id);
        }
        databaseServerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<DatabaseServerSearchResponseDTO> search(
            String filter, String search, Pageable pageable) {

        Specification<DatabaseServer> spec = Specification.where(null);

        if (filter != null && !filter.trim().isEmpty() && search != null && !search.trim().isEmpty()) {
            spec = this.criterialParser.parse(filter, search);
        } else if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("engine")), searchPattern),
                    cb.like(cb.lower(root.get("version")), searchPattern),
                    cb.like(cb.lower(root.get("host")), searchPattern),
                    cb.like(cb.lower(root.get("environment")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern)
            ));
        }

        Page<DatabaseServer> page = databaseServerRepository.findAll(spec, pageable);

        List<DatabaseServerSearchResponseDTO> data = page.getContent().stream()
                .map(this::convertToSearchDTO)
                .collect(Collectors.toList());

        PaginationMetadata pagination = new PaginationMetadata(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        SearchMetadata metadata = new SearchMetadata(
                List.of("name", "engine", "version", "host", "environment", "description")
        );

        return new PageResponse<>(data, pagination, metadata);
    }

    private DatabaseServerResponse convertToDTO(DatabaseServer server) {
        return new DatabaseServerResponse(
                server.getId(),
                server.getName(),
                server.getEngine(),
                server.getVersion(),
                server.getHost(),
                server.getPort(),
                server.getEnvironment(),
                server.getDescription(),
                server.isActive(),
                server.getCreatedAt(),
                server.getUpdatedAt()
        );
    }

    private DatabaseServerSearchResponseDTO convertToSearchDTO(DatabaseServer server) {
        return new DatabaseServerSearchResponseDTO(
                server.getId(),
                server.getName(),
                server.getEngine(),
                server.getVersion(),
                server.getHost(),
                server.getPort(),
                server.getEnvironment(),
                server.getDescription(),
                server.isActive(),
                server.getCreatedAt(),
                server.getUpdatedAt()
        );
    }
}

