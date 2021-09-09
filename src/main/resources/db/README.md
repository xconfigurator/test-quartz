# 说明
1. 实验情况下可以使用Spring Boot提供的：spring.quartz.jdbc.initialize-schema=always选项来自动创建schema
2. 实际生产环境持久化则从依赖包中把DDL拿出来即可。e.g. org.quartz.impl.jdbcjobstore/tables_mysql_innodb.sql

```sql
select table_schema, table_name, engine, table_rows, index_length 
from tables 
where table_schema = 'test-quartz';
```