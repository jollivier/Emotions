package emo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import vue.Fenetre;


public class Main {

	
	public static float run_study(String txt, Affinity aff){
		//***************************************************
		//***********running study***************************
		Main.setInput(txt);
		String[][] chunk = Main.treeTaggerChunker_res();//tableau du chunk de la phrase
		for(int i=0; i<chunk.length; i++){
			for(int j=0; j<chunk[i].length; j++){
				System.out.print(chunk[i][j]+" ");
			}
			System.out.println();
		}
		
		//
		BlocParser blc = new BlocParser(chunk);
		
		//on etudie le texte en apprenant de nouvelles affinités
		return blc.process_study_syntax(blc.getBlocs(), aff, true);
		
	}
	
	/**
	 * lance le programme <a href="http://www.ims.uni-stuttgart.de/projekte/corplex/TreeTagger/">TreeTagger</a> avec input.txt en entrée
	 * @return String réponce du programme TreeTagger
	 */
	public static String treeTagger_res(){

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		
		try {
			String[] arg = { Fenetre.path + "Ttagger/cmd/tree-tagger-english", "<input.txt" };
			process = runtime.exec(arg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStream in = process.getInputStream();
		
		byte b = 0;
		String rep = "";
		try {
			while((b = (byte) in.read()) != -1){
				rep += (char) b;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rep;
	}
	
	/**
	 * lance le programme <a href="http://www.ims.uni-stuttgart.de/projekte/corplex/TreeTagger/">TreeTagger</a> (chunker) avec input.txt en entrée
	 * @return String réponce du programme TreeTagger
	 */
	public static String[][] treeTaggerChunker_res(){

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		
		try {
			String[] arg = { "cmd.exe", "/c","start", "C:/TreeTagger/bin/chunk-english.bat", "input.txt" };
			process = runtime.exec(arg);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileReader output=null;
		try {
			output = new FileReader("output.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader result = new BufferedReader(output);
		
		byte b = 0;
		String rep = "";
		try {
			while((b = (byte) result.read()) != -1){
				rep += (char) b;
			}
			result.close();
			output.close();
			process.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//on forme le tableau avec un split("\n");->lignes  puis un autre split("\t");->colonnes
		String[] lignes = rep.split("\n");
		for(int i=0;i<lignes.length;i++){
			lignes[i]=lignes[i].substring(0,lignes[i].length()-1);
		}
		String[][] tab = new String[lignes.length][3];
		for(int i=0; i<lignes.length; i++){
			tab[i] = lignes[i].split("\t");
		}
		
		return tab;
	}
	
	/**
	 * met à jour le fichier d'input pour TreeTagger
	 * @return void
	 */
	public static void setInput(String input){
		try {
			FileWriter fstream = new FileWriter("input.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(input);
			//Close the output stream
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * converti un string en un tableau mot|type|lemme
	 * @param la phrase à analyser
	 * @return String[][] mot|type|lemme
	 */
	public static String[][] getTable(String str){
		String[][] tab = null;
		
		// Create input file
		setInput(str);
		  
		String rep = treeTagger_res();
		tab = organize_responce(rep);
		
		return tab;
	}
	
	/**
	 * organise le string de réponce de TreeTagger
	 * en un tableau bidimensionnel
	 * @param rep la réponce de TreeTagger
	 * @return String[][] mot|typr|lemme
	 */
	public static String[][] organize_responce(String rep){
		
		String[] l = rep.split("\n");
		String[][] L = new String[l.length][3];
		
		for(int i=0; i<l.length; i++){
			L[i] = l[i].split("\t");
		}
		
		return L;
	}
	
	/**
	 * Affiche le contenu de la reponse du <a href="http://www.ims.uni-stuttgart.de/projekte/corplex/TreeTagger/">TreeTagger</a>
	 * organisée en tableau bidimensionnel.
	 * @param respTab le tableau mot|type|lemme
	 * @return void
	 */
	public static void affiche_resp(String[][] respTab){
		String[] id = {"mot", "type", "lemme"};
		
		for(int i=0; i<respTab.length; i++){
			for(int j=0; j<3; j++){
				
				System.out.print(id[j]+":");
				System.out.print(respTab[i][j]);
				System.out.print("\t");
			}
			System.out.println("\n**********************");
		}
		Determination.getVerbs(respTab);
		String[] det = Determination.identifierSOA(respTab);
		System.out.println("Sujet: '"+det[0]+"'\tAction: '"+det[1]+"'\tObjet: '"+det[2]+"'");
	}
}
