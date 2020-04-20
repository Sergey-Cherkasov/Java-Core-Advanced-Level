package network.server.handlers;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс аутентификации клиентов
 */
public class AuthenticationService implements AuthenticationServiceInterface{

   private final List<DataUsers> dataUsersList;

   @Override
   public void start() {
      System.out.println("Authentication service has been starting");
   }

   @Override
   public void stop() {
      System.out.println("Authentication service has been stopped");
   }

   public AuthenticationService(){
      dataUsersList = new ArrayList<>();
      dataUsersList.add(new DataUsers("Denials", "pass1", "Denials"));
      dataUsersList.add(new DataUsers("Lawson", "pass2", "Lawson"));
      dataUsersList.add(new DataUsers("McGregor", "pass3", "McGregor"));
   }

   /**
    * Метод осуществляет проверку введенных логина/пароля. Возвращает имя пользователя
    * при успешной проверке логина/пароля, либо null.
    * @param login логин пользователя
    * @param password пароль пользователя
    * @return имя пользователя, либо null.
    */
   @Override
   public String getUserNameByLoginPassword(String login, String password) {
      for (DataUsers user : dataUsersList) {
         if (user.login.equals(login) && user.password.equals(password)){
            return user.userName;
         }
      }
      return null;
   }

   /**
    * Внутреннй клас, описывающий модель данных клиента (логин/пароль/имя пользователя)
    */
   private static class DataUsers{
      private final String login;
      private final String password;
      private final String userName;

      public DataUsers(String login, String password, String userName){
         this.login = login;
         this.password = password;
         this.userName = userName;
      }
   }

}
