package com.example.webscoketnotification.controller;

import com.example.webscoketnotification.Message;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

   @Autowired
   SimpMessagingTemplate simpMessagingTemplate;

   /**
    * Aquí estamos aceptando mensajes en el /application punto final. En realidad, este
    * es un subdestino del destino de la aplicación que definimos anteriormente, que era /app. Esto
    * significa que el cliente debe enviar el mensaje al destino /app/applicationpara llegar a este controlador.
    */
   @MessageMapping("/application")
   @SendTo("/all/messages") // reenviamos el mensaje entrante a /all/messages, todos los suscritos, verán el mensaje
   public Message send(final Message message) throws Exception {
      System.out.println(message);
      return message;
   }

   /**
    * Aceptamos mensajes enviados usando '/app/private' hacia un destino '/user/specific-<user-session-id>'
    * creado luego de que un usuario incia sesión y se suscribe al destino
    * @param message
    */
   @MessageMapping("/private")
   public void sendToSpecificUser(@Payload Message message) {
      //Envia un mensaje a un destino que comienza con /user se agrega el destino /specific
      // y se adjunta la identificación de usuario
      simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
   }

   /*
    La suscripción a '/user/specific-<user-session-id>' no se realiza directamente desde el cliente como una
    suscripción explícita, sino que se configura automáticamente por Spring Security y Spring WebSocket cuando
    un usuario inicia sesión. Cuando un usuario se autentica y se establece una sesión, Spring Security agrega
    automáticamente un prefijo '/user/' al destino STOMP y adjunta el ID de sesión del usuario para formar el
    destino completo, que tiene la forma '/user/specific-<user-session-id>'.

    En tu código del cliente, tienes dos conexiones STOMP configuradas, una para mensajes generales y otra
    para mensajes privados. La conexión general se establece mediante stompClient y se suscribe a
    '/all/messages', mientras que la conexión privada se establece mediante privateStompClient y se
    suscribe a '/user/specific'. Sin embargo, en la suscripción privada, no deberías especificar
    '/user/specific' directamente, ya que Spring Security y Spring WebSocket lo manejarán automáticamente.
    En su lugar, solo suscríbete a '/user/specific' sin especificar el ID de sesión del usuario.
    */
}
