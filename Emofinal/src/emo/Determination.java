package emo;
import java.util.ArrayList;


public abstract class Determination {
	
	/**
	 * Method that returns the subject, the object,
	 * and the action of the subject on the object.
	 * @param String[][] tabify sentence from TreeTagger
	 * @return String[] ['subject', 'action', 'object']
	 */
	public static String[] identifierSOA(String[][] tab){
		
		String p1 = "";
		String verb = "";
		String p2 = "";
		
		boolean startVerb = false;
		boolean verbFound = false;
		for(int i = 0; i<tab.length; i++){
			if(!tab[i][1].startsWith("VER") && !startVerb){
				p1 += " "+tab[i][0];
			}
			else if(tab[i][1].startsWith("VER") && !verbFound){
				verb += tab[i][0]+" ";
				startVerb = true;
				//gérer les PP avec la fonction getVerb()
			}
			else{
				verbFound = true;
				p2 += tab[i][0]+" ";
			}
		}
		
		String[] T = new String[3];
		T[0] = ""+p1;
		T[1] = verb;
		T[2] = p2;
		return T;
	}
	
	/**
	 * Select the list of the verbes in the sentence.
	 * @param String[][] tabify sentence from TreeTagger
	 * @return Arraylist<String[]> <-- ['index', 'time', 'lemme'] ex:['2', 'pres', 'être'] if the 3rd word is a present verb
	 */
	public static ArrayList<String[]> getVerbs(String[][] tab){
		ArrayList<String[]> liste = new ArrayList<String[]>();
		
		for(int i=0; i<tab.length; i++){
			String form = "";
			if((form = tab[i][1]).startsWith("VB")){
				String lemme = tab[i][2];
				String[] T = {""+i, form, lemme};
				System.out.println(i+":"+form+"<--"+lemme);
				liste.add(T);
			}
		}
		return liste; //['index', 'time', 'lemme']
	}
	
	/**
	 * détecte les verbes composés, et retourne l'indice de la particule
	 * et celui du participe passé
	 * @param ArrayList<String[]> liste des verbes de la phrase
	 * @return int[][] les indices [VER,VER:pper]
	 */
	public static ArrayList<int[]> getIntervalsVerb(ArrayList<String[]> list){
		ArrayList<int[]> list_index = new ArrayList<int[]>();
		
		for(int i=1; i<list.size(); i++){
			if(list.get(i)[1].equals("VBN")){
				for(int j=i; j>=0; j--){
					if(!list.get(j)[1].equals("VBN") && (list.get(j)[2].equals("be") || list.get(j)[2].equals("have"))){
						int a = Integer.parseInt(list.get(j)[0]);
						int b = Integer.parseInt(list.get(i)[0]);
						int[] interval = {a, b};
						list_index.add(interval);
					}
				}
				
				//on vérifie que le verbe en b se rapporte bien à celui en a
				//
			}
		}
		return list_index;
	}
}
