package homework.one.models;

import homework.one.interfaces.FitnessClasses;

public class Robot implements FitnessClasses {

   private String name;
   private int runDistance;
   private double jumpHeight;

   public Robot(String name, int runDistance, double jumpHeight) {
      this.name = name;
      this.runDistance = runDistance;
      this.jumpHeight = jumpHeight;
   }

   @Override
   public String toString() {
      return "Robot{" + "name='" + name + '\'' + ", runDistance=" + runDistance + ", jumpHeight=" + jumpHeight + '}';
   }

   @Override
   public double jump() {
      System.out.println("Jumping");
      return jumpHeight;
   }

   @Override
   public int run() {
      System.out.println("Running");
      return runDistance;
   }
}
