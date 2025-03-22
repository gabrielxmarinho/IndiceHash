import java.util.LinkedList;

public class Tabela {
	private LinkedList<Pagina> paginas;
	
	public Tabela() {
		paginas = new LinkedList<Pagina>();
	}
	public LinkedList<Pagina> getPaginas() {
		return paginas;
	}
    public void setPaginas(LinkedList<Pagina> paginas) {
		this.paginas = paginas;
	}
}
