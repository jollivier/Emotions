package vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderColor extends JSlider {

	private int amplitude;
	
	Icon icon = new DynamicIcon();
	final JLabel dynamicLabel = new JLabel(icon);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SliderColor(int amplitude){
		this.amplitude = amplitude;
		this.setValue(0);
		this.setSnapToTicks(false);
		this.setPaintTrack(false);
		this.setMinimum(-amplitude);
		this.setMaximum(amplitude);
		this.setBackground(Color.WHITE);
		this.setForeground(Color.WHITE);
		this.setEnabled(false);
		
		setMajorTickSpacing(2);
		setMinorTickSpacing(2);
		setPaintTicks(true);
		setPaintLabels(true);
		
		//setIcon(new ImageIcon("img/smil0.png"));
		
	}
	
	public void setVal(float val){
		if (val<-amplitude)
			val = -amplitude;
		if(val>amplitude){
			val = amplitude;
		}
		//this.
		this.setValue((int) val);
	}
	
	
	class Updater implements ChangeListener {
	    public void stateChanged(ChangeEvent ev) {
	      dynamicLabel.repaint();
	    }
	  }

	  class DynamicIcon implements Icon {

	    public void paintIcon(Component c, Graphics g, int x, int y) {
	      g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
	    }

		@Override
		public int getIconHeight() {
			// TODO Auto-generated method stub
			return 20;
		}

		@Override
		public int getIconWidth() {
			// TODO Auto-generated method stub
			return 20;
		}
	  }
}
