package network.server;

import network.server.models.Server;

public class ServerApplication {

   public static void main(String[] args) {
      if (args.length != 0) {
         Server.getServer(Integer.parseInt(args[0])).initConnection();
      }
      Server.getServer().initConnection();
   }

}
