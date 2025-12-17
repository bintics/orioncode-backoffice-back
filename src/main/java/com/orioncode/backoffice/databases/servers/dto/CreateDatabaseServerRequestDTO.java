package com.orioncode.backoffice.databases.servers.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDatabaseServerRequestDTO {

    @NotBlank(message = "El ID del servidor es requerido")
    private String id;

    @NotBlank(message = "El nombre del servidor es requerido")
    private String name;

    @NotBlank(message = "El motor de base de datos es requerido")
    private String engine;

    @NotBlank(message = "La versión es requerida")
    private String version;

    @NotBlank(message = "El host es requerido")
    private String host;

    @Min(value = 1, message = "El puerto debe ser mayor a 0")
    @Max(value = 65535, message = "El puerto debe ser menor o igual a 65535")
    private int port;

    @NotBlank(message = "El ambiente es requerido")
    private String environment;

    private String description;

}

