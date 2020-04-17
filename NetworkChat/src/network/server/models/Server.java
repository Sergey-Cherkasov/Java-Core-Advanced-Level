package network.server.models;

import network.server.handlers.AuthenticationService;
import network.client.handlers.ClientHandler;
import network.server.handlers.AuthenticationServiceInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

   private static Server server;
   private final int port;
   private AuthenticationServiceInterface authenticationServiceInterface;
   private List<ClientHandler> clients;


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

   public void initConnection(){
      try(ServerSocket serverSocket = new ServerSocket(port)) {
         authenticationServiceInterface = new AuthenticationService();
         authenticationServiceInterface.start();
         clients = new ArrayList<>();
         while (true){
            System.out.println("Сервер ожидает подключения клиента");
            Socket socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            new ClientHandler(this, socket);
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

   public synchronized boolean isUserNameBusy(String userName) {
      for (ClientHandler client : clients) {
         if (client.getName().equals(userName)){
            return true;
         }
      }
      return false;
   }

   public synchronized void unsubscribe(ClientHandler client) {
      clients.remove(client);
   }

   public synchronized void subscribe(ClientHandler client) {
      clients.add(client);
   }

   public synchronized void broadcastMessage(String message) {
      for (ClientHandler client : clients){
         client.sendMessage(message);
      }
   }
}
