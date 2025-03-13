import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class IndiceHash {
    
    public int funcaoHash(String chaveBusca) {
        return Math.abs(chaveBusca.hashCode())%(buckets.length);
    }
    private Tabela tabela;
    private long numRegistros;
    private LinkedList<Bucket> buckets[];
    private int totalBuckets = 0;
    private int totalPaginas = 0;
    private int tamanhoBucket = 10;
    private int tamanhoPagina = 10;
    private int nColisoes;
    private int nBucketOverflow;
    
    public IndiceHash(String nomeArquivo) {
        this.nColisoes = 0;
        this.nBucketOverflow = 0;
        try {
            this.numRegistros = Files.lines(Paths.get(nomeArquivo)).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buckets = (LinkedList<Bucket>[]) new LinkedList[(int) Math.ceil((double) this.numRegistros / this.tamanhoBucket)];
        tabela = new Tabela();
        try {
            File arq = new File(nomeArquivo);
            Scanner scanner = new Scanner(arq);
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                int hash = this.funcaoHash(linha);
                if (buckets[hash] == null) {
                    buckets[hash] = new LinkedList<Bucket>();
                    buckets[hash].add(new Bucket());
                    buckets[hash].get(buckets[hash].size()-1).getNumerosPaginas().add(tabela.getPaginas().size());
                    //Zero pois a página e o Bucket acabaram de ser criados
                    //Aponta para o primeiro endereço desse Bucket
                    Pagina novaPagina = new Pagina();
                    novaPagina.getTuplas().add(new Tupla(linha));
                    tabela.getPaginas().add(novaPagina);
                } else {
                	int bucketIndex = buckets[hash].size()-1;
                	int paginaIndex = buckets[hash].get(bucketIndex).getNumerosPaginas().get(buckets[hash].get(bucketIndex).getNumerosPaginas().size()-1);
                	if(tabela.getPaginas().get(paginaIndex).getTuplas().size()<this.tamanhoPagina) {
            			tabela.getPaginas().get(paginaIndex).getTuplas().add(new Tupla(linha));
            		}else {
            			//Bucket Overflow
            			if (buckets[hash].get(bucketIndex).getNumerosPaginas().size() == this.tamanhoBucket) {
                        	buckets[hash].add(new Bucket());
                        	buckets[hash].get(buckets[hash].size()-1).getNumerosPaginas().add(tabela.getPaginas().size());
                            Pagina novaPagina = new Pagina();
                            novaPagina.getTuplas().add(new Tupla(linha));
                            tabela.getPaginas().add(novaPagina);
                            this.nBucketOverflow++;
                        }else {
                        	//Colisão
                        	buckets[hash].get(buckets[hash].size()-1).getNumerosPaginas().add(tabela.getPaginas().size());
                			Pagina novaPagina = new Pagina();
                            novaPagina.getTuplas().add(new Tupla(linha));
                            tabela.getPaginas().add(novaPagina);
                            this.nColisoes++;
                        }
            		}
                }
            }
            scanner.close();
            //Calculando o total de Buckets
            for(int i=0;i<buckets.length;i++) {
            	if(buckets[i]!=null) {
            		this.totalBuckets+=buckets[i].size();
            	}
            }
            
            this.totalPaginas = tabela.getPaginas().size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Getter para nColisoes
    public int getNColisoes() {
        return nColisoes;
    }

    // Getter para nBucketOverflow
    public int getNBucketOverflow() {
        return nBucketOverflow;
    }

    // Getter para numRegistros
    public long getNumRegistros() {
        return numRegistros;
    }

    // Getter para buckets
    public LinkedList<Bucket>[] getBuckets() {
        return buckets;
    }
    public int getPaginasLength() {
    	return this.totalPaginas;
    }
    
    public int getBucketsLength(){
    	return this.totalBuckets;
    }
    
    //Percorrer todos os buckets e todas as páginas dentro do bucket para AQUELE hash
    public int chaveBusca(String chave) throws Exception {
    	boolean teste = false;
        int qtd = 0;
        int hash = this.funcaoHash(chave);
        
        for (int i = 0; i < this.buckets[hash].size(); i++) {
            for (int j = 0; j < this.buckets[hash].get(i).getNumerosPaginas().size(); j++) {
            	qtd++;
            	for(int k = 0; k < tabela.getPaginas().get(buckets[hash].get(i).getNumerosPaginas().get(j)).getTuplas().size(); k++) {
            		if(tabela.getPaginas().get(this.buckets[hash].get(i).getNumerosPaginas().get(j)).getTuplas().get(k).getChave().equals(chave)) {
                		teste = true;
                	}
    			}
            }
        }
        if(teste == false) {
        	throw new Exception("Chave não Encontrada");
        }
        return qtd;
    }
    //Percorrer o total de páginas
    public int tableScan(String chave) throws Exception {
        int qtd = 0;
        for(int i=0;i<buckets.length;i++) {
        	if(buckets[i]!=null) {
        		for(int j=0;j<buckets[i].size();j++) {
            		for(int k=0;k<buckets[i].get(j).getNumerosPaginas().size();k++) {
            			qtd++;
            		}
            	}
        	}
        }
        return qtd;
    }
}
