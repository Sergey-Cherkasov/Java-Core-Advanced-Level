package network.server;

import network.server.models.Server;

/** Основной класс для запуска сервера */

public class ServerApplication {

   public static void main(String[] args) {
      if (args.length != 0) {
         Server.getServer(Integer.parseInt(args[0])).initConnection();
      }
      Server.getServer().initConnection();
   }

}
