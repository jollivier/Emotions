package emo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import vue.Fenetre;

import com.google.gson.Gson;


public class Affinity {

	private String filePath = "";
	private ArrayList<Tuple> list = new ArrayList<Tuple>();
	
	/**
	 * load a file of affinities, and set the liste of Tuples and the dico.
	 * @param path to the file of affinity
	 */
	public Affinity(String file){
		this.filePath = file;
		
		Gson gson = new Gson();
		File f = new File(file);
		FileReader fr;
		try {
			fr = new FileReader(f);
			
			int b;
			String str = "";
			while((b = fr.read())!=-1){
				char ch = (char) b;
				if(ch == '\n'&& str != ""){
					list.add(gson.fromJson(str, Tuple.class));
					str = "";
				}
				else{
					str += ch;
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * add or update a relation in the list of tuples
	 * @param type (like or dislike)
	 * @param subject
	 * @param object
	 * @param valence (float between 0.0 and 10.0)
	 */
	public void addRelation(String sub, String obj, float val){
		boolean done = false;
		String newtype = "like";
		if(val<5){
			newtype = "dislike";
		}
		for(int i=0; i<list.size(); i++){
			Tuple tup = list.get(i);
			if(tup.getSubject().equals(sub) && tup.getObject().equals(obj)){
				
				float meanval = (tup.getValence()+val)/2;
				if(meanval<5){
					newtype = "dislike";
				}
				if((tup.getType().equals("like")&&meanval<5)||(tup.getType().equals("dislike")&&meanval>5)){
					if(tup.isModifiable()){
						tup.setType(newtype);
						tup.setValence(val);
						tup.setModifiable(true);
					}
					
				}
				else{
					if(tup.isModifiable()){
						tup.setValence(meanval);
					}
				}
				done = true;
				break;
			}
		}
		if(!done){
			list.add(new Tuple(newtype, sub, obj, val));
		}
		//writeFile();
	}
	
	public void addRelation(String type, String sub, String obj, float val){
		boolean done = false;
		
		for(int i=0; i<list.size(); i++){
			Tuple tup = list.get(i);
			if(tup.getSubject().equals(sub) && tup.getObject().equals(obj) && tup.isModifiable()){
				tup.setType(type);
				tup.setValence(val);
				
				done = true;
				break;
			}
		}
		if(!done){
			list.add(new Tuple(type, sub, obj, val));
		}
		//writeFile();
	}
	
	public void writeFile(){
		Gson gson = new Gson();
		File f = new File(this.filePath);
		FileWriter fw;
		try {
			fw = new FileWriter(f);
			fw.write("");
			for (Tuple elem : this.list){
				String str = gson.toJson(elem)+"\n";
				fw.append(str);
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void export_txt(){
		Gson gson = new Gson();
		File f = new File(Fenetre.path + "affinity_export.txt");
		FileWriter fw;
		try {
			fw = new FileWriter(f);
			fw.write("");
			for (Tuple elem : this.list){
				String str = gson.toJson(elem)+"\n";
				fw.append(str);
			}
			fw.close();
			
			JOptionPane.showMessageDialog(null, "saved at : " + Fenetre.path + "affinity_export.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void affiche(){
		for(int i=0; i<list.size(); i++)
			System.out.println(list.get(i));
	}
	//getters and setters:
	public ArrayList<Tuple> getList() {
		return list;
	}

	public void setList(ArrayList<Tuple> list) {
		this.list = list;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
