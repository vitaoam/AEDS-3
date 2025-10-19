package aed3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class ControleProduto {
    
    private Arquivo<Produto> arqProdutos;
    private HashExtensivel<ParGtinID> indiceGtin;
    private Scanner console;
    
    public ControleProduto() throws Exception {
        arqProdutos = new Arquivo<>(
            "produtos",
            Produto.class.getConstructor()
        );
        
        indiceGtin = new HashExtensivel<>(
            ParGtinID.class.getConstructor(),
            4,
            ".\\dados\\produtos\\produtos_gtin.d.db",
            ".\\dados\\produtos\\produtos_gtin.c.db"
        );
        
        console = new Scanner(System.in);
    }
    
    // Cria um novo produto
    public int criar(Produto produto) throws Exception {
        // Verifica se GTIN-13 ja existe
        ParGtinID parGtin = indiceGtin.read(produto.getGtin13().hashCode());
        if(parGtin != null) {
            System.out.println("\nErro: Ja existe um produto com este GTIN-13!");
            return -1;
        }
        
        int id = arqProdutos.create(produto);
        produto.setId(id);
        indiceGtin.create(new ParGtinID(produto.getGtin13(), id));
        return id;
    }
    
    // Le um produto por ID
    public Produto ler(int id) throws Exception {
        return arqProdutos.read(id);
    }
    
    // Le um produto por GTIN-13
    public Produto lerPorGtin(String gtin13) throws Exception {
        ParGtinID parGtin = indiceGtin.read(gtin13.hashCode());
        if(parGtin != null) {
            return arqProdutos.read(parGtin.getId());
        }
        return null;
    }
    
    // Atualiza um produto
    public boolean atualizar(Produto produto) throws Exception {
        Produto produtoAntigo = arqProdutos.read(produto.getId());
        if(produtoAntigo == null) {
            return false;
        }
        
        // Se mudou o GTIN-13, atualiza o indice
        if(!produtoAntigo.getGtin13().equals(produto.getGtin13())) {
            // Verifica se novo GTIN ja existe
            ParGtinID parGtinNovo = indiceGtin.read(produto.getGtin13().hashCode());
            if(parGtinNovo != null) {
                System.out.println("\nErro: Ja existe um produto com este GTIN-13!");
                return false;
            }
            
            // Remove indice antigo e cria novo
            indiceGtin.delete(produtoAntigo.getGtin13().hashCode());
            indiceGtin.create(new ParGtinID(produto.getGtin13(), produto.getId()));
        }
        
        return arqProdutos.update(produto);
    }
    
    // Inativa um produto (nao exclui)
    public boolean inativar(int id) throws Exception {
        Produto produto = arqProdutos.read(id);
        if(produto != null) {
            produto.setAtivo(false);
            return arqProdutos.update(produto);
        }
        return false;
    }
    
    // Reativa um produto inativado
    public boolean reativar(int id) throws Exception {
        Produto produto = arqProdutos.read(id);
        if(produto != null) {
            produto.setAtivo(true);
            return arqProdutos.update(produto);
        }
        return false;
    }
    
    // Lista todos os produtos (ativos e inativos)
    public ArrayList<Produto> listarTodos() throws Exception {
        ArrayList<Produto> produtos = new ArrayList<>();
        
        // Le todos os produtos do arquivo
        // Como nao temos um metodo readAll, vamos tentar ler por IDs sequenciais
        for(int id = 1; id <= 1000; id++) {
            try {
                Produto produto = arqProdutos.read(id);
                if(produto != null) {
                    produtos.add(produto);
                }
            } catch(Exception e) {
                // ID nao existe, continua
            }
        }
        
        // Ordena por nome
        Collections.sort(produtos, new Comparator<Produto>() {
            public int compare(Produto p1, Produto p2) {
                return p1.getNome().compareToIgnoreCase(p2.getNome());
            }
        });
        
        return produtos;
    }
    
    // Lista apenas produtos ativos
    public ArrayList<Produto> listarAtivos() throws Exception {
        ArrayList<Produto> todos = listarTodos();
        ArrayList<Produto> ativos = new ArrayList<>();
        
        for(Produto p : todos) {
            if(p.isAtivo()) {
                ativos.add(p);
            }
        }
        
        return ativos;
    }
    
    // Fecha os arquivos
    public void fechar() throws Exception {
        arqProdutos.close();
        indiceGtin.close();
    }
}

