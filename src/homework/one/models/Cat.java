package homework.one.models;

import homework.one.interfaces.FitnessClasses;

public class Cat implements FitnessClasses {

   private String name;
   private int runDistance;
   private double jumpHeight;

   public Cat(String name, int runDistance, double jumpHeight) {
      this.name = name;
      this.runDistance = runDistance;
      this.jumpHeight = jumpHeight;
   }

   public Cat(String name, int runDistance) {
      this(name, runDistance, FitnessClasses.MAX_JUMP_HEIGHT);
   }

   @Override
   public String toString() {
      return "Cat{" + "name='" + name + '\'' + ", runDistance=" + runDistance + ", jumpHeight=" + jumpHeight + '}';
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
