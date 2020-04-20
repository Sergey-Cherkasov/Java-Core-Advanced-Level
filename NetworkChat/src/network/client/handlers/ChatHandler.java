package network.client.handlers;

import network.server.models.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Класс обработчика действий клиента
 */
public class ChatHandler {
   private final Server server;
   private final Socket socket;
   private final DataInputStream inputStream;
   private final DataOutputStream outputStream;

   private String name;

   public String getName(){
      return name;
   }

   /**
    * Конструктор обработчика действий клиента запускает аутентификацию и метод чтения сообщений
    * в отдельном потоке.
    * @param server экземпляр запущенного сервера
    * @param socket экземпляр сокета
    */
   public ChatHandler(Server server, Socket socket){
      try{
         this.server = server;
         this.socket = socket;
         this.inputStream = new DataInputStream(socket.getInputStream());
         this.outputStream = new DataOutputStream(socket.getOutputStream());
         this.name = "";
         new Thread(() ->{
            try{
               authentication();
               readMessages();
            }catch (IOException e){
               System.err.println(e.getMessage());
            }finally {
               closeConnection();
            }
         });
      }catch (IOException e){
         System.err.println(e.getMessage());
         throw new RuntimeException("Ошибка при создании обработчика клиента");
      }
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
         if (stringDataFromClient.equals("/end")){
            return;
         }
         server.broadcastMessage(name + ": " + stringDataFromClient);
      }
   }

   /**
    * Метод аутентификации клиента
    * @throws IOException может вызвать исключение IOException
    */
   private void authentication() throws IOException {
      while (true){
         String stringWithData = inputStream.readUTF();
         if (stringWithData.startsWith("/auth")){
            String[] splitDataStrings = stringWithData.split("\\s+");
            String userName = server.getAuthenticationServiceInterface()
                    .getUserNameByLoginPassword(splitDataStrings[1], splitDataStrings[2]);
            if (userName != null){
               if (!server.isUserNameBusy(userName)){
                  sendMessage("/auth_ok " + userName);
                  name = userName;
                  server.broadcastMessage(name + " зашел в чат");
                  server.subscribe(this);
                  return;
               }else{
                  sendMessage("Учетная запись уже испоьзуется");
               }
            }else{
               sendMessage("Неверные логин/пароль");
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

}
