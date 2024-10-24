package co.edu.unicauca.microserviciousuarios.infrastructure.broker.rabbit;

import co.edu.unicauca.microserviciousuarios.presentation.dto.UserDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private final AmqpTemplate amqpTemplate;
    private final String exchange = "myExchange";
    private final String routingKey = "routingKey";

    public MessageProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(UserDTO objClienteCreado) {
        amqpTemplate.convertAndSend(exchange, routingKey, objClienteCreado);
        System.out.println("Datos del cliente enviado a la cola");
    }
}
    