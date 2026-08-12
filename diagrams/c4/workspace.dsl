workspace "FitPass - Stage 02" "Modular Monolith" {

    model {

        member = person "Gym Member" "Usuario que utiliza FitPass para gestionar su experiencia en gimnasios."

        fitpass = softwareSystem "FitPass" "Plataforma para gestión de gimnasios, usuarios, membresías, pagos y accesos." {

            backend = container "FitPass Backend" "Monolito modular organizado por dominios de negocio." "Spring Boot" {

                userModule = component "User Module" "Gestiona usuarios, roles y datos asociados." "Spring Boot Module"

                gymModule = component "Gym Module" "Gestiona gimnasios y su información." "Spring Boot Module"

                membershipModule = component "Membership Module" "Gestiona membresías y membresías adquiridas por usuarios." "Spring Boot Module"

                paymentModule = component "Payment Module" "Gestiona los pagos realizados en FitPass." "Spring Boot Module"

                accessModule = component "Access Module" "Gestiona check-ins y acceso de usuarios a gimnasios." "Spring Boot Module"

                bookingModule = component "Booking Module" "Representa el dominio de reservas dentro del monolito." "Spring Boot Module"

                userModule -> gymModule "Consulta información asociada al gimnasio"
                membershipModule -> userModule "Utiliza información del usuario"
                paymentModule -> membershipModule "Procesa pagos asociados a membresías"
                accessModule -> userModule "Valida información del usuario"
            }

            database = container "FitPass Database" "Base de datos central compartida por los módulos del monolito." "PostgreSQL" "Database"

            backend -> database "Lee y escribe datos" "JPA/JDBC"
        }

        member -> fitpass "Usa"
        member -> backend "Realiza solicitudes" "HTTPS/REST"
    }

    views {

        systemContext fitpass "Stage02-C1" {
            include *
            autoLayout lr
        }

        container fitpass "Stage02-C2" {
            include *
            autoLayout lr
        }

        component backend "Stage02-C3" {
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