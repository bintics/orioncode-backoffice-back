-- Ejemplos de inserción de objetos de base de datos

-- Tablas
INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-001', 'users', 'TABLE', 'dbs-001', 'admin', 'Tabla de usuarios del sistema', 15000, 2048.5, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-002', 'roles', 'TABLE', 'dbs-001', 'admin', 'Tabla de roles y permisos', 250, 128.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-003', 'user_roles', 'TABLE', 'dbs-001', 'admin', 'Relación usuarios-roles', 20000, 512.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-004', 'audit_logs', 'TABLE', 'dbs-001', 'admin', 'Logs de auditoría del sistema', 500000, 8192.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-005', 'sessions', 'TABLE', 'dbs-001', 'admin', 'Sesiones activas de usuarios', 5000, 1024.0, NOW(), NOW());

-- Vistas
INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-006', 'v_active_users', 'VIEW', 'dbs-001', 'admin', 'Vista de usuarios activos', 12000, 0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-007', 'v_user_permissions', 'VIEW', 'dbs-001', 'admin', 'Vista de permisos por usuario', 15000, 0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-008', 'v_audit_summary', 'VIEW', 'dbs-001', 'admin', 'Resumen de auditoría', 500000, 0, NOW(), NOW());

-- Procedimientos almacenados
INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-009', 'sp_create_user', 'PROCEDURE', 'dbs-001', 'admin', 'Crear nuevo usuario', NULL, 16.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-010', 'sp_assign_role', 'PROCEDURE', 'dbs-001', 'admin', 'Asignar rol a usuario', NULL, 12.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-011', 'sp_cleanup_sessions', 'PROCEDURE', 'dbs-001', 'admin', 'Limpiar sesiones expiradas', NULL, 8.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-012', 'sp_audit_report', 'PROCEDURE', 'dbs-001', 'admin', 'Generar reporte de auditoría', NULL, 24.0, NOW(), NOW());

-- Funciones
INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-013', 'fn_user_permissions', 'FUNCTION', 'dbs-001', 'admin', 'Obtener permisos de usuario', NULL, 8.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-014', 'fn_is_admin', 'FUNCTION', 'dbs-001', 'admin', 'Verificar si usuario es admin', NULL, 4.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-015', 'fn_session_valid', 'FUNCTION', 'dbs-001', 'admin', 'Validar sesión activa', NULL, 6.0, NOW(), NOW());

-- Triggers
INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-016', 'trg_user_audit', 'TRIGGER', 'dbs-001', 'admin', 'Auditar cambios en usuarios', NULL, 8.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-017', 'trg_role_audit', 'TRIGGER', 'dbs-001', 'admin', 'Auditar cambios en roles', NULL, 8.0, NOW(), NOW());

-- Índices
INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-018', 'idx_users_email', 'INDEX', 'dbs-001', 'admin', 'Índice en email de usuarios', NULL, 256.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-019', 'idx_users_created_at', 'INDEX', 'dbs-001', 'admin', 'Índice en fecha de creación', NULL, 128.0, NOW(), NOW());

INSERT INTO database_objects (id, name, type, schema_id, owner, description, row_count, size_in_kb, created_at, updated_at)
VALUES ('dbo-020', 'idx_audit_logs_timestamp', 'INDEX', 'dbs-001', 'admin', 'Índice en timestamp de logs', NULL, 512.0, NOW(), NOW());

