//
// Name: Santana, Daniel
// Project: #4
// Due: 05/09/19
// Course: cs-2450-02-sp19
//
// Description:
//        In the assignment we created the menu bar and text area
//        that notepad uses along with the key accelerators
//        which allow use to take quick shortcuts to the 
//        menu option. Also updated the about notepad and
//        open... option to display the current directory files.
//        to add to the text area.
//        Update: I implemented the status bar and the go to section
//        when prompted. It checks if its within the JTextArea total
//        lines.
//        Update: Added the JFontChooser & Color Chooser
//        Update: Fixed menus and Find/Next
//         
//add constructor for command line
//back space on pop up menu reset 

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import javax.swing.undo.UndoManager;

public class Notepad {

    private JFrame jf;
    private JLabel jlabStatus;
    private int lines;
    private int columns;
    private boolean setCheckBox;
    private JTextArea jta;
    private JButton jbtFind;

    //open in terminal FIX
    public Notepad(String fileName) {
        this();
        try {
            //use JFileChooser
            File f = new File(fileName);
            jf.setTitle(f.getName() + " - Notepad");
            try (BufferedReader bf = new BufferedReader(new FileReader(f.getAbsolutePath()))) {
                jta.read(bf, "File");
                //check IO exception
            }
        } catch (IOException ex) {
            ex.getLocalizedMessage();
           JOptionPane.showMessageDialog(jf, "Invalid file must\n"
                    + "be java or txt", "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Notepad() {
        //borderlayout default
        //FIND DEFAULT LOCATION!!!
        jf = new JFrame("Untitled - Notepad");
        jf.setSize(800, 600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setIconImage(new ImageIcon("Notepad.png").getImage());

        //save if changed or close
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (!(jta.getText() == null || jta.getText().equals(""))) {
                    int value = displayMessage(jf, "Notepad");
                    if(value ==1) jf.dispose();
                    
                } else {
                    jf.dispose();
                }
            }
        });
        jta = new JTextArea();
        jta.setBackground(Color.WHITE);
        jta.setFont(new Font("Courier New", Font.PLAIN, 12));
        jta.setForeground(Color.BLACK);

        //UNDO
        UndoManager undoMan= new UndoManager();
        jta.getDocument().addUndoableEditListener((UndoableEditEvent e) -> {
            undoMan.addEdit(e.getEdit());
        });
        //CHECK THIS
        jta.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                displayMessage(jf, "Notepad");
            }
        });
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setPreferredSize(new Dimension(640, 480));
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jlabStatus = new JLabel("");
        JPanel jplStatus = new JPanel();
        jplStatus.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jplStatus.add(jlabStatus);

        JMenuBar jmenu = new JMenuBar();
        JMenu cFile = new JMenu("File");
        //setMnemonic 'F' is obsolete for all methods
        cFile.setMnemonic(KeyEvent.VK_F);
        JMenuItem brand = new JMenuItem("New", 'N');
        brand.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        JMenuItem open = new JMenuItem("Open...", 'O');
        open.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        JMenuItem save = new JMenuItem("Save", 'S');
        save.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        JMenuItem saveAs = new JMenuItem("Save As...", 'A');
        JMenuItem page = new JMenuItem("Page Setup...", 'U');
        JMenuItem print = new JMenuItem("Print...", 'P');
        print.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        JMenuItem exit = new JMenuItem("Exit", 'X');

        saveAs.addActionListener((ae) -> {
            callSaveAs();
        });
        save.addActionListener((ae) -> {
            callSave();
        });
        brand.addActionListener((ae) -> {
            if (!(jta.getText() == null || jta.getText().equals(""))) {
                int display = displayMessage(jf, "Notepad");
                if(display ==1){ jf.dispose(); Notepad np = new Notepad();}
            } else {
                jf.dispose();
                Notepad np = new Notepad();
            }
        });
        print.addActionListener((ae) -> {
            //add printing
        });
        exit.addActionListener((ae) -> {
            if (!(jta.getText() == null || jta.getText().equals(""))) {
                int displayMessage = displayMessage(jf, "Notepad");
                if(displayMessage == 1) jf.dispose();
            } else {
                System.exit(0);
            }
        });
        
        open.addActionListener((ae) -> {

            File file = new File(System.getProperty("user.dir"));
            //jfc.getCurrentDirectory
            JFileChooser jfc = new JFileChooser(file);
            FileFilter filter = new FileNameExtensionFilter("Text (*.txt)", "txt", "text");
            FileFilter filter2 = new FileNameExtensionFilter("Java (*.java)", "java");
            jfc.setFileFilter(filter);
            jfc.addChoosableFileFilter(filter2);
            int showD = jfc.showOpenDialog(jf);
            boolean accept = (filter.accept(jfc.getSelectedFile())
                    || filter2.accept(jfc.getSelectedFile()));
            if (showD == JFileChooser.APPROVE_OPTION && (accept)) {
                try {
                    File f = jfc.getSelectedFile();
                    jf.setTitle(f.getName() + " - Notepad");
                    try (BufferedReader bf = new BufferedReader(new FileReader(f))) {
                        jta.read(bf, "File");
                    }

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(jf, "Invalid file must\n"
                            + "be java or txt", "Message", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        
        cFile.add(brand);
        cFile.add(open);
        cFile.add(save);
        cFile.add(saveAs);
        cFile.addSeparator();
        cFile.add(page);
        cFile.add(print);
        cFile.addSeparator();
        cFile.add(exit);
        jmenu.add(cFile);

        JMenu edit = new JMenu("Edit");
        edit.setMnemonic('E');
        JMenuItem undo = new JMenuItem("Undo", 'U');
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        JMenuItem cut = new JMenuItem("Cut", 'T');
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        JMenuItem copy = new JMenuItem("Copy", 'C');
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        JMenuItem paste = new JMenuItem("Paste", 'P');
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        JMenuItem delete = new JMenuItem("Delete", 'L');
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        JMenuItem find = new JMenuItem("Find...", 'F');
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        JMenuItem findNext = new JMenuItem("Find Next", 'N');
        findNext.setDisplayedMnemonicIndex(5);
        findNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        JMenuItem replace = new JMenuItem("Replace...", 'R');
        //check control G not working!!
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        JMenuItem gto = new JMenuItem("Go To...", 'G');
        gto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
        JMenuItem selectAll = new JMenuItem("Select All", 'A');
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        JMenuItem datetime = new JMenuItem("Time/Date", 'D');
        datetime.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

        undo.addActionListener((ae) -> {
            if(undoMan.canUndo()){
                undoMan.undo();
            }
        });
        cut.addActionListener((ae) -> {
            jta.cut();
        });
        copy.addActionListener((ae) -> {
            jta.copy();
        });
        paste.addActionListener((ae) -> {
            jta.paste();
        });
        delete.addActionListener((ae) -> {
            jta.replaceSelection("");
        });
        find.addActionListener((ae) -> {
            find(jf, "Find");
        });
        findNext.addActionListener((ae) -> {
            jbtFind.doClick();
            //call method in find to findNext (method)
        });
        replace.addActionListener((ae) -> {
            replace(jf, "Replace");
        });
        gto.addActionListener((ae) -> {
            try {
                //check text to set line position check status bar
                jta.setCaretPosition(jta.getDocument().getDefaultRootElement()
                        .getElement(goToLine(jf, "Go To Line")).getStartOffset());
            } catch (BadLocationException ex) {
                ex.getLocalizedMessage();
            }
        });
        selectAll.addActionListener((ae) -> {
            jta.selectAll();
        });
        datetime.addActionListener((ae) -> {
            String s = new SimpleDateFormat("hh:mm a M/dd/yyy").format(new Date());
            jta.insert(s, jta.getCaretPosition());
            //append at current mouse location
        });

        edit.add(undo);
        edit.addSeparator();
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.addSeparator();
        edit.add(find);
        edit.add(findNext);
        edit.add(replace);
        edit.add(gto);
        edit.addSeparator();
        edit.add(selectAll);
        edit.add(datetime);

        jmenu.add(edit);

        JMenu format = new JMenu("Format");
        format.setMnemonic('o');
        JCheckBoxMenuItem ww = new JCheckBoxMenuItem("Word Wrap");
        ww.setMnemonic('W');
        JMenuItem font = new JMenuItem("Font...", 'F');
        JMenu color = new JMenu("Color");
        color.setMnemonic(KeyEvent.VK_C);
        JMenuItem foreg = new JMenuItem("Foreground", KeyEvent.VK_F);
        JMenuItem backg = new JMenuItem("Backgroud", KeyEvent.VK_B);
        color.add(foreg);
        color.add(backg);
        
        foreg.addActionListener((ae) -> {
            jta.setForeground(JColorChooser.showDialog(jf, "Foreground Color", jta.getForeground()));
        });
        backg.addActionListener((ae) -> {
            jta.setBackground(JColorChooser.showDialog(jf, "Background Color", jta.getBackground()));
        });

        font.addActionListener((ae) -> {
            Font selected = JFontChooser.getFontChooser(jf, "Font", jta.getFont());
            if (selected != null) {
                jta.setFont(selected);
            }

        });

        format.add(ww);
        format.add(font);
        format.add(color);
        jmenu.add(format);

        JMenu view = new JMenu("View");
        view.setMnemonic('v');
        JCheckBoxMenuItem stats = new JCheckBoxMenuItem("Status Bar");
        stats.setMnemonic('S');
        stats.setSelected(false);
        view.add(stats);
        jmenu.add(view);
        copy.setEnabled(false);
        delete.setEnabled(false);
        
        cut.setEnabled(false);
        //pop up menu
        JPopupMenu jpm = new JPopupMenu();

        JMenuItem undoPop = new JMenuItem("Undo", 'U');
        JMenuItem cutPop = new JMenuItem("Cut", 'T');
        JMenuItem copyPop = new JMenuItem("Copy", 'C');
        JMenuItem pastePop = new JMenuItem("Paste", 'P');
        JMenuItem deletePop = new JMenuItem("Delete", 'D');
        JMenuItem selectPop = new JMenuItem("Select All", 'A');

        undoPop.addActionListener((ae) -> {
             if(undoMan.canUndo()){
                undoMan.undo();
            }
        });
        cutPop.addActionListener((ae) -> {
            jta.cut();
        });
        copyPop.addActionListener((ae) -> {
            jta.copy();
        });
        pastePop.addActionListener((ae) -> {
            jta.paste();
        });
        deletePop.addActionListener((ae) -> {
            jta.replaceSelection("");
        });
        selectPop.addActionListener((ae) -> {
            jta.selectAll();
        });

        jpm.setPreferredSize(new Dimension(150, 150));
        jpm.add(undoPop);
        jpm.addSeparator();
        jpm.add(cutPop);
        jpm.add(copyPop);
        jpm.add(pastePop);
        jpm.add(deletePop);
        jpm.addSeparator();
        jpm.add(selectPop);

        jta.addMouseListener(new MouseAdapter() {
            @Override
            //check if backspace or delete is pressed when highlighting 
            //to reset selected text
            public void mouseReleased(MouseEvent e) {
                if (jta.getSelectedText() != null) {
                    copy.setEnabled(true);
                    delete.setEnabled(true);
                    cut.setEnabled(true);

                    copyPop.setEnabled(true);
                    deletePop.setEnabled(true);
                    cutPop.setEnabled(true);

                } else {
                    copy.setEnabled(false);
                    delete.setEnabled(false);
                    cut.setEnabled(false);

                    copyPop.setEnabled(false);
                    deletePop.setEnabled(false);
                    cutPop.setEnabled(false);
                }

                if (e.isPopupTrigger()) {
                    jpm.show(e.getComponent(), e.getX(), e.getY());
                }

                if (jta.getText() == null || jta.getText().equals("")) {
                    selectPop.setEnabled(false);
                    selectAll.setEnabled(false);
                } else if (!(jta.getSelectedText() == null)) {
                    selectPop.setEnabled(false);
                    selectAll.setEnabled(false);
                } else {
                    selectPop.setEnabled(true);
                    selectAll.setEnabled(true);
                }
            }
        });
        jta.addCaretListener((cl) -> {
            try {
                lines = jta.getLineOfOffset(jta.getCaretPosition());
                lines++;
                columns = jta.getCaretPosition() - Utilities.getRowStart(jta, jta.getCaretPosition());
                columns++;
                updateBar(lines, columns);
            } catch (BadLocationException e) {
                updateBar(lines, columns);
            }
        });

        stats.addItemListener((ie) -> {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                //enable
                jplStatus.setVisible(true);
                jlabStatus.setVisible(true);
            } else {
                //disable
                jplStatus.setVisible(false);
                jlabStatus.setVisible(false);
            }
        });

        JMenu help = new JMenu("Help");
        help.setMnemonic('h');
        JMenuItem vh = new JMenuItem("View Help", 'H');
        JMenuItem about = new JMenuItem("About Notepad", 'A');
        setCheckBox = false;
        vh.addActionListener((ae) -> {
//                Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome "
//                        + "https://website
            try {
                Desktop.getDesktop().browse(new URL("https://www.google.com"
                        + "/search?source=hp&ei=4Wu5XJKnGcmEsAXX0aqIAw&q="
                        + "get+help+with+notepad+in+windows+10&oq="
                        + "&gs_l=psy-ab.1.1.35i39l6.0.0..4084...2.0."
                        + ".0.132.132.0j1......0......gws-wiz.....6.QFngiA-BWvI").toURI());
            } catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(jf, "Chrome desktop "
                        + "is not supported", "Support", JOptionPane.ERROR_MESSAGE);
            }
        });

        ww.addItemListener((ie) -> {
            //add set boolean     
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                jta.setLineWrap(true);
                jta.setWrapStyleWord(true);
                jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                stats.setEnabled(false);
                setCheckBox = stats.isSelected();
                stats.setSelected(false);
                gto.setEnabled(false);
            } else {
                if (setCheckBox) {
                    stats.setSelected(true);
                }
                gto.setEnabled(true);
                stats.setEnabled(true);
                jta.setLineWrap(false);
                jta.setWrapStyleWord(false);
                jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            }
        });
        about.addActionListener((ae) -> {
            JOptionPane.showMessageDialog(jf, "Notepad \n \u00A9 2019 D. Santana ", "About Notepad",
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Notepad.png"));
        });

        help.add(vh);
        help.addSeparator();
        help.add(about);
        jmenu.add(help);

        jf.setJMenuBar(jmenu);
        jf.add(jsp);
        jf.add(jplStatus, BorderLayout.SOUTH);
        jlabStatus.setVisible(false);
        jplStatus.setVisible(false);
        updateBar(lines + 1, columns + 1);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    private void find(JFrame jf, String find) {
        //modeless
        //add the listeners and Find next will inherit the findNext method too
        JDialog jd = new JDialog(jf, find, false);
        jd.setSize(425, 200);
        jd.setLayout(new FlowLayout());
        jd.setResizable(false);

        JLabel findWhat = new JLabel("Find What:");
        findWhat.setBorder(new EmptyBorder(0, 0, 0, 15));
        JTextField jtfFind = new JTextField(20);
        
        JPanel jplButtons = new JPanel();
        jplButtons.setLayout(new BorderLayout(1, 5));
        jbtFind = new JButton("Find Next");
        jbtFind.setActionCommand("no");
        jbtFind.setActionCommand("cancel");
        jbtFind.setEnabled(false);
        JButton jbtCancel = new JButton("Cancel");
        jplButtons.add(jbtFind, BorderLayout.NORTH);
        jplButtons.add(jbtCancel, BorderLayout.SOUTH);
        
        JPanel jpl = new JPanel();
        jpl.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "Direction", 0, 0));
        JRadioButton jrbUp = new JRadioButton("Up");
        JRadioButton jrbDown = new JRadioButton("Down");
        
        jrbDown.setSelected(true);
        
        ButtonGroup jbg = new ButtonGroup();
        jbg.add(jrbUp);
        jbg.add(jrbDown);

        jpl.add(jrbUp);
        jpl.add(jrbDown);

         
        JCheckBox jcb = new JCheckBox("Match case");
        jtfFind.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void changed(){
                if(jtfFind.getText().isEmpty())
                   jbtFind.setEnabled(false);
                else jbtFind.setEnabled(true);
            }
        });
        
        jtfFind.addActionListener((ae)->{
            jbtFind.doClick();
        });
        jbtCancel.addActionListener((ae)->{
            jd.dispose();
        });
           
        jbtFind.addActionListener((ae)->{
            int start;
            if(jrbDown.isSelected()){
                start= jta.getCaretPosition();
            }
            else {
                start= 0;
            }
            
           String jtfText = jtfFind.getText();
               if(jtfText != null)
               {
                  String jtaText = jta.getText();
                  boolean end = false;
                  while(!end && (jta.getCaret() != jta.getSize()) )
                  {
                     int index;
                     if(jcb.isSelected())
                         //indexOf returns -1 not found
                        index = jtaText.indexOf(jtfText, start);
                     else
                        index = jtaText.toLowerCase().indexOf(jtfText.toLowerCase(), start);
                     if(index != -1)
                     {
                        end = true;
                        jta.select(index, index + jtfText.length());
                        start = jtfFind.getCaretPosition();
                     }
                     //not found
                     else
                     {
                        end = true;
                        JOptionPane.showMessageDialog(jd, "Cannot find " + "\"" + jtfText + "\"", "Notepad", JOptionPane.INFORMATION_MESSAGE);
                     }
                  }
               }
        });
        jd.getRootPane().setDefaultButton(jbtFind);
        jd.add(findWhat);
        jd.add(jtfFind);
        jd.add(jplButtons);
        jd.add(jcb);
        jd.add(jpl);

        jd.setLocationRelativeTo(jf);
        jd.setVisible(true);
        
        
    }

    private void replace(JFrame jf, String replace) {
        //modal less
        JDialog jd = new JDialog(jf, replace, false);
        jd.setSize(500, 225);
        jd.setLayout(new FlowLayout(FlowLayout.LEFT));
        jd.setResizable(false);

        JPanel jplFields = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        jplFields.setPreferredSize(new Dimension(340, 50));
        JLabel findWhat = new JLabel("Find what:      ");
        JTextField jtfFind = new JTextField(20);
        JLabel replaceWith = new JLabel("Replace with:");

        JTextField jtfReplace = new JTextField(20);
        jplFields.add(findWhat);
        jplFields.add(jtfFind);
        jplFields.add(replaceWith);
        jplFields.add(jtfReplace);

        JPanel jplButtons = new JPanel();
        jplButtons.setLayout(new GridLayout(4, 1, 3, 5));
        jbtFind = new JButton("Find Next");
        JButton jbtReplace = new JButton("Replace");
        JButton jbtAll = new JButton("Replace All");
        JButton jbtCancel = new JButton("Cancel");
        jplButtons.add(jbtFind);
        jplButtons.add(jbtReplace);
        jplButtons.add(jbtAll);
        jplButtons.add(jbtCancel);

        JCheckBox jcb = new JCheckBox("Match case");

        //add components
        jd.add(jplFields);
        jd.add(jplButtons);
        jd.add(jcb);
        jd.getRootPane().setDefaultButton(jbtFind);

        jd.setLocationRelativeTo(jf);
        jd.setVisible(true);
        //return if necessary
    }

    private int goToLine(JFrame frame, String title) throws BadLocationException {
        //fix 
        JDialog jd = new JDialog(frame, title, true);
        jd.setSize(350, 150);
        jd.setLayout(new FlowLayout());
        jd.setResizable(false);

        JLabel lab = new JLabel("Line number:");
        lab.setDisplayedMnemonic('L');
        JTextField jtfield = new JTextField(25);
        lab.setLabelFor(jtfield);

        //set jtfield with intial position
        int line = jta.getLineOfOffset(jta.getCaretPosition());
        jtfield.setText(String.valueOf(line + 1));

        JButton jbtGo = new JButton("Go To");
        jbtGo.setActionCommand("cancel");
        JButton jbtCancel = new JButton("Cancel");

        jtfield.addKeyListener(new KeyAdapter() {
            //add for alphabetsn keyPressed
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    jbtGo.doClick();
                }
            }
        });
        jbtGo.addActionListener((ae) -> {
            try {
                int val = Integer.parseInt(jtfield.getText());

                if (val > 0 && val <= jta.getLineCount()) {
                    jbtGo.setActionCommand("Go To");
                    jd.dispose();
                } else {
                    jtfield.setText(String.valueOf(jta.getLineOfOffset(jta.getCaretPosition()) + 1));
                    jtfield.selectAll();
                    jtfield.requestFocusInWindow();
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Line number must be within the total"
                        + " line numbers!");
            } catch (BadLocationException ex) {
                ex.getLocalizedMessage();
            }
        });

        jbtCancel.addActionListener((ae) -> {
            jd.dispose();
        });

        jd.getRootPane().setDefaultButton(jbtGo);
        jd.add(lab);
        jd.add(jtfield);
        jd.add(jbtGo);
        jd.add(jbtCancel);

        jd.setLocationRelativeTo(frame);
        jd.setVisible(true);

        if (jbtGo.getActionCommand().equals("Go To")) {
            return Integer.valueOf(jtfield.getText()) - 1;

        } else {
            jd.dispose();
            return (jta.getLineOfOffset(jta.getCaretPosition()));
        } 

    }

    private void updateBar(int lines, int column) {
        jlabStatus.setText(" Ln " + lines + ", Col " + column);
    }

    private int displayMessage(JFrame jf, String title) {
        //fix calls to save
        JDialog jd = new JDialog(jf, title, true);
        jd.setLayout(new BorderLayout());
        jd.setSize(350, 150);
        jd.setResizable(false);
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JLabel jlab = new JLabel("Do you want to save changes to "
                + jf.getTitle().substring(0, jf.getTitle().indexOf("-")).trim() + "?");
        jlab.setBorder(new EmptyBorder(10, 10, 10, 10));
        jlab.setForeground(Color.BLUE);
        jlab.setFont(new Font("Courier New", Font.BOLD, 12));
        JPanel jplButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        jplButtons.setBackground(Color.LIGHT_GRAY);
        JButton save = new JButton("Save");
        JButton dont = new JButton("Don't Save");
        dont.setActionCommand("do");
        JButton cancel = new JButton("Cancel");

        save.addActionListener((ae) -> {
            jd.dispose();
            callSave();
        });

        dont.addActionListener((ae) -> {
            dont.setActionCommand("dont");
            jd.dispose();
        });

        cancel.addActionListener((ae) -> {
            jd.dispose();
        });
        
        jplButtons.add(save);
        jplButtons.add(dont);
        jplButtons.add(cancel);
        //set actionliisteners

        jd.add(jlab);
        jd.add(jplButtons, BorderLayout.SOUTH);
        jd.setLocationRelativeTo(jf);

        jd.setVisible(true);
        
        return dont.getActionCommand().equals("dont") ? 1 : 0;
        
    }

    private void callSave() {
        File fl = new File(jf.getTitle().substring(0, jf.getTitle().indexOf("-")));
        if (fl.exists()) {
            try {
                try (BufferedWriter bf = new BufferedWriter(new FileWriter(fl))) {
                    bf.write(jta.getText());
                }
            } catch (IOException ex) {
                ex.getLocalizedMessage();
            }
        } else {
            callSaveAs();
        }

    }

    private void callSaveAs() {
        JFileChooser jfc = new JFileChooser();
        
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        FileFilter filter = new FileNameExtensionFilter("Text (*.txt)", "txt", "text");
        FileFilter filter2 = new FileNameExtensionFilter("Java (*.java)", "java");
        jfc.setFileFilter(filter);
        jfc.addChoosableFileFilter(filter2);
        jfc.addChoosableFileFilter(filter);
        
//        jfc.addPropertyChangeListener((PropertyChangeEvent evt) -> {
//            Object o = evt.getNewValue();
//            
//            if (o instanceof FileNameExtensionFilter) {
//                FileNameExtensionFilter filter1 = (FileNameExtensionFilter) o;
//                jfc.setSelectedFile(new File(jfc.getSelectedFile().toString() + filter1.getDescription()));
//            }
//        });
        
        if (jfc.showSaveDialog(jf) == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().exists()) {
                int ans = JOptionPane.showConfirmDialog(jf,
                        jfc.getSelectedFile().getName() + " already exists. \n"
                        + "Do you want to replace it?", "Confirm Save As",
                        JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    try (BufferedWriter bf = new BufferedWriter(
                            new FileWriter(jfc.getSelectedFile().getPath()))) {
                        bf.write(jta.getText());
                        
                        jf.setTitle(jfc.getSelectedFile().getName() + " - Notepad");
                        jf.dispose();
                    } catch (IOException ex) {
                        ex.getLocalizedMessage();
                    } 
                }
            }//dne file
            else{
                //check if filter selected
                String l = jfc.getSelectedFile().getName();
                if(jfc.getFileFilter().getDescription().equals(".java")){
                    l = l + ".java";
                }
                else{
                    l= l+".txt";
                }
                try {
                    try (BufferedWriter bfw = new BufferedWriter(
                            new FileWriter(l))) {
                        bfw.write(jta.getText());
                    }
                } catch (IOException ex) {
                    ex.getLocalizedMessage();
                }
                
            } 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (args.length > 0) {
                //find pathfile of args[0]
                Notepad notepad = new Notepad(args[0]);
            } else {
                Notepad notepad = new Notepad();
            }
        });
    }
}
