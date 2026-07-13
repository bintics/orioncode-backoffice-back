package com.orioncode.frontoffice.projects.listener;

import com.orioncode.frontoffice.projects.event.ProjectCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectCreatedListener {
    @EventListener
    public void handleProjectCreated(ProjectCreatedEvent event) {
        // Aquí se implementaría la lógica para crear el repositorio en GitHub, GitLab, etc.
        // Por ahora solo es un placeholder agnóstico
        System.out.println("Evento recibido: Creación de repositorio para el proyecto " + event.getProject().getId());
    }
}

