package emo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import vue.Fenetre;

import com.google.gson.Gson;


public class Verb {

	private static String file_path = Fenetre.path + "verb_db.txt";
	
	public static ArrayList<Verb.Tuple> list = new ArrayList<Verb.Tuple>();
	
	
	public Verb(){
		
		Gson gson = new Gson();
		File f = new File(file_path);
		FileReader fr;
		try {
			fr = new FileReader(f);
			
			int b;
			String str = "";
			while((b = fr.read())!=-1){
				char ch = (char) b;
				if(ch == '\n'&& str != ""){
					list.add(gson.fromJson(str, Verb.Tuple.class));
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
	
	public static Verb.Tuple find(String verb){
		
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getVerb().equals(verb)){
				return list.get(i);
			}
		}
		return null;
	}
	
	public class Tuple {
		private String verb = "";
		private float subject;
		private float object;
		private int operator = -1;
		
		public Tuple(String verb, float subject, float object, int operator){
			this.verb = verb; this.subject = subject; this.object = object; this.operator = operator;
		}

		public String getVerb() {
			return verb;
		}

		public void setVerb(String verb) {
			this.verb = verb;
		}

		public float getSubject() {
			return subject;
		}

		public void setSubject(float subject) {
			this.subject = subject;
		}

		public float getObject() {
			return object;
		}

		public void setObject(float object) {
			this.object = object;
		}

		public int getOperator() {
			return operator;
		}

		public void setOperator(int operator) {
			this.operator = operator;
		}
		
	}
}
