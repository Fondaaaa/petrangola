package petrangola.model;

import java.util.ArrayList;
import java.util.List;

public class Bot extends Player {

   private int number;

   public Bot(int number) {
      super();
      this.number = number;
   }

   public List<Card> takeTurn(Hand field) {
      List<Card> swaps = new ArrayList<Card>();
      Hand current = super.getHand();
      Hand bestHand = Hand.bestCombinedHand(current, field);

      if (bestHand.equals(current)) {
         return swaps; // already optimal, no swap needed
      }

      // For each position in the hand
      for (int i = 0; i < Hand.HAND_SIZE; i++) {
         Card currentCard = current.get(i);
         Card desiredCard = bestHand.get(i);

         if (!currentCard.equals(desiredCard)) {
            // Look for the desiredCard in the field
            for (int j = 0; j < Hand.HAND_SIZE; j++) {
               if (field.get(j).equals(desiredCard)) {
                  swaps.add(super.getHand().get(i));
                  swaps.add(field.get(j));
                  super.getHand().swap(field, i, j); // perform the swap
                  break;
               }
            }
         }
      }

      return swaps;
   }

   @Override
   public String toString() {
      return "CPU" + number;
   }


}