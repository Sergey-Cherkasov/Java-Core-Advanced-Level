package homework.one.fitnessequipment;

import homework.one.interfaces.FitnessClasses;

public class Treadmill {

   private int length;

   public Treadmill(int length) {
      this.length = length;
   }

   public boolean onRun(FitnessClasses fitnessClasses){
      int runDistance = fitnessClasses.run();
      return runDistance >= length;
   }

   public int getLength() {
      return length;
   }
}
