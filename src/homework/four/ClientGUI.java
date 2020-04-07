package homework.four;

import javax.swing.*;

public class ClientGUI extends JFrame{
   private JList list1;
   private JTextArea textArea1;
   private JTextField textField1;
   private JButton sendButton;
   private JPanel mainPanel;

   ClientGUI(){
      setTitle("Chat::client");
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setBounds(300, 150, 500, 500);
      setContentPane(mainPanel);
      setVisible(true);
   }

}
