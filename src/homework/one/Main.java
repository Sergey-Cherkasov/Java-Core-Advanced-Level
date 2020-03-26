package homework.one;

import homework.one.fitnessequipment.Treadmill;
import homework.one.fitnessequipment.Wall;
import homework.one.interfaces.FitnessClasses;
import homework.one.interfaces.ObstacleTypes;
import homework.one.models.Cat;
import homework.one.models.Human;
import homework.one.models.Robot;

public class Main {

   public static void main(String[] args) {

      ObstacleTypes treadmill = new Treadmill(50);
      ObstacleTypes wall = new Wall(2.80);

      FitnessClasses human = new Human("Jack Daniels", 100, 3.00);
      FitnessClasses cat = new Cat("Barsik", 60);
      FitnessClasses robot = new Robot("T1000", 20, 1.50);

      ObstacleTypes[] obstacleTypes = {treadmill, wall};
      FitnessClasses[] fitnessClassesMembers = {human, cat, robot};

      fitnessAction(obstacleTypes, fitnessClassesMembers);

   }

   private static void fitnessAction(ObstacleTypes[] obstacles, FitnessClasses... members) {
      for (FitnessClasses member : members) {
         System.out.println(member + "\n");
         for (ObstacleTypes obstacle : obstacles) {
            System.out.println(obstacle);
            if (obstacle.onPassObstacle(member)) {
               System.out.println("Passed");
            } else {
               System.out.printf("Not passed!%n%n");
               break;
            }
            System.out.println();
         }
         System.out.println();
      }
   }

}
