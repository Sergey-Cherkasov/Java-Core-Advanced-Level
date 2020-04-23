package network.server.models;

import network.Command;
import network.auth.AuthenticationService;
import network.auth.AuthenticationServiceInterface;
import network.client.handlers.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс Server создает экземпляр сервера для обмена сообщениями с данными между клиентами.
 */

public class Server {

   private static Server server;
   private final int port;
   private final AuthenticationServiceInterface authenticationServiceInterface;
   private final List<ClientHandler> clients;


   private Server() {
      this.port = 82_83;
      this.clients = new ArrayList<>();
      this.authenticationServiceInterface = new AuthenticationService();
   }

   private Server(int port) {
      this.port = port;
      this.clients = new ArrayList<>();
      this.authenticationServiceInterface = new AuthenticationService();
   }

   public static Server getServer(int... port) {
      if (server == null) {
         if (port.length != 0) {
            server = new Server(port[0]);
         } else {
            server = new Server();
         }
      }
      return server;
   }

   /**
    * Метод осуществляет инициализацию сервера и ожидает подключения клиента.
    * При подключении клиента создает экземпляр обработчика действий клиента.
    */
   public void initConnection() {
      try (ServerSocket serverSocket = new ServerSocket(port)) {
         authenticationServiceInterface.start();
         while (true) {
            System.out.println("Сервер ожидает подключения клиента");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился");
            ClientHandler handler = new ClientHandler(this, clientSocket);
            try {
               handler.handle();
            } catch (IOException e) {
               System.err.println("Failed to handle client connection");
               clientSocket.close();
            }
         }
      } catch (IOException e) {
         System.err.println(e.getMessage());
         e.printStackTrace();
      } finally {
         authenticationServiceInterface.stop();
      }
   }

   public AuthenticationServiceInterface getAuthenticationServiceInterface() {
      return authenticationServiceInterface;
   }

   /**
    * Метод осуществляет проверку имени пользователя на существование
    *
    * @param userName имя пользователя
    * @return true - если имя пользователя совпадает с уже имеющимися именами пользователей в списке,
    * false - если имя пользователя нет в списке
    */
   public boolean isUserNameBusy(String userName) {
      for (ClientHandler client : clients) {
         if (client.getName().equals(userName)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Метод осуществляет отписку клиента на получение сообщений
    *
    * @param client объект типа ClientHandler
    */
   public synchronized void unsubscribe(ClientHandler client) throws IOException {
      clients.remove(client);
      List<String> users = getAllUsernames();
      broadcastMessage(Command.updateUsersListCommand(users));
   }

   /**
    * Метод осуществляет подписку клиента на получение сообщений
    *
    * @param client объект типа ClientHandler
    */
   public synchronized void subscribe(ClientHandler client) throws IOException {
      clients.add(client);
      List<String> users = getAllUsernames();
      broadcastMessage(Command.updateUsersListCommand(users));
   }

   /**
    * Метод осуществляет широковещательную отправку всем клиентам, подключенным к серверу
    *
    * @param command текст с данными, отправляемый подключенным клиентам.
    */
   public synchronized void broadcastMessage(Command command) throws IOException {
      for (ClientHandler client : clients) {
         client.sendMessage(command);
      }
   }

   public synchronized void sendPrivateMessage(String receiverUserName, Command command) throws IOException {
      for (ClientHandler client : clients) {
         if (client.getName().equals(receiverUserName)) {
            client.sendMessage(command);
            return;
         }
      }
   }

   private List<String> getAllUsernames() {
      return clients.stream().map(ClientHandler::getName).collect(Collectors.toList());
/*
      List<String> result = new ArrayList<>();
      for (ClientHandler client : clients) {
         result.add(client.getName());
      }
      return result;
*/
   }

}
