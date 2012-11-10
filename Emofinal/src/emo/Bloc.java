package emo;
import java.util.ArrayList;


public class Bloc {

	private String[][] words;//[mot, type, lemme]
	private String type = "";
	
	public Bloc(String type, ArrayList<String[]> words_l){
		this.type = type;
		this.words = new String[words_l.size()][3];
		for(int i=0; i<words.length; i++){
			String lemme = words_l.get(i)[2];
			if(lemme.equals("<unknown>")){
				lemme = words_l.get(i)[0];
			}
			words[i][0] = words_l.get(i)[0];
			words[i][1] = words_l.get(i)[1];
			words[i][2] = lemme;
		}
	}
	
	public String toString(){
		String cont = "";
		//System.out.println("taille du bloc = "+words.length);
		for(int i=0; i<words.length; i++){
			float val = Data.getVal(words[i][0]);
			cont+= words[i][0]+"("+words[i][1]+"|"+words[i][2]+")";
			if(val!=-1){
				cont+="("+val+")";
			}
			cont+=" ";
		}
		return type+"\t"+cont;
	}

	public String[][] getWords() {
		return words;
	}

	public void setWords(String[][] words) {
		this.words = words;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
