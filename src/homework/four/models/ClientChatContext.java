package homework.four.models;

public class ClientChatContext {

   private String userName;
   private String textMessage;

   private static ClientChatContext clientChatContext;

   private ClientChatContext(){
   }

   public static ClientChatContext getClientChatContext(){
      if (clientChatContext == null) clientChatContext = new ClientChatContext();
      return clientChatContext;
   }

   public void addRecord(String userName, String textMessage){
      this.userName = userName;
      this.textMessage = textMessage;
   }

   public String getUserName() {
      return userName;
   }

   public String getTextMessage() {
      return textMessage;
   }
}
