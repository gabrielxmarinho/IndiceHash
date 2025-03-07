
public class Tupla {
	private String chave;
	
	private int hash;
	
	public Tupla(String chave) {
		this.chave = chave;
	}
	public String getChave() {
		return this.chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public int getHash() {
		return this.hash;
	}
	public void setHash(int hash) {
		this.hash = hash;
	}
}
