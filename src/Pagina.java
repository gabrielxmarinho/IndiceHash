import java.util.LinkedList;

public class Pagina {
	private LinkedList<Tupla> tuplas;

	public Pagina() {
		this.tuplas = new LinkedList<Tupla>();
	}
	
	public LinkedList<Tupla> getTuplas() {
		return tuplas;
	}
	public void setTuplas(LinkedList<Tupla> tuplas) {
		this.tuplas = tuplas;
	}
}
