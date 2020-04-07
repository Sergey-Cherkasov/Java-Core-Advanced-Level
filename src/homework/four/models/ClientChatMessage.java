package homework.four.models;

public class ClientChatMessage {

   private String userName;
   private String textMessage;

   private static ClientChatMessage clientChatMessage;

   private ClientChatMessage(){
   }

   public static ClientChatMessage getClientChatMessage(){
      if (clientChatMessage == null) clientChatMessage = new ClientChatMessage();
      return clientChatMessage;
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
