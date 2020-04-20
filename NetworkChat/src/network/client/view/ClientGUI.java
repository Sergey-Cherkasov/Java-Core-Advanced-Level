package network.client.view;

import network.client.controller.ClientController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientGUI extends JFrame {
   private JTextField textField;
   private JButton sendButton;
   private JPanel mainPanel;
   private JList<String> userList;
   private JPanel userListPanel;
   private JTextArea textArea;
   private JTextField inputText;

   private ClientController clientController;

   String[] userNameList = new String[]{"Jack", "Bob", "Frank", "Alice"};
   DefaultListModel<String> listModel = new DefaultListModel<>();

   public ClientGUI(ClientController clientController) {
      this.clientController = clientController;
      initClientWindow();
      initUserList();
//      setVisible(true);
      inputText.requestFocus();
   }

   private void initUserList() {
      for (String userName : userNameList) {
         listModel.add(0, userName);
      }
      userList.setModel(listModel);
   }

   private void initClientWindow() {
      setTitle("Network chat::client");
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setSize(500,500);
      setLocationRelativeTo(null);
      sendButton.setText("Send");
      sendButton.addActionListener(e -> {
         try {
            ClientGUI.this.sendMessage();
         } catch (IOException ioException) {
            ioException.printStackTrace();
         }
      });
      inputText.addActionListener(e -> {
         try {
            sendMessage();
         } catch (IOException ioException) {
            ioException.printStackTrace();
         }
      });
      setContentPane(mainPanel);
   }

   private void sendMessage() throws IOException {
      String textMessage = inputText.getText().trim();
      if (textMessage.isEmpty()) {
         return;
      }
      appendOwnMessageIntoTextChatArea(textMessage);
      String selectedUserName = userList.getSelectedValue();
      if (selectedUserName != null){
         appendMessageIntoTextChatArea(selectedUserName, textMessage);
         clientController.sendMessage(textMessage);
      }
//      clientContext.addRecord(selectedUserName, textMessage);
      inputText.setText(null);
      System.out.println(String.format("%s: %s", selectedUserName, textMessage));
   }

   private void appendMessageIntoTextChatArea(String selectedUserName, String textMessage) {
      String formattedMessage = String.format("%s -> %s: %s%n", "Я", selectedUserName, textMessage);
      textArea.append(formattedMessage);
   }

   public void appendOwnMessageIntoTextChatArea(String textMessage) {
      SwingUtilities.invokeLater(() -> {
         String formattedMessage = String.format("%s -> %s: %s%n", "Я", "All", textMessage);
         textArea.append(formattedMessage);
      });
   }

}
