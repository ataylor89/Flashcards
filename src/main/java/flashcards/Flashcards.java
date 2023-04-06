package flashcards;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author andrewtaylor
 */
public class Flashcards extends JFrame implements ActionListener {
    
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
        newDeck = new JMenuItem("New");
        openDeck = new JMenuItem("Open");
        saveDeck = new JMenuItem("Save");
        saveDeckAs = new JMenuItem("Save as");
        exit = new JMenuItem("Exit");
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
        scrollPane = new JScrollPane(flashcard);
        scrollPane.setMinimumSize(new Dimension(600, 400));
        scrollPane.setMaximumSize(new Dimension(600, 400));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane);
        controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setMinimumSize(new Dimension(600, 40));
        controls.setMaximumSize(new Dimension(600, 40));
        next = new JButton("Next");
        back = new JButton("Back");
        flip = new JButton("Flip");
        up = new JButton("Up");
        down = new JButton("Down");
        create = new JButton("New");
        delete = new JButton("Delete");
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
        setupKeyStrokes();
        setupListeners();
        updateView();
    }
    
    public void setupKeyStrokes() {
        InputMap im = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "next");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "previous");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "flip");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "flip");
        ActionMap am = contentPane.getActionMap();
        am.put("next", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                next();
            }
        });
        am.put("previous", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previous();
            }
        });
        am.put("flip", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flip();
            }
        });
    }
    
    public void setupListeners() {
        newDeck.addActionListener(this);
        openDeck.addActionListener(this);
        saveDeck.addActionListener(this);
        saveDeckAs.addActionListener(this);
        exit.addActionListener(this);
        next.addActionListener(this);
        back.addActionListener(this);
        flip.addActionListener(this);
        up.addActionListener(this);
        down.addActionListener(this);
        create.addActionListener(this);
        delete.addActionListener(this);
        contentPane.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               contentPane.requestFocusInWindow();
           } 
        });
        fileMenu.addMenuListener(new MenuListener() {
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
        });
        flashcard.addFocusListener(new FocusAdapter() {
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
        });
    }
    
    private void updateView() {
        position.setText(deck.position() + 1 + " of " + deck.size() + (faceUp ? " (front) " : " (back) "));
        Card card = deck.current();
        flashcard.setText(faceUp ? card.getFront() : card.getBack());
    }
    
    public void next() {
        deck.next();
        faceUp = true;
        updateView();
    }
    
    public void previous() {
        deck.previous();
        faceUp = true;
        updateView();
    }
    
    public void flip() {
        faceUp = !faceUp;
        updateView();
    }
    
    public void up() {
        deck.swap(deck.position(), deck.position() + 1);
        deck.next();
        updateView();
    }
    
    public void down() {
        deck.swap(deck.position(), deck.position() - 1);
        deck.previous();
        updateView();
    }
    
    public void newCard() {
        deck.add(new Card());
        deck.last();
        faceUp = true;
        updateView();
    }
    
    public void removeCard() {
        deck.remove();
        faceUp = true;
        updateView();
    }
    
    public void newDeck() {
        this.deck = new Deck();
        this.deck.add(new Card());
        this.file = null;
        faceUp = true;
        updateView();
    }
    
    public void open(File file) {
        try {
            XmlMapper mapper = new XmlMapper();
            this.deck = mapper.readValue(file, Deck.class);
            updateView();
        } catch (IOException e) {
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
        try {
            XmlMapper mapper = new XmlMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(file, deck);
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
            next();
        }
        else if (e.getSource() == back) {
            previous();
        }
        else if (e.getSource() == flip) {
            flip();
        }
        else if (e.getSource() == up) {
            up();
        }
        else if (e.getSource() == down) {
            down();
        }
        else if (e.getSource() == create) {
            newCard();
        }
        else if (e.getSource() == delete) {
            removeCard();
        }
        else if (e.getSource() == newDeck) {
            newDeck();
        }
        else if (e.getSource() == openDeck) {
            faceUp = true;
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
            
    public static void main(String[] args) {
        Flashcards flashcards = new Flashcards();
        flashcards.createAndShowGui();
        flashcards.setVisible(true);
    }
}