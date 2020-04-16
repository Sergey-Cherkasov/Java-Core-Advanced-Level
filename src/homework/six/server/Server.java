package homework.six.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

   public static final int SERVER_PORT = 8283;
   private static DataInputStream inputStream;
   private static DataOutputStream outputStream;
   private static ServerSocket serverSocket;

   private static Thread serverSendMessageThread;

   public static void main(String[] args) {
      System.out.println("Start server...");
      openConnection();
   }

   private static void openConnection() {
      try {
         serverSocket = new ServerSocket(SERVER_PORT);
         System.out.println("Server is starting...");
         Socket clientSocket = serverSocket.accept();
         System.out.println("Клиент подключился");

         inputStream = new DataInputStream(clientSocket.getInputStream());
         outputStream = new DataOutputStream(clientSocket.getOutputStream());

         serverSendMessageThread = getSendMessageToClient();
         serverSendMessageThread.start();

         while (true){
            String messageFromClient = inputStream.readUTF();
            if ("/end".equals(messageFromClient)){
               outputStream.writeUTF("/end");
               break;
            }
            System.out.println("> " + messageFromClient);
         }
         serverSendMessageThread.interrupt();
         serverSocket.close();

      }catch (EOFException e){
         System.out.println("Клиент отключился и неуспел ничего передать на сервер");
         e.printStackTrace();
      }catch (SocketException e){
         System.out.println("Клиент отключился");
         e.printStackTrace();
      }catch (IOException e) {
         System.out.println("Port already using");
         e.printStackTrace();
      }
   }

   private static Thread getSendMessageToClient() {
      return new Thread(() -> {
         InputStreamReader streamReader = new InputStreamReader(System.in);
         BufferedReader reader;
         String message;
         reader = new BufferedReader(streamReader);
         try {
            try {
               do {
                  message = reader.readLine();
                  outputStream.writeUTF(message);
               } while (!"/end".equals(message));
               throw new InterruptedException();
            } catch (IOException e) {
               e.printStackTrace();
            }
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      });
   }
}
