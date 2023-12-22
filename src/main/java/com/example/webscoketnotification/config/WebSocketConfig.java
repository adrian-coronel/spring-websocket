package com.example.webscoketnotification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

   /**
    * Este método se utiliza para configurar el broker de mensaje, el broker de mensajes
    * es responsable de enviar mensajes desde el servidor a los clientes conectados.
    * @param config
    */
   @Override
   public void configureMessageBroker(MessageBrokerRegistry config) {
      // Habilita un broker simple que permite a los clientes suscribirse al canal "/all" y "/specific"
      // Cuando el servidor envía un mensaje, los sucritos suscritos a este canal recibirán el mensaje.
      config.enableSimpleBroker("/all","/specific");
      // Establece un prefijo para los destinos de mensajes de la aplicación.
      config.setApplicationDestinationPrefixes("/app");
   }

   /**
    * Este método se utiliza para registrar los puntos finales de STOMP, que son las URL a
    * las que los clientes se conectarán para establecer la conexión WebSocket.
    * @param registry
    */
   @Override
   public void registerStompEndpoints(StompEndpointRegistry registry) {
      // Registro un punto final WebSocket en la ruta "/ws" al cual los clientes podrán conectarse
      // podrán conectarse y utilizar STOMP para la comunicación bidireccional
      registry.addEndpoint("/ws");
      // Esto se hace porque no todos los navegadores admiten WebSockets y,
      // cuando no está disponible, podemos recurrir a SockJS.
      registry.addEndpoint("/ws").withSockJS();
   }
}
