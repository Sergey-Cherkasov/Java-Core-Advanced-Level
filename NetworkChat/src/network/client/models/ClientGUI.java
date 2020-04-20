package network.client.models;

import javax.swing.*;

public class ClientGUI extends JFrame {
   private JTextField textField;
   private JButton sendButton;
   private JPanel mainPanel;
   private JList<String> userList;
   private JPanel userListPanel;
   private JTextArea textArea;
   private JTextField inputText;

   String[] userNameList = new String[]{"Jack", "Bob", "Frank", "Alice"};
   DefaultListModel<String> listModel = new DefaultListModel<>();

   public ClientGUI() {
      initClientWindow();
      initUserList();
      setVisible(true);
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
      sendButton.addActionListener(e -> sendMessage());
      inputText.addActionListener(e -> sendMessage());
      setContentPane(mainPanel);
   }

   private void sendMessage() {
      String textMessage = inputText.getText().trim();
      if (textMessage.isEmpty()) {
         return;
      }
      appendOwnMessageIntoTextChatArea(textMessage);
      String selectedUserName = userList.getSelectedValue();
      if (selectedUserName != null){
         appendMessageIntoTextChatArea(selectedUserName, textMessage);
      }
//      clientContext.addRecord(selectedUserName, textMessage);
      inputText.setText(null);
      System.out.println(String.format("%s: %s", selectedUserName, textMessage));
   }

   private void appendMessageIntoTextChatArea(String selectedUserName, String textMessage) {
      String formattedMessage = String.format("%s -> %s: %s%n", "Я", selectedUserName, textMessage);
      textArea.append(formattedMessage);
   }

   private void appendOwnMessageIntoTextChatArea(String textMessage) {
      String formattedMessage = String.format("%s -> %s: %s%n", "Я", "All", textMessage);
      textArea.append(formattedMessage);
   }

}
