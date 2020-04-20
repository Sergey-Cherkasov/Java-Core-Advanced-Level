package network.client.controller;

import network.client.model.ClientService;
import network.client.view.AuthForm;
import network.client.view.ClientGUI;

import javax.swing.*;
import java.io.IOException;

public class ClientController {


   private final ClientService clientService;
   private final AuthForm authForm;
   private final ClientGUI clientGUI;

   public ClientController(String serverHost, int serverPort) {
      this.clientService = ClientService.getClientService(serverHost, serverPort);
      this.authForm = new AuthForm(this);
      this.clientGUI = new ClientGUI(this);
   }


   public void initConnection() throws IOException {
      connectToServer();
      runAuthProcess();
   }

   private void runAuthProcess() {
      clientService.setSuccessfulAuthEvent(userName -> {
         ClientController.this.setUserName(userName);
         ClientController.this.openChat();
      });
      authForm.setVisible(true);
   }

   private void openChat() {
      authForm.dispose();
      clientService.setMessageHandler(clientGUI::appendOwnMessageIntoTextChatArea);
      clientGUI.setVisible(true);
   }

   private void setUserName(String userName) {
      SwingUtilities.invokeLater(() -> clientGUI.setTitle(userName));
   }

   private void connectToServer() throws IOException {
      try {
         clientService.connect();
      } catch (IOException e) {
         System.err.println("Failed to establish server connection");
         throw e;
      }
   }

   public void sendAuthMessage(String login, String password) throws IOException {
      clientService.sendAuthMessage(login, password);
   }

   public void sendMessage(String textMessage) {
      try {
         clientService.sendMessage(textMessage);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
