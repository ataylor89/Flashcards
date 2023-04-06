package flashcards;

/**
 *
 * @author andrewtaylor
 */
public class Card {
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
