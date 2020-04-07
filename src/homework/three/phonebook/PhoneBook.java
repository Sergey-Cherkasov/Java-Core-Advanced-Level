package homework.three.phonebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneBook {

   private Map<String, String> phoneBooks = new HashMap<>();

   public PhoneBook(String firstName, String phoneNumber) {
      this.phoneBooks.put(phoneNumber, firstName);
   }

   public void add(String firstName, String phoneNumber) {
      this.phoneBooks.put(phoneNumber, firstName);
   }

   public List<String> get(String firstName) {
      List<String> searchResult = new ArrayList<>();
      for (String key : phoneBooks.keySet()) {
         String value = phoneBooks.get(key);
         if (value.equals(firstName)) {
            searchResult.add(key);
         }
      }
      return searchResult;
   }

   @Override
   public String toString() {
      StringBuilder stringResult = new StringBuilder();
      for (String key : phoneBooks.keySet()) {
         String value = phoneBooks.get(key);
         stringResult.append(String.format("Фамилия: %s, номер телефона: %s%n", value, key));
      }
      return stringResult.toString();
   }
}
