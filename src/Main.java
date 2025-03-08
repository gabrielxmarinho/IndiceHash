
public class Main{

	public static void main(String[] args) {
		//Dados do Usuario
		String chave = "2D";
		IndiceHash indice = new IndiceHash("C:\\Users\\User\\Downloads\\words.txt");
		try {
			System.out.println(indice.chaveBusca(chave));
		}catch(Exception e) {
			System.out.println("Registro não encontrado");
		}
	}
}
