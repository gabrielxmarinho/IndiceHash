
public class Main{

	public static void main(String[] args) {
		//Dados do Usuario
		String chave = "ZZZ";
		IndiceHash indice = new IndiceHash("C:\\Users\\User\\Downloads\\words.txt");
		//Especificar caminho do arquivo na máquina
		try {
			System.out.println(indice.chaveBusca(chave));
			System.out.println(indice.tableScan(chave));
		}catch(Exception e) {
			System.out.println("Registro não encontrado");
		}
	}
}
