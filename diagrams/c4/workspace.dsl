workspace "FitPass - Stage 06" "Saga Choreography" {

    model {

        member = person "Gym Member" "Usuario que compra membresías y utiliza los servicios de FitPass."

        fitpass = softwareSystem "FitPass" "Plataforma digital para la gestión de una red de gimnasios." {

            backend = container "FitPass Backend" "Monolito modular con Saga por coreografía para la compra de membresías." "Spring Boot" {

                membershipModule = component "Membership Module" "Gestiona membresías de usuarios y participa en la Saga." "Spring Boot Module"

                paymentModule = component "Payment Module" "Gestiona el procesamiento de pagos." "Spring Boot Module"

                membershipPurchaseListener = component "Membership Purchase Listener" "Consume MembershipPurchaseRequested y simula el procesamiento del pago." "RabbitMQ Listener"

                membershipSagaListener = component "Membership Saga Listener" "Consume PaymentCompleted o PaymentFailed y actualiza UserMembership." "RabbitMQ Listener"

                paymentEventListener = component "Payment Event Listener" "Consume PaymentCompleted como consumidor independiente." "RabbitMQ Listener"

                membershipSagaListener -> membershipModule "Activa o cancela la membresía"
            }

            database = container "FitPass Database" "Base PostgreSQL compartida por los módulos del monolito." "PostgreSQL" "Database"

            rabbitmq = container "RabbitMQ" "Broker externo utilizado para coordinar la Saga mediante eventos." "RabbitMQ" "Message Broker"

            backend -> database "Lee y escribe datos" "JPA/JDBC"

            membershipModule -> rabbitmq "Publica MembershipPurchaseRequested" "AMQP"

            rabbitmq -> membershipPurchaseListener "Entrega MembershipPurchaseRequested" "AMQP"

            membershipPurchaseListener -> rabbitmq "Publica PaymentCompleted o PaymentFailed" "AMQP"

            rabbitmq -> membershipSagaListener "Entrega PaymentCompleted o PaymentFailed" "AMQP"

            rabbitmq -> paymentEventListener "Entrega PaymentCompleted" "AMQP"
        }

        member -> fitpass "Usa"
        member -> backend "Solicita compra de membresía" "HTTPS/REST"
    }

    views {

        systemContext fitpass "Stage06-C1" {
            include *
            autoLayout lr
        }

        container fitpass "Stage06-C2" {
            include *
            autoLayout lr
        }

        component backend "Stage06-C3" {
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