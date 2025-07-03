package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import petrangola.model.Card;
import petrangola.model.Deck;
import petrangola.model.Hand;
import petrangola.model.Rank;
import petrangola.model.Seed;

public class HandTest {
    
    Deck deck;

   
    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    public void swap() {
        Hand hand = Hand.of(new Card(Seed.BASTONI,Rank.TRE), new Card(Seed.SPADE, Rank.RE), new Card(Seed.DENARI, Rank.ASSO));
        Hand field = Hand.of(new Card(Seed.COPPE,Rank.DUE), new Card(Seed.SPADE, Rank.QUATTRO), new Card(Seed.COPPE, Rank.FANTE));
        
        var newHand = List.copyOf(hand.getCards());
        var newField = List.copyOf(field.getCards());

        hand.swap(field, List.of(0,1), List.of(1,2));
        
        assertEquals(List.of(newField.get(1),newField.get(2),newHand.get(2)), hand.getCards());
        assertEquals(List.of(newField.get(0),newHand.get(0),newHand.get(1)),field.getCards());
        
        
        
    }

    @Test
    public void tris() {
        Hand handTris = Hand.of(new Card(Seed.BASTONI, Rank.TRE), new Card(Seed.SPADE,Rank.TRE), new Card(Seed.DENARI, Rank.TRE));
        assertTrue(handTris.isTris());
        
        assertEquals(33,handTris.calcPoints());
    }

    @Test 
    public void scala() {
        Hand scala = Hand.of(new Card(Seed.COPPE,Rank.DUE),new Card(Seed.COPPE,Rank.TRE), new Card(Seed.COPPE,Rank.QUATTRO));
        Hand scalaAssoInizio = Hand.of(new Card(Seed.COPPE,Rank.ASSO),new Card(Seed.COPPE,Rank.DUE), new Card(Seed.COPPE,Rank.TRE));
        Hand scalaAssoFine =  Hand.of(new Card(Seed.SPADE, Rank.CAVALLO), new Card(Seed.SPADE, Rank.RE), new Card(Seed.SPADE, Rank.ASSO));

        assertTrue(scala.isScala());
        assertTrue(scalaAssoInizio.isScala());
        assertTrue(scalaAssoInizio.containsAsso());
        assertTrue(scalaAssoFine.isScala());
        assertEquals(43,scala.calcPoints());
        assertEquals(42,scalaAssoInizio.calcPoints());
        assertEquals(50, scalaAssoFine.calcPoints());

    }

   

}
