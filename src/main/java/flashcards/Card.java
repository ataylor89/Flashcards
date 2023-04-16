package flashcards;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author andrewtaylor
 */
public class Card {
    private String front, back;
    private boolean frontSideUp;
    
    public Card() {
        this.front = "";
        this.back = "";
    }
    
    public Card(String front, String back) {
        this.front = front;
        this.back = back;
    }
    
    public void setFront(String front) {
        this.front = front;
    }
    
    public String getFront() {
        return front;
    }
    
    public void setBack(String back) {
        this.back = back;
    }
    
    public String getBack() {
        return back;
    }
    
    public void flip() {
        frontSideUp = !frontSideUp;
    }
    
    public void reset() {
        frontSideUp = true;
    }
    
    @JsonIgnore
    public boolean frontSideUp() {
        return frontSideUp;
    }
    
    @JsonIgnore
    public boolean backSideUp() {
        return !frontSideUp;
    }
}
