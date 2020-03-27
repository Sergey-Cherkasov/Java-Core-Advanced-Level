package homework.two;

public class CatchingErrors {

   public static void main(String[] args) {

      String[][] arrayString1 = {{"1","2","3","4"},{"1","2","3","4"},{"1","2","3","4"},{"1","2","3","4"}};
      String[][] arrayString2 = {{"1","2","3","4"},{"1","2","3","4"},{"1","2","3","4"}};
      String[][] arrayString3 = {{"1","2","3","4"},{"1","2","3","Four"},{"1","2","3","4"},{"1","2","3","4"}};

      catchSizeErrorException(arrayString1);
      catchSizeErrorException(arrayString2);
      catchSizeErrorException(arrayString3);
   }

   private static void catchSizeErrorException(String[][] strings) {
      try {
         checkArraySize(strings); // Checking the size of the array
         int sumArray = 0;
         for (int row = 0; row < strings.length; row++) {
            for (int column = 0; column < strings[row].length; column++) {
               sumArray += checkIntValue(strings[row][column], row, column); // Checking the array values
            }
         }
         System.out.printf("Array sum = %d%n", sumArray);
      }catch (MyArraySizeException | MyArrayDataException ex){
         System.out.println(ex.getMessage());
//         ex.printStackTrace(); // Uncomment this line to display the stack printout
      }
   }

   /**
    * The method checks the array values for an integer type
    * @param symbol Value from array
    * @param row Array row
    * @param column Array column
    * @return Value of the Integer type
    * @throws MyArrayDataException if the array value is not correct
    */
   private static int checkIntValue(String symbol, int row, int column) throws MyArrayDataException{
      int i = 0;
      if (symbol.equals(""))
         throw new MyArrayDataException(symbol, row, column);
      while (i < symbol.length()){
         if (!Character.isDigit(symbol.charAt(i))){
            throw new MyArrayDataException(symbol, row, column);
         }
         i++;
      }
      return Integer.parseInt(symbol);
   }

   /**
    * The method checks the size of the array
    * @param strings String array
    * @throws MyArraySizeException if the array size is not correct
    */
   public static void checkArraySize(String[][] strings) throws MyArraySizeException {
      if (strings.length != 4){
         throw new MyArraySizeException();
      }
      for (String[] string : strings) {
         if (string.length != 4)
            throw new MyArraySizeException();
      }
   }

   /**
    * Thrown to indicate that the size of a two-dimensional array is out of range.
    * Applications cannot subclass this class to specify similar exceptions.
    */
   private static final class MyArraySizeException extends RuntimeException {
      public MyArraySizeException() {
         super("Wrong size array");
      }
   }

   /**
    * Thrown to indicate that the application tried to convert the string to one of the numeric types,
    * but that the string does not have the appropriate format.
    */
   private static final class MyArrayDataException extends NumberFormatException {
      public MyArrayDataException(String s, int row, int column) {
         super("The value '" + s + "' into arrayStrings[" + row + "][" + column + "] is not the Integer");
      }
   }

}
