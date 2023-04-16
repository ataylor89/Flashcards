package flashcards;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author andrewtaylor
 */
public class Flashcards extends JFrame implements ActionListener, MenuListener {
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newDeck, openDeck, saveDeck, saveDeckAs, exit;
    private JPanel contentPane;
    private CenterPanel centerPanel;
    private JFileChooser fileChooser;
    private File file;
    private Deck deck;
    
    public Flashcards() {
        super("Flashcards");
        deck = new Deck();
    }
    
    public void createAndShowGui() {
        setSize(1200, 900);
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
        centerPanel = new CenterPanel();
        centerPanel.setDeck(deck);
        centerPanel.update();
        contentPane.add(centerPanel);
        contentPane.add(Box.createVerticalStrut(100));
        setContentPane(contentPane);
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        setupListeners();
    }
    
    public void setupListeners() {
        newDeck.addActionListener(this);
        openDeck.addActionListener(this);
        saveDeck.addActionListener(this);
        saveDeckAs.addActionListener(this);
        exit.addActionListener(this);
        contentPane.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               contentPane.requestFocusInWindow();
           } 
        });
        fileMenu.addMenuListener(this);
        InputMap im = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        ActionMap am = contentPane.getActionMap();
        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int orientation = deck.current().getOrientation();
                boolean frontSideUp = orientation == Card.FRONT;
                if (frontSideUp) {
                    deck.current().flip();
                    centerPanel.update();
                }
                else if (!deck.isLast()) {
                    deck.next();
                    centerPanel.update();
                }
            }
        });
        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int orientation = deck.current().getOrientation();
                boolean frontSideUp = orientation == Card.FRONT;
                if (!frontSideUp) {
                    deck.current().flip();
                    centerPanel.update();
                }
                else if (!deck.isFirst()) {
                    deck.previous();
                    centerPanel.update();
                }
            }
        });
        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deck.last();
                centerPanel.update();
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deck.first();
                centerPanel.update();
            }
        });
    }
    
    public void newDeck() {
        this.deck = new Deck();
        this.file = null;
        centerPanel.setDeck(deck);
        centerPanel.update();
    }
    
    public void open(File file) {
        try {
            XmlMapper mapper = new XmlMapper();
            this.deck = mapper.readValue(file, Deck.class);
            centerPanel.setDeck(deck);
            centerPanel.update();
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
        if (e.getSource() == newDeck) {
            newDeck();
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