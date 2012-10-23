package emo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class BlocParser {

	private ArrayList<Bloc> blocs = new ArrayList<Bloc>();
	/**
	 * 
	 * @param chunk - the resulte of the chunker from TreeTagger.
	 */
	public BlocParser(String[][] chunk){
		String input = "";
		for(int i=0; i<chunk.length; i++){
			input += chunk[i][0] + "\n";
		}
		setInput(input);
		
		this.parseChunk(chunk);
	}
	
	/**
	 * met à jour le fichier d'input pour TreeTagger
	 * @return void
	 */
	public static void setInput(String input){
		try {
			FileWriter fstream = new FileWriter("chunk.xml");
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
	 * converti un output xml en une liste de block, qui sont des objets
	 * contenant une portion de phrase et son type (groupe nominale, verbale etc...)
	 * @param input
	 * @return la liste des blocs créés après le parsing
	 */
	public ArrayList<Bloc> parseChunk(String[][] chunk){
		
		String[] input = new String[chunk.length];
		for(int i=0; i<input.length; i++){
			input[i] = chunk[i][0];
		}
		ArrayList<Bloc> sortie = new ArrayList<Bloc>();
		
		String start = "";
		String end = "";
		
		ArrayList<String[]> content = new ArrayList<String[]>();
		
		for(int i=0; i<input.length; i++){
			if(start.equals("") && input[i].startsWith("<")){
				if(input[i].equals("<PC>")){
					//on avance jusqu'à la prochaine balise
					i++;
					while(!input[i].startsWith("<")){
						i++;
					}
				}
				else if(input[i].equals("</PC>")){
					i++;
				}
				
				start = input[i];
				end = start.replace("<", "</");
			}
			
			else if(start.equals("") && !input[i].startsWith("<")){
				content.add(chunk[i]);
				Bloc bl = new Bloc("<none>", content);
				this.blocs.add(bl);
				sortie.add(bl);
				content = new ArrayList<String[]>();
			}
			else if(input[i].equals(end)&&!input[i].equals("")){
				Bloc bl = new Bloc(start, content);
				this.blocs.add(bl);
				sortie.add(bl);
				start = "";
				end = "";
				content = new ArrayList<String[]>();
			}
			else if(!input[i].equals("<PC>")){
				if(chunk[i].length == 1){
					String[] altchunk = {chunk[i][0], "", ""};
					content.add(altchunk);
				}
				else{
					content.add(chunk[i]);
				}
			}
			
			if(input[i].equals("<PC>")){
				i--;
				start = ""; end = "";
			}
		}
		return sortie;
	}
	
	public void affiche(){
		for(int i=0; i<blocs.size(); i++){
			System.out.println(blocs.get(i));
		}
	}
	
	/**
	 * réduit la phrase en une suite de type|mot|valeure
	 * @param list
	 * @param affinity
	 * @param affiche
	 * @return ArrayList<String> 
	 */
	public float process_study_syntax(ArrayList<Bloc> list, Affinity affinity, boolean affiche){
		ArrayList<String> action = new ArrayList<String>();
		if(affiche){
			System.out.println("\n\nliste des blocs:\n");
			for(int l=0; l<list.size(); l++){
				System.out.println(list.get(l));
			}
			System.out.println();
		}
		boolean neg = false;//pour savoir si le groupe verbale contient une négation.
		for(int i=0; i<list.size(); i++){
			Bloc bloc = list.get(i);
			
			//si on traite un bloc nominal : "<NC>"
			if(bloc.getType().equals("<NC>")){
				ArrayList<Float> mod = new ArrayList<Float>();
				String subject = "";
				for(int k=0; k<bloc.getWords().length; k++){
					
					
					if(bloc.getWords()[k][2].equals("<unknown>"))
						subject = bloc.getWords()[k][0];
					else
						subject = bloc.getWords()[k][2];
					
					if(bloc.getWords()[k][1].equals("JJ")){
						if(Data.getVal(bloc.getWords()[k][2])!=-1){
							mod.add(Data.getVal(bloc.getWords()[k][2]));
						}
					}
					else if(bloc.getWords()[k][1].startsWith("NN")){
						for(int p=0; p<mod.size(); p++){
							affinity.addRelation("this", subject, mod.get(p));
						}
						mod = new ArrayList<Float>();
					}
					else if(bloc.getWords()[k][1].startsWith("NP")){
						for(int p=0; p<mod.size(); p++){
							affinity.addRelation("this", subject, mod.get(p));
						}
						mod = new ArrayList<Float>();
					}
					else if(bloc.getWords()[k][1].equals("PP")){
						for(int p=0; p<mod.size(); p++){
							affinity.addRelation("this", subject, mod.get(p));
						}
						mod = new ArrayList<Float>();
					}
					if(!bloc.getWords()[k][1].equals("DT"))
						action.add(bloc.getWords()[k][1]+"/"+bloc.getWords()[k][2]+"/"+Data.getVal(bloc.getWords()[k][2], affinity));
				}
				
			}
			
			
			//si on traite un groupe verbal
			else if(bloc.getType().equals("<VC>")){
				
				String tree = "";
				String current_VB = "";
				for(int l=0; l<bloc.getWords().length; l++){
					
					//on check la présence de négation
					if(bloc.getWords()[l][2].equals("not")||bloc.getWords()[l][2].equals("n't")){
						neg = true;
					}
					
					if(bloc.getWords()[l][1].startsWith("V")){
						Verb.Tuple vb;
						if((vb = Verb.find(bloc.getWords()[l][2])) != null){
							float s = vb.getSubject();
							float o = vb.getObject();
							int op = vb.getOperator();
							current_VB = "VB/"+vb.getVerb()+"/"+s+"/"+o+"/"+op;
							if(l==0)
								tree = bloc.getWords()[l][2]+":"+bloc.getWords()[l][1];
							else
								tree += "/"+bloc.getWords()[l][2]+":"+bloc.getWords()[l][1];
						}
						else{
							if(l==0)
								tree = bloc.getWords()[l][2]+":"+bloc.getWords()[l][1];
							else
								tree += "/"+bloc.getWords()[l][2]+":"+bloc.getWords()[l][1];
							current_VB = "VB/"+bloc.getWords()[l][2]+"/5.0/5.0/-1";
						}
					}
				}
				if(affiche){
					System.out.println("BlocParser l.218 -> "+tree);
					System.out.println("neg -> "+neg);
					System.out.println("PP -> "+this.verb_tree_process(tree));
					System.out.println(current_VB);
				}
				
				//on ajoute ce qu'il faut dans action...
				action.add(current_VB+"/"+neg+"/"+verb_tree_process(tree));
			}
			
			else{
				for(int k=0; k<bloc.getWords().length; k++){
					String type = bloc.getWords()[k][1];
					if(!type.equals("DT")){
						String word = bloc.getWords()[k][2];
						if(word.equals("<unknown>"))
							word = bloc.getWords()[k][0];
						action.add(type+"/"+word+"/"+Data.getVal(word, affinity));
					}
				}
			}
			
		}
		if(affiche){
			System.out.println("\n\nliste des actions (des mots):\n");
			for(int l=0; l<list.size(); l++){
				System.out.println(action.get(l));
			}
			System.out.println();
		}
		
		if(affiche)
			System.out.println("\naffinités après:");affinity.affiche();System.out.println();
		
		float eval = eval(action, affinity, affiche);
		return eval;
	}
	
	/**
	 * uses the following verbs to determine if the form is passive
	 * @param tree
	 * @return the relevant verb
	 */
	private boolean verb_tree_process(String tree){
		boolean pp = false;
		String[] list = tree.split("/");
		
		if(list.length == 2){
			if(list[0].split(":")[0].equals("be") && list[1].split(":")[1].equals("VBN")){
				pp = true;
			}
		}
		else if(list.length >= 2){
			if(list[0].split(":")[0].equals("have") && list[1].split(":")[0].equals("be")){
				pp = true;
			}
		}
		
		return pp;
	}
	
	/**
	 * 
	 * @param actions
	 * @return valence de la phrase
	 */
	public static float eval(ArrayList<String> actions, Affinity aff, boolean affiche){
		int nb_VB = 0;
		int index_VB = 0;
		
		boolean neg = false;//pour savoir si le groupe verbale contient une négation.
		boolean PP = false;//pour savoir si le groupe verbale est au passif.
		
		float val_sub = -1;
		float val_obj = -1;
		
		//les couples mot/valeur à transmettre pour l'annalyse syntaxique.
		//on a un couple pour le sujet et un autre pour l'objet
		ArrayList<String> subjMot_list = new ArrayList<String>();
		ArrayList<Float> subjVal_list = new ArrayList<Float>();
		
		ArrayList<String> objMot_list = new ArrayList<String>();
		ArrayList<Float> objVal_list = new ArrayList<Float>();
		//***************************************************************
		
		for(int i=0; i<actions.size(); i++){
			String type = actions.get(i).split("/")[0];
			if(type.equals("VB")){
				nb_VB+=1;
				if(index_VB==0)
					index_VB = i;
			}
		}
		
		int op = 0;
		if(nb_VB>=1){
			PP = Boolean.parseBoolean(actions.get(index_VB).split("/")[6]);
			neg = Boolean.parseBoolean(actions.get(index_VB).split("/")[5]);
			op = Integer.parseInt(actions.get(index_VB).split("/")[4]);
		
		
		if(op==1){
			String ty = "like";
			float val = 6;
			
			if(Float.parseFloat(actions.get(index_VB).split("/")[2])<5){
				ty = "dislike";
				val = 4;
			}
			if(neg){
				if(val == 6){
					ty = "dislike";
					val = 4;
				}
				else{
					ty = "like";
					val = 6;
				}
			}
			
			for(int a=0; a<index_VB; a++){
				String mot_class = actions.get(a).split("/")[0];
				if(mot_class.startsWith("NN")||mot_class.startsWith("NP")||mot_class.startsWith("PP")){
					String sub = actions.get(a).split("/")[1];
					for(int b=index_VB+1; b<actions.size(); b++){
						String mot_class2 = actions.get(b).split("/")[0];
						if(mot_class2.startsWith("NN")||mot_class2.startsWith("NP")||mot_class2.startsWith("PP")){
							String obj = actions.get(b).split("/")[1];
							aff.addRelation(ty, sub, obj, val);
						}
					}
				}
			}
			
			for(int a=0; a<index_VB; a++){
				subjMot_list.add(actions.get(a).split("/")[1]);
				subjVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
			for(int a=index_VB+1; a<actions.size(); a++){
				objMot_list.add(actions.get(a).split("/")[1]);
				objVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
		}
		
		else if(op==2){
			for(int a=0; a<index_VB; a++){
				//étude syntaxique:
				subjMot_list.add(actions.get(a).split("/")[1]);
				subjVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
			for(int a=index_VB+1; a<actions.size(); a++){
				objMot_list.add(actions.get(a).split("/")[1]);
				objVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
			
			//mise à jour des affinités
			for(int a=0; a<index_VB; a++){
				String mot_class = actions.get(a).split("/")[0];
				String mot = actions.get(a).split("/")[1];
				if(mot_class.startsWith("NN")||mot_class.startsWith("NP")||mot_class.startsWith("PP")){
					
					for(int b=index_VB+1; b<actions.size(); b++){
						String mot_class2 = actions.get(b).split("/")[0];
						if(mot_class2.startsWith("JJ")){
							float valJJ = Float.parseFloat(actions.get(b).split("/")[2]);
							aff.addRelation("this", mot, valJJ);
						}
					}
				}
			}
			
		}
		
		else if(op==-1){
			for(int a=0; a<index_VB; a++){
				subjMot_list.add(actions.get(a).split("/")[1]);
				subjVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
			for(int a=index_VB+1; a<actions.size(); a++){
				objMot_list.add(actions.get(a).split("/")[1]);
				objVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
		}
		
		else{
			for(int a=0; a<index_VB; a++){
				subjMot_list.add(actions.get(a).split("/")[1]);
				subjVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
			for(int a=index_VB+1; a<actions.size(); a++){
				objMot_list.add(actions.get(a).split("/")[1]);
				objVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
		}
		}
		float val_Tot = -1;
		float vb_s = 0;// = Float.parseFloat(actions.get(index_VB).split("/")[2])-5;
		float vb_o = 0;// = Float.parseFloat(actions.get(index_VB).split("/")[3])-5;
		if(nb_VB>=1){
			vb_s = Float.parseFloat(actions.get(index_VB).split("/")[2])-5;
			vb_o = Float.parseFloat(actions.get(index_VB).split("/")[3])-5;
		}
		
		if(affiche){
			System.out.println("\n\nlistes à transmettre pour analyse syntaxe:");
			System.out.println("***sub***");
			for(int b=0; b<subjMot_list.size(); b++)
				System.out.println(subjMot_list.get(b)+":"+subjVal_list.get(b));
			System.out.println("***obj***");
			for(int a=0; a<objMot_list.size(); a++)
				System.out.println(objMot_list.get(a)+":"+objVal_list.get(a));
		}
		
		
		float q = 0;//où on stock le résultat temporaire
		
		
		if(nb_VB==0){
			for(int a=0; a<actions.size(); a++){
				subjMot_list.add(actions.get(a).split("/")[1]);
				subjVal_list.add(Data.getVal(actions.get(a).split("/")[1], aff));
			}
			
			q = Texte_j.syntaxique(subjMot_list, subjVal_list);
			return q;
		}
		else{
			val_sub = Texte_j.syntaxique(subjMot_list, subjVal_list);
			val_obj = Texte_j.syntaxique(objMot_list, objVal_list);
		}
		
		if(affiche)
			for(int i=0; i<actions.size(); i++)
				System.out.println(actions.get(i));
			System.out.println("\nl.453 : synthax -> sub="+val_sub+" obj="+val_obj);
		
		if(PP){
			float temp = val_sub;
			val_sub = val_obj;
			val_obj = temp;
		}
		
		
		if(val_sub!=-21 && val_obj!=-21){
			if(affiche){
				System.out.println("\nnombre de verbes : "+nb_VB);
				System.out.println("s:"+vb_s+" o:"+vb_o+"\nval_sub="+val_sub+" val_obj="+val_obj+"\n");
			}
			
			q = ((val_sub)*sgn(vb_s)+sgn(vb_o)*(val_obj))/2;
		}
		else if(val_sub!=-21){
			if(affiche){
				System.out.println("\nnombre de verbes : "+nb_VB);
				System.out.println("s:"+vb_s+" o:"+vb_o+"\nval_sub="+(val_sub)+" val_obj="+(val_obj)+"\n");
			}
			q = (val_sub)*sgn(vb_s);
		}
		else if(val_obj!=-21){
			if(affiche){
				System.out.println("\nnombre de verbes : "+nb_VB);
				System.out.println("s:"+vb_s+" o:"+vb_o+"\nval_sub="+(val_sub)+" val_obj="+(val_obj)+"\n");
			}
			q = sgn(vb_o)*(val_obj);
		}
		
		val_Tot = q;//(float) (sgn(q)*Math.sqrt(Math.abs(q))) + 5; pour tenir compte de la valence associée...
		if(neg){
			val_Tot = -val_Tot;
		}
		
		System.out.println("\nvalence [-20; 20] : "+val_Tot+"\n");
		return val_Tot;
	}
	
	private static int sgn(float x){
		if(x<0)
			return -1;
		else
			return 1;
	}
	
	public float syntaxe_result(){
		return 0;
	}
	
	public ArrayList<Bloc> getBlocs() {
		return blocs;
	}

	public void setBlocs(ArrayList<Bloc> blocs) {
		this.blocs = blocs;
	}

}
