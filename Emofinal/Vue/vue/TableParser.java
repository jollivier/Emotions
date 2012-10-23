package vue;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class TableParser extends JTable{

	private static String[] columnNames_init = {"n¤", "sentence", "valence"};
	private static Object[][] data_init = {{"1", "", ""}};
	
	private Object[][] data = {{"1", "", ""}};
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TableParser(){
		super(data_init, columnNames_init);
		TableColumn column = null;
		column = this.getColumnModel().getColumn(0);
		column.setPreferredWidth(50);
		column = this.getColumnModel().getColumn(2);
		column.setPreferredWidth(50);
	}
	
	public TableParser(Object[][] data){
		super(data, columnNames_init);
		this.data = data;
		TableColumn column = null;
		column = this.getColumnModel().getColumn(0);
		column.setPreferredWidth(50);
		column = this.getColumnModel().getColumn(2);
		column.setPreferredWidth(50);
		
	}
	
	public String toString(){
		String str = "";
		for(int i=0; i<data.length; i++){
			for(int j=0; j<3; j++)
				str += data[i][j]+" ";
			str +="\n";
		}
		str += "\n";
		return str;
	}

	public Object[][] getData() {
		return data;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

}
