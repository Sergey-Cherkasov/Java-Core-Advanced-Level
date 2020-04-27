package network.server.handlers;

import network.common.Command;
import network.common.CommandType;
import network.auth.AuthenticationServiceInterface;
import network.common.commands.AuthCommand;
import network.common.commands.BroadcastMessageCommand;
import network.common.commands.PrivateMessageCommand;
import network.server.models.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Класс обработчика действий клиента
 */
public class ClientHandler {
   private final Server server;
   private final Socket socket;
   private ObjectInputStream inputStream;
   private ObjectOutputStream outputStream;

   private String name;

   private final AuthenticationServiceInterface authenticationServiceInterface;

   /**
    * Конструктор обработчика действий клиента запускает аутентификацию и метод чтения сообщений
    * в отдельном потоке.
    *
    * @param server экземпляр запущенного сервера
    * @param socket экземпляр сокета
    */
   public ClientHandler(Server server, Socket socket) {
      this.server = server;
      this.socket = socket;
      this.authenticationServiceInterface = server.getAuthenticationServiceInterface();
   }

   /**
    * Метод осуществляет закрытие соединения и освобождение ресурсов
    */
   private void closeConnection() {
      try {
         server.unsubscribe(this);
         server.broadcastMessage(Command.broadcastMessageCommand(name + " вышел из чата"));
         socket.close();
      } catch (IOException e) {
         System.err.println(e.getMessage());
      }
   }

   /**
    * Метод осуществляет чтение сообщений, полученных от сервера
    */
   private void readMessages() throws IOException {
      while (true) {
         Command command = readCommand();
         if (command == null) {
            continue;
         }
         switch (command.getType()) {
            case CMD_END:
               return;
            case CMD_BROADCAST_MESSAGE:
               BroadcastMessageCommand data = (BroadcastMessageCommand) command.getData();
               server.broadcastMessage(Command.messageCommand(name, data.getMessage()));
               break;
            case CMD_PRIVATE_MESSAGE:
               PrivateMessageCommand privateMessageCommand = (PrivateMessageCommand) command.getData();
               String receiver = privateMessageCommand.getReceiver();
               String message = privateMessageCommand.getMessage();
               server.sendPrivateMessage(receiver, Command.messageCommand(name, message));
               break;
            default:
               String errorMessage = "Unknown type of command : " + command.getType();
               System.err.println(errorMessage);
               sendMessage(Command.errorCommand(errorMessage));
         }
      }
   }

   private Command readCommand() throws IOException {
      try {
         return (Command) inputStream.readObject();
      } catch (ClassNotFoundException e) {
         String errorMessage = "Unknown type of object from client!";
         System.err.println(errorMessage);
         e.printStackTrace();
         sendMessage(Command.errorCommand(errorMessage));
         return null;
      }
   }

   /**
    * Метод аутентификации клиента
    */
   private void authentication() throws IOException {

      Thread timeOut = new Thread(() -> {
         try {
            Thread.sleep(15000);
            String errorMessage = "Вышло время авторизации.\nПерезапустите приложение";
            sendMessage(Command.errorCommand(errorMessage));
            System.err.println(errorMessage);
            closeConnection();
         } catch (InterruptedException e) {
            System.out.println("Авторизация успешна");
         } catch (IOException e) {
            e.printStackTrace();
         }
      }, "timeOut");
      timeOut.start();

      while (true) {
         Command command = readCommand();

         if (command == null) {
            continue;
         }

         if (command.getType() == CommandType.CMD_AUTH) {
            if (processAuthCommand(command)) {
               timeOut.interrupt();
               return;
            }
         } else {
            String errorMessage = "Illegal command for authentication: " + command.getType();
            System.err.println(errorMessage);
            sendMessage(Command.errorCommand(errorMessage));
         }
      }
   }

   private boolean processAuthCommand(Command command) throws IOException {
      AuthCommand authCommand = (AuthCommand) command.getData();
      String login = authCommand.getLogin();
      String password = authCommand.getPassword();
      String nickname = authenticationServiceInterface.getUserNameByLoginPassword(login, password);
      if (nickname == null) {
         sendMessage(Command.authErrorCommand("Неверные логин/пароль!"));
      } else if (server.isUserNameBusy(nickname)) {
         sendMessage(Command.authErrorCommand("Учетная запись уже используется!"));
      } else {
         authCommand.setUsername(nickname);
         sendMessage(command);
         setName(nickname);
         server.broadcastMessage(Command.messageCommand(null, nickname + " Зашел в чат!"));
         server.subscribe(this);
         return true;
      }
      return false;
   }

   /**
    * Метод осуществляет отправку сообщения на сервер
    *
    * @param command текст с данными сообщения
    */
   public void sendMessage(Command command) throws IOException {
      outputStream.writeObject(command);
   }

   public void handle() throws IOException {
      inputStream = new ObjectInputStream(socket.getInputStream());
      outputStream = new ObjectOutputStream(socket.getOutputStream());

      new Thread(() -> {
         try {
            authentication();
            readMessages();
         } catch (IOException e) {
            System.out.println("Connect has been failed.");
            System.err.println(e.getMessage());
         } finally {
            closeConnection();
         }
      }).start();
      //      this.name = "";
   }

   private void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

}
