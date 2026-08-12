workspace "FitPass - Stage 04" "In-Memory Event Bus" {

    model {

        member = person "Gym Member" "Usuario que utiliza FitPass para membresías, pagos, reservas y accesos."

        fitpass = softwareSystem "FitPass" "Plataforma digital para la gestión de una red de gimnasios." {

            backend = container "FitPass Backend" "Monolito modular con comunicación interna basada en eventos." "Spring Boot" {

                userModule = component "User Module" "Gestiona usuarios y roles." "Spring Boot Module"

                gymModule = component "Gym Module" "Gestiona gimnasios." "Spring Boot Module"

                membershipModule = component "Membership Module" "Gestiona membresías." "Spring Boot Module"

                paymentModule = component "Payment Module" "Gestiona pagos y publica eventos relacionados." "Spring Boot Module"

                accessModule = component "Access Module" "Gestiona check-ins y publica eventos relacionados." "Spring Boot Module"

                bookingModule = component "Booking Module" "Gestiona reservas y publica eventos relacionados." "Spring Boot Module"

                eventBus = component "In-Memory Event Bus" "Distribuye eventos del dominio dentro del mismo proceso de la aplicación." "Spring Application Events"

                paymentListener = component "Payment Event Listener" "Reacciona al evento PaymentCompleted." "Event Listener"

                accessListener = component "Access Event Listener" "Reacciona al evento UserCheckedIn." "Event Listener"

                bookingListener = component "Booking Event Listener" "Reacciona al evento BookingCreated." "Event Listener"


                membershipModule -> userModule "Utiliza información del usuario"

                paymentModule -> membershipModule "Procesa pagos asociados a membresías"

                accessModule -> userModule "Utiliza información del usuario"


                paymentModule -> eventBus "Publica PaymentCompleted"

                accessModule -> eventBus "Publica UserCheckedIn"

                bookingModule -> eventBus "Publica BookingCreated"


                eventBus -> paymentListener "Distribuye PaymentCompleted"

                eventBus -> accessListener "Distribuye UserCheckedIn"

                eventBus -> bookingListener "Distribuye BookingCreated"
            }

            database = container "FitPass Database" "Base PostgreSQL central compartida por los módulos." "PostgreSQL" "Database"

            backend -> database "Lee y escribe datos" "JPA/JDBC"
        }

        member -> fitpass "Usa"
        member -> backend "Realiza solicitudes" "HTTPS/REST"
    }


    views {

        systemContext fitpass "Stage04-C1" {
            include *
            autoLayout lr
        }


        container fitpass "Stage04-C2" {
            include *
            autoLayout lr
        }


        component backend "Stage04-C3" {
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