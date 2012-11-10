package vue;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import emo.Affinity;
import emo.Tuple;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class AffinityEdit extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String[] val;
	


	/**
	 * Create the frame.
	 */
	public AffinityEdit(final Affinity affinity) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 400);
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		val = new String[affinity.getList().size()];
		for(int i=0; i<affinity.getList().size(); i++){
			val[i] = affinity.getList().get(i).toString();
		}
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JList<String> list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new AbstractListModel<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] values = val;
			
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane.setViewportView(list);
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		menuBar.add(mntmSave);
		
		mntmSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				affinity.export_txt();
			}
			
		});
		
		ArrayList<Tuple> aff_list = affinity.getList();
		for(int i=0; i<aff_list.size(); i++){
			list.add(new JLabel(aff_list.get(i).toString()));
		}
	}

}
