package emo;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import com.javanetworkframework.rb.com.freetranslation.FreeTranslationTranslatorRB;


public class Texte_j {
	
	private static String contenu;
	
	public Texte_j(String nom,String content){
		contenu=content;

	}
	
		public void lexical(){
		/*
		Parce que bon, il faut bien commencer par là
		*/
		String[] texte=contenu.split(" ");
		String test="";
		int compteurmot=0;
		float resultat=0;
		String[] mot;
		for (int i=0;i<texte.length;i++){
	
		try{
		FileInputStream fstream = new FileInputStream("SentiWordNet.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		
		mot = strLine.split("	");
		if( mot[0].startsWith("#")==false){						//extraction des coeff à partir de la base.
			String[] base=mot[4].split(" ");
		
			for(int k=0;k<base.length;k++){
		
			
		if((base[k].substring(base[k].length()-2,base[k].length()-1)).equals("#")==true){
		test = base[k].substring(0,base[k].length()-2);
		}
		else{																				//on enleve le n° à la fin du mot étudié
			test=base[k].substring(0,base[k].length()-3);
		}
		
		if (texte[i].equals(test)){
			compteurmot=compteurmot+1;
			resultat=resultat+Float.valueOf(mot[2].trim()).floatValue()-Float.valueOf(mot[3].trim()).floatValue();}
		}}}																					//on ajoute le résultat pour chaque mot porteur de sens emotionnel
		in.close();
		
		}
	catch (Exception e){//Catch exception if any
		System.err.println("Error: " + e.getMessage());

		}
		
		}
		if (compteurmot!=0){									//on moyenne et on affiche le résultat
			if (resultat/compteurmot>0){
				System.out.println("positif");
			}
			else if(resultat/compteurmot<0){
				System.out.println("negatif");
			}
			else{
				System.out.println("neutre");
			}
		}
		else{
			System.out.println("neutre");
		}
		System.out.println(resultat/compteurmot);
	}

	
	public static float get_coef_intensifier(String mot){
		/*
		* Fonction qui retourne le coefficient trouvé dans la base des adverbes "ad.txt" présente dans le dossier courant
		*@param : String mot à analyser
		*@return : float valeur émotive de l'intenssifieur (1 valeur par défaut)
		*/
		float result=1;
		try{
				
			FileInputStream f2stream = new FileInputStream("ad.txt");
			DataInputStream in2 = new DataInputStream(f2stream);
			BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
			String strLine2;
			while ((strLine2 = br2.readLine()) != null){
				//split des adv et des coeff
				String[] adv = strLine2.split(" ");
				
				if(mot.equals(adv[0])){
					result=(Float.valueOf(adv[1].trim()).floatValue()+1);
				}
				
			}
		}		
		catch (Exception e){//Catch exception if any
		System.err.println("Error: " + e.getMessage());		
		
		}
	return result;
	}
	
	public static boolean exist_intensifier(String mot){
		/*
		* Fonction qui cherche seulement si le mot donné est un adverbe existant dans la base ou non
		*@ param : String mot à analyser
		*@ return : boolean présence ou non dans la base
		*/
		boolean result=false;
		try{
				
			FileInputStream f2stream = new FileInputStream("ad.txt");
			DataInputStream in2 = new DataInputStream(f2stream);
			BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
			String strLine2;

			while ((strLine2 = br2.readLine()) != null){
				//split des adv et des coeff
				String[] adv = strLine2.split(" ");
				if(mot.equals(adv[0])){ //recherche de l'intessifieur dans la base
					result=true;
				}
				
				}
			}		
		catch (Exception e){//Catch exception if any
		System.err.println("Error: " + e.getMessage());		
		
		}
	return result;
	}
	
	public static float syntaxique(ArrayList <String>mots, ArrayList <Float>valeurs){
		/*
		* Fonction qui traite une partie de phrase pour en faire l'analyse syntaxique à partir d'une base d'intessifieurs et des valeurs émotionnelles des mots
		* @param : liste de mots à traiter & valeurs émotionnelle de chacun
		* @return : valeur émotionnelle globale de la suite de mot
		*/

		float resultat=0;
		float coef=1;
		
		for (int i=0;i<mots.size();i++){ //lecture mot à mot de la phrase d'entrée
			
			if(mots.get(i).equals("not")){ //si c'est un "not" voir ce qu'est le prochain mot
				if(exist_intensifier(mots.get(i+1)) == true){//si un intensifieur -> abaisser celui-ci
					coef = (float) (coef * 0.5);
				}
				else{//-> sinon not = négation (* -0.5)
					coef = (float) (coef * -0.5);
				}
			}
			else{ //sinon récupérer le coefficient et les accumuler jusqu'au prochain mot à valeur émotionnelle
				coef = coef * get_coef_intensifier(mots.get(i));
			}

			if( exist_intensifier(mots.get(i)) == false		//ne modifier le résultat que si le mot courant n'est pas un intensifieur
								&& valeurs.get(i)!=-1){		//ne le modifier que si le mot courant n'a pas une valeur = -1 (indéterminée)
				resultat=resultat+coef*(valeurs.get(i)-5);	//ajouter le coef précédent
				coef=1;										//réinitialisation du coefficient d'intenssifieur
			}
		}
		if(resultat == 0){ 
			return -21;
		}
		else
			return (resultat);
	}
	
	
	
	
	
	/*public void translate(String lang1,String lang2){
		FreeTranslationTranslatorRB fr = new FreeTranslationTranslatorRB();
	    contenu=fr.webTranslate(Locale.forLanguageTag(lang1),Locale.forLanguageTag(lang2),contenu);
	    
		// TODO Auto-generated method stub
	}
	*/
	public static String traduct(String txt, String lang){
		FreeTranslationTranslatorRB fr = new FreeTranslationTranslatorRB();
	    return fr.webTranslate(Locale.forLanguageTag(lang),Locale.forLanguageTag("en"),txt);
	    
		// TODO Auto-generated method stub
	}

	@Override
	public String toString() {
		return "Text [contenu=" + contenu + "]";
	}	
}
