import java.util.LinkedList;

public class Pagina {
	private Tupla tuplas[];
	
	public Pagina(int qtd) {
		this.tuplas = new Tupla[qtd];
	}
	
	public Tupla[] getTuplas() {
		return tuplas;
	}
	public void setTuplas(Tupla[] tuplas) {
		this.tuplas = tuplas;
	}
}
