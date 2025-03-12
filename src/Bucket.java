import java.util.LinkedList;

public class Bucket {
	private LinkedList<Pagina> paginas;
	
	public Bucket() {
		this.paginas = new LinkedList<Pagina>();
	}
	public LinkedList<Pagina> getPaginas() {
		return paginas;
	}
	public void setPaginas(LinkedList<Pagina> paginas) {
		this.paginas = paginas;
	}
}	
