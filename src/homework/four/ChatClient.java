package homework.four;

import javax.swing.*;

public class ChatClient {

   public static void main(String[] args) {
      SwingUtilities.invokeLater(ChatClientGUI::new);
      System.out.println("Main thread stop.");
   }
}

