package aed3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VisaoLista {
    private Scanner scanner;

    public VisaoLista() {
        this.scanner = new Scanner(System.in);
    }

    public Lista leLista(int idUsuario) {
        System.out.println("\n=== NOVA LISTA ===");
        
        System.out.print("Nome da lista: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descricao detalhada: ");
        String descricao = scanner.nextLine();
        
        LocalDate dataLimite = null;
        System.out.print("Data limite (dd/mm/aaaa) ou Enter para pular: ");
        String dataLimiteStr = scanner.nextLine();
        if (!dataLimiteStr.trim().isEmpty()) {
            try {
                dataLimite = LocalDate.parse(dataLimiteStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida! Lista sera criada sem data limite.");
            }
        }
        
        return new Lista(nome, descricao, dataLimite, idUsuario);
    }

    public void mostraLista(Lista lista) {
        if (lista != null) {
            System.out.println("\n=== DADOS DA LISTA ===");
            System.out.println("CODIGO: " + lista.getCodigoCompartilhavel());
            System.out.println("NOME: " + lista.getNome());
            System.out.println("DESCRICAO: " + lista.getDescricao());
            System.out.println("DATA DE CRIACAO: " + lista.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (lista.getDataLimite() != null) {
                System.out.println("DATA LIMITE: " + lista.getDataLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                System.out.println("DATA LIMITE: Nao definida");
            }
        } else {
            System.out.println("Lista nao encontrada!");
        }
    }

    public void mostraListas(List<Lista> listas) {
        if (listas == null || listas.isEmpty()) {
            System.out.println("Nenhuma lista encontrada.");
            return;
        }

        System.out.println("\n=== SUAS LISTAS ===");
        for (int i = 0; i < listas.size(); i++) {
            Lista lista = listas.get(i);
            String dataLimite = lista.getDataLimite() != null ? 
                lista.getDataLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : 
                "Sem data limite";
            System.out.println("(" + (i + 1) + ") " + lista.getNome() + " - " + dataLimite);
        }
    }

    public void mostraMenuListas() {
        System.out.println("\n=== MENU LISTAS ===");
        System.out.println("(N) Nova lista");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("Opcao: ");
    }

    public void mostraMenuDetalhesLista() {
        System.out.println("\n=== MENU DETALHES DA LISTA ===");
        System.out.println("(1) Gerenciar produtos da lista");
        System.out.println("(2) Alterar dados da lista");
        System.out.println("(3) Excluir lista");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("Opcao: ");
    }

    public int leOpcaoMenuListas() {
        String opcao = scanner.nextLine().toUpperCase();
        if (opcao.equals("N")) {
            return 1;
        } else if (opcao.equals("R")) {
            return -1;
        }
        return 0;
    }

    public int leOpcaoMenuDetalhesLista() {
        String opcao = scanner.nextLine().toUpperCase();
        try {
            if (opcao.equals("R")) {
                return -1;
            }
            return Integer.parseInt(opcao);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int leNumeroLista(int totalListas) {
        System.out.print("Digite o numero da lista (1-" + totalListas + "): ");
        try {
            int numero = Integer.parseInt(scanner.nextLine());
            if (numero >= 1 && numero <= totalListas) {
                return numero - 1; // Converte para indice baseado em 0
            }
        } catch (NumberFormatException e) {
            // Número inválido
        }
        return -1;
    }

    public String leCodigoCompartilhavel() {
        System.out.print("Digite o codigo da lista: ");
        return scanner.nextLine();
    }

    public Lista leDadosAlteracaoLista(Lista listaAtual) {
        System.out.println("\n=== ALTERAR DADOS DA LISTA ===");
        
        System.out.print("Nome da lista [" + listaAtual.getNome() + "]: ");
        String nome = scanner.nextLine();
        if (nome.trim().isEmpty()) {
            nome = listaAtual.getNome();
        }
        
        System.out.print("Descricao detalhada [" + listaAtual.getDescricao() + "]: ");
        String descricao = scanner.nextLine();
        if (descricao.trim().isEmpty()) {
            descricao = listaAtual.getDescricao();
        }
        
        LocalDate dataLimite = listaAtual.getDataLimite();
        System.out.print("Data limite [" + (dataLimite != null ? dataLimite.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Sem data limite") + "] ou Enter para manter: ");
        String dataLimiteStr = scanner.nextLine();
        if (!dataLimiteStr.trim().isEmpty()) {
            try {
                dataLimite = LocalDate.parse(dataLimiteStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida! Mantendo data atual.");
            }
        }
        
        Lista listaAlterada = new Lista();
        listaAlterada.setId(listaAtual.getId());
        listaAlterada.setNome(nome);
        listaAlterada.setDescricao(descricao);
        listaAlterada.setDataCriacao(listaAtual.getDataCriacao());
        listaAlterada.setDataLimite(dataLimite);
        listaAlterada.setCodigoCompartilhavel(listaAtual.getCodigoCompartilhavel());
        listaAlterada.setIdUsuario(listaAtual.getIdUsuario());
        
        return listaAlterada;
    }

    public boolean confirmaExclusaoLista(String nome) {
        System.out.print("Tem certeza que deseja excluir a lista '" + nome + "'? (s/n): ");
        String resposta = scanner.nextLine().toLowerCase();
        return resposta.equals("s") || resposta.equals("sim");
    }

    public void mostraMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void mostraErro(String erro) {
        System.out.println("ERRO: " + erro);
    }

    public void mostraBreadcrumb(String breadcrumb) {
        System.out.println("\nPresenteFacil 1.0");
        System.out.println("-----------------");
        System.out.println("> " + breadcrumb);
    }

    public void close() {
        scanner.close();
    }
}
