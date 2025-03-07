import java.util.LinkedList;

public class Bucket {
	private LinkedList<Integer> numeroPagina;
	
	public Bucket() {
		this.numeroPagina = new LinkedList<Integer>();
	}
	public LinkedList<Integer> getNumeroPagina() {
		return numeroPagina;
	}
	public void setNumeroPagina(LinkedList<Integer> numeroPagina) {
		this.numeroPagina = numeroPagina;
	}
}	
