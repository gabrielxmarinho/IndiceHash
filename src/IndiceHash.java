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
		return chaveBusca.length()%((int) numRegistros);
	}
	private long numRegistros;
	
	private Tabela tabela;
	//Array de LinkedList contendo buckets
	private LinkedList<Bucket> buckets[];
	
	private int tamanhoBucket = 10;
	
	private int tamanhoPagina = 10;
	
	private int nColisoes;
	
	private int nBucketOverflow;
	public IndiceHash(String nomeArquivo) {
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
				// Adicionando a tupla na tabela
				if(tabela.getPaginas()[hash] == null) {
					//Criando uma lista de páginas dentro do array da tabela, adicionando a página e dentro da ultima página adiciona-se a tupla
					tabela.getPaginas()[hash] = new LinkedList<Pagina>();
					tabela.getPaginas()[hash].add(new Pagina());
					tabela.getPaginas()[hash].get(tabela.getPaginas()[hash].size()-1).getTuplas().add(new Tupla(linha));
				}else if(tabela.getPaginas()[hash].get(tabela.getPaginas()[hash].size()-1).getTuplas().size() == this.tamanhoPagina){
					//A página lotou,cria-se uma nova e adiciona-se nela a tupla
					tabela.getPaginas()[hash].add(new Pagina());
					tabela.getPaginas()[hash].get(tabela.getPaginas()[hash].size()-1).getTuplas().add(new Tupla(linha));
				}else{
					//Somente Adiciona-se na ultima página a tupla
					tabela.getPaginas()[hash].get(tabela.getPaginas()[hash].size()-1).getTuplas().add(new Tupla(linha));
				}
			}
			System.out.printf("Taxa de Colisões = %f%%\n",100 * (float) this.nColisoes/this.numRegistros);
			System.out.printf("Taxa de Buckets Overflow = %f%%\n",100 * (float) this.nBucketOverflow/this.numRegistros);
			//Inicializando parâmetros da Busca
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Busca pelo Índice
	public int chaveBusca(String chave) throws Exception {
		int qtd = 0;
		int hash = this.funcaoHash(chave);
		for(int i=0;i<this.buckets[hash].size();i++) {
			for(int j=0;j<this.buckets[hash].get(i).getNumeroPagina().size();j++) {
				for (int k=0;k<this.tabela.getPaginas()[this.buckets[hash].get(i).getNumeroPagina().get(j)].size();k++) {
					for(int w=0;w<this.tabela.getPaginas()[this.buckets[hash].get(i).getNumeroPagina().get(j)].get(k).getTuplas().size();w++) {
						qtd++;
						if(this.tabela.getPaginas()[this.buckets[hash].get(i).getNumeroPagina().get(j)].get(k).getTuplas().get(w).getChave().equals(chave)) {
							return qtd;
						}
					}
				}
			}
		}
		throw new Exception("Registro não encontrado");
	}
	
	//Full Table Scan
	public int tableScan(String chave) throws Exception {
		int qtd = 0;
		for(int i=0;i<this.tabela.getPaginas().length;i++) {
			//Verificando se a página foi preenchida
			if(this.tabela.getPaginas()[i]!=null) {
				for(int j=0;j<this.tabela.getPaginas()[i].size();j++) {
					for(int k=0;k<this.tabela.getPaginas()[i].get(j).getTuplas().size();k++) {
						qtd++;
						if(this.tabela.getPaginas()[i].get(j).getTuplas().get(k).getChave().equals(chave)) {
							return qtd;
						}
					}
				}
			}
		}
		throw new Exception("Registro não encontrado");
	}
	
}
