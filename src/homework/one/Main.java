package homework.one;

import homework.one.fitnessequipment.Treadmill;
import homework.one.fitnessequipment.Wall;
import homework.one.interfaces.FitnessClasses;
import homework.one.models.Cat;
import homework.one.models.Human;
import homework.one.models.Robot;

public class Main {

   public static void main(String[] args) {

      Treadmill treadmill = new Treadmill(50);
      Wall wall = new Wall(2.80);

      FitnessClasses human = new Human("Jack Daniels", 100, 3.00);
      FitnessClasses cat = new Cat("Barsik", 60);
      FitnessClasses robot = new Robot("T1000", 20, 1.50);

      FitnessClasses[] fitnessClassesMembers = {human, cat, robot};

      fitnessAction(treadmill, wall, fitnessClassesMembers);

   }

   private static void fitnessAction(Treadmill treadmill, Wall wall, FitnessClasses... members) {
      System.out.printf("Treadmill: distance=%s, wall: height=%.2f%n%n", treadmill.getLength(), wall.getHeight());
      for (FitnessClasses member : members) {
         System.out.println(member);
         if (treadmill.onRun(member)){
            System.out.println("Passed");
         } else {
            System.out.printf("Not passed!%n%n");
            continue;
         }
         if (wall.onJump(member)) {
            System.out.println("Passed");
         } else {
            System.out.println("Not passed!");
         }
         System.out.println();
      }
   }

}
