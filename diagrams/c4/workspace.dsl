workspace "FitPass - Stage 09" "CQRS" {

    model {

        member = person "Gym Member" "Usuario que utiliza FitPass para membresías, gimnasios y reservas."

        fitpass = softwareSystem "FitPass" "Plataforma digital para la gestión de una red de gimnasios." {

            apiGateway = container "API Gateway" "Punto único de entrada lógico para clientes." "Spring Cloud Gateway :8090"

            backend = container "FitPass Backend" "Monolito modular con usuarios, gimnasios, membresías, pagos, accesos y notificaciones." "Spring Boot :8081"

            bookingService = container "Booking Service" "Microservicio independiente de reservas con separación CQRS." "Spring Boot :8082" {

                bookingController = component "Booking Controller" "Recibe solicitudes REST y delega hacia Command o Query Side." "Spring MVC"

                commandService = component "Booking Command Service" "Gestiona operaciones que modifican el estado: crear y cancelar reservas." "CQRS Command Side"

                queryService = component "Booking Query Service" "Gestiona operaciones de solo lectura: listar y buscar reservas." "CQRS Query Side"

                bookingRepository = component "Booking Repository" "Gestiona persistencia de reservas." "Spring Data JPA"

                eventPublisher = component "Booking Event Publisher" "Publica BookingCreatedEvent después de una operación de escritura." "RabbitMQ Producer"

                bookingController -> commandService "POST / PUT"

                bookingController -> queryService "GET"

                commandService -> bookingRepository "Escribe datos"

                queryService -> bookingRepository "Lee datos"

                commandService -> eventPublisher "Publica eventos después de cambios"
            }

            mainDatabase = container "FitPass Database" "Base PostgreSQL utilizada por el monolito." "PostgreSQL" "Database"

            bookingDatabase = container "Booking Database" "Base PostgreSQL dedicada al Booking Service." "PostgreSQL" "Database"

            rabbitmq = container "RabbitMQ" "Broker externo para comunicación asíncrona." "RabbitMQ" "Message Broker"

            apiGateway -> backend "Enruta /api/**" "HTTP"

            apiGateway -> bookingService "Enruta /api/bookings/**" "HTTP"

            backend -> mainDatabase "Lee y escribe datos" "JPA/JDBC"

            bookingService -> bookingDatabase "Lee y escribe reservas" "JPA/JDBC"

            eventPublisher -> rabbitmq "Publica BookingCreated" "AMQP"
        }

        member -> fitpass "Usa"

        member -> apiGateway "Realiza solicitudes a FitPass" "HTTPS/REST"
    }

    views {

        systemContext fitpass "Stage09-C1" {
            include *
            autoLayout lr
        }

        container fitpass "Stage09-C2" {
            include *
            autoLayout lr
        }

        component bookingService "Stage09-C3" {
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