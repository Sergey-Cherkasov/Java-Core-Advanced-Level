package network.client.models;

public class ClientGUIContext {

   private String userName;
   private String textMessage;

   private static ClientGUIContext clientGUIContext;

   private ClientGUIContext(){
   }

   public static ClientGUIContext getClientGUIContext(){
      if (clientGUIContext == null){
         clientGUIContext = new ClientGUIContext();
      }
      return clientGUIContext;
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
