package flashcards;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author andrewtaylor
 */
public class Flashcards extends JFrame implements ActionListener, FocusListener, MenuListener {
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newDeck, openDeck, saveDeck, saveDeckAs, exit;
    private JPanel contentPane;
    private JPanel top;
    private JLabel position;
    private JTextPane flashcard;
    private JScrollPane scrollPane;
    private JPanel controls;
    private JButton next, back, flip, up, down, create, delete;
    private JFileChooser fileChooser;
    private File file;
    private Deck deck;
    private boolean faceUp = true;
    
    public Flashcards() {
        super("Flashcards");
        deck = new Deck();
        deck.add(new Card());
    }
    
    public void createAndShowGui() {
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.addMenuListener(this);
        newDeck = new JMenuItem("New");
        newDeck.addActionListener(this);
        openDeck = new JMenuItem("Open");
        openDeck.addActionListener(this);
        saveDeck = new JMenuItem("Save");
        saveDeck.addActionListener(this);
        saveDeckAs = new JMenuItem("Save as");
        saveDeckAs.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        fileMenu.add(newDeck);
        fileMenu.add(openDeck);
        fileMenu.add(saveDeck);
        fileMenu.add(saveDeckAs);
        fileMenu.add(exit);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(Box.createVerticalStrut(100));
        top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setMinimumSize(new Dimension(600, 40));
        top.setMaximumSize(new Dimension(600, 40));
        position = new JLabel();
        top.add(position);
        contentPane.add(top);
        flashcard = new JTextPane();
        flashcard.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        flashcard.addFocusListener(this);
        scrollPane = new JScrollPane(flashcard);
        scrollPane.setMinimumSize(new Dimension(600, 400));
        scrollPane.setMaximumSize(new Dimension(600, 400));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane);
        controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setMinimumSize(new Dimension(600, 40));
        controls.setMaximumSize(new Dimension(600, 40));
        next = new JButton("Next");
        next.addActionListener(this);
        back = new JButton("Back");
        back.addActionListener(this);
        flip = new JButton("Flip");
        flip.addActionListener(this);
        up = new JButton("Up");
        up.addActionListener(this);
        down = new JButton("Down");
        down.addActionListener(this);
        create = new JButton("New");
        create.addActionListener(this);
        delete = new JButton("Delete");
        delete.addActionListener(this);
        controls.add(next);
        controls.add(back);
        controls.add(flip);
        controls.add(up);
        controls.add(down);
        controls.add(create);
        controls.add(delete);
        contentPane.add(controls);
        setContentPane(contentPane);
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        updateView(true);
    }
    
    private void updateView(boolean faceUp) {
        this.faceUp = faceUp;
        position.setText(deck.position() + 1 + " of " + deck.size() + (faceUp ? " (front) " : " (back) "));
        Card card = deck.current();
        flashcard.setText(faceUp ? card.getFront() : card.getBack());
    }
    
    public void open(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            this.deck = (Deck) in.readObject();
            updateView(true);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
        }
    }
    
    public void open() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.file = fileChooser.getSelectedFile();
	    open(file);
	}
    }
    
    public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(deck);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    public void saveAs() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.file = fileChooser.getSelectedFile();
	    save();
	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == next) {
            deck.next();
            updateView(true);
        }
        else if (e.getSource() == back) {
            deck.previous();
            updateView(true);
        }
        else if (e.getSource() == flip) {
            updateView(!faceUp);
        }
        else if (e.getSource() == up) {
            deck.swap(deck.position(), deck.position() + 1);
            deck.next();
            updateView(true);
        }
        else if (e.getSource() == down) {
            deck.swap(deck.position(), deck.position() - 1);
            deck.previous();
            updateView(true);
        }
        else if (e.getSource() == create) {
            deck.add(new Card());
            deck.last();
            updateView(true);
        }
        else if (e.getSource() == delete) {
            deck.remove();
            updateView(true);
        }
        else if (e.getSource() == newDeck) {
            this.deck = new Deck();
            this.deck.add(new Card());
            this.file = null;
            updateView(true);
        }
        else if (e.getSource() == openDeck) {
            open();
        }
        else if (e.getSource() == saveDeck) {
            save();
        }
        else if (e.getSource() == saveDeckAs) {
            saveAs();
        }
        else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        }
    }    
    
    @Override
    public void focusGained(FocusEvent e) {}

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == flashcard) {
            String text = flashcard.getText();
            Card card = deck.current();
            if (faceUp) {
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
    }
    
    @Override
    public void menuSelected(MenuEvent e) {
        if (file == null) {
            saveDeck.setEnabled(false);
        } else {
            saveDeck.setEnabled(true);
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}
    
    public static void main(String[] args) {
        Flashcards flashcards = new Flashcards();
        flashcards.createAndShowGui();
        flashcards.setVisible(true);
    }
}
