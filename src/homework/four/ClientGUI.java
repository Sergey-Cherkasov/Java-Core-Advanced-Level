package homework.four;

import homework.four.models.ClientChatMessage;

import javax.swing.*;

public class ClientGUI extends JFrame {
   private JList userList; // Not used now
   private JTextArea textChatArea;
   private JTextField inputText;
   private JButton sendButton;
   private JPanel mainPanel;

   ClientChatMessage clientChatMessage;

   ClientGUI() {
      clientChatMessage = ClientChatMessage.getClientChatMessage();
      initWindowGUI();
      setVisible(true);
      inputText.requestFocus();
   }

   private void initWindowGUI() {
      setTitle("Chat::client");
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setBounds(500, 150, 500, 500);
      sendButton.addActionListener(e -> sendMessage());
      inputText.addActionListener(e -> sendMessage());
      setContentPane(mainPanel);
   }

   private void sendMessage() {
      if (!inputText.getText().equals("")) {
         textChatArea.append(String.format("%s%n", inputText.getText()));
         clientChatMessage.addRecord(inputText.getName(), inputText.getText());
         inputText.setText("");
         System.out.println(clientChatMessage.getListMessage());
      }
      inputText.requestFocus();
   }

}
