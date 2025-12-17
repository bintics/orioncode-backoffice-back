package com.orioncode.backoffice.databases.schemas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDatabaseSchemaRequestDTO {

    @NotBlank(message = "El ID del schema es requerido")
    private String id;

    @NotBlank(message = "El nombre del schema es requerido")
    private String name;

    @NotBlank(message = "El ID del servidor es requerido")
    private String serverId;

    private String description;

    @NotBlank(message = "El propietario del schema es requerido")
    private String owner;

    @Min(value = 0, message = "El número de tablas no puede ser negativo")
    private Integer tablesCount;

    @Min(value = 0, message = "El número de vistas no puede ser negativo")
    private Integer viewsCount;

    @Min(value = 0, message = "El número de procedimientos no puede ser negativo")
    private Integer proceduresCount;

    @Min(value = 0, message = "El número de funciones no puede ser negativo")
    private Integer functionsCount;

    private LocalDateTime lastModified;

    @Min(value = 0, message = "El tamaño no puede ser negativo")
    private Double sizeInMB;

}

