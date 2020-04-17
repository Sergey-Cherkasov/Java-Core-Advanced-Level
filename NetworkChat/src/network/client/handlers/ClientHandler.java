package network.client.handlers;

import network.server.models.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
   private final Server server;
   private final Socket socket;
   private final DataInputStream inputStream;
   private final DataOutputStream outputStream;

   private String name;

   public String getName(){
      return name;
   }

   public ClientHandler(Server server, Socket socket){
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

   public void sendMessage(String message) {
      try{
         outputStream.writeUTF(message);
      }catch (IOException e){
         System.err.println(e.getMessage());
      }
   }

}
