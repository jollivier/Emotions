package emo;

public class Tuple{
		
		private String type;
		private String subject;
		private String object;
		private float valence;
		private boolean modifiable;
		
		public Tuple(String type, String subject, String object, float valence){
			this.type = type;
			this.subject = subject;
			this.object = object;
			this.valence = valence;
			this.modifiable = true;
		}
		
		public String toString(){
			return type+"  |  "+subject+"  |  "+object+"  |  "+valence+"  |  "+modifiable;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			if(modifiable)
				this.type = type;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			if(modifiable)
				this.subject = subject;
		}

		public String getObject() {
			return object;
		}

		public void setObject(String object) {
			if(modifiable)
				this.object = object;
		}

		public float getValence() {
			return valence;
		}

		public void setValence(float valence) {
			if(modifiable)
				this.valence = valence;
		}

		public boolean isModifiable() {
			return modifiable;
		}

		public void setModifiable(boolean modifiable) {
			this.modifiable = modifiable;
		}
		
		
	}
