package network.client.handlers;

import network.client.models.ClientGUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

   private static final int PORT_TO_CONNECT = 82_83;
   private static DataInputStream inputStream;
   private static DataOutputStream outputStream;
   private static ClientGUI clientGUI;

   public void initConnection() {
      clientGUI = new ClientGUI();
      Socket clientSocket;
      try{
         clientSocket = new Socket("localhost", PORT_TO_CONNECT);
         inputStream = new DataInputStream(clientSocket.getInputStream());
         outputStream = new DataOutputStream(clientSocket.getOutputStream());
         //          setAuthorized(false);
      }catch (IOException e){
         System.err.println(e.getMessage());
      }
   }

   public void startIdentification() {
      new Thread(() -> {
         try {
            String messageFromServer;
            //               setAuthorized(true);
            do {
               messageFromServer = inputStream.readUTF();
            } while (!messageFromServer.startsWith("/auth_ok"));

            do {
               messageFromServer = inputStream.readUTF();
            } while (!messageFromServer.equalsIgnoreCase("/end"));
         } catch (IOException e) {
            e.printStackTrace();
         }
      });
   }
}
