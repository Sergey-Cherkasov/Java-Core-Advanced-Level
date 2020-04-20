package network.client.models;

import javax.swing.*;

public class AuthForm extends JFrame {

   private JPanel mainPanel;
   private JButton cancelButton;
   private JButton okButton;
   private JTextField passwordTextField;
   private JTextField loginTextField;

   public AuthForm(){
      initAuthWindow();
      setVisible(true);
   }

   private void initAuthWindow() {
      setTitle("Network chat::Authorization");
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setSize(300,175);
      setResizable(false);
      setLocationRelativeTo(null);
      setContentPane(mainPanel);
   }

}
