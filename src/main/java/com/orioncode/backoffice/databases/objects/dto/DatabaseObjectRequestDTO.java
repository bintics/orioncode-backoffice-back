package com.orioncode.backoffice.databases.objects.dto;

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
public class DatabaseObjectRequestDTO {

    @NotBlank(message = "El nombre del objeto es requerido")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;

    @NotBlank(message = "El tipo del objeto es requerido")
    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    private String type;

    @NotBlank(message = "El ID del schema es requerido")
    @Size(max = 100, message = "El ID del schema no puede exceder 100 caracteres")
    private String schemaId;

    @Size(max = 100, message = "El propietario no puede exceder 100 caracteres")
    private String owner;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @Min(value = 0, message = "El número de filas no puede ser negativo")
    private Double rowCount;

    @Min(value = 0, message = "El tamaño no puede ser negativo")
    private Double sizeInKB;

}

