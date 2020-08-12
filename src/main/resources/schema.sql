/* Generate Environment
hibernate.format_sql: true
hibernate.id.new_generator_mappings: true
hibernate.physical_naming_strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
hibernate.dialect: class org.hibernate.dialect.H2Dialect
hibernate.implicit_naming_strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
*/
create sequence hibernate_sequence start with 1 increment by 1;

create table data_weather (
    id integer not null,
    uvlevel varchar(255),
    height varchar(255),
    humidity varchar(255),
    pressure varchar(255),
    temperature varchar(255),
    primary key (id)
);
