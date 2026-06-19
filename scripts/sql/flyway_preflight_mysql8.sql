SELECT DATABASE() AS current_database;

SELECT @@version AS mysql_version;

SELECT table_name
FROM information_schema.tables
WHERE table_schema = DATABASE()
  AND table_name IN ('user', 'key_results', 'refresh_tokens', 'flyway_schema_history')
ORDER BY table_name;

SELECT table_name, engine, table_collation
FROM information_schema.tables
WHERE table_schema = DATABASE()
  AND table_name IN ('user', 'key_results', 'refresh_tokens', 'flyway_schema_history')
ORDER BY table_name;

SELECT table_name,
       column_name,
       column_type,
       is_nullable,
       column_default,
       extra
FROM information_schema.columns
WHERE table_schema = DATABASE()
  AND table_name IN ('user', 'key_results', 'refresh_tokens')
ORDER BY table_name, ordinal_position;

SELECT table_name,
       index_name,
       non_unique,
       seq_in_index,
       column_name
FROM information_schema.statistics
WHERE table_schema = DATABASE()
  AND table_name IN ('user', 'key_results', 'refresh_tokens')
ORDER BY table_name, index_name, seq_in_index;

SELECT table_name,
       constraint_name,
       constraint_type
FROM information_schema.table_constraints
WHERE table_schema = DATABASE()
  AND table_name IN ('user', 'key_results', 'refresh_tokens')
ORDER BY table_name, constraint_type, constraint_name;

SELECT table_name,
       constraint_name,
       column_name,
       referenced_table_name,
       referenced_column_name
FROM information_schema.key_column_usage
WHERE table_schema = DATABASE()
  AND table_name IN ('user', 'key_results', 'refresh_tokens')
  AND referenced_table_name IS NOT NULL
ORDER BY table_name, constraint_name, ordinal_position;

SELECT trigger_name,
       event_object_table,
       action_timing,
       event_manipulation
FROM information_schema.triggers
WHERE trigger_schema = DATABASE()
  AND event_object_table IN ('user', 'key_results', 'refresh_tokens')
ORDER BY event_object_table, trigger_name;

SELECT routine_name,
       routine_type
FROM information_schema.routines
WHERE routine_schema = DATABASE()
ORDER BY routine_type, routine_name;

SELECT event_name,
       status
FROM information_schema.events
WHERE event_schema = DATABASE()
ORDER BY event_name;

SHOW CREATE TABLE `user`;
SHOW CREATE TABLE key_results;
SHOW CREATE TABLE refresh_tokens;

SELECT installed_rank,
       version,
       description,
       type,
       script,
       success
FROM flyway_schema_history
ORDER BY installed_rank;
