package aed3;

import java.util.ArrayList;
import java.util.Scanner;

public class ControleListaProduto {
    
    private Arquivo<ListaProduto> arqListaProduto;
    private ArvoreBMais<ParListaProduto> indiceListaProduto;  // idLista -> idListaProduto
    private ArvoreBMais<ParProdutoLista> indiceProdutoLista;  // idProduto -> idListaProduto
    
    public ControleListaProduto() throws Exception {
        arqListaProduto = new Arquivo<>(
            "lista_produto",
            ListaProduto.class.getConstructor()
        );
        
        indiceListaProduto = new ArvoreBMais<>(
            ParListaProduto.class.getConstructor(),
            4,
            ".\\dados\\lista_produto\\arvore_lista_produto.db"
        );
        
        indiceProdutoLista = new ArvoreBMais<>(
            ParProdutoLista.class.getConstructor(),
            4,
            ".\\dados\\lista_produto\\arvore_produto_lista.db"
        );
        
        // Nenhum Scanner aqui; utilizamos os scanners das visões para evitar conflitos em System.in
    }
    
    // Adiciona um produto a uma lista
    public int adicionar(int idLista, int idProduto, int quantidade, String observacoes) throws Exception {
        // Verifica duplicidade com varredura linear segura
        int maxId = arqListaProduto.getUltimoID();
        for (int id = 1; id <= maxId; id++) {
            ListaProduto existente = null;
            try { existente = arqListaProduto.read(id); } catch (Exception ignore) {}
            if (existente != null && existente.getIdLista() == idLista && existente.getIdProduto() == idProduto) {
                System.out.println("\nEste produto ja esta nesta lista!");
                return -1;
            }
        }
        
        // Cria a associacao
        ListaProduto lp = new ListaProduto(idLista, idProduto, quantidade, observacoes);
        int id = arqListaProduto.create(lp);
        lp.setId(id);
        
        // Atualiza os indices
        indiceListaProduto.create(new ParListaProduto(idLista, id));
        indiceProdutoLista.create(new ParProdutoLista(idProduto, id));
        
        return id;
    }
    
    // Le uma associacao por ID
    public ListaProduto ler(int id) throws Exception {
        return arqListaProduto.read(id);
    }
    
    // Atualiza uma associacao (quantidade e observacoes)
    public boolean atualizar(ListaProduto lp) throws Exception {
        return arqListaProduto.update(lp);
    }
    
    // Remove um produto de uma lista
    public boolean remover(int idListaProduto) throws Exception {
        ListaProduto lp = arqListaProduto.read(idListaProduto);
        if(lp == null) {
            return false;
        }
        
        // Remove dos indices
        indiceListaProduto.delete(new ParListaProduto(lp.getIdLista(), idListaProduto));
        indiceProdutoLista.delete(new ParProdutoLista(lp.getIdProduto(), idListaProduto));
        
        // Remove do arquivo
        return arqListaProduto.delete(idListaProduto);
    }
    
    // Busca todos os produtos de uma lista
    public ArrayList<ListaProduto> buscarPorLista(int idLista) throws Exception {
        ArrayList<ListaProduto> resultado = new ArrayList<>();
        
        // Varredura linear confiável
        int maxId = arqListaProduto.getUltimoID();
        for (int id = 1; id <= maxId; id++) {
            ListaProduto lp = null;
            try { lp = arqListaProduto.read(id); } catch (Exception ignore) {}
            if (lp != null && lp.getIdLista() == idLista) {
                resultado.add(lp);
            }
        }
        
        return resultado;
    }
    
    // Busca todas as listas que contem um produto
    public ArrayList<ListaProduto> buscarPorProduto(int idProduto) throws Exception {
        ArrayList<ListaProduto> resultado = new ArrayList<>();
        
        // Varredura linear confiável
        int maxId2 = arqListaProduto.getUltimoID();
        for (int id = 1; id <= maxId2; id++) {
            ListaProduto lp = null;
            try { lp = arqListaProduto.read(id); } catch (Exception ignore) {}
            if (lp != null && lp.getIdProduto() == idProduto) {
                resultado.add(lp);
            }
        }
        
        return resultado;
    }
    
    // Remove todos os produtos de uma lista (quando lista e excluida)
    public boolean removerTodosDaLista(int idLista) throws Exception {
        ArrayList<ListaProduto> produtos = buscarPorLista(idLista);
        boolean sucesso = true;
        
        for(ListaProduto lp : produtos) {
            if(!remover(lp.getId())) {
                sucesso = false;
            }
        }
        
        return sucesso;
    }
    
    // Conta quantas listas contem um produto
    public int contarListasComProduto(int idProduto) throws Exception {
        return buscarPorProduto(idProduto).size();
    }
    
    // Fecha os arquivos
    public void fechar() throws Exception {
        arqListaProduto.close();
        indiceListaProduto.close();
        indiceProdutoLista.close();
    }
}

