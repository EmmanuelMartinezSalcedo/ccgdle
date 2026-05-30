# CCGDLE

CCGDLE es un juego inspirado en Wordle que utiliza cartas de Hearthstone como elemento principal de adivinanza.

## Tecnologías Utilizadas

### Backend

- **Java 25**
- **Spring Boot**
- **Spring Web** para la creación de APIs REST.
- **RestClient** para la integración con APIs externas y la sincronización automática de información de cartas de Hearthstone.
- **Spring Data JPA** y **JPA Repository** para el acceso y gestión de datos.
- **Flyway** para el versionado y migración de la base de datos.
- **PostgreSQL** como sistema de almacenamiento persistente.
- **Maven** para la gestión de dependencias y construcción del proyecto.

### Frontend

- **Angular**
- **Tailwind CSS**
- **DaisyUI**

## Características

- Sincronización automática de cartas desde APIs externas.
- Persistencia de información mediante JPA y PostgreSQL.
- Migraciones de base de datos controladas con Flyway.
- API REST desarrollada con Spring Boot.
- Sistema de intentos similar a Wordle.
- Arquitectura desacoplada entre frontend y backend.

## Arquitectura

El backend expone una API REST desarrollada con Spring Boot. La información de las cartas se obtiene mediante llamadas a servicios externos utilizando RestClient, se procesa y almacena en PostgreSQL mediante JPA Repository. Flyway se encarga de mantener la consistencia y evolución del esquema de base de datos a lo largo del tiempo.
