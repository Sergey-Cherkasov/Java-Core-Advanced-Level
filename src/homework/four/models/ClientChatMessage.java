package homework.four.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClientChatMessage {

   private List<Map<String, String>> listMessage;

   private static ClientChatMessage clientChatMessage;

   private ClientChatMessage(){
      listMessage = new ArrayList<>();
   }

   public static ClientChatMessage getClientChatMessage(){
      if (clientChatMessage == null) clientChatMessage = new ClientChatMessage();
      return clientChatMessage;
   }

   public List<Map<String, String>> getListMessage() {
      return listMessage;
   }

   public void setListMessage(List<Map<String, String>> listMessage) {
      this.listMessage = listMessage;
   }

   public void addRecord(String userName, String textMessage){
      Map<String, String> mapBuilder = new LinkedHashMap<>();
      mapBuilder.put(userName,textMessage);
      listMessage.add(mapBuilder);
   }

}
