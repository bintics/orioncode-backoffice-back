package com.orioncode.frontoffice.projects.event;

import com.orioncode.frontoffice.projects.entity.Project;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectCreatedEvent extends ApplicationEvent {

    private final Project project;

    public ProjectCreatedEvent(Object source, Project project) {
        super(source);
        this.project = project;
    }

}

