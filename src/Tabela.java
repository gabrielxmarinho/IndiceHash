import java.util.ArrayList;

public class Tabela {
	
	private Pagina paginas[]; 
	
	public Tabela(int qtd) {
		this.paginas = new Pagina[qtd];
	}
	public Pagina[] getPaginas() {
		return paginas;
	}
	public void setPaginas(Pagina[] paginas) {
		this.paginas = paginas;
	}
}
