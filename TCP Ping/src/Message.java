import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	int id, t;
	boolean x;
	
	public Message(int id) {
		this.id = id;
	}
	
	 public int getId() {
	        return id;
	    }
	 
	 public int getTime() {
		 return t;
	 }
	 
	 public void setTime(int time) {
		 this.t = time;
	 }
	 
}
