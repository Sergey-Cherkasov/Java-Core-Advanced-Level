package network.server.models;

import network.server.handlers.AuthenticationService;
import network.client.handlers.ChatHandler;
import network.server.handlers.AuthenticationServiceInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** Класс Server создает экземпляр сервера для обмена сообщениями с данными между клиентами. */

public class Server {

   private static Server server;
   private final int port;
   private AuthenticationServiceInterface authenticationServiceInterface;
   private List<ChatHandler> clients;


   private Server(){
      this.port = 82_83;
   }

   private Server(int port){
      this.port = port;
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
         authenticationServiceInterface = new AuthenticationService();
         authenticationServiceInterface.start();
         clients = new ArrayList<>();
         while (true){
            System.out.println("Сервер ожидает подключения клиента");
            Socket socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            new ChatHandler(this, socket);
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (authenticationServiceInterface != null){
            authenticationServiceInterface.stop();
         }
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
      for (ChatHandler client : clients) {
         if (client.getName().equals(userName)){
            return true;
         }
      }
      return false;
   }

   /**
    * Метод осуществляет отписку клиента на получение сообщений
    * @param client объект типа ChatHandler
    */
   public synchronized void unsubscribe(ChatHandler client) {
      clients.remove(client);
   }

   /**
    * Метод осуществляет подписку клиента на получение сообщений
    * @param client объект типа ChatHandler
    */
   public synchronized void subscribe(ChatHandler client) {
      clients.add(client);
   }

   /**
    * Метод осуществляет широковещательную отправку всем клиентам, подключенным к серверу
    * @param message текст с данными, отправляемый подключенным клиентам.
    */
   public synchronized void broadcastMessage(String message) {
      for (ChatHandler client : clients){
         client.sendMessage(message);
      }
   }
}
