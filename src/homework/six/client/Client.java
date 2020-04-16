package homework.six.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class Client {

   private static final String SERVER_HOST = "10.111.10.100";
   private static final int PORT_CONNECT = 8283;
   private static DataInputStream inputStream;
   private static DataOutputStream outputStream;
   private static Socket socketClient;

   static Thread clientSendMessageThread;

   public Client(){
   }

   public static void main(String[] args) {
      initConnection();
      clientSendMessageThread = getSendMessageThread();
      clientSendMessageThread.start();
      receivingMessagesFromServer();
      System.out.println("Main thread was ended...");
   }

   private static void initConnection() {
      socketClient = new Socket();
      try {
         socketClient.connect(new InetSocketAddress(SERVER_HOST, PORT_CONNECT));
         inputStream = new DataInputStream(socketClient.getInputStream());
         outputStream = new DataOutputStream(socketClient.getOutputStream());

      }catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static void receivingMessagesFromServer() {
      new Thread(() -> {
         try {
            while (true) {
               String messageFromServer = inputStream.readUTF();
               if ("/end".equals(messageFromServer)) {
                  break;
               }
               System.out.println("> " + messageFromServer);
            }
            clientSendMessageThread.interrupt();
            socketClient.close();
         } catch (SocketException e) {
            System.out.println("Сервер отключил соединение");
            e.printStackTrace();
         } catch (IOException e) {
            System.out.println("Соединение с сервером было закрыто");
            e.printStackTrace();
         }
      }).start();
   }

   private static Thread getSendMessageThread() {
      return new Thread(() -> {
         InputStreamReader streamReader = new InputStreamReader(System.in);
         BufferedReader reader = new BufferedReader(streamReader);
         try {
            try {
               String message;
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
      }, "Client.SendMessage");
   }
}
