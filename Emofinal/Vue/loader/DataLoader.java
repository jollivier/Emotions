package loader;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import vue.Fenetre;

import emo.Data;

public class DataLoader extends JPanel implements  PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
    private Task task;
    private JFrame frame;
    
    
    
    class Task extends SwingWorker<Void, Void> {
    	private int current;
    	private String path;
    	private Fenetre fenetre;
    	
    	public Task(String path, Fenetre fenetre){
    		this.path = path;
    		this.fenetre = fenetre;
    	}
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
        	
        	/*
        	 * on cherche la taille de la base
        	 */
        	FileInputStream fis = null;
			try {
				fis = new FileInputStream(path);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	LineNumberReader l = new LineNumberReader(       
        	       new BufferedReader(new InputStreamReader(fis)));
        	int count = 0;
        	              try {
							while ((l.readLine())!=null)
							 {
							    count = l.getLineNumber();
							 }
							l.close();
							fis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        	
        	
        	progressBar.setMaximum(count);
        	
        	/*
        	 * on boucle pour enrichier nos tableaux mot<-->valence
        	 */
        	ArrayList<String> l_word = new ArrayList<String>();
    		ArrayList<Float> l_val = new ArrayList<Float>();
    		
        	File f = new File(path);
    		FileReader fr;
    		try {
    			fr = new FileReader(f);
    			
    			int b;
    			String str = "";
    			while((b = fr.read())!=-1){
    				char ch = (char) b;
    				if(ch =='\n' && str != "" && (str.startsWith("﻿#")==false && str.startsWith("#")==false)){
    					progressBar.setValue(progressBar.getValue()+1);
    					String[] row = str.split("	");
    					String[] base=row[4].split(" ");
    					for(int k=0;k<base.length;k++){
    						if((base[k].substring(base[k].length()-2,base[k].length()-1)).equals("#")==true){
    							base[k] = base[k].substring(0,base[k].length()-2);
    						}
    						else{
    							base[k]=base[k].substring(0,base[k].length()-3);
    						}}
    					
    					for(int i=0;i<base.length;i++){
    						l_word.add(base[i]);
    					l_val.add(Float.parseFloat(row[2])-Float.parseFloat(row[3]));
    					}
    					str = "";
    				}
    				
    				else if(ch =='\n' && str != "" && (str.startsWith("﻿#")==true || str.startsWith("#")==true)){
    					str="";
    					//System.out.println(str);
    				}
    				
    				else{
    					str+=ch;
    					//System.out.println(str);
    				}
    			}
    			
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		Data.setWords(new String[l_word.size()]);
    		Data.setValence(new float[l_val.size()]);
    		for(int i=0; i<Data.getWords().length; i++){
    			Data.getWords()[i] = l_word.get(i);
    			Data.getValence()[i] = (l_val.get(i)*5)+5;
    		}
    		
    		l_word.clear();
    		l_val.clear();
    		//********************************************************************
        	              
        	  
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
            current = 0;
            frame.dispose();
            fenetre.setVisible(true);
        }
		public int getCurrent() {
			//this.updateObservateur();
			return current;
		}

		public void setCurrent(int current) {
			this.current = current;
			//this.updateObservateur();
		}
    }

    public DataLoader(String path, JFrame frame, Fenetre fenetre) {
        super(new BorderLayout());
        this.frame = frame;
        progressBar = new JProgressBar(0);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel();
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task(path, fenetre);
        task.addPropertyChangeListener(this);
        task.execute();
        
    }

	/**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        } 
    }


    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    public static void createAndShowGUI(String path, Fenetre fenetre) {
        //Create and set up the window.
        JFrame frame = new JFrame("SentiWordNet loading...");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        //Create and set up the content pane.
        JComponent newContentPane = new DataLoader(path, frame, fenetre);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


}
