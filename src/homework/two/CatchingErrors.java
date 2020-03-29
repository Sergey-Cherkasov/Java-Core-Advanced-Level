package homework.two;

public class CatchingErrors {

   public static void main(String[] args) {

      String[][] arrayString1 = {{"1","2","3","4"},{"1","2","3","4"},{"1","2","3","4"},{"1","2","3","4"}};
      String[][] arrayString2 = {{"1","2","3","4"},{"1","2","3","4"},{"1","2","3","4"}};
      String[][] arrayString3 = {{"1","2","3","4"},{"1","2","3","Four"},{"1","2","3","4"},{"1","2","3","4"}};

      try{
         printSumArray(arrayString1);
         printSumArray(arrayString2);
         printSumArray(arrayString3);

      } catch (MyArraySizeException ex){
         System.out.println(ex.getMessage());
//         ex.printStackTrace(); // Uncomment this line to print out stack
      }
   }

   private static void printSumArray(String[][] strings) {
      System.out.printf("Array sum = %d%n", getSumArray(strings));
   }

   private static int getSumArray(String[][] strings) {
      checkArraySize(strings); // Checking the size of the array
      int sumArray = 0;
      try {
         for (int row = 0; row < strings.length; row++) {
            for (int column = 0; column < strings[row].length; column++) {
               sumArray += elementArray(strings, row, column); // Checking the array values
            }
         }
      }catch (MyArrayDataException ex){
         System.out.println(ex.getMessage());
//         ex.printStackTrace(); // Uncomment this line to print out stack
      }
      return sumArray;
   }

   private static int elementArray(String[][] strings, int row, int column) {
      return checkIntValue(strings[row][column], row, column);
   }

   /**
    * The method checks the array values for an integer type
    * @param symbol Value from array
    * @param row Array row
    * @param column Array column
    * @return Value of the Integer type
    * @throws MyArrayDataException if the array value is not correct
    */
   private static int checkIntValue(String symbol, int row, int column) {
      try {
         Integer.parseInt(symbol);
      }catch(NumberFormatException ex){
         throw new MyArrayDataException(symbol, row,column);
      }
      return Integer.parseInt(symbol);
   }

   /**
    * The method checks the size of the array
    * @param strings String array
    * @throws MyArraySizeException if the array size is not correct
    */
   public static void checkArraySize(String[][] strings) {
      if (strings.length != 4){
         throw new MyArraySizeException(strings.length);
      }
      for (String[] string : strings) {
         if (string.length != 4)
            throw new MyArraySizeException(strings.length, string.length);
      }
   }

   /**
    * Thrown to indicate that the size of an one/two-dimension array is out of range.
    * Applications cannot subclass this class to specify similar exceptions.
    */
   private static final class MyArraySizeException extends RuntimeException {
      public MyArraySizeException(int row) {
         super("Wrong count array rows: required - [4], provided - [" + row + "]");
      }
      public MyArraySizeException(int row, int column) {
         super("Wrong size array: required size [4][4], provided - [" + row + "][" + column + "]");
      }
   }

   /**
    * Thrown to indicate that the application tried to convert the string to one of the numeric types,
    * but that the string does not have the appropriate format.
    */
   private static final class MyArrayDataException extends NumberFormatException {

      private String string;
      private int row;
      private int column;

      public MyArrayDataException(String s, int row, int column) {
         this.string = s;
         this.row = row;
         this.column = column;
      }

      @Override
      public String getMessage(){
         return "The value '" + string + "' into array element [" + row + "][" + column + "] is not the Integer";
      }

   }

}
