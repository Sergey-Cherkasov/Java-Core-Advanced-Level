package network.client.controller;

import network.common.Command;
import network.client.model.ClientService;
import network.client.view.AuthForm;
import network.client.view.ClientGUI;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ClientController {


   private static final String ALL_USERS_LIST_ITEM = "All";
   private static ClientService clientService;
   private final AuthForm authForm;
   private final ClientGUI clientGUI;
   private String userName;

   public ClientController(String serverHost, int serverPort) {
      clientService = ClientService.getClientService(serverHost, serverPort, this);
      this.authForm = new AuthForm(this);
      this.clientGUI = new ClientGUI(this);
   }

   public static void shutdown() {
      clientService.close();
   }


   public void initConnection() throws IOException {
      connectToServer();
      runAuthProcess();
   }

   private void runAuthProcess() {
      clientService.setSuccessfulAuthEvent (nickname -> {
         ClientController.this.setUserName(nickname);
         ClientController.this.openChat();
      });
      authForm.setVisible(true);
   }

   private void openChat() {
      authForm.dispose();
      clientService.setMessageHandler(clientGUI::appendMessageIntoTextChatArea);
      clientGUI.setVisible(true);
   }

   private void setUserName(String userName) {
      SwingUtilities.invokeLater(() -> clientGUI.setTitle(userName));
      this.userName = userName;
   }

   public String getUserName(){
      return userName;
   }

   private void connectToServer() throws IOException {
      try {
         clientService.connect();
      } catch (IOException e) {
         System.err.println("Failed to establish server connection");
         throw e;
      }
   }

   public void sendAuthMessage(String login, String password) {
      sendCommand(Command.authCommand(login, password));
   }

   private void sendCommand(Command command) {
      try {
         clientService.sendCommand(command);
      } catch (IOException e) {
         showErrorMessage(e.getMessage());
      }
   }

   public void sendMessage(String textMessage) {
      sendCommand(Command.broadcastMessageCommand(textMessage));
   }

   public void sendPrivateMessage(String selectedUserName, String textMessage) {
      sendCommand(Command.privateMessageCommand(selectedUserName, textMessage));
//      sendMessage(String.format("/w %s %s", selectedUserName, textMessage));
   }

   public void updateUsersList(List<String> users) {
      users.remove(userName);
      users.add(0, ALL_USERS_LIST_ITEM);
      clientGUI.updateUsers(users);
   }

   public void showErrorMessage(String errorMessage) {
      if (clientGUI.isActive()) {
         clientGUI.showError(errorMessage);
      } else if (authForm.isActive()) {
         authForm.showError(errorMessage);
      }
      System.err.println(errorMessage);
   }

}
