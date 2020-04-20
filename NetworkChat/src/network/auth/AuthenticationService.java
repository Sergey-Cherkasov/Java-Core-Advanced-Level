package network.auth;

import java.util.*;

/**
 * Класс аутентификации клиентов
 */
public class AuthenticationService implements AuthenticationServiceInterface{

//   private final List<DataUsers> dataUsersList;

   private static final Map<DataUsers, String> USERNAME_BY_LOGIN_PASSWORD = Map.of(
           new DataUsers("Denials", "pass1"), "Denials",
           new DataUsers("Lawson", "pass2"), "Lawson",
           new DataUsers("McGregor", "pass3"), "McGregor"
   );

   @Override
   public void start() {
      System.out.println("Authentication service has been started");
   }

   @Override
   public void stop() {
      System.out.println("Authentication service has been stopped");
   }

   public AuthenticationService(){
   }

   /**
    * Метод возвращает имя пользователя при совпадении логина/пароля, либо null.
    * @param login логин пользователя
    * @param password пароль пользователя
    * @return имя пользователя, либо null.
    */
   @Override
   public String getUserNameByLoginPassword(String login, String password) {
      return USERNAME_BY_LOGIN_PASSWORD.get(new DataUsers(login, password));
   }

   /**
    * Внутреннй клас, описывающий модель данных клиента (логин/пароль/имя пользователя)
    */
   private static class DataUsers{
      private final String login;
      private final String password;

      public DataUsers(String login, String password){
         this.login = login;
         this.password = password;
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         DataUsers dataUsers = (DataUsers) o;
         return Objects.equals(login, dataUsers.login) &&
                 Objects.equals(password, dataUsers.password);
      }

      @Override
      public int hashCode() {
         return Objects.hash(login, password);
      }
   }

}
