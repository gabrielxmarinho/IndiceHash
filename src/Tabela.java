import java.util.ArrayList;
import java.util.LinkedList;

public class Tabela {
	
	private LinkedList<Pagina> paginas[]; 
	
	public Tabela(int qtd) {
		paginas = (LinkedList<Pagina>[]) new LinkedList[qtd];
	}
	
	public LinkedList<Pagina>[] getPaginas() {
		return paginas;
	}
	public void setPaginas(LinkedList<Pagina>[] paginas) {
		this.paginas = paginas;
	}
}
