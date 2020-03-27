package homework.one.models;

import homework.one.interfaces.FitnessClasses;

public class Human implements FitnessClasses {

   private String name;
   private int runDistance;
   private double jumpHeight;

   public Human(String name, int runDistance, double jumpHeight) {
      this.name = name;
      this.runDistance = runDistance;
      this.jumpHeight = jumpHeight;
   }

   @Override
   public String toString() {
      return "Human{" + "name ='" + name + '\'' + ", runDistance=" + runDistance + ", jumpHeight=" + jumpHeight + '}';
   }

   @Override
   public int run() {
      System.out.println("Running");
      return runDistance;
   }

   @Override
   public double jump() {
      System.out.println("Jumping");
      return jumpHeight;
   }
}
