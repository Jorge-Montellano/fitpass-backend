workspace "FitPass - Stage 08" "API Gateway" {

    model {

        member = person "Gym Member" "Usuario que utiliza FitPass para membresías, gimnasios y reservas."

        fitpass = softwareSystem "FitPass" "Plataforma digital para la gestión de una red de gimnasios." {

            apiGateway = container "API Gateway" "Punto único de entrada para clientes. Enruta solicitudes hacia el monolito o Booking Service." "Spring Cloud Gateway :8090" {

                bookingRoute = component "Booking Route" "Enruta /api/bookings/** hacia Booking Service." "Spring Cloud Gateway Route"

                backendRoute = component "Backend Route" "Enruta /api/** hacia FitPass Backend." "Spring Cloud Gateway Route"
            }

            backend = container "FitPass Backend" "Monolito modular que mantiene usuarios, gimnasios, membresías, pagos, accesos y notificaciones." "Spring Boot :8081" {

                userModule = component "User Module" "Gestiona usuarios y roles." "Spring Boot Module"

                gymModule = component "Gym Module" "Gestiona gimnasios." "Spring Boot Module"

                membershipModule = component "Membership Module" "Gestiona membresías y Saga de compra." "Spring Boot Module"

                paymentModule = component "Payment Module" "Gestiona pagos." "Spring Boot Module"

                accessModule = component "Access Module" "Gestiona check-ins." "Spring Boot Module"

                notificationListener = component "Booking Notification Listener" "Consume BookingCreated y simula una notificación al usuario." "RabbitMQ Listener"
            }

            bookingService = container "Booking Service" "Microservicio independiente responsable de reservas." "Spring Boot :8082" {

                bookingController = component "Booking Controller" "Expone endpoints REST de Booking." "Spring MVC"

                bookingServiceComponent = component "Booking Service" "Ejecuta la lógica de creación y cancelación de reservas." "Spring Service"

                bookingRepository = component "Booking Repository" "Gestiona persistencia de reservas." "Spring Data JPA"

                bookingEventPublisher = component "Booking Event Publisher" "Publica BookingCreatedEvent." "RabbitMQ Producer"

                bookingController -> bookingServiceComponent "Usa"

                bookingServiceComponent -> bookingRepository "Persiste reservas"

                bookingServiceComponent -> bookingEventPublisher "Publica eventos"
            }

            mainDatabase = container "FitPass Database" "Base PostgreSQL utilizada por el monolito modular." "PostgreSQL" "Database"

            bookingDatabase = container "Booking Database" "Base PostgreSQL dedicada y aislada para Booking." "PostgreSQL" "Database"

            rabbitmq = container "RabbitMQ" "Broker externo para comunicación asíncrona." "RabbitMQ" "Message Broker"

            apiGateway -> bookingService "Enruta /api/bookings/**" "HTTP"

            apiGateway -> backend "Enruta /api/**" "HTTP"

            backend -> mainDatabase "Lee y escribe datos" "JPA/JDBC"

            bookingService -> bookingDatabase "Lee y escribe reservas" "JPA/JDBC"

            bookingEventPublisher -> rabbitmq "Publica BookingCreated" "AMQP"

            rabbitmq -> notificationListener "Entrega BookingCreated" "AMQP"
        }

        member -> fitpass "Usa"

        member -> apiGateway "Realiza solicitudes a FitPass" "HTTPS/REST"
    }


    views {

        systemContext fitpass "Stage08-C1" {
            include *
            autoLayout lr
        }


        container fitpass "Stage08-C2" {
            include *
            autoLayout lr
        }


        component apiGateway "Stage08-C3" {
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