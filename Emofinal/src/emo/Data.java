package emo;

import java.util.ArrayList;

import loader.DataLoader;
import vue.Fenetre;


public abstract class Data {

	private static String[] words;
	private static float[] valence;
	
	public static void load(String database_path, Fenetre fenetre){
		DataLoader.createAndShowGUI(database_path, fenetre);
		/*
		ArrayList<String> l_word = new ArrayList<String>();
		ArrayList<Float> l_val = new ArrayList<Float>();
		
		File f = new File(database_path);
		FileReader fr;
		try {
			fr = new FileReader(f);
			
			int b;
			String str = "";
			while((b = fr.read())!=-1){
				char ch = (char) b;
				if(ch =='\n' && str != "" && (str.startsWith("﻿#")==false && str.startsWith("#")==false)){
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
		
		words = new String[l_word.size()];
		valence = new float[l_val.size()];
		for(int i=0; i<words.length; i++){
			words[i] = l_word.get(i);
			valence[i] = (l_val.get(i)*5)+5;
		}
		
		l_word.clear();
		l_val.clear();*/
	}
	
	/**
	 * 
	 * @param word
	 * @return valence
	 */
	public static float getVal(String word){
		
		//on parcour le tableau
		for(int i=0; i<words.length; i++){
			if(word.equals(words[i])){
				return valence[i];
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * @param word
	 * @param aff
	 * @return valence
	 */
	public static float getVal(String word, Affinity aff){
		ArrayList<Tuple> list = aff.getList();
		for(int i=0; i<list.size(); i++){
			Tuple tup = list.get(i);
			if(tup.getSubject().equals("this")&&tup.getObject().equals(word)){
				return tup.getValence();
			}
		}
		return getVal(word);
	}
	
	public static void affiche(){
		for(int i=0; i<words.length; i++)
			System.out.println(words[i]+"\t"+valence[i]);
	}

	public static String[] getWords() {
		return words;
	}

	public static void setWords(String[] words) {
		Data.words = words;
	}

	public static float[] getValence() {
		return valence;
	}

	public static void setValence(float[] valence) {
		Data.valence = valence;
	}
	
	
}
