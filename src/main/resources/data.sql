-- =====================================================
-- Script SQL para poblar la base de datos
-- Generado el: 2025-11-10
-- Contenido: Puestos, Equipos y Colaboradores de ejemplo (INSERTs explícitos)
-- Compatible MySQL 5.7+ y H2 (sin CTE)
-- =====================================================

-- Limpiar datos existentes (comentar si no deseas eliminar datos)
-- DELETE FROM collaborator_tags;
-- DELETE FROM collaborators;
-- DELETE FROM teams;
-- DELETE FROM positions;

-- =====================================================
-- INSERTAR PUESTOS (20 posiciones)
-- =====================================================

INSERT INTO positions (id, name, description, created_at, updated_at) VALUES
('pos-001', 'Software Developer', 'Desarrollador de software con experiencia inicial', NOW(), NOW()),
('pos-002', 'Software Developer SSr', 'Desarrollador de software semi-senior con 2-4 años de experiencia', NOW(), NOW()),
('pos-003', 'Software Developer Sr', 'Desarrollador de software senior con más de 5 años de experiencia', NOW(), NOW()),
('pos-004', 'Technical Leader', 'Líder técnico responsable de la dirección técnica del equipo', NOW(), NOW()),
('pos-005', 'Manager', 'Gerente de equipo responsable de la gestión de proyectos y personas', NOW(), NOW()),
('pos-006', 'Sr Manager', 'Gerente senior con responsabilidad sobre múltiples equipos', NOW(), NOW()),
('pos-007', 'QA Engineer', 'Ingeniero de calidad encargado de testing y aseguramiento de calidad', NOW(), NOW()),
('pos-008', 'QA Automation Engineer', 'Ingeniero de automatización de pruebas', NOW(), NOW()),
('pos-009', 'DevOps Engineer', 'Ingeniero DevOps especializado en CI/CD y automatización', NOW(), NOW()),
('pos-010', 'Cloud Architect', 'Arquitecto de soluciones en la nube', NOW(), NOW()),
('pos-011', 'Data Engineer', 'Ingeniero de datos especializado en pipelines y procesamiento', NOW(), NOW()),
('pos-012', 'ML Engineer', 'Ingeniero de Machine Learning', NOW(), NOW()),
('pos-013', 'Security Engineer', 'Ingeniero de seguridad informática', NOW(), NOW()),
('pos-014', 'Frontend Developer', 'Desarrollador especializado en frontend', NOW(), NOW()),
('pos-015', 'Backend Developer', 'Desarrollador especializado en backend', NOW(), NOW()),
('pos-016', 'Mobile Developer', 'Desarrollador de aplicaciones móviles', NOW(), NOW()),
('pos-017', 'Full Stack Developer', 'Desarrollador full stack', NOW(), NOW()),
('pos-018', 'UI/UX Designer', 'Diseñador de interfaces y experiencia de usuario', NOW(), NOW()),
('pos-019', 'Product Owner', 'Dueño de producto responsable de la visión del producto', NOW(), NOW()),
('pos-020', 'Scrum Master', 'Facilitador de metodologías ágiles', NOW(), NOW());

-- =====================================================
-- INSERTAR EQUIPOS (15 equipos)
-- =====================================================

INSERT INTO teams (id, name, description, created_at, updated_at) VALUES
('team-001', 'Desarrollo Backend', 'Equipo dedicado al desarrollo de servicios backend, APIs y lógica de negocio', NOW(), NOW()),
('team-002', 'Desarrollo Frontend', 'Equipo enfocado en la creación de interfaces de usuario modernas y responsivas', NOW(), NOW()),
('team-003', 'Desarrollo Mobile', 'Equipo especializado en desarrollo de aplicaciones móviles nativas y multiplataforma', NOW(), NOW()),
('team-004', 'DevOps e Infraestructura', 'Equipo responsable de automatización, CI/CD y gestión de infraestructura', NOW(), NOW()),
('team-005', 'Quality Assurance', 'Equipo dedicado al aseguramiento de calidad y testing de software', NOW(), NOW()),
('team-006', 'Arquitectura de Software', 'Equipo encargado del diseño arquitectónico y decisiones técnicas estratégicas', NOW(), NOW()),
('team-007', 'Data Engineering', 'Equipo especializado en pipelines de datos, ETL y big data', NOW(), NOW()),
('team-008', 'Machine Learning', 'Equipo enfocado en modelos de ML, IA y análisis predictivo', NOW(), NOW()),
('team-009', 'Ciberseguridad', 'Equipo dedicado a la seguridad informática y protección de sistemas', NOW(), NOW()),
('team-010', 'Desarrollo Full Stack', 'Equipo con capacidades tanto en frontend como backend', NOW(), NOW()),
('team-011', 'Cloud Solutions', 'Equipo especializado en soluciones y arquitectura cloud', NOW(), NOW()),
('team-012', 'Inteligencia de Negocios', 'Equipo enfocado en análisis de datos y visualización', NOW(), NOW()),
('team-013', 'Soporte Técnico', 'Equipo de soporte y mantenimiento de aplicaciones', NOW(), NOW()),
('team-014', 'Innovación y R&D', 'Equipo de investigación y desarrollo de nuevas tecnologías', NOW(), NOW()),
('team-015', 'Integración de Sistemas', 'Equipo especializado en integración de sistemas empresariales', NOW(), NOW());

-- =====================================================
-- INSERTAR COLABORADORES (EJEMPLOS EXPLÍCITOS, SIN CTE)
-- Si prefieres más filas avísame y genero el bloque completo.
-- Patrón rotación: nombres(20) -> equipos(15) -> rangos de posición.
-- =====================================================
-- DELETE FROM collaborator_tags;
-- DELETE FROM collaborators;

INSERT INTO collaborators (id, first_name, last_name, position_id, team_id, created_at, updated_at) VALUES
('coll-0001','Juan','Pérez','pos-001','team-001',NOW(),NOW()),
('coll-0002','María','García','pos-001','team-002',NOW(),NOW()),
('coll-0003','Pedro','López','pos-001','team-003',NOW(),NOW()),
('coll-0004','Lucía','Rodríguez','pos-001','team-004',NOW(),NOW()),
('coll-0005','Carlos','Fernández','pos-001','team-005',NOW(),NOW()),
('coll-0006','Ana','Martínez','pos-001','team-006',NOW(),NOW()),
('coll-0007','Jorge','Sánchez','pos-001','team-007',NOW(),NOW()),
('coll-0008','Sofía','Ramírez','pos-001','team-008',NOW(),NOW()),
('coll-0009','Luis','Herrera','pos-001','team-009',NOW(),NOW()),
('coll-0010','Laura','Castro','pos-001','team-010',NOW(),NOW()),
('coll-0011','Andrés','Morales','pos-001','team-011',NOW(),NOW()),
('coll-0012','Paula','Guzmán','pos-001','team-012',NOW(),NOW()),
('coll-0013','Miguel','Navarro','pos-001','team-013',NOW(),NOW()),
('coll-0014','Daniela','Ríos','pos-001','team-014',NOW(),NOW()),
('coll-0015','Fernando','Vargas','pos-001','team-015',NOW(),NOW()),
('coll-0016','Gabriela','Mendoza','pos-001','team-001',NOW(),NOW()),
('coll-0017','José','Aguilar','pos-001','team-002',NOW(),NOW()),
('coll-0018','Valentina','Domínguez','pos-001','team-003',NOW(),NOW()),
('coll-0019','Ricardo','Campos','pos-001','team-004',NOW(),NOW()),
('coll-0020','Elena','Vega','pos-001','team-005',NOW(),NOW()),
('coll-0021','Juan','Pérez','pos-001','team-006',NOW(),NOW()),
('coll-0022','María','García','pos-001','team-007',NOW(),NOW()),
('coll-0023','Pedro','López','pos-001','team-008',NOW(),NOW()),
('coll-0024','Lucía','Rodríguez','pos-001','team-009',NOW(),NOW()),
('coll-0025','Carlos','Fernández','pos-001','team-010',NOW(),NOW()),
('coll-0026','Ana','Martínez','pos-001','team-011',NOW(),NOW()),
('coll-0027','Jorge','Sánchez','pos-001','team-012',NOW(),NOW()),
('coll-0028','Sofía','Ramírez','pos-001','team-013',NOW(),NOW()),
('coll-0029','Luis','Herrera','pos-001','team-014',NOW(),NOW()),
('coll-0030','Laura','Castro','pos-001','team-015',NOW(),NOW()),
('coll-0031','Andrés','Morales','pos-001','team-001',NOW(),NOW()),
('coll-0032','Paula','Guzmán','pos-001','team-002',NOW(),NOW()),
('coll-0033','Miguel','Navarro','pos-001','team-003',NOW(),NOW()),
('coll-0034','Daniela','Ríos','pos-001','team-004',NOW(),NOW()),
('coll-0035','Fernando','Vargas','pos-001','team-005',NOW(),NOW()),
('coll-0036','Gabriela','Mendoza','pos-001','team-006',NOW(),NOW()),
('coll-0037','José','Aguilar','pos-001','team-007',NOW(),NOW()),
('coll-0038','Valentina','Domínguez','pos-001','team-008',NOW(),NOW()),
('coll-0039','Ricardo','Campos','pos-001','team-009',NOW(),NOW()),
('coll-0040','Elena','Vega','pos-001','team-010',NOW(),NOW());

-- Nuevos colaboradores (coll-0041 a coll-0140)
INSERT INTO collaborators (id, first_name, last_name, position_id, team_id, created_at, updated_at) VALUES
('coll-0041','Juan','Pérez','pos-001','team-011',NOW(),NOW()),
('coll-0042','María','García','pos-001','team-012',NOW(),NOW()),
('coll-0043','Pedro','López','pos-001','team-013',NOW(),NOW()),
('coll-0044','Lucía','Rodríguez','pos-001','team-014',NOW(),NOW()),
('coll-0045','Carlos','Fernández','pos-001','team-015',NOW(),NOW()),
('coll-0046','Ana','Martínez','pos-001','team-001',NOW(),NOW()),
('coll-0047','Jorge','Sánchez','pos-001','team-002',NOW(),NOW()),
('coll-0048','Sofía','Ramírez','pos-001','team-003',NOW(),NOW()),
('coll-0049','Luis','Herrera','pos-001','team-004',NOW(),NOW()),
('coll-0050','Laura','Castro','pos-001','team-005',NOW(),NOW()),
('coll-0051','Andrés','Morales','pos-001','team-006',NOW(),NOW()),
('coll-0052','Paula','Guzmán','pos-001','team-007',NOW(),NOW()),
('coll-0053','Miguel','Navarro','pos-001','team-008',NOW(),NOW()),
('coll-0054','Daniela','Ríos','pos-001','team-009',NOW(),NOW()),
('coll-0055','Fernando','Vargas','pos-001','team-010',NOW(),NOW()),
('coll-0056','Gabriela','Mendoza','pos-001','team-011',NOW(),NOW()),
('coll-0057','José','Aguilar','pos-001','team-012',NOW(),NOW()),
('coll-0058','Valentina','Domínguez','pos-001','team-013',NOW(),NOW()),
('coll-0059','Ricardo','Campos','pos-001','team-014',NOW(),NOW()),
('coll-0060','Elena','Vega','pos-001','team-015',NOW(),NOW()),
('coll-0061','Juan','Pérez','pos-001','team-001',NOW(),NOW()),
('coll-0062','María','García','pos-001','team-002',NOW(),NOW()),
('coll-0063','Pedro','López','pos-001','team-003',NOW(),NOW()),
('coll-0064','Lucía','Rodríguez','pos-001','team-004',NOW(),NOW()),
('coll-0065','Carlos','Fernández','pos-001','team-005',NOW(),NOW()),
('coll-0066','Ana','Martínez','pos-001','team-006',NOW(),NOW()),
('coll-0067','Jorge','Sánchez','pos-001','team-007',NOW(),NOW()),
('coll-0068','Sofía','Ramírez','pos-001','team-008',NOW(),NOW()),
('coll-0069','Luis','Herrera','pos-001','team-009',NOW(),NOW()),
('coll-0070','Laura','Castro','pos-001','team-010',NOW(),NOW()),
('coll-0071','Andrés','Morales','pos-001','team-011',NOW(),NOW()),
('coll-0072','Paula','Guzmán','pos-001','team-012',NOW(),NOW()),
('coll-0073','Miguel','Navarro','pos-001','team-013',NOW(),NOW()),
('coll-0074','Daniela','Ríos','pos-001','team-014',NOW(),NOW()),
('coll-0075','Fernando','Vargas','pos-001','team-015',NOW(),NOW()),
('coll-0076','Gabriela','Mendoza','pos-001','team-001',NOW(),NOW()),
('coll-0077','José','Aguilar','pos-001','team-002',NOW(),NOW()),
('coll-0078','Valentina','Domínguez','pos-001','team-003',NOW(),NOW()),
('coll-0079','Ricardo','Campos','pos-001','team-004',NOW(),NOW()),
('coll-0080','Elena','Vega','pos-001','team-005',NOW(),NOW()),
('coll-0081','Juan','Pérez','pos-001','team-006',NOW(),NOW()),
('coll-0082','María','García','pos-001','team-007',NOW(),NOW()),
('coll-0083','Pedro','López','pos-001','team-008',NOW(),NOW()),
('coll-0084','Lucía','Rodríguez','pos-001','team-009',NOW(),NOW()),
('coll-0085','Carlos','Fernández','pos-001','team-010',NOW(),NOW()),
('coll-0086','Ana','Martínez','pos-001','team-011',NOW(),NOW()),
('coll-0087','Jorge','Sánchez','pos-001','team-012',NOW(),NOW()),
('coll-0088','Sofía','Ramírez','pos-001','team-013',NOW(),NOW()),
('coll-0089','Luis','Herrera','pos-001','team-014',NOW(),NOW()),
('coll-0090','Laura','Castro','pos-001','team-015',NOW(),NOW()),
('coll-0091','Andrés','Morales','pos-001','team-001',NOW(),NOW()),
('coll-0092','Paula','Guzmán','pos-001','team-002',NOW(),NOW()),
('coll-0093','Miguel','Navarro','pos-001','team-003',NOW(),NOW()),
('coll-0094','Daniela','Ríos','pos-001','team-004',NOW(),NOW()),
('coll-0095','Fernando','Vargas','pos-001','team-005',NOW(),NOW()),
('coll-0096','Gabriela','Mendoza','pos-001','team-006',NOW(),NOW()),
('coll-0097','José','Aguilar','pos-001','team-007',NOW(),NOW()),
('coll-0098','Valentina','Domínguez','pos-001','team-008',NOW(),NOW()),
('coll-0099','Ricardo','Campos','pos-001','team-009',NOW(),NOW()),
('coll-0100','Elena','Vega','pos-001','team-010',NOW(),NOW()),
('coll-0101','Juan','Pérez','pos-001','team-011',NOW(),NOW()),
('coll-0102','María','García','pos-001','team-012',NOW(),NOW()),
('coll-0103','Pedro','López','pos-001','team-013',NOW(),NOW()),
('coll-0104','Lucía','Rodríguez','pos-001','team-014',NOW(),NOW()),
('coll-0105','Carlos','Fernández','pos-001','team-015',NOW(),NOW()),
('coll-0106','Ana','Martínez','pos-001','team-001',NOW(),NOW()),
('coll-0107','Jorge','Sánchez','pos-001','team-002',NOW(),NOW()),
('coll-0108','Sofía','Ramírez','pos-001','team-003',NOW(),NOW()),
('coll-0109','Luis','Herrera','pos-001','team-004',NOW(),NOW()),
('coll-0110','Laura','Castro','pos-001','team-005',NOW(),NOW()),
('coll-0111','Andrés','Morales','pos-001','team-006',NOW(),NOW()),
('coll-0112','Paula','Guzmán','pos-001','team-007',NOW(),NOW()),
('coll-0113','Miguel','Navarro','pos-001','team-008',NOW(),NOW()),
('coll-0114','Daniela','Ríos','pos-001','team-009',NOW(),NOW()),
('coll-0115','Fernando','Vargas','pos-001','team-010',NOW(),NOW()),
('coll-0116','Gabriela','Mendoza','pos-001','team-011',NOW(),NOW()),
('coll-0117','José','Aguilar','pos-001','team-012',NOW(),NOW()),
('coll-0118','Valentina','Domínguez','pos-001','team-013',NOW(),NOW()),
('coll-0119','Ricardo','Campos','pos-001','team-014',NOW(),NOW()),
('coll-0120','Elena','Vega','pos-001','team-015',NOW(),NOW()),
('coll-0121','Juan','Pérez','pos-001','team-001',NOW(),NOW()),
('coll-0122','María','García','pos-001','team-002',NOW(),NOW()),
('coll-0123','Pedro','López','pos-001','team-003',NOW(),NOW()),
('coll-0124','Lucía','Rodríguez','pos-001','team-004',NOW(),NOW()),
('coll-0125','Carlos','Fernández','pos-001','team-005',NOW(),NOW()),
('coll-0126','Ana','Martínez','pos-001','team-006',NOW(),NOW()),
('coll-0127','Jorge','Sánchez','pos-001','team-007',NOW(),NOW()),
('coll-0128','Sofía','Ramírez','pos-001','team-008',NOW(),NOW()),
('coll-0129','Luis','Herrera','pos-001','team-009',NOW(),NOW()),
('coll-0130','Laura','Castro','pos-001','team-010',NOW(),NOW()),
('coll-0131','Andrés','Morales','pos-001','team-011',NOW(),NOW()),
('coll-0132','Paula','Guzmán','pos-001','team-012',NOW(),NOW()),
('coll-0133','Miguel','Navarro','pos-001','team-013',NOW(),NOW()),
('coll-0134','Daniela','Ríos','pos-001','team-014',NOW(),NOW()),
('coll-0135','Fernando','Vargas','pos-001','team-015',NOW(),NOW()),
('coll-0136','Gabriela','Mendoza','pos-001','team-001',NOW(),NOW()),
('coll-0137','José','Aguilar','pos-001','team-002',NOW(),NOW()),
('coll-0138','Valentina','Domínguez','pos-001','team-003',NOW(),NOW()),
('coll-0139','Ricardo','Campos','pos-001','team-004',NOW(),NOW()),
('coll-0140','Elena','Vega','pos-001','team-005',NOW(),NOW());

-- Ejemplo de tags (primeros 10 colaboradores)
-- DELETE FROM collaborator_tags;
INSERT INTO collaborator_tags (collaborator_id, tag) VALUES
('coll-0001','Java'),('coll-0001','Clean Code'),
('coll-0002','Spring'),('coll-0002','Design Patterns'),
('coll-0003','SQL'),('coll-0003','NoSQL'),
('coll-0004','React'),('coll-0004','TypeScript'),
('coll-0005','Angular'),('coll-0005','Vue'),
('coll-0006','Node.js'),('coll-0006','GCP'),
('coll-0007','AWS'),('coll-0007','Azure'),
('coll-0008','Docker'),('coll-0008','CI/CD'),
('coll-0009','Kubernetes'),('coll-0009','Bash'),
('coll-0010','Python'),('coll-0010','Go');

-- =====================================================
-- INSERTAR PROYECTOS (30 proyectos de ejemplo)
-- =====================================================

INSERT INTO projects (name, description, status, type_id, owner_id, created_at, updated_at) VALUES
('E-Commerce Platform', 'Plataforma de comercio electrónico escalable con integración de pagos', 'ACTIVE', 'WEB_APPLICATION', 'team-001', NOW(), NOW()),
('Customer Portal', 'Portal web para gestión de clientes y autoservicio', 'ACTIVE', 'WEB_APPLICATION', 'team-002', NOW(), NOW()),
('Mobile Banking App', 'Aplicación móvil para operaciones bancarias', 'ACTIVE', 'MOBILE_APP', 'team-003', NOW(), NOW()),
('CI/CD Pipeline', 'Infraestructura automatizada de integración y despliegue continuo', 'ACTIVE', 'INFRASTRUCTURE', 'team-004', NOW(), NOW()),
('Automated Testing Suite', 'Suite de pruebas automatizadas end-to-end', 'ACTIVE', 'TOOLING', 'team-005', NOW(), NOW()),
('Microservices Architecture', 'Migración de monolito a arquitectura de microservicios', 'ACTIVE', 'ARCHITECTURE', 'team-006', NOW(), NOW()),
('Data Warehouse', 'Sistema de almacenamiento y procesamiento de datos empresariales', 'ACTIVE', 'DATA_ENGINEERING', 'team-007', NOW(), NOW()),
('Recommendation Engine', 'Motor de recomendaciones basado en ML', 'ACTIVE', 'MACHINE_LEARNING', 'team-008', NOW(), NOW()),
('Security Audit System', 'Sistema de auditoría y monitoreo de seguridad', 'ACTIVE', 'SECURITY', 'team-009', NOW(), NOW()),
('Admin Dashboard', 'Panel de administración con reportes y métricas', 'ACTIVE', 'WEB_APPLICATION', 'team-010', NOW(), NOW()),
('Cloud Migration', 'Migración de infraestructura on-premise a la nube', 'ACTIVE', 'INFRASTRUCTURE', 'team-011', NOW(), NOW()),
('Business Intelligence', 'Plataforma de análisis y visualización de datos', 'ACTIVE', 'DATA_ENGINEERING', 'team-012', NOW(), NOW()),
('Support Ticketing System', 'Sistema de gestión de tickets de soporte', 'ACTIVE', 'WEB_APPLICATION', 'team-013', NOW(), NOW()),
('IoT Platform', 'Plataforma para gestión de dispositivos IoT', 'ACTIVE', 'IOT', 'team-014', NOW(), NOW()),
('ERP Integration', 'Integración con sistemas ERP empresariales', 'ACTIVE', 'INTEGRATION', 'team-015', NOW(), NOW()),
('API Gateway', 'Gateway centralizado para gestión de APIs', 'DRAFT', 'MICROSERVICE', 'team-001', NOW(), NOW()),
('Payment Gateway', 'Integración de múltiples métodos de pago', 'DRAFT', 'INTEGRATION', 'team-002', NOW(), NOW()),
('Social Media Integration', 'Integración con redes sociales principales', 'DRAFT', 'INTEGRATION', 'team-003', NOW(), NOW()),
('Monitoring Dashboard', 'Dashboard de monitoreo de infraestructura', 'DRAFT', 'TOOLING', 'team-004', NOW(), NOW()),
('Load Testing Framework', 'Framework para pruebas de carga y rendimiento', 'DRAFT', 'TOOLING', 'team-005', NOW(), NOW()),
('Service Mesh Implementation', 'Implementación de service mesh para microservicios', 'ARCHIVED', 'INFRASTRUCTURE', 'team-006', NOW(), NOW()),
('Legacy System Migration', 'Migración de sistemas legacy a tecnologías modernas', 'ARCHIVED', 'ARCHITECTURE', 'team-007', NOW(), NOW()),
('Chatbot Platform', 'Plataforma de chatbots con IA conversacional', 'ACTIVE', 'MACHINE_LEARNING', 'team-008', NOW(), NOW()),
('Identity Management', 'Sistema de gestión de identidades y accesos', 'ACTIVE', 'SECURITY', 'team-009', NOW(), NOW()),
('Content Management System', 'CMS personalizado para gestión de contenido', 'ACTIVE', 'WEB_APPLICATION', 'team-010', NOW(), NOW()),
('Kubernetes Cluster', 'Cluster de Kubernetes para orquestación de contenedores', 'ACTIVE', 'INFRASTRUCTURE', 'team-011', NOW(), NOW()),
('Real-time Analytics', 'Sistema de analytics en tiempo real', 'ACTIVE', 'DATA_ENGINEERING', 'team-012', NOW(), NOW()),
('Knowledge Base', 'Base de conocimiento interna para equipos', 'DRAFT', 'WEB_APPLICATION', 'team-013', NOW(), NOW()),
('Blockchain Integration', 'Integración con tecnología blockchain', 'DRAFT', 'INTEGRATION', 'team-014', NOW(), NOW()),
('Video Streaming Platform', 'Plataforma de streaming de video bajo demanda', 'ACTIVE', 'WEB_APPLICATION', 'team-015', NOW(), NOW());

-- =====================================================
-- INSERTAR TIPOS DE PROYECTO (11 tipos predefinidos)
-- =====================================================

INSERT INTO project_types (id, name, description, created_at, updated_at) VALUES
('pt-001', 'WEB_APPLICATION', 'Aplicaciones web tradicionales, portales, dashboards y sistemas web interactivos', NOW(), NOW()),
('pt-002', 'MOBILE_APP', 'Aplicaciones móviles nativas o híbridas para iOS, Android o multiplataforma', NOW(), NOW()),
('pt-003', 'MICROSERVICE', 'Servicios independientes que forman parte de una arquitectura de microservicios', NOW(), NOW()),
('pt-004', 'INFRASTRUCTURE', 'Proyectos relacionados con infraestructura, DevOps y plataforma', NOW(), NOW()),
('pt-005', 'DATA_ENGINEERING', 'Proyectos de ingeniería de datos, ETL, data warehouses y análisis', NOW(), NOW()),
('pt-006', 'MACHINE_LEARNING', 'Proyectos de inteligencia artificial, machine learning y data science', NOW(), NOW()),
('pt-007', 'SECURITY', 'Proyectos enfocados en seguridad, auditoría y cumplimiento', NOW(), NOW()),
('pt-008', 'INTEGRATION', 'Proyectos de integración entre sistemas, APIs y plataformas externas', NOW(), NOW()),
('pt-009', 'TOOLING', 'Herramientas internas, frameworks y utilidades para desarrolladores', NOW(), NOW()),
('pt-010', 'IOT', 'Proyectos relacionados con Internet of Things y dispositivos conectados', NOW(), NOW()),
('pt-011', 'ARCHITECTURE', 'Proyectos de diseño arquitectónico, migraciones y modernizaciones', NOW(), NOW());

