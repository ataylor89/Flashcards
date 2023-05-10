package flashcards;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.awt.Dimension;
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

/**
 *
 * @author andrewtaylor
 */
public class Flashcards extends JFrame implements ActionListener {
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newFile, openFile, saveFile, saveFileAs, exit;
    private JPanel contentPane;
    private CenterPanel centerPanel;
    private JFileChooser fileChooser;
    private Deck deck;
    
    public Flashcards() {
        super("Flashcards");
        deck = new Deck();
    }
    
    public void createAndShowGui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newFile = new JMenuItem("New");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        saveFile.setEnabled(false);
        saveFileAs = new JMenuItem("Save as");
        exit = new JMenuItem("Exit");
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveFileAs);
        fileMenu.add(exit);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setPreferredSize(new Dimension(1200, 900));
        contentPane.add(Box.createVerticalStrut(125));
        centerPanel = new CenterPanel();
        centerPanel.setDeck(deck);
        centerPanel.update();
        contentPane.add(centerPanel);
        contentPane.add(Box.createVerticalStrut(125));
        setContentPane(contentPane);
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        addListeners();
        pack();
        setVisible(true);
    }
    
    public void addListeners() {
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        saveFileAs.addActionListener(this);
        exit.addActionListener(this);
        contentPane.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               contentPane.requestFocusInWindow();
           } 
        });
        InputMap im = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        ActionMap am = contentPane.getActionMap();
        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card card = deck.current();
                if (card.frontSideUp()) {
                    card.flip();
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
                Card card = deck.current();
                if (card.backSideUp()) {
                    card.flip();
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
    
    public void newFile() {
        this.deck = new Deck();
        centerPanel.setDeck(deck);
        centerPanel.update();
        saveFile.setEnabled(false);
    }
    
    public void openFile(File file) {
        try {
            XmlMapper mapper = new XmlMapper();
            deck = mapper.readValue(file, Deck.class);
            deck.setFile(file);
            centerPanel.setDeck(deck);
            centerPanel.update();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    public void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
	    openFile(file);
            saveFile.setEnabled(true);
	}
    }
    
    public void saveFile() {
        try {
            XmlMapper mapper = new XmlMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            File file = deck.getFile();
            mapper.writeValue(file, deck);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    public void saveFileAs() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            deck.setFile(file);
	    saveFile();
            saveFile.setEnabled(true);
	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFile) {
            newFile();
        }
        else if (e.getSource() == openFile) {
            openFile();
        }
        else if (e.getSource() == saveFile) {
            saveFile();
        }
        else if (e.getSource() == saveFileAs) {
            saveFileAs();
        }
        else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        }
    }
                
    public static void main(String[] args) {
        Flashcards flashcards = new Flashcards();
        flashcards.createAndShowGui();
    }
}