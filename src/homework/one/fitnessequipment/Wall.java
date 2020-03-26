package homework.one.fitnessequipment;

import homework.one.interfaces.FitnessClasses;
import homework.one.interfaces.ObstacleTypes;

public class Wall implements ObstacleTypes {

   private final String TYPE_OBSTACLE = "Стена";

   private double height;
   public Wall(double height) {
      this.height = height;
   }

   public Wall(){
      this(ObstacleTypes.MAX_HEIGHT);
   }

   public double getHeight() {
      return height;
   }

   @Override
   public boolean onPassObstacle(FitnessClasses fitnessClasses) {
      double jumpHeight = fitnessClasses.jump();
      return jumpHeight >= height;
   }

   @Override
   public String toString() {
      return "Wall{" + "TYPE_OBSTACLE='" + TYPE_OBSTACLE + '\'' + ", height=" + height + '}';
   }
}
