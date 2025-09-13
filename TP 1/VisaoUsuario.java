package aed3;

import java.util.Scanner;

public class VisaoUsuario {
    private Scanner scanner;

    public VisaoUsuario() {
        this.scanner = new Scanner(System.in);
    }

    public Usuario leUsuario() {
        System.out.println("\n=== CADASTRO DE USUARIO ===");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        System.out.print("Pergunta secreta: ");
        String perguntaSecreta = scanner.nextLine();
        
        System.out.print("Resposta secreta: ");
        String respostaSecreta = scanner.nextLine();
        
        return new Usuario(nome, email, senha, perguntaSecreta, respostaSecreta);
    }

    public String leEmail() {
        System.out.print("E-mail: ");
        return scanner.nextLine();
    }

    public String leSenha() {
        System.out.print("Senha: ");
        return scanner.nextLine();
    }

    public String lePerguntaSecreta() {
        System.out.print("Pergunta secreta: ");
        return scanner.nextLine();
    }

    public String leRespostaSecreta() {
        System.out.print("Resposta secreta: ");
        return scanner.nextLine();
    }

    public String leNovaSenha() {
        System.out.print("Nova senha: ");
        return scanner.nextLine();
    }

    public void mostraUsuario(Usuario usuario) {
        if (usuario != null) {
            System.out.println("\n=== DADOS DO USUARIO ===");
            System.out.println("ID: " + usuario.getId());
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("E-mail: " + usuario.getEmail());
            System.out.println("Pergunta secreta: " + usuario.getPerguntaSecreta());
        } else {
            System.out.println("Usuário nao encontrado!");
        }
    }

    public void mostraMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void mostraErro(String erro) {
        System.out.println("ERRO: " + erro);
    }

    public boolean confirmaExclusao(String nome) {
        System.out.print("Tem certeza que deseja excluir o usuario '" + nome + "'? (s/n): ");
        String resposta = scanner.nextLine().toLowerCase();
        return resposta.equals("s") || resposta.equals("sim");
    }

    public void mostraMenuUsuario() {
        System.out.println("\n=== MENU USUARIO ===");
        System.out.println("(1) Visualizar dados");
        System.out.println("(2) Alterar nome");
        System.out.println("(3) Alterar email");
        System.out.println("(4) Alterar senha");
        System.out.println("(5) Alterar pergunta secreta");
        System.out.println("(6) Alterar resposta secreta");
        System.out.println("(7) Excluir usuario");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("Opcao: ");
    }

    public int leOpcaoMenuUsuario() {
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

    public String leNovoNome() {
        System.out.print("Novo nome: ");
        return scanner.nextLine();
    }

    public String leNovoEmail() {
        System.out.print("Novo email: ");
        return scanner.nextLine();
    }

    public String leNovaPerguntaSecreta() {
        System.out.print("Nova pergunta secreta: ");
        return scanner.nextLine();
    }

    public String leNovaRespostaSecreta() {
        System.out.print("Nova resposta secreta: ");
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
