import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class IndiceHash {
	
	public int funcaoHash(String chaveBusca) {
		//Pendente a definição da função 
		return chaveBusca.length()/((int) numRegistros);
	}
	private long numRegistros;
	
	private Tabela tabela;
	//Array de LinkedList contendo buckets
	private LinkedList<Bucket> buckets[];
	
	private int tamanhoBucket = 5;
	
	private int tamanhoPagina = 10;
	
	private int nColisoes;
	
	private int nBucketOverflow;
	public IndiceHash(String nomeArquivo,String chaveBusca) {
		this.nColisoes = 0;
		nBucketOverflow = 0;
		try {
			this.numRegistros = Files.lines(Paths.get(nomeArquivo)).count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tabela = new Tabela((int) Math.ceil((double) this.numRegistros/this.tamanhoPagina));
		buckets = (LinkedList<Bucket>[]) new LinkedList[(int) Math.ceil((double) this.numRegistros / this.tamanhoBucket)];
		try {
			File arq = new File(nomeArquivo);
			Scanner scanner= new Scanner(arq);
			//Criando as Páginas
			while(scanner.hasNextLine()) {
				//Atingiu o número máximo de tuplas por página
				String linha = scanner.nextLine();
				int hash = this.funcaoHash(linha);
				//Se o Bucket não foi criado ainda 
				if(buckets[hash] == null) {
					buckets[hash] = new LinkedList<Bucket>();
					buckets[hash].add(new Bucket());
					buckets[hash].get(buckets[hash].size()-1).getNumeroPagina().add(hash);
				}else if(buckets[hash].get(buckets[hash].size()-1).getNumeroPagina().size()==this.tamanhoBucket) {
					//Bucket Overflow
					buckets[hash].add(new Bucket());
					buckets[hash].get(buckets[hash].size()-1).getNumeroPagina().add(hash);
					nBucketOverflow++;
				}
				else {
					//Colisão
					buckets[hash].get(buckets[hash].size()-1).getNumeroPagina().add(hash);
					this.nColisoes++;
				}
			}
			System.out.printf("Nº de Colisoes = %d\n",this.nColisoes);
			System.out.printf("Nº de Buckets Overflow = %d\n",this.nBucketOverflow);
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}	
}
