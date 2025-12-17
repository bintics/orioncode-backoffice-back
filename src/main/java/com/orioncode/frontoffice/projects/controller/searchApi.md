
Ayúdame actualizar la implementación de buscar proyectos para que soporte múltiples filtros en una sola solicitud.
La API debe permitir filtrar de la siguiente manera:

GET /projects?filter=name;team&search=api;Arquitectura de Software

Donde filter especifica los campos por los cuales se desea filtrar y search contiene los valores de búsqueda correspondientes.
Te explico los parámetros:
- filter: una lista separada por punto y coma (;) que indica los campos a filtrar. Los campos permitidos son: name, team, createdAt.
- search: una lista separada por punto y coma (;) que contiene los valores de búsqueda correspondientes a los campos especificados en filter. Los valores deben estar en el mismo orden que los campos en filter.
    - Para el campo name, el valor es una cadena que debe coincidir parcialmente con el nombre del proyecto.
    - Para el campo team, el valor es una cadena que debe coincidir parcialmente con el nombre del equipo asignado al proyecto.


