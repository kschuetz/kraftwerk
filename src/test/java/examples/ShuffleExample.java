package examples;

import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Generators;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static examples.ShuffleExample.Rank.*;
import static examples.ShuffleExample.Suit.*;
import static lombok.AccessLevel.PRIVATE;

public class ShuffleExample {

    enum Suit {
        CLUBS("♣"),
        HEARTS("♥"),
        SPADES("♠"),
        DIAMONDS("♦");

        private String displayName;

        Suit(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    enum Rank {
        TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"),
        SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"),
        QUEEN("Q"), KING("K"), ACE("A");

        private String displayName;

        Rank(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    @Value
    private static class Card {
        Suit suit;
        Rank rank;

        static Card card(Suit suit, Rank rank) {
            return new Card(suit, rank);
        }

        public String toString() {
            return rank.getDisplayName() + suit.getDisplayName();
        }
    }

    private static NonEmptyVector<Suit> suits = Vector.of(CLUBS, HEARTS, SPADES, DIAMONDS);

    private static NonEmptyVector<Rank> ranks = Vector.of(TWO, THREE, FOUR, FIVE, SIX, SEVEN,
            EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE);

    private static NonEmptyVector<Card> cards = suits.cross(ranks).fmap(into(Card::card));

    public static void main(String[] args) {
        Generators.generateShuffled(cards)
                .run()
                .take(10)
                .forEach(System.out::println);
    }

}
