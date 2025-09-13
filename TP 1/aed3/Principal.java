package aed3;

import java.util.Scanner;

public class Principal {
    private static Scanner scanner = new Scanner(System.in);
    private static ControleUsuario controleUsuario;
    private static ControleLista controleLista;
    private static Usuario usuarioLogado;

    public static void main(String[] args) {
        try {
            // Inicializa os controles
            controleUsuario = new ControleUsuario();
            controleLista = new ControleLista();
            
            // Menu principal
            menuPrincipal();
            
        } catch (Exception e) {
            System.out.println("Erro fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (controleUsuario != null) controleUsuario.close();
                if (controleLista != null) controleLista.close();
                scanner.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    private static void menuPrincipal() throws Exception {
        int opcao;
        do {
            System.out.println("\nPresenteFacil 1.0");
            System.out.println("-----------------");
            System.out.println("> Inicio");
            System.out.println();
            System.out.println("(1) Login");
            System.out.println("(2) Novo usuario");
            System.out.println("(S) Sair");
            System.out.print("Opcao: ");
            
            String entrada = scanner.nextLine().toUpperCase();
            
            switch (entrada) {
                case "1":
                    fazerLogin();
                    break;
                case "2":
                    criarNovoUsuario();
                    break;
                case "S":
                    System.out.println("Obrigado por usar o PresenteFacil!");
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (true);
    }

    private static void fazerLogin() throws Exception {
        System.out.println("\n=== LOGIN ===");
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        usuarioLogado = controleUsuario.autenticarUsuario(email, senha);
        if (usuarioLogado != null) {
            System.out.println("Login realizado com sucesso!");
            menuUsuarioLogado();
        } else {
            System.out.println("E-mail ou senha incorretos!");
        }
    }

    private static void criarNovoUsuario() throws Exception {
        try {
            Usuario novoUsuario = new Usuario();
            
            System.out.println("\n=== CADASTRO DE USUARIO ===");
            System.out.print("Nome: ");
            novoUsuario.setNome(scanner.nextLine());
            
            System.out.print("E-mail: ");
            novoUsuario.setEmail(scanner.nextLine());
            
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            novoUsuario.alterarSenha(senha);
            
            System.out.print("Pergunta secreta: ");
            novoUsuario.setPerguntaSecreta(scanner.nextLine());
            
            System.out.print("Resposta secreta: ");
            novoUsuario.setRespostaSecreta(scanner.nextLine());
            
            controleUsuario.criarUsuario(novoUsuario);
            System.out.println("Usuário criado com sucesso! Faça login para continuar.");
            
        } catch (Exception e) {
            System.out.println("Erro ao criar usuario: " + e.getMessage());
        }
    }

    private static void menuUsuarioLogado() throws Exception {
        int opcao;
        do {
            System.out.println("\nPresenteFacil 1.0");
            System.out.println("-----------------");
            System.out.println("> Inicio");
            System.out.println();
            System.out.println("(1) Meus dados");
            System.out.println("(2) Minhas listas");
            System.out.println("(3) Produtos");
            System.out.println("(4) Buscar lista");
            System.out.println("(S) Sair");
            System.out.print("Opcao: ");
            
            String entrada = scanner.nextLine().toUpperCase();
            
            switch (entrada) {
                case "1":
                    controleUsuario.menuUsuario(usuarioLogado);
                    break;
                case "2":
                    menuMinhasListas();
                    break;
                case "3":
                    System.out.println("Funcionalidade será implementada no TP2!");
                    break;
                case "4":
                    controleLista.buscarListaPorCodigo();
                    break;
                case "S":
                    usuarioLogado = null;
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (true);
    }

    private static void menuMinhasListas() throws Exception {
        int opcao;
        do {
            System.out.println("\nPresenteFacil 1.0");
            System.out.println("-----------------");
            System.out.println("> Inicio > Minhas listas");
            System.out.println();
            System.out.println("(1) Ver minhas listas");
            System.out.println("(2) Nova lista");
            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("Opcao: ");
            
            String entrada = scanner.nextLine().toUpperCase();
            
            switch (entrada) {
                case "1":
                    controleLista.menuSelecaoLista(usuarioLogado);
                    break;
                case "2":
                    Lista novaLista = new Lista();
                    
                    System.out.println("\n=== NOVA LISTA ===");
                    System.out.print("Nome da lista: ");
                    novaLista.setNome(scanner.nextLine());
                    
                    System.out.print("Descricao detalhada: ");
                    novaLista.setDescricao(scanner.nextLine());
                    
                    System.out.print("Data limite (dd/mm/aaaa) ou Enter para pular: ");
                    String dataLimiteStr = scanner.nextLine();
                    if (!dataLimiteStr.trim().isEmpty()) {
                        try {
                            novaLista.setDataLimite(java.time.LocalDate.parse(dataLimiteStr, 
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        } catch (Exception e) {
                            System.out.println("Data invalida! Lista sera criada sem data limite.");
                        }
                    }
                    
                    novaLista.setIdUsuario(usuarioLogado.getId());
                    controleLista.criarLista(novaLista);
                    break;
                case "R":
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (true);
    }
}
