package flashcards;

import java.io.Serializable;

/**
 *
 * @author andrewtaylor
 */
public class Card implements Serializable {
    private String front, back;
    
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
}
