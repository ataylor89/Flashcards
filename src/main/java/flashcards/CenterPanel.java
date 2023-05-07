package flashcards;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author andrewtaylor
 */
public final class CenterPanel extends JPanel implements ActionListener {
    
    private JLabel index;
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private JPanel controls;
    private JButton back, forward, first, last, up, down, create, delete;
    private Deck deck;
    
    public CenterPanel() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(750, 500));
        setMaximumSize(new Dimension(750, 500));
        addComponents();
    }
    
    public void addComponents() {
        index = new JLabel();
        add(index, BorderLayout.NORTH);
        textPane = new JTextPane();
        textPane.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        textPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String text = textPane.getText();
                Card card = deck.current();
                if (card.frontSideUp()) {
                    if (!card.getFront().equals(text)) {
                        card.setFront(text);
                    }
                }
                else {
                    if (!card.getBack().equals(text)) {
                        card.setBack(text);
                    }
                }
            }
        });
        scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);
        controls = new JPanel();
        back = new JButton("Back");
        forward = new JButton("Forward");
        first = new JButton("First");
        last = new JButton("Last");
        up = new JButton("Up");
        down = new JButton("Down");
        create = new JButton("New");
        delete = new JButton("Delete");
        back.addActionListener(this);
        forward.addActionListener(this);
        first.addActionListener(this);
        last.addActionListener(this);
        up.addActionListener(this);
        down.addActionListener(this);
        create.addActionListener(this);
        delete.addActionListener(this);
        controls.add(back);
        controls.add(forward);
        controls.add(first);
        controls.add(last);
        controls.add(up);
        controls.add(down);
        controls.add(create);
        controls.add(delete);
        add(controls, BorderLayout.SOUTH);
    }
    
    public void setDeck(Deck deck) {
        this.deck = deck;
    }
    
    public Deck getDeck() {
        return deck;
    }

    public void update() {        
        Card card = deck.current();
        index.setText(deck.position() + 1 + " of " + deck.size() + (card.frontSideUp() ? " (front) " : " (back) "));
        textPane.setText(card.frontSideUp() ? card.getFront() : card.getBack());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == forward) {
            Card card = deck.current();
            if (card.frontSideUp()) {
                card.flip();
                update();
            }
            else if (!deck.isLast()) {
                deck.next();
                update();
            }
        }
        else if (e.getSource() == back) {
            Card card = deck.current();
            if (card.backSideUp()) {
                card.flip();
                update();
            }
            else if (!deck.isFirst()) {
                deck.previous();
                update();
            }
        }
        else if (e.getSource() == first) {
            deck.first();
            update();
        }
        else if (e.getSource() == last) {
            deck.last();
            update();
        }
        else if (e.getSource() == up) {
            deck.swap(deck.position(), deck.position() + 1);
            deck.next();
            update();
        }
        else if (e.getSource() == down) {
            deck.swap(deck.position(), deck.position() - 1);
            deck.previous();
            update();
        }
        else if (e.getSource() == create) {
            deck.add(new Card());
            deck.last();
            update();
        }
        else if (e.getSource() == delete) {
            deck.remove();
            update();
        }
    }
}
