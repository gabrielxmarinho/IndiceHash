import java.util.LinkedList;

public class Bucket {
	private LinkedList<Integer> numerosPaginas;
	
	public Bucket() {
		this.numerosPaginas = new LinkedList<Integer>();
	}
	public LinkedList<Integer> getNumerosPaginas() {
		return numerosPaginas;
	}
	public void setNumerosPaginas(LinkedList<Integer> paginas) {
		this.numerosPaginas = paginas;
	}
}	
