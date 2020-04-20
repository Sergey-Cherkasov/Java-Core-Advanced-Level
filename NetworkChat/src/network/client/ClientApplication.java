package network.client;

import network.client.handlers.ClientHandler;
import network.client.models.AuthForm;

import javax.swing.*;

public class ClientApplication {

   public static void main(String[] args) {

      ClientHandler clientHandler = new ClientHandler();
      clientHandler.initConnection();
      clientHandler.startIdentification();

//      SwingUtilities.invokeLater(ClientGUI::new);
//      SwingUtilities.invokeLater(AuthForm::new);
   }

}
