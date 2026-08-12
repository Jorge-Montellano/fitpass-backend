workspace "FitPass - Stage 07" "Booking Microservice Extraction" {

    model {

        member = person "Gym Member" "Usuario que utiliza FitPass para membresías, gimnasios y reservas."

        fitpass = softwareSystem "FitPass" "Plataforma digital para la gestión de una red de gimnasios." {

            backend = container "FitPass Backend" "Monolito modular que conserva los dominios todavía no extraídos." "Spring Boot" {

                userModule = component "User Module" "Gestiona usuarios y roles." "Spring Boot Module"

                gymModule = component "Gym Module" "Gestiona gimnasios." "Spring Boot Module"

                membershipModule = component "Membership Module" "Gestiona membresías y compras de membresías." "Spring Boot Module"

                paymentModule = component "Payment Module" "Gestiona pagos y participa en la Saga." "Spring Boot Module"

                accessModule = component "Access Module" "Gestiona check-ins y acceso a gimnasios." "Spring Boot Module"

                notificationListener = component "Booking Notification Listener" "Consume BookingCreated para simular una notificación de reserva confirmada." "RabbitMQ Listener"
            }

            bookingService = container "Booking Service" "Microservicio independiente responsable de las reservas." "Spring Boot" {

                bookingController = component "Booking Controller" "Expone los endpoints REST de reservas." "Spring MVC"

                bookingServiceComponent = component "Booking Service" "Ejecuta la lógica de creación y cancelación de reservas." "Spring Service"

                bookingRepository = component "Booking Repository" "Gestiona la persistencia de reservas." "Spring Data JPA"

                bookingEventPublisher = component "Booking Event Publisher" "Publica BookingCreatedEvent después de crear una reserva." "RabbitMQ Producer"

                bookingController -> bookingServiceComponent "Usa"

                bookingServiceComponent -> bookingRepository "Persiste reservas"

                bookingServiceComponent -> bookingEventPublisher "Publica eventos después de crear una reserva"
            }

            mainDatabase = container "FitPass Database" "Base PostgreSQL utilizada por el monolito modular." "PostgreSQL" "Database"

            bookingDatabase = container "Booking Database" "Base PostgreSQL dedicada y aislada para Booking." "PostgreSQL" "Database"

            rabbitmq = container "RabbitMQ" "Broker externo para comunicación asíncrona entre dominios y servicios." "RabbitMQ" "Message Broker"

            backend -> mainDatabase "Lee y escribe datos" "JPA/JDBC"

            bookingService -> bookingDatabase "Lee y escribe reservas" "JPA/JDBC"

            bookingEventPublisher -> rabbitmq "Publica BookingCreated" "AMQP"

            rabbitmq -> notificationListener "Entrega BookingCreated" "AMQP"
        }

        member -> fitpass "Usa"

        member -> backend "Consulta funcionalidades del monolito" "HTTPS/REST"

        member -> bookingService "Crea y cancela reservas" "HTTPS/REST"
    }

    views {

        systemContext fitpass "Stage07-C1" {
            include *
            autoLayout lr
        }

        container fitpass "Stage07-C2" {
            include *
            autoLayout lr
        }

        component bookingService "Stage07-C3" {
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