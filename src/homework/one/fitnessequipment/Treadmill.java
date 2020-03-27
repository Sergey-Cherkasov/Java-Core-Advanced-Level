package homework.one.fitnessequipment;

import homework.one.interfaces.FitnessClasses;
import homework.one.interfaces.ObstacleTypes;

public class Treadmill implements ObstacleTypes {

   private final String TYPE_OBSTACLE = "Беговая дорожка";

   private int length;

   public Treadmill(int length) {
      this.length = length;
   }

   public Treadmill(){
      this(ObstacleTypes.MAX_LENGTH);
   }

   public int getLength() {
      return length;
   }

   @Override
   public boolean onPassObstacle(FitnessClasses fitnessClasses) {
      int runDistance = fitnessClasses.run();
      return runDistance >= length;
   }

   @Override
   public String toString() {
      return "Treadmill{" + "TYPE_OBSTACLE='" + TYPE_OBSTACLE + '\'' + ", length=" + length + '}';
   }
}
