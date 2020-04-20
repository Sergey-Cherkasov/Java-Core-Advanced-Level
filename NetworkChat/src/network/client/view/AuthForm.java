package network.client.view;

import network.client.controller.ClientController;

import javax.swing.*;
import java.io.IOException;

public class AuthForm extends JFrame {

   private JPanel mainPanel;
   private JButton cancelButton;
   private JButton okButton;
   private JTextField loginTextField;
   private JPasswordField passwordTextField;

   private ClientController clientController;

   public AuthForm(ClientController clientController){
      this.clientController = clientController;
      initAuthWindow();
//      setVisible(true);
   }

   private void initAuthWindow() {
      setTitle("Network chat::Authorization");
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setSize(300,175);
      setResizable(false);
      setLocationRelativeTo(null);
      okButton.addActionListener(e -> onOkButton());
      cancelButton.addActionListener(e -> onCancelButton());
      setContentPane(mainPanel);
   }

   private void onCancelButton() {
      System.exit(0);
   }

   private void onOkButton() {
      String login = loginTextField.getText().trim();
      String pass = new String(passwordTextField.getPassword()).trim();
      try {
         clientController.sendAuthMessage(login, pass);
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Ошибка при попытки аутентификации");
      }
   }


}
