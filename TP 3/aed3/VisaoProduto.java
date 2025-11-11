package aed3;

import java.util.ArrayList;
import java.util.Scanner;

public class VisaoProduto {
    
    private Scanner console;
    private static final int ITENS_POR_PAGINA = 10;
    
    public VisaoProduto(Scanner console) {
        this.console = console;
    }
    
    // Le os dados de um novo produto
    public Produto lerNovoProduto() {
        System.out.println("\n=== CADASTRAR NOVO PRODUTO ===");

        System.out.print("GTIN-13 (13 digitos): ");
        String gtin13 = console.nextLine().trim();
        
        System.out.print("Nome: ");
        String nome = console.nextLine().trim();
        
        System.out.print("Descricao: ");
        String descricao = console.nextLine().trim();
        
        return new Produto(gtin13, nome, descricao);
    }
    
    // Mostra detalhes de um produto
    public void mostrarProduto(Produto produto) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("NOME.......: " + produto.getNome());
        System.out.println("GTIN-13....: " + produto.getGtin13());
        System.out.println("DESCRICAO..: " + produto.getDescricao());
        if(!produto.isAtivo()) {
            System.out.println("STATUS.....: INATIVADO");
        }
    }
    
    // Le GTIN-13 para busca
    public String lerGtin() {
        System.out.print("\nDigite o GTIN-13: ");
        return console.nextLine().trim();
    }

    // Le termos de consulta por palavras
    public String lerConsultaTermos() {
        System.out.print("\nDigite palavras para busca (separe por espaco): ");
        return console.nextLine().trim();
    }
    
    // Menu de opcoes do produto
    public int menuProduto(Produto produto, int numListasOutros) {
        System.out.println("\n(1) Alterar os dados do produto");
        if(produto.isAtivo()) {
            System.out.println("(2) Inativar o produto");
        } else {
            System.out.println("(2) Reativar o produto");
        }
        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpcao: ");
        
        String opcao = console.nextLine().trim().toUpperCase();
        
        if(opcao.equals("R")) return 0;
        try {
            return Integer.parseInt(opcao);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
    
    // Le alteracoes de um produto
    public Produto lerAlteracoes(Produto produto) {
        System.out.println("\n=== ALTERAR DADOS DO PRODUTO ===");
        System.out.println("(pressione ENTER para manter o valor atual)");

        System.out.print("GTIN-13 [" + produto.getGtin13() + "]: ");
        String gtin13 = console.nextLine().trim();
        if(!gtin13.isEmpty()) {
            produto.setGtin13(gtin13);
        }
        
        System.out.print("Nome [" + produto.getNome() + "]: ");
        String nome = console.nextLine().trim();
        if(!nome.isEmpty()) {
            produto.setNome(nome);
        }
        
        System.out.print("Descricao [" + produto.getDescricao() + "]: ");
        String descricao = console.nextLine().trim();
        if(!descricao.isEmpty()) {
            produto.setDescricao(descricao);
        }
        
        return produto;
    }
    
    // Exibe listagem paginada de produtos
    public int exibirListagemPaginada(ArrayList<Produto> produtos, int paginaAtual, String titulo) {
        int totalPaginas = (int) Math.ceil((double) produtos.size() / ITENS_POR_PAGINA);
        
        if(totalPaginas == 0) {
            System.out.println("\nNenhum produto cadastrado.");
            return -1;
        }
        
        if(paginaAtual < 1) paginaAtual = 1;
        if(paginaAtual > totalPaginas) paginaAtual = totalPaginas;
        
        int inicio = (paginaAtual - 1) * ITENS_POR_PAGINA;
        int fim = Math.min(inicio + ITENS_POR_PAGINA, produtos.size());
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println(titulo);
        System.out.println("=".repeat(50));
        System.out.println("\nPagina " + paginaAtual + " de " + totalPaginas);
        System.out.println();
        
        for(int i = inicio; i < fim; i++) {
            Produto p = produtos.get(i);
            String status = p.isAtivo() ? "" : " (INATIVADO)";
            int numero = (i % ITENS_POR_PAGINA) + 1;
            if(numero == 10) numero = 0;  // ultimo item vira 0
            System.out.println("(" + numero + ") " + p.getNome() + status);
        }
        
        System.out.println();
        if(paginaAtual > 1) {
            System.out.println("(A) Pagina anterior");
        }
        if(paginaAtual < totalPaginas) {
            System.out.println("(P) Proxima pagina");
        }
        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpcao: ");
        
        String opcao = console.nextLine().trim().toUpperCase();
        
        if(opcao.equals("R")) return -2;
        if(opcao.equals("A") && paginaAtual > 1) return -3;  // pagina anterior
        if(opcao.equals("P") && paginaAtual < totalPaginas) return -4;  // proxima pagina
        
        try {
            int num = Integer.parseInt(opcao);
            if(num == 0) num = 10;  // 0 representa o item 10
            int indice = inicio + num - 1;
            if(indice >= inicio && indice < fim) {
                return indice;  // retorna o indice do produto selecionado
            }
        } catch(NumberFormatException e) {
            // opcao invalida
        }
        
        return -1;  // opcao invalida
    }
    
    // Confirma uma acao
    public boolean confirmar(String mensagem) {
        System.out.print("\n" + mensagem + " (S/N): ");
        String resp = console.nextLine().trim().toUpperCase();
        return resp.equals("S");
    }
    
    // Exibe mensagem
    public void exibirMensagem(String mensagem) {
        System.out.println("\n" + mensagem);
    }
    
    // Pausa para o usuario ler
    public void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        console.nextLine();
    }
}

