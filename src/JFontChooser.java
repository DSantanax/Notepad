//
// Name: Santana, Daniel
// Homework: #5
// Due: 04/25/19
// Course: cs-2450-02-sp19
//
// Description:
//      This class will implement the usage of choosing a 
//      font (JFontChooser) by providing the user a dialogue 
//      and the proper selections.
//      
//
//fix the size increase in jtextfield when a new font is picked
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import javax.swing.*;
//fix font size after change
public class JFontChooser {
    
     public static Font getFontChooser(Frame jf, String title, Font initialFont){
        //true for modal (full control window)
        JDialog jdlg = new JDialog(jf, title, true);
        jdlg.setSize(450, 350);
        jdlg.setLayout(new FlowLayout());
        jdlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jdlg.setResizable(false);
        
        JPanel jplLabels= new JPanel();
        jplLabels.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelFont = new JLabel("Font:");
        labelFont.setDisplayedMnemonic(KeyEvent.VK_F);
        
        JLabel labelStyle = new JLabel("Font Style:");
        labelStyle.setDisplayedMnemonic(KeyEvent.VK_Y);
        
        JLabel labelSize = new JLabel("Size:");
        labelSize.setDisplayedMnemonic(KeyEvent.VK_S);
        labelFont.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 55));
        labelStyle.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 50)); 
        labelSize.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 50));
        jplLabels.add(labelFont);
        jplLabels.add(labelStyle);
        jplLabels.add(labelSize);
        
        JPanel jplSample = new JPanel();
        JTextField jtfSample = new JTextField("The quick brown fox jumps over"
                + " the lazy dog.");
        jtfSample.setFont(initialFont);
        jtfSample.setEditable(false);
        jtfSample.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sample: ", 0, 0));
        jplSample.add(jtfSample);
        
        //add JTF actionListeners
        JPanel jplText= new JPanel();
        jplText.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField jtfFont = new JTextField(10);
        JTextField jtfStyle= new JTextField(10);
        JTextField jtfSize= new JTextField(5);
        jplText.add(jtfFont);
        jplText.add(jtfStyle);
        jplText.add(jtfSize);
        
        jtfFont.setText(String.valueOf(initialFont.getName()));
        jtfSize.setText(String.valueOf(initialFont.getSize()));
        
        labelFont.setLabelFor(jtfFont);
        labelStyle.setLabelFor(jtfStyle);
        labelSize.setLabelFor(jtfSize);
        
        JList<String> jlistFont = new JList<>(GraphicsEnvironment.
                getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        jlistFont.setSelectedValue(initialFont.getFontName(), true);
        jlistFont.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jsbFont = new JScrollPane(jlistFont);
        jsbFont.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jsbFont.setPreferredSize(new Dimension(150, 150));
        //addstyle to fonts and styles
        //jlistFont.setCellRenderer();
        
        int[] stylesType = {Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC};
        String[] styles = {"Plain", "Bold","Italics"
                , "Bold + Italics"};
        JComboBox<JLabel> jcmStyle = new JComboBox(styles);
        
        jcmStyle.setSelectedItem(styles[initialFont.getStyle()]);
        jcmStyle.setEditable(false);
        
        jtfStyle.setText(styles[initialFont.getStyle()]);
        Integer[] sizes= {8,12,15,18,22,30,40,50};
        JList<Integer> jlistSize = new JList(sizes);
        jlistSize.setSelectedValue(initialFont.getSize(), true);
        JScrollPane jspSize= new JScrollPane(jlistSize);
        jspSize.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jspSize.setPreferredSize(new Dimension(150, 150));
        
        jlistFont.addListSelectionListener((le)->{
            jtfFont.setText(jlistFont.getSelectedValue());
            jtfSample.setFont(new Font((String)jlistFont.getSelectedValue(), 
                   stylesType[jcmStyle.getSelectedIndex()], 
                   jlistSize.getSelectedValue()));
        });
        jcmStyle.addActionListener((ae)->{ 
            jtfStyle.setText((String)jcmStyle.getSelectedItem());
            jtfSample.setFont(new Font((String)jlistFont.getSelectedValue(), 
                   stylesType[jcmStyle.getSelectedIndex()], 
                   jlistSize.getSelectedValue()));
        });
        jlistSize.addListSelectionListener((le)->{
            jtfSize.setText(jlistSize.getSelectedValue().toString());
            jtfSample.setFont(new Font((String)jlistFont.getSelectedValue(), 
                   stylesType[jcmStyle.getSelectedIndex()], 
                   jlistSize.getSelectedValue()));
        });
        
        JPanel jplB = new JPanel();
        JButton ok = new JButton("OK");
        ok.setActionCommand("cancel");
        JButton cancel= new JButton("Cancel");
        jplB.add(ok);
        jplB.add(cancel);
        
        ok.addActionListener((ae)->{
            ok.setActionCommand("ok");
            jdlg.dispose();
        });
        cancel.addActionListener((ae)->{ 
            jdlg.dispose();
        });
        
        
        jdlg.add(jplLabels);
     
        jdlg.add(jplText);
     
        jdlg.add(jsbFont);
        jdlg.add(jcmStyle);
        jdlg.add(jspSize);
        
        jdlg.add(jplSample);
        jdlg.add(jplB);
        jdlg.getRootPane().setDefaultButton(ok);
       
        jdlg.setLocationRelativeTo(null);
        jdlg.setVisible(true);
        
        //fix to return
        if(ok.getActionCommand().equals("ok")){
           return new Font((String)jlistFont.getSelectedValue(), 
                   stylesType[jcmStyle.getSelectedIndex()], 
                   jlistSize.getSelectedValue());
        }
        else{
            return null;
        }
    }    
    
}

