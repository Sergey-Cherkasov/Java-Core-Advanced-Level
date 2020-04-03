package homework.three;

import homework.three.phonebook.PhoneBook;

import java.util.*;

public class Main {

   public static void main(String[] args) {

      //      Задание 1.
      String[] arrayWords = {"whisky", "cognac", "vodka", "whisky", "liquor", "cognac", "whisky", "tequila", "wine", "beer", "wine"};
      Set<String> uniqueWords = new HashSet<>(Arrays.asList(arrayWords));
      Map<String, Integer> mapWords = new HashMap<>();
      for (String uniqueWord : uniqueWords) {
         int count = 0;
         for (String word : arrayWords) {
            if (word.equals(uniqueWord)) {
               count++;
            }
         }
         mapWords.put(uniqueWord, count);
      }
      System.out.println("Задание 1.");
      System.out.println("Список слов в массиве: " + Arrays.asList(arrayWords));
      System.out.println("Список уникальных слов и частота их встречаемости: " + mapWords);
      System.out.println();

      //      Задание 2.
      System.out.println("Задание 2.");
      PhoneBook phoneBook = new PhoneBook("Denials", "+111234567890");
      phoneBook.add("Ford", "+111234567892");
      phoneBook.add("Denials", "+111234567891");
      phoneBook.add("Petrov", "+71234567890");
      phoneBook.add("Denials", "+111234567893");
      phoneBook.add("Petrov", "+71234567891");
      System.out.println("Список контактов:");
      System.out.println(phoneBook);

      //      String firstName = "Denials";
      //      String firstName = "Ford";
      String firstName = "Petrov";
      System.out.printf("Вывод списка телефонов, найденных по фамилии: %s%n%s", firstName, phoneBook.get(firstName));

   }

}
