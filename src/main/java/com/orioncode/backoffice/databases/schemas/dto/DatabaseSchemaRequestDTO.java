package com.orioncode.backoffice.databases.schemas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseSchemaRequestDTO {

    @NotBlank(message = "El nombre del schema es requerido")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;

    @NotBlank(message = "El ID del servidor es requerido")
    @Size(max = 100, message = "El ID del servidor no puede exceder 100 caracteres")
    private String serverId;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @NotBlank(message = "El propietario del schema es requerido")
    @Size(max = 100, message = "El propietario no puede exceder 100 caracteres")
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

