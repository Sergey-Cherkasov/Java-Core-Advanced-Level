package homework.one.fitnessequipment;

import homework.one.interfaces.FitnessClasses;

public class Wall {

   private double height;

   public Wall(double height) {
      this.height = height;
   }

   public boolean onJump(FitnessClasses fitnessJump){
      double jumpHeight = fitnessJump.jump();
      return jumpHeight >= height;
   }

   public double getHeight() {
      return height;
   }
}
