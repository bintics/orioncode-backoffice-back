package com.orioncode.backoffice.databases.objects.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDatabaseObjectRequestDTO {

    @NotBlank(message = "El ID del objeto es requerido")
    private String id;

    @NotBlank(message = "El nombre del objeto es requerido")
    private String name;

    @NotBlank(message = "El tipo del objeto es requerido")
    private String type;

    @NotBlank(message = "El ID del schema es requerido")
    private String schemaId;

    private String owner;

    private String description;

    @Min(value = 0, message = "El número de filas no puede ser negativo")
    private Double rowCount;

    @Min(value = 0, message = "El tamaño no puede ser negativo")
    private Double sizeInKB;

}


