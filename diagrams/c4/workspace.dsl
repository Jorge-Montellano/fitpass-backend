workspace "FitPass - Stage 05" "RabbitMQ External Message Broker" {

    model {

        member = person "Gym Member" "Usuario que utiliza FitPass para membresías, pagos, reservas y accesos."

        fitpass = softwareSystem "FitPass" "Plataforma digital para la gestión de una red de gimnasios." {

            backend = container "FitPass Backend" "Monolito modular que publica y consume eventos mediante RabbitMQ." "Spring Boot" {

                userModule = component "User Module" "Gestiona usuarios y roles." "Spring Boot Module"

                gymModule = component "Gym Module" "Gestiona gimnasios." "Spring Boot Module"

                membershipModule = component "Membership Module" "Gestiona membresías." "Spring Boot Module"

                paymentModule = component "Payment Module" "Gestiona pagos y publica PaymentCompleted." "Spring Boot Module"

                accessModule = component "Access Module" "Gestiona check-ins y publica UserCheckedIn." "Spring Boot Module"

                bookingModule = component "Booking Module" "Gestiona reservas y publica BookingCreated." "Spring Boot Module"

                paymentListener = component "Payment Event Listener" "Consume eventos relacionados con pagos." "RabbitMQ Listener"

                accessListener = component "Access Event Listener" "Consume eventos relacionados con check-ins." "RabbitMQ Listener"

                bookingListener = component "Booking Event Listener" "Consume eventos relacionados con reservas." "RabbitMQ Listener"

                membershipModule -> userModule "Utiliza información del usuario"

                paymentModule -> membershipModule "Procesa pagos asociados a membresías"

                accessModule -> userModule "Utiliza información del usuario"
            }

            database = container "FitPass Database" "Base PostgreSQL compartida por los módulos del monolito." "PostgreSQL" "Database"

            rabbitmq = container "RabbitMQ" "Broker externo utilizado para distribuir eventos del dominio." "RabbitMQ" "Message Broker"

            backend -> database "Lee y escribe datos" "JPA/JDBC"

            paymentModule -> rabbitmq "Publica PaymentCompleted" "AMQP"
            accessModule -> rabbitmq "Publica UserCheckedIn" "AMQP"
            bookingModule -> rabbitmq "Publica BookingCreated" "AMQP"

            rabbitmq -> paymentListener "Entrega eventos de pago" "AMQP"
            rabbitmq -> accessListener "Entrega eventos de check-in" "AMQP"
            rabbitmq -> bookingListener "Entrega eventos de reserva" "AMQP"
        }

        member -> fitpass "Usa"
        member -> backend "Realiza solicitudes" "HTTPS/REST"
    }

    views {

        systemContext fitpass "Stage05-C1" {
            include *
            autoLayout lr
        }

        container fitpass "Stage05-C2" {
            include *
            autoLayout lr
        }

        component backend "Stage05-C3" {
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

            element "Message Broker" {
                shape Pipe
                background #f28c28
                color #ffffff
            }
        }
    }
}