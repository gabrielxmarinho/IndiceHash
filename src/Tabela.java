import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class Tabela {
    
    public int funcaoHash(String chaveBusca) {
        return Math.abs(chaveBusca.hashCode())%(buckets.length);
    }
    
    private long numRegistros;
    private LinkedList<Bucket> buckets[];
    private int totalBuckets = 0;
    private int totalPaginas = 0;
    private int tamanhoBucket = 10;
    private int tamanhoPagina = 10;
    private int nColisoes;
    private int nBucketOverflow;
    
    public Tabela(String nomeArquivo) {
        this.nColisoes = 0;
        this.nBucketOverflow = 0;
        try {
            this.numRegistros = Files.lines(Paths.get(nomeArquivo)).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buckets = (LinkedList<Bucket>[]) new LinkedList[(int) Math.ceil((double) this.numRegistros / this.tamanhoBucket)];
        try {
            File arq = new File(nomeArquivo);
            Scanner scanner = new Scanner(arq);
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                int hash = this.funcaoHash(linha);
                if (buckets[hash] == null) {
                    buckets[hash] = new LinkedList<Bucket>();
                    buckets[hash].add(new Bucket());
                    buckets[hash].get(buckets[hash].size() - 1).getPaginas().add(new Pagina());
                    buckets[hash].get(buckets[hash].size() - 1).getPaginas().get(buckets[hash].get(buckets[hash].size() - 1).getPaginas().size()-1).getTuplas().add(new Tupla(linha));  
                } else if (buckets[hash].get(buckets[hash].size() - 1).getPaginas().size() == this.tamanhoBucket) {
                    buckets[hash].add(new Bucket());
                    buckets[hash].get(buckets[hash].size() - 1).getPaginas().add(new Pagina());
                    buckets[hash].get(buckets[hash].size() - 1).getPaginas().get(buckets[hash].get(buckets[hash].size() - 1).getPaginas().size()-1).getTuplas().add(new Tupla(linha));
                    this.nBucketOverflow++;
                } else {
                	if(buckets[hash].get(buckets[hash].size() - 1).getPaginas().get(buckets[hash].get(buckets[hash].size() - 1).getPaginas().size()-1).getTuplas().size()<this.tamanhoPagina){
                		//Adiciona-se a Tupla em uma página dentro da ultima página criada no Bucket respectivo
                		buckets[hash].get(buckets[hash].size() - 1).getPaginas().get(buckets[hash].get(buckets[hash].size() - 1).getPaginas().size()-1).getTuplas().add(new Tupla(linha));
                	}else {
                		//Foi criada uma nova página no mesmo Bucket => Colisão
                		buckets[hash].get(buckets[hash].size() - 1).getPaginas().add(new Pagina());
                		buckets[hash].get(buckets[hash].size() - 1).getPaginas().get(buckets[hash].get(buckets[hash].size() - 1).getPaginas().size()-1).getTuplas().add(new Tupla(linha));
                		this.nColisoes++;
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
            //Calculando o total de páginas
            for(int i=0;i<buckets.length;i++) {
            	if(buckets[i]!=null) {
            		for(int j=0;j<buckets[i].size();j++) {
                		for(int k=0;k<buckets[i].get(j).getPaginas().size();k++) {
                			this.totalPaginas++;
                		}
                	}
            	}
            }
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
        int qtd = 0;
        int hash = this.funcaoHash(chave);
        for (int i = 0; i < this.buckets[hash].size(); i++) {
            for (int j = 0; j < this.buckets[hash].get(i).getPaginas().size(); j++) {
            	qtd++;
            }
        }
        return qtd;
    }
    //Percorrer o total de páginas
    public int tableScan(String chave) throws Exception {
        int qtd = 0;
        for(int i=0;i<buckets.length;i++) {
        	if(buckets[i]!=null) {
        		for(int j=0;j<buckets[i].size();j++) {
            		qtd++;
            	}
        	}
        }
        return qtd;
    }
}
