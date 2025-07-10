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

   
    

    @Test
    public void swap() {
        Hand hand = Hand.of(new Card(Seed.BASTONI,Rank.TRE), new Card(Seed.SPADE, Rank.RE), new Card(Seed.DENARI, Rank.ASSO));
        Hand field = Hand.of(new Card(Seed.COPPE,Rank.DUE), new Card(Seed.SPADE, Rank.QUATTRO), new Card(Seed.COPPE, Rank.FANTE));
        
        Card temp = hand.get(2);
        hand.set(field.get(0),2);
        field.set(temp,0);
        
        assertEquals(Hand.of(new Card(Seed.BASTONI,Rank.TRE), new Card(Seed.SPADE, Rank.RE),new Card(Seed.COPPE,Rank.DUE)), hand);
        assertEquals(Hand.of(new Card(Seed.DENARI, Rank.ASSO),new Card(Seed.SPADE, Rank.QUATTRO), new Card(Seed.COPPE, Rank.FANTE)),field);
        
        
        
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
        assertTrue(scalaAssoFine.isScala());
        assertEquals(43,scala.calcPoints());
        assertEquals(42,scalaAssoInizio.calcPoints());
        assertEquals(52, scalaAssoFine.calcPoints());

    }

   

}
