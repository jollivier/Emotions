package loader;



import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import vue.Fenetre;
import emo.Affinity;
import emo.Main;

public class ProgressBarLoader extends JPanel
                             implements  PropertyChangeListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
    private Task task;
    private JFrame frame;
    
    class Task extends SwingWorker<Void, Void> {
    	private int current;
    	private Affinity affinity;
    	private String[] str;
    	
    	private Fenetre fenetre;
    	
    	public Task(String[] str, Affinity affinity, Fenetre fenetre){
    		this.affinity = affinity;
    		this.str = str;
    		this.fenetre = fenetre;
    	}
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
        	float val = 0;
	        Object[][] data = new Object[str.length][3];
	        for(int i=0; i<str.length; i++){
	        	System.out.println(str.length+"str = "+str[i]);
	        	str[i] = str[i].trim();
	        	if(!str[i].equals(""))
	        		val = Main.run_study(str[i], affinity);
	        	else
	        		val = 0;
	        	
	        	String valu = ""+val;
	        	if(val==-21){
	        		valu = "OUPS";
	        	}
	        	data[i][0] = new Integer(i+1);
	        	data[i][1] = str[i];
	        	data[i][2] = valu;
	        	progressBar.setValue(progressBar.getValue()+1);
	    	}
	        fenetre.setTable(data);
        	
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

    public ProgressBarLoader(String[] str, Affinity affinity, Fenetre fenetre, JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        progressBar = new JProgressBar(0, str.length);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel();
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task(str, affinity, fenetre);
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
    public static void createAndShowGUI(String[] str, Affinity affinity, Fenetre fenetre) {
        //Create and set up the window.
        JFrame frame = new JFrame("Chargement");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        //Create and set up the content pane.
        JComponent newContentPane = new ProgressBarLoader(str, affinity, fenetre, frame);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


}