package flashcards;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewtaylor
 */
public class Deck {
    private List<Card> cards;
    private int position;
        
    public Deck() {   
        cards = new ArrayList<>();
    }
    
    public void add(Card card) {
        cards.add(card);
    }
    
    public void remove() {
        if (cards.size() > 1) {
            cards.remove(position);
        }
        if (position == cards.size()) {
            position--;
        }
    }
    
    public Card current() {
        return cards.get(position);
    }
    
    public Card next() {
        if (position < cards.size() - 1)
            position++;
        return cards.get(position);
    }
    
    public Card previous() {
        if (position > 0)
            position--;
        return cards.get(position);
    }
    
    public Card first() {
        position = 0;
        return cards.get(position);
    }
    
    public Card last() {
        position = cards.size() - 1;
        return cards.get(position);
    }
        
    public int position() {
        return position;
    }
    
    public int size() {
        return cards.size();
    }
    
    public void swap(int i, int j) {
        int start = 0, end = cards.size() - 1;
        if (i >= start && i <= end && j >= start && j <= end && i != j) {
            Card card_i = cards.get(i);
            Card card_j = cards.get(j);
            cards.set(i, card_j);
            cards.set(j, card_i);
        }
    }
    
    public List<Card> getCards() {
        return cards;
    }
}