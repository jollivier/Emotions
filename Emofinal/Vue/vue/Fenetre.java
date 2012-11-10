package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import loader.ProgressBarLoader;
import emo.Affinity;
import emo.Data;
import emo.Texte_j;
import emo.Verb;

public class Fenetre extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_south;
	private JScrollPane scrollPane;
	private JTextPane textPane;
	private SliderColor slider;
	
	private JMenuBar menuBar;
	private JMenuItem mntmRun;
	private JMenuItem mntmRes;
	
	private JLabel sad = new JLabel(new ImageIcon(getClass().getResource("smil-1.png")));
	private JLabel happy = new JLabel(new ImageIcon( getClass() .getResource("smil1.png")));
	
	private Affinity affinity;
	private JMenuItem mntmAffinity;
	
	public static String path = "";
	private String language = "en";
	
	private String[][] lang_code;
	
	private JPanel lang_pane = new JPanel();
	private JComboBox<String> combobox_language = new JComboBox<String>();
	public static boolean absolut = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fenetre frame = new Fenetre();
					frame.setVisible(false);
					
					String database_path = Fenetre.path + "SentiWordNet.txt";
					Data.load(database_path, frame);//on charge la base des valences pour notre dictionnaire
					new Verb();//on charge la base de verbes
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }

	/**
	 * Create the frame.
	 */
	public Fenetre() {
		
		path = this.getClass().getClassLoader().getResource("Main.class").toString();
		path = path.split("!")[0];
		path = path.replace("jar:", "");
		path = path.replace("file:", "");
		String file_name = "";
		/*if (path.split("\\").length >1){
			file_name = path.split("\\")[path.split("\\").length - 1];
		}
		else{*/
			file_name = path.split("/")[path.split("/").length - 1];
		//}
			path = path.replace(file_name, "");
		
			if(!absolut){
				path = "";
			}
			affinity = new Affinity(Fenetre.path + "affinity.txt");//on charge le fichier d'affinités choisi
			
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panel_south = new JPanel();
		panel_south.setLayout(new BorderLayout(0, 0));
		contentPane.add(panel_south, BorderLayout.SOUTH);
		panel_south.add(sad, BorderLayout.WEST);
		panel_south.add(happy, BorderLayout.EAST);
		
		slider = new SliderColor(4);
		slider.setBackground(Color.LIGHT_GRAY);
		slider.setForeground(Color.WHITE);
		
		panel_south.add(slider);
		
		menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		lang_code = new String[5][2];
		lang_code[0][0]="English";
		lang_code[1][0]="Francais";
		lang_code[2][0]="Espanol";
		lang_code[3][0]="Italian";
		lang_code[4][0]="Deutsch";
		lang_code[0][1]="en";
		lang_code[1][1]="fr";
		lang_code[2][1]="es";
		lang_code[3][1]="it";
		lang_code[4][1]="de";
		
		contentPane.add(lang_pane, BorderLayout.WEST);
		lang_pane.add(combobox_language);
		for(int i=0; i<this.lang_code.length; i++)
			combobox_language.addItem(lang_code[i][0]);
		
		combobox_language.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				language = lang_code[combobox_language.getSelectedIndex()][1];
			}
			
		});
		
		mntmRun = new JMenuItem("RUN");
		menuBar.add(mntmRun);
		
		mntmAffinity = new JMenuItem("Affinity");
		mntmAffinity.setHorizontalAlignment(SwingConstants.RIGHT);
		
		mntmAffinity.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new AffinityEdit(affinity);
			}
			
		});
		
		mntmRes = new JMenuItem("Reset");
		
		mntmRun.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				run_study();
				menuBar.removeAll();
				menuBar.add(mntmRes);
				menuBar.add(mntmAffinity);
				menuBar.revalidate();
				menuBar.repaint();
				
			}
			
		});
		
		mntmRes.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				reset_study();
				menuBar.removeAll();
				menuBar.add(mntmRun);
				slider.setValue(0);
				menuBar.revalidate();
				menuBar.repaint();
				
			}
			
		});
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
	}

	public void run_study(){
		String text = "";
		try {
			text = this.textPane.getDocument().getText(0, textPane.getDocument().getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		text = Texte_j.traduct(text, language);
		final String[] str = text.split("\n");
		if(str.length>0){
			ProgressBarLoader.createAndShowGUI(str, affinity, this);
			
		}
		
	}
	
	public void setTable(Object[][] data){
		panel.removeAll();
        final TableParser table = new TableParser(data);
        
        table.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				if(row!=-1){
					float val = Float.parseFloat((String) table.getValueAt(row, 2));
					slider.setVal((int)(val*2));
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        table.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				if(row!=-1){
					float val = Float.parseFloat((String) table.getValueAt(row, 2));

					slider.setVal((int)(val*2));
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
       
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
	}
	
	public void reset_study(){
		panel.removeAll();
		scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		panel.revalidate();
		panel.repaint();
	}
	
}
