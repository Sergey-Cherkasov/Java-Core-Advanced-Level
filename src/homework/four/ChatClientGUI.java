package homework.four;

import homework.four.models.ClientChatContext;

import javax.swing.*;

public class ChatClientGUI extends JFrame {
   private JList<String> userList; // Not used now
   private JTextArea textChatArea;
   private JTextField inputText;
   private JButton sendButton;
   private JPanel mainPanel;

   ClientChatContext clientChatContext;
   String[] userNameList = new String[]{"Jack", "Bob", "Frank", "Alice"};
   DefaultListModel<String> listModel = new DefaultListModel<>();

   ChatClientGUI() {
      clientChatContext = ClientChatContext.getClientChatContext();
      initWindowGUI();
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

   private void initWindowGUI() {
      setTitle("Chat::client");
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
      clientChatContext.addRecord(selectedUserName, textMessage);
      inputText.setText(null);
      System.out.println(String.format("%s: %s", selectedUserName, textMessage));
   }

   private void appendOwnMessageIntoTextChatArea(String textMessage){
      String formattedMessage = String.format("%s -> %s: %s%n", "Я", "All", textMessage);
      textChatArea.append(formattedMessage);
   }

   private void appendMessageIntoTextChatArea(String selectedUser, String textMessage) {
      String formattedMessage = String.format("%s -> %s: %s%n", "Я", selectedUser, textMessage);
      textChatArea.append(formattedMessage);
   }

}
