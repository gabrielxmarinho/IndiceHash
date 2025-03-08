
public class Main{

	public static void main(String[] args) {
		//Dados do Usuario
		String chave = "2D";
		IndiceHash indice = new IndiceHash("C:\\Users\\User\\Downloads\\words.txt");//Coloque o diretório da sua máquina
		try {
			System.out.println(indice.chaveBusca(chave));
			System.out.println(indice.tableScan(chave));
		}catch(Exception e) {
			System.out.println("Registro não encontrado");
		}
	}
}
