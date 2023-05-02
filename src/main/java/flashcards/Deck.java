package flashcards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewtaylor
 */
public class Deck {
    private final List<Card> cards;
    private int position;
        
    public Deck() {   
        cards = new ArrayList<>();
        cards.add(new Card());
        position = 0;
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
        Card card = cards.get(position);
        card.reset();
        return card;
    }
    
    public Card last() {
        position = cards.size() - 1;
        Card card = cards.get(position);
        card.reset();
        return card;
    }
    
    @JsonIgnore
    public boolean isFirst() {
        return position == 0;
    }
    
    @JsonIgnore
    public boolean isLast() {
        return position == cards.size() - 1;
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