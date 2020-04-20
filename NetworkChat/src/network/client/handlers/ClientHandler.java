package network.client.handlers;

import network.ExtraClass;
import network.auth.AuthenticationServiceInterface;
import network.server.models.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static network.ExtraClass.*;

/**
 * Класс обработчика действий клиента
 */
public class ClientHandler {
   private final Server server;
   private final Socket socket;
   private DataInputStream inputStream;
   private DataOutputStream outputStream;

   private String name;

   private final AuthenticationServiceInterface authenticationServiceInterface;
   /**
    * Конструктор обработчика действий клиента запускает аутентификацию и метод чтения сообщений
    * в отдельном потоке.
    * @param server экземпляр запущенного сервера
    * @param socket экземпляр сокета
    */
   public ClientHandler(Server server, Socket socket){
      this.server = server;
      this.socket = socket;
      this.authenticationServiceInterface = server.getAuthenticationServiceInterface();
   }

   /**
    * Метод осуществляет закрытие соединения и освобождение ресурсов
    */
   private void closeConnection() {
      server.unsubscribe(this);
      server.broadcastMessage(name + " вышел из чата");
      try{
         inputStream.close();
         outputStream.close();
         socket.close();
      }catch (IOException e){
         System.err.println(e.getMessage());
      }
   }

   /**
    * Метод осуществляет чтение сообщений, полученных от сервера
    * @throws IOException может вызвать исключение IOException.
    */
   private void readMessages() throws IOException {
      while (true){
         String stringDataFromClient = inputStream.readUTF();
         System.out.println(name + ": " + stringDataFromClient);
         if (stringDataFromClient.startsWith(END_CMD)){
            return;
         } else if (stringDataFromClient.startsWith(PRIVATE_CMD)){
            String[] splitMessageParts = stringDataFromClient.split("\\s+");
            server.sendPrivateMessage(name, splitMessageParts[1], splitMessageParts[2]);
            continue;
         }
         server.broadcastMessage(String.format("%s: %s", name, stringDataFromClient));
      }
   }

   /**
    * Метод аутентификации клиента
    * @throws IOException может вызвать исключение IOException
    */
   private void authentication() throws IOException {
      while (true){
         String stringWithData = inputStream.readUTF();
         if (stringWithData.startsWith(AUTH_CMD)){
            String[] splitDataStrings = stringWithData.split("\\s+");
            String userName = authenticationServiceInterface
                    .getUserNameByLoginPassword(splitDataStrings[1], splitDataStrings[2]);
            if (userName == null){
               sendMessage("Неверные логин/пароль");
            }else if (server.isUserNameBusy(userName)){
               sendMessage("Учетная запись уже испоьзуется");
            } else{
               sendMessage(AUTH_SUCCESSFULLY + " " + userName);
               setName(userName);
               server.broadcastMessage(name + " зашел в чат");
               server.subscribe(this);
               break;
            }
         }
      }
   }

   /**
    * Метод осуществляет отправку сообщения на сервер
    * @param message текст с данными сообщения
    */
   public void sendMessage(String message) {
      try{
         outputStream.writeUTF(message);
      }catch (IOException e){
         System.err.println(e.getMessage());
      }
   }

   public void handle() throws IOException{
      try {
         inputStream = new DataInputStream(socket.getInputStream());
         outputStream = new DataOutputStream(socket.getOutputStream());

         new Thread(() ->{
            try{
               authentication();
               readMessages();
            }catch (IOException e){
               System.err.println(e.getMessage());
            }finally {
               closeConnection();
            }
         }).start();

      } catch (IOException e) {
         e.printStackTrace();
      }
      this.name = "";
   }

   private void setName(String name) {
      this.name = name;
   }

   public String getName(){
      return name;
   }

}
