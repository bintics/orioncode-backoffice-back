package com.orioncode.backoffice.databases.servers.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseServerRequestDTO {

    @NotBlank(message = "El nombre del servidor es requerido")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;

    @NotBlank(message = "El motor de base de datos es requerido")
    @Size(max = 50, message = "El motor no puede exceder 50 caracteres")
    private String engine;

    @NotBlank(message = "La versión es requerida")
    @Size(max = 50, message = "La versión no puede exceder 50 caracteres")
    private String version;

    @NotBlank(message = "El host es requerido")
    @Size(max = 255, message = "El host no puede exceder 255 caracteres")
    private String host;

    @Min(value = 1, message = "El puerto debe ser mayor a 0")
    @Max(value = 65535, message = "El puerto debe ser menor o igual a 65535")
    private int port;

    @NotBlank(message = "El ambiente es requerido")
    @Size(max = 50, message = "El ambiente no puede exceder 50 caracteres")
    private String environment;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    private boolean active;

}

