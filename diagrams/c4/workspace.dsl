workspace "FitPass - Stage 03" "Domain Events" {

    model {

        member = person "Gym Member" "Usuario que utiliza FitPass para membresías, pagos, reservas y accesos."

        fitpass = softwareSystem "FitPass" "Plataforma digital para la gestión de una red de gimnasios." {

            backend = container "FitPass Backend" "Monolito modular que comienza a modelar eventos del dominio." "Spring Boot" {

                userModule = component "User Module" "Gestiona usuarios y roles." "Spring Boot Module"

                gymModule = component "Gym Module" "Gestiona gimnasios." "Spring Boot Module"

                membershipModule = component "Membership Module" "Gestiona membresías y membresías adquiridas por usuarios." "Spring Boot Module"

                paymentModule = component "Payment Module" "Gestiona pagos." "Spring Boot Module"

                accessModule = component "Access Module" "Gestiona check-ins y accesos a gimnasios." "Spring Boot Module"

                bookingModule = component "Booking Module" "Gestiona el dominio de reservas." "Spring Boot Module"

                paymentCompleted = component "PaymentCompleted Event" "Representa el hecho de que un pago fue completado." "Domain Event"

                userCheckedIn = component "UserCheckedIn Event" "Representa el hecho de que un usuario realizó check-in." "Domain Event"

                bookingCreated = component "BookingCreated Event" "Representa el hecho de que una reserva fue creada." "Domain Event"

                membershipModule -> userModule "Utiliza información del usuario"

                paymentModule -> membershipModule "Procesa pagos asociados a membresías"

                accessModule -> userModule "Utiliza información del usuario"

                paymentModule -> paymentCompleted "Genera"

                accessModule -> userCheckedIn "Genera"

                bookingModule -> bookingCreated "Genera"
            }

            database = container "FitPass Database" "Base PostgreSQL central compartida por todos los módulos." "PostgreSQL" "Database"

            backend -> database "Lee y escribe datos" "JPA/JDBC"
        }

        member -> fitpass "Usa"
        member -> backend "Realiza solicitudes" "HTTPS/REST"
    }

    views {

        systemContext fitpass "Stage03-C1" {
            include *
            autoLayout lr
        }

        container fitpass "Stage03-C2" {
            include *
            autoLayout lr
        }

        component backend "Stage03-C3" {
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