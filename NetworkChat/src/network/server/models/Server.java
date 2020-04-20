package network.server.models;

import network.auth.AuthenticationService;
import network.client.handlers.ClientHandler;
import network.auth.AuthenticationServiceInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** Класс Server создает экземпляр сервера для обмена сообщениями с данными между клиентами. */

public class Server {

   private static Server server;
   private final int port;
   private final AuthenticationServiceInterface authenticationServiceInterface;
   private final List<ClientHandler> clients;


   private Server(){
      this.port = 82_83;
      this.clients = new ArrayList<>();
      this.authenticationServiceInterface = new AuthenticationService();
   }

   private Server(int port){
      this.port = port;
      this.clients = new ArrayList<>();
      this.authenticationServiceInterface = new AuthenticationService();
   }

   public static Server getServer(int... port) {
      if (server == null) {
         if (port.length != 0){
            server = new Server(port[0]);
         }else{
            server = new Server();
         }
      }
      return server;
   }

   /**
    * Метод осуществляет инициализацию сервера и ожидает подключения клиента.
    * При подключении клиента создает экземпляр обработчика действий клиента.
    */
   public void initConnection(){
      try(ServerSocket serverSocket = new ServerSocket(port)) {
         authenticationServiceInterface.start();
         while (true){
            System.out.println("Сервер ожидает подключения клиента");
            Socket socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            ClientHandler handler = new ClientHandler(this, socket);
            try {
               handler.handle();
            } catch (IOException e) {
               System.err.println("Failed to handle client connection");
               socket.close();
               e.printStackTrace();
            }
         }
      } catch (IOException e) {
         System.err.println(e.getMessage());
         e.printStackTrace();
      } finally {
         authenticationServiceInterface.stop();
      }
   }

   public AuthenticationServiceInterface getAuthenticationServiceInterface(){
      return authenticationServiceInterface;
   }

   /**
    * Метод осуществляет проверку имени пользователя на существование
    * @param userName имя пользователя
    * @return true - если имя пользователя совпадает с уже имеющимися именами пользователей в списке,
    * false - если имя пользователя нет в списке
    */
   public synchronized boolean isUserNameBusy(String userName) {
      for (ClientHandler client : clients) {
         if (client.getName().equals(userName)){
            return true;
         }
      }
      return false;
   }

   /**
    * Метод осуществляет отписку клиента на получение сообщений
    * @param client объект типа ClientHandler
    */
   public synchronized void unsubscribe(ClientHandler client) {
      clients.remove(client);
   }

   /**
    * Метод осуществляет подписку клиента на получение сообщений
    * @param client объект типа ClientHandler
    */
   public synchronized void subscribe(ClientHandler client) {
      clients.add(client);
   }

   /**
    * Метод осуществляет широковещательную отправку всем клиентам, подключенным к серверу
    * @param message текст с данными, отправляемый подключенным клиентам.
    */
   public synchronized void broadcastMessage(String message) {
      for (ClientHandler client : clients){
         client.sendMessage(message);
      }
   }

   public void sendPrivateMessage(String name, String nickname, String textMessage) {
      for (ClientHandler client : clients) {
         if (client.getName().equals(nickname)){
            client.sendMessage(String.format("%s: %s", name, textMessage));
         }
      }
   }
}
