workspace "FitPass - Stage 01" "Layered Monolith" {

    model {

        member = person "Gym Member" "Usuario de la plataforma FitPass."

        fitpass = softwareSystem "FitPass" "Plataforma digital para gestión de gimnasios, membresías, pagos y check-ins." {

            backend = container "FitPass Backend" "Aplicación backend monolítica." "Spring Boot" {

                controller = component "Controllers" "Reciben solicitudes HTTP y exponen la API REST." "Spring MVC"

                service = component "Services" "Implementan la lógica de negocio." "Spring"

                repository = component "Repositories" "Gestionan el acceso y persistencia de datos." "Spring Data JPA"

                entities = component "Entities" "Modelo persistente del dominio." "JPA / Hibernate"

                controller -> service "Usa"
                service -> repository "Usa"
                repository -> entities "Persiste"
            }

            database = container "FitPass Database" "Base de datos central del sistema." "PostgreSQL" "Database"

            backend -> database "Lee y escribe datos" "JDBC/JPA"
        }

        member -> fitpass "Usa"
        member -> backend "Realiza solicitudes" "HTTPS/REST"
    }

    views {

        systemContext fitpass "Stage01-C1" {
            include *
            autoLayout lr
        }

        container fitpass "Stage01-C2" {
            include *
            autoLayout lr
        }

        component backend "Stage01-C3" {
            include *
            autoLayout lr
        }

        styles {

            element "Person" {
                shape Person
                background #666666
                color #ffffff
            }

            element "Software System" {
                background #1168bd
                color #ffffff
            }

            element "Container" {
                background #438dd5
                color #ffffff
            }

            element "Component" {
                background #85bbf0
                color #000000
            }

            element "Database" {
                shape Cylinder
                background #2e8b57
                color #ffffff
            }
        }
    }
}