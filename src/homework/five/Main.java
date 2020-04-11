package homework.five;

import java.sql.Time;
import java.util.Arrays;

public class Main {

   static final int SIZE = 10000000;
   static final int HALF_SIZE = SIZE / 2;

   public static void main(String[] args) {
      methodOne();
      methodTwo();
   }

   private static void methodOne() {
      float[] array = new float[SIZE];
      Arrays.fill(array, 1);
      long timeStart = System.currentTimeMillis();
      for (int i = 0; i < array.length; i++) {
         array[i] = (float) (array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
      }
      long timeEnd = System.currentTimeMillis();
      System.out.println("Результат выполнения метода 1");
      System.out.println(String.format("Время начала выполнения: %s.%n" + "Время завершения выполнение = %s.%n" + "Время выполнения = %d милисекунд.%n", new Time(timeStart), new Time(timeEnd), timeEnd - timeStart));
   }

   private static void methodTwo() {
      float[] array = new float[SIZE];
      float[] array1 = new float[HALF_SIZE];
      float[] array2 = new float[HALF_SIZE];
      Arrays.fill(array, 1);
      long timeStart = System.currentTimeMillis();
      System.arraycopy(array, 0, array1, 0, HALF_SIZE);
      System.arraycopy(array, HALF_SIZE, array2, 0, HALF_SIZE);
      Thread thread1 = new Thread(() -> {
         for (int i = 0; i < array1.length; i++) {
            array1[i] = (float) (array1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
         }
         System.out.println("Thread 1 has been finished");
      });
      Thread thread2 = new Thread(() -> {
         for (int i = 0; i < array2.length; i++) {
            array2[i] = (float) (array2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
         }
         System.out.println("Thread 2 has been finished");
      });
      thread1.start();
      thread2.start();
      try {
         thread1.join();
         thread2.join();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      System.arraycopy(array1, 0, array, 0, HALF_SIZE);
      System.arraycopy(array2, 0, array, HALF_SIZE, HALF_SIZE);
      long timeEnd = System.currentTimeMillis();
      System.out.println("Результат выполнения метода 2");
      System.out.println(String.format("Время начала выполнения: %s.%n" + "Время завершения выполнение = %s.%n" + "Время выполнения = %d милисекунд.%n", new Time(timeStart), new Time(timeEnd), timeEnd - timeStart));
   }
}
