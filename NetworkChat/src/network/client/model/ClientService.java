package network.client.model;

import network.ExtraClass;
import network.client.controller.AuthEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientService {

   private final String serverHost;
   private final int serverPort;
   private static DataInputStream inputStream;
   private static DataOutputStream outputStream;

   private static ClientService clientService;
   private Socket clientSocket;
   private String userName;

   private Consumer<String> messageHandler;
   private AuthEvent successfulAuthEvent;

   private ClientService(String serverHost, int serverPort) {
      this.serverHost = serverHost;
      this.serverPort = serverPort;
   }

   public static ClientService getClientService(String serverHost, int serverPort) {
      if (clientService == null) {
         clientService = new ClientService(serverHost, serverPort);
      }
      return clientService;
   }

   public void connect() throws IOException {
      clientSocket = new Socket(serverHost, serverPort);
      inputStream = new DataInputStream(clientSocket.getInputStream());
      outputStream = new DataOutputStream(clientSocket.getOutputStream());
      runReadThread();
   }

   private void runReadThread() {
      new Thread(() -> {
         while (true) {
            try {
               String message = inputStream.readUTF();
               if (message.startsWith(ExtraClass.AUTH_SUCCESSFULLY)) {
                  String[] splitMessageStrings = message.split("\\s+", 2);
                  System.out.println(splitMessageStrings[0]);
                  userName = splitMessageStrings[1];
                  successfulAuthEvent.authIsSuccessful(userName);
               } else if (messageHandler != null) {
                  messageHandler.accept(message);
               }
            } catch (IOException e) {
               e.printStackTrace();
               return;
            }
         }
      }).start();
   }

   public void sendAuthMessage(String login, String password) throws IOException {
      outputStream.writeUTF(ExtraClass.AUTH_CMD + " " + login + " " + password);
   }

   public void sendMessage(String message) throws IOException {
      outputStream.writeUTF(message);
   }

   public void setMessageHandler(Consumer<String> messageHandler) {
      this.messageHandler = messageHandler;
   }

   public void setSuccessfulAuthEvent(AuthEvent successfulAuthEvent) {
      this.successfulAuthEvent = successfulAuthEvent;
   }

   public void close() {
      try {
         clientSocket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}
