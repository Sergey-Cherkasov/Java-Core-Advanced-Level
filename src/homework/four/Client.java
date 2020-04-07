package homework.four;

import javax.swing.*;

public class Client {

   public static void main(String[] args) {
      SwingUtilities.invokeLater(ClientGUI::new);
      System.out.println("Main thread stop.");
   }
}

