package homework.four;

import javax.swing.*;

public class Client {

   public static void main(String[] args) {
      SwingUtilities.invokeLater(ClientGUI::new);
      System.out.println("Main thread stop.");
   }
}

class ClientWindow extends JFrame{
   ClientWindow(){
      setTitle("Chat::client");
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setBounds(300, 150, 500, 500);
      setVisible(true);
   }
}
