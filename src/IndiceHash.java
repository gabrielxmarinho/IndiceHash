import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class IndiceHash {
    
    public int funcaoHash(String chaveBusca) {
        return chaveBusca.length() % (this.buckets.length);
    }
    
    private long numRegistros;
    private Tabela tabela;
    private LinkedList<Bucket> buckets[];
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
        tabela = new Tabela((int) Math.ceil((double) this.numRegistros / this.tamanhoPagina));
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
                    buckets[hash].get(buckets[hash].size() - 1).getNumeroPagina().add(hash);
                } else if (buckets[hash].get(buckets[hash].size() - 1).getNumeroPagina().size() == this.tamanhoBucket) {
                    buckets[hash].add(new Bucket());
                    buckets[hash].get(buckets[hash].size() - 1).getNumeroPagina().add(hash);
                    nBucketOverflow++;
                } else {
                    buckets[hash].get(buckets[hash].size() - 1).getNumeroPagina().add(hash);
                    this.nColisoes++;
                }

                if (tabela.getPaginas()[hash] == null) {
                    tabela.getPaginas()[hash] = new LinkedList<Pagina>();
                    tabela.getPaginas()[hash].add(new Pagina());
                    tabela.getPaginas()[hash].get(tabela.getPaginas()[hash].size() - 1).getTuplas().add(new Tupla(linha));
                } else if (tabela.getPaginas()[hash].get(tabela.getPaginas()[hash].size() - 1).getTuplas().size() == this.tamanhoPagina) {
                    tabela.getPaginas()[hash].add(new Pagina());
                    tabela.getPaginas()[hash].get(tabela.getPaginas()[hash].size() - 1).getTuplas().add(new Tupla(linha));
                } else {
                    tabela.getPaginas()[hash].get(tabela.getPaginas()[hash].size() - 1).getTuplas().add(new Tupla(linha));
                }
            }
            scanner.close();
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

    // Getter para tabela
    public Tabela getTabela() {
        return tabela;
    }

    // Getter para o número de buckets
    public int getBucketsLength() {
        return buckets.length;
    }

    public int chaveBusca(String chave) throws Exception {
        int qtd = 0;
        int hash = this.funcaoHash(chave);
        for (int i = 0; i < this.buckets[hash].size(); i++) {
            for (int j = 0; j < this.buckets[hash].get(i).getNumeroPagina().size(); j++) {
                for (int k = 0; k < this.tabela.getPaginas()[this.buckets[hash].get(i).getNumeroPagina().get(j)].size(); k++) {
                    for (int w = 0; w < this.tabela.getPaginas()[this.buckets[hash].get(i).getNumeroPagina().get(j)].get(k).getTuplas().size(); w++) {
                        qtd++;
                        if (this.tabela.getPaginas()[this.buckets[hash].get(i).getNumeroPagina().get(j)].get(k).getTuplas().get(w).getChave().equals(chave)) {
                            return qtd;
                        }
                    }
                }
            }
        }
        throw new Exception("Registro não encontrado");
    }

    public int tableScan(String chave) throws Exception {
        int qtd = 0;
        for (int i = 0; i < this.tabela.getPaginas().length; i++) {
            if (this.tabela.getPaginas()[i] != null) {
                for (int j = 0; j < this.tabela.getPaginas()[i].size(); j++) {
                    for (int k = 0; k < this.tabela.getPaginas()[i].get(j).getTuplas().size(); k++) {
                        qtd++;
                        if (this.tabela.getPaginas()[i].get(j).getTuplas().get(k).getChave().equals(chave)) {
                            return qtd;
                        }
                    }
                }
            }
        }
        throw new Exception("Registro não encontrado");
    }
}
