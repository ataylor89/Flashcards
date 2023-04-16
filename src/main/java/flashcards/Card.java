package flashcards;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author andrewtaylor
 */
public class Card {
    private String front, back;
    private int orientation;
    public static final int FRONT = 0;
    public static final int BACK = 1;
    
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
    
    @JsonIgnore
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    
    @JsonIgnore
    public int getOrientation() {
        return orientation;
    }
    
    public void flip() {
        if (orientation == FRONT) {
            orientation = BACK;
        }
        else if (orientation == BACK) {
            orientation = FRONT;
        }
    }
}
