package aed3;

import java.util.ArrayList;
import java.util.Scanner;

public class VisaoListaComProdutos {
    private Scanner scanner;

    public VisaoListaComProdutos(Scanner scanner) {
        this.scanner = scanner;
    }

    // Exibe menu de produtos de uma lista
    public void mostraMenuProdutos(ArrayList<Produto> produtos, ArrayList<ListaProduto> associacoes) {
        System.out.println("\n=== PRODUTOS DA LISTA ===");
        
        if(produtos.isEmpty()) {
            System.out.println("\nNenhum produto nesta lista.");
        } else {
            for(int i = 0; i < produtos.size(); i++) {
                Produto p = produtos.get(i);
                ListaProduto lp = associacoes.get(i);
                System.out.println("(" + (i + 1) + ") " + p.getNome() + " (x" + lp.getQuantidade() + ")");
            }
        }
        
        System.out.println("\n(V) Ver produto");
        System.out.println("\n(A) Acrescentar produto");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpcao: ");
    }
    
    // Le opcao do menu de produtos
    public String leOpcaoMenuProdutos(int totalProdutos) {
        String opcao = scanner.nextLine().trim().toUpperCase();
        return opcao;
    }

    // Le numero de um produto listado
    public int leNumeroProduto(int totalProdutos) {
        System.out.print("\nDigite o numero do produto: ");
        try {
            int n = Integer.parseInt(scanner.nextLine().trim());
            if(n >= 1 && n <= totalProdutos) return n;
        } catch(NumberFormatException e) {
            // invalido
        }
        return -1;
    }
    
    // Mostra detalhes de um produto na lista
    public void mostraDetalhesProdutoNaLista(Produto produto, ListaProduto lp) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("NOME.......: " + produto.getNome());
        System.out.println("GTIN-13....: " + produto.getGtin13());
        System.out.println("DESCRICAO..: " + produto.getDescricao());
        System.out.println("QUANTIDADE.: " + lp.getQuantidade());
        System.out.println("OBSERVACOES: " + (lp.getObservacoes().isEmpty() ? "(nenhuma)" : lp.getObservacoes()));
    }
    
    // Menu de opcoes do produto na lista
    public int menuProdutoNaLista() {
        System.out.println("\n(1) Alterar a quantidade");
        System.out.println("(2) Alterar as observacoes");
        System.out.println("(3) Remover o produto desta lista");
        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpcao: ");
        
        String opcao = scanner.nextLine().trim().toUpperCase();
        if(opcao.equals("R")) return 0;
        try {
            return Integer.parseInt(opcao);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
    
    // Le nova quantidade
    public int leQuantidade() {
        System.out.print("\nNova quantidade: ");
        try {
            int qtd = Integer.parseInt(scanner.nextLine().trim());
            if(qtd > 0) return qtd;
        } catch(NumberFormatException e) {
            // quantidade invalida
        }
        System.out.println("Quantidade invalida!");
        return -1;
    }
    
    // Le novas observacoes
    public String leObservacoes() {
        System.out.print("\nObservacoes (ou Enter para deixar em branco): ");
        return scanner.nextLine().trim();
    }
    
    // Menu para acrescentar produto
    public int menuAcrescentarProduto() {
        System.out.println("\n=== ACRESCENTAR PRODUTO ===");
        System.out.println("(1) Buscar produtos por GTIN");
        System.out.println("(2) Listar todos os produtos");
        System.out.println("(3) Buscar por palavras");
        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpcao: ");
        
        String opcao = scanner.nextLine().trim().toUpperCase();
        if(opcao.equals("R")) return 0;
        try {
            return Integer.parseInt(opcao);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
    
    // Confirma adicao de produto
    public boolean confirmaAdicaoProduto(Produto produto) {
        System.out.print("\nAdicionar '" + produto.getNome() + "' a esta lista? (S/N): ");
        String resp = scanner.nextLine().trim().toUpperCase();
        return resp.equals("S");
    }
    
    // Le quantidade e observacoes para novo produto
    public int[] leDadosNovoProduto() {
        System.out.print("\nQuantidade: ");
        int quantidade = 1;
        try {
            quantidade = Integer.parseInt(scanner.nextLine().trim());
            if(quantidade < 1) quantidade = 1;
        } catch(NumberFormatException e) {
            quantidade = 1;
        }
        
        return new int[]{quantidade};
    }
    
    // Confirma remocao
    public boolean confirmaRemocao(String nomeProduto) {
        System.out.print("\nRemover '" + nomeProduto + "' desta lista? (S/N): ");
        String resp = scanner.nextLine().trim().toUpperCase();
        return resp.equals("S");
    }
    
    // Exibe mensagem
    public void exibirMensagem(String mensagem) {
        System.out.println("\n" + mensagem);
    }
    
    // Pausa
    public void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}

