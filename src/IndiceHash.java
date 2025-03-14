import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class IndiceHash {
    
    public int funcaoHash(String chaveBusca) {
    	//Função Hash deve ser simples e com baixas métricas de Colisão e Bucket Overflow
    	// Convertendo os caracteres em códigos ASCII
        int a = chaveBusca.length() > 0 ? chaveBusca.charAt(0) * 31 : 0;
        int b = chaveBusca.length() > 1 ? chaveBusca.charAt(1) * 17 : 0;
        int c = chaveBusca.length() > 2 ? chaveBusca.charAt(2) * 13 : 0;
        int d = chaveBusca.length() > 3 ? chaveBusca.charAt(3) * 7  : 0;
        
        // Combinação matemática sem operadores lógicos
        int hash = (a + b + c + d) * 33;

        return Math.abs(hash) % (this.buckets.length); 
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
                    Pagina novaPagina = new Pagina();
                    novaPagina.getTuplas().add(new Tupla(linha));
                    tabela.getPaginas().add(novaPagina);
                } else {
                	//Ultimo Bucket apontado por aquele hash
                	int bucketIndex = buckets[hash].size()-1;
                	//Ultima página para onde o último bucket dessa posição aponta
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
            	Pagina pagina = tabela.getPaginas().get(buckets[hash].get(i).getNumerosPaginas().get(j));
    			LinkedList<Tupla> tuplas = pagina.getTuplas();
    			for(int x=0;x<tuplas.size();x++) {
    				if(tuplas.get(x).getChave().equals(chave)) {
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
    	boolean teste = false;
        int qtd = 0;
        for(int i=0;i<buckets.length;i++) {
        	if(buckets[i]!=null) {
        		for(int j=0;j<buckets[i].size();j++) {
            		for(int k=0;k<buckets[i].get(j).getNumerosPaginas().size();k++) {
            			qtd++;
            			Pagina pagina = tabela.getPaginas().get(buckets[i].get(j).getNumerosPaginas().get(k));
            			LinkedList<Tupla> tuplas = pagina.getTuplas();
            			for(int x=0;x<tuplas.size();x++) {
            				if(tuplas.get(x).getChave().equals(chave)) {
            					teste = true;
            				}
            			}
            		}
            	}
        	}
        }
        if(teste == false) {
        	throw new Exception("Chave não Encontrada");
        }
        return qtd;
    }
}
