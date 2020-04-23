package network.client.model;

import network.common.Command;
import network.client.controller.AuthEvent;
import network.client.controller.ClientController;
import network.common.commands.AuthCommand;
import network.common.commands.ErrorCommand;
import network.common.commands.MessageCommand;
import network.common.commands.UpdateUsersListCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;

public class ClientService {

   private final String serverHost;
   private final int serverPort;
   private static ObjectInputStream inputStream;
   private static ObjectOutputStream outputStream;

   private static ClientService clientService;
   private Socket clientSocket;

   private Consumer<String> messageHandler;
   private AuthEvent successfulAuthEvent;
   private final ClientController clientController;

   private ClientService(String serverHost, int serverPort, ClientController clientController) {
      this.serverHost = serverHost;
      this.serverPort = serverPort;
      this.clientController = clientController;
   }

   public static ClientService getClientService(String serverHost, int serverPort, ClientController clientController) {
      if (clientService == null) {
         clientService = new ClientService(serverHost, serverPort, clientController);
      }
      return clientService;
   }

   public void connect() throws IOException {
      clientSocket = new Socket(serverHost, serverPort);
      outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
      inputStream = new ObjectInputStream(clientSocket.getInputStream());
      runReadThread();
   }

   private void runReadThread() {
      new Thread(() -> {
         while (true) {
            try {
               Command command = (Command) inputStream.readObject();
               processCommand(command);
            } catch (IOException e) {
               System.out.println("Поток чтения был прерван.");
               return;
            } catch (ClassNotFoundException e) {
               e.printStackTrace();
            }
         }
      }).start();
   }

   private void processCommand(Command command) {

      switch (command.getType()) {
         case CMD_AUTH: {
            processAuthCommand(command);
            break;
         }
         case CMD_MESSAGE: {
            processMessageCommand(command);
            break;
         }
         case CMD_AUTH_ERROR:
         case CMD_ERROR: {
            processErrorCommand(command);
            break;
         }
         case CMD_UPDATE_USERS_LIST: {
            UpdateUsersListCommand data = (UpdateUsersListCommand) command.getData();
            List<String> users = data.getUsers();
            clientController.updateUsersList(users);
            break;
         }
         default:
            System.err.println("Unknown type of command: " + command.getType());
      }

   }

   private void processErrorCommand(Command command) {
      ErrorCommand data = (ErrorCommand) command.getData();
      clientController.showErrorMessage(data.getErrorMessage());
   }

   private void processMessageCommand(Command command) {
      MessageCommand data = (MessageCommand) command.getData();
      if (messageHandler != null) {
         String message = data.getMessage();
         String username = data.getUsername();
         if (username != null) {
            message = username + ": " + message;
         }
         messageHandler.accept(message);
      }
   }

   private void processAuthCommand(Command command) {
      AuthCommand data = (AuthCommand) command.getData();
      String nickname = data.getUsername();
      successfulAuthEvent.authIsSuccessful(nickname);
   }

/*
   public void sendAuthMessage(Command command) throws IOException {
      outputStream.writeObject(command);
   }

   public void sendMessage(Command command) throws IOException {
      outputStream.writeObject(command);
   }
*/

   public void setMessageHandler(Consumer<String> messageHandler) {
      this.messageHandler = messageHandler;
   }

   public void setSuccessfulAuthEvent(AuthEvent successfulAuthEvent) {
      this.successfulAuthEvent = successfulAuthEvent;
   }

   public void close() {
      try {
         sendCommand(Command.endCommand());
         clientSocket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void sendCommand(Command command) throws IOException {
      outputStream.writeObject(command);
   }
}
