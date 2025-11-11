package aed3;

import java.util.ArrayList;
import java.util.Scanner;

public class Principal {
    private static Scanner scanner = new Scanner(System.in);
    private static ControleUsuario controleUsuario;
    private static ControleLista controleLista;
    private static ControleProduto controleProduto;
    private static ControleListaProduto controleListaProduto;
    private static VisaoProduto visaoProduto;
    private static VisaoListaComProdutos visaoListaProdutos;
    private static Usuario usuarioLogado;

    public static void main(String[] args) {
        try {
            // Inicializa os controles e visoes
            controleUsuario = new ControleUsuario();
            controleLista = new ControleLista(scanner);
            controleProduto = new ControleProduto();
            controleListaProduto = new ControleListaProduto();
            visaoProduto = new VisaoProduto(scanner);
            visaoListaProdutos = new VisaoListaComProdutos(scanner);
            
            // Menu principal
            menuPrincipal();
            
        } catch (Exception e) {
            System.out.println("Erro fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (controleUsuario != null) controleUsuario.close();
                if (controleLista != null) controleLista.close();
                if (controleProduto != null) controleProduto.fechar();
                if (controleListaProduto != null) controleListaProduto.fechar();
                scanner.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    private static void buscarProdutosPorPalavras() throws Exception {
        String consulta = visaoProduto.lerConsultaTermos();
        ArrayList<Produto> produtos = controleProduto.buscarPorTermos(consulta);
        int paginaAtual = 1;
        do {
            int resultado = visaoProduto.exibirListagemPaginada(produtos, paginaAtual, 
                "> Inicio > Produtos > Busca por palavras");
            if(resultado == -2) {
                return;  // retornar
            } else if(resultado == -3) {
                paginaAtual--;  // pagina anterior
            } else if(resultado == -4) {
                paginaAtual++;  // proxima pagina
            } else if(resultado >= 0) {
                Produto p = produtos.get(resultado);
                menuDetalhesProduto(p);
            }
        } while(true);
    }

    private static void menuBuscaTermosParaAdicionar(Lista lista) throws Exception {
        String consulta = visaoProduto.lerConsultaTermos();
        ArrayList<Produto> produtos = controleProduto.buscarPorTermos(consulta);
        int paginaAtual = 1;
        do {
            int resultado = visaoProduto.exibirListagemPaginada(produtos, paginaAtual, 
                "> Inicio > Minhas listas > " + lista.getNome() + " > Produtos > Buscar por palavras");
            if(resultado == -2) {
                return;  // retornar
            } else if(resultado == -3) {
                paginaAtual--;  // pagina anterior
            } else if(resultado == -4) {
                paginaAtual++;  // proxima pagina
            } else if(resultado >= 0) {
                Produto p = produtos.get(resultado);
                if(visaoListaProdutos.confirmaAdicaoProduto(p)) {
                    int[] dados = visaoListaProdutos.leDadosNovoProduto();
                    String obs = visaoListaProdutos.leObservacoes();
                    controleListaProduto.adicionar(lista.getId(), p.getId(), dados[0], obs);
                    visaoListaProdutos.exibirMensagem("Produto adicionado!");
                    return;
                }
            }
        } while(true);
    }

    private static void menuPrincipal() throws Exception {
        do {
            System.out.println("\nPresenteFacil 2.0");
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
            System.out.println("Usuario criado com sucesso! Faca login para continuar.");
            
        } catch (Exception e) {
            System.out.println("Erro ao criar usuario: " + e.getMessage());
        }
    }

    private static void menuUsuarioLogado() throws Exception {
        do {
            System.out.println("\nPresenteFacil 2.0");
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
                    menuProdutos();
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
        do {
            System.out.println("\nPresenteFacil 2.0");
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
                    menuSelecaoLista();
                    break;
                case "2":
                    criarNovaLista();
                    break;
                case "R":
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (true);
    }

    private static void criarNovaLista() throws Exception {
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
    }

    private static void menuSelecaoLista() throws Exception {
        ArrayList<Lista> listas = controleLista.buscarListasPorUsuario(usuarioLogado.getId());
        
        if(listas.isEmpty()) {
            System.out.println("\nVoce ainda nao possui listas cadastradas.");
            return;
        }
        
        System.out.println("\n=== SUAS LISTAS ===");
        for(int i = 0; i < listas.size(); i++) {
            Lista lista = listas.get(i);
            String dataLimite = lista.getDataLimite() != null ? 
                lista.getDataLimite().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : 
                "Sem data limite";
            System.out.println("(" + (i + 1) + ") " + lista.getNome() + " - " + dataLimite);
        }
        
        System.out.print("\nDigite o numero da lista (ou R para retornar): ");
        String entrada = scanner.nextLine().toUpperCase();
        
        if(entrada.equals("R")) return;
        
        try {
            int num = Integer.parseInt(entrada);
            if(num >= 1 && num <= listas.size()) {
                menuDetalhesLista(listas.get(num - 1));
            } else {
                System.out.println("Numero invalido!");
            }
        } catch(NumberFormatException e) {
            System.out.println("Entrada invalida!");
        }
    }

    private static void menuDetalhesLista(Lista lista) throws Exception {
        do {
            System.out.println("\nPresenteFacil 2.0");
            System.out.println("-----------------");
            System.out.println("> Inicio > Minhas listas > " + lista.getNome());
            System.out.println();
            System.out.println("CODIGO: " + lista.getCodigoCompartilhavel());
            System.out.println("NOME: " + lista.getNome());
            System.out.println("DESCRICAO: " + lista.getDescricao());
            System.out.println("DATA DE CRIACAO: " + lista.getDataCriacao().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if(lista.getDataLimite() != null) {
                System.out.println("DATA LIMITE: " + lista.getDataLimite().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                System.out.println("DATA LIMITE: Nao definida");
            }
            
            System.out.println("\n(1) Gerenciar produtos da lista");
            System.out.println("(2) Alterar dados da lista");
            System.out.println("(3) Excluir lista");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpcao: ");
            
            String entrada = scanner.nextLine().toUpperCase();
            
            switch(entrada) {
                case "1":
                    menuProdutosDaLista(lista);
                    break;
                case "2":
                    // Alterar dados da lista (implementacao do TP1)
                    controleLista.alterarLista(lista);
                    break;
                case "3":
                    // Excluir lista (remove produtos tambem)
                    if(controleLista.excluirLista(lista.getId())) {
                        controleListaProduto.removerTodosDaLista(lista.getId());
                        System.out.println("Lista excluida com sucesso!");
                        return;
                    }
                    break;
                case "R":
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while(true);
    }

    private static void menuProdutosDaLista(Lista lista) throws Exception {
        do {
            ArrayList<ListaProduto> associacoes = controleListaProduto.buscarPorLista(lista.getId());
            ArrayList<Produto> produtos = new ArrayList<>();
            
            for(ListaProduto lp : associacoes) {
                Produto p = controleProduto.ler(lp.getIdProduto());
                if(p != null) {
                    produtos.add(p);
                }
            }
            
            visaoListaProdutos.mostraMenuProdutos(produtos, associacoes);
            String opcao = visaoListaProdutos.leOpcaoMenuProdutos(produtos.size());
            
            if(opcao.equals("R")) {
                return;
            } else if(opcao.equals("A")) {
                menuAcrescentarProdutoLista(lista);
            } else if(opcao.equals("V")) {
                int num = visaoListaProdutos.leNumeroProduto(produtos.size());
                if(num >= 1 && num <= produtos.size()) {
                    menuDetalhesProdutoNaLista(produtos.get(num - 1), associacoes.get(num - 1), lista);
                } else {
                    System.out.println("Numero invalido!");
                }
            } else {
                try {
                    int num = Integer.parseInt(opcao);
                    if(num >= 1 && num <= produtos.size()) {
                        menuDetalhesProdutoNaLista(produtos.get(num - 1), associacoes.get(num - 1), lista);
                    } else {
                        System.out.println("Numero invalido!");
                    }
                } catch(NumberFormatException e) {
                    System.out.println("Opcao invalida!");
                }
            }
        } while(true);
    }

    private static void menuDetalhesProdutoNaLista(Produto produto, ListaProduto lp, Lista lista) throws Exception {
        do {
            visaoListaProdutos.mostraDetalhesProdutoNaLista(produto, lp);
            int opcao = visaoListaProdutos.menuProdutoNaLista();
            
            switch(opcao) {
                case 0:
                    return;
                case 1:
                    int novaQtd = visaoListaProdutos.leQuantidade();
                    if(novaQtd > 0) {
                        lp.setQuantidade(novaQtd);
                        if(controleListaProduto.atualizar(lp)) {
                            visaoListaProdutos.exibirMensagem("Quantidade atualizada!");
                        }
                    }
                    break;
                case 2:
                    String obs = visaoListaProdutos.leObservacoes();
                    lp.setObservacoes(obs);
                    if(controleListaProduto.atualizar(lp)) {
                        visaoListaProdutos.exibirMensagem("Observacoes atualizadas!");
                    }
                    break;
                case 3:
                    if(visaoListaProdutos.confirmaRemocao(produto.getNome())) {
                        if(controleListaProduto.remover(lp.getId())) {
                            visaoListaProdutos.exibirMensagem("Produto removido da lista!");
                            return;
                        }
                    }
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while(true);
    }

    private static void menuAcrescentarProdutoLista(Lista lista) throws Exception {
        int opcao = visaoListaProdutos.menuAcrescentarProduto();
        
            switch(opcao) {
            case 0:
                return;
            case 1:
                // Buscar por GTIN
                String gtin = visaoProduto.lerGtin();
                Produto p = controleProduto.lerPorGtin(gtin);
                if(p != null && p.isAtivo()) {
                    if(visaoListaProdutos.confirmaAdicaoProduto(p)) {
                        int[] dados = visaoListaProdutos.leDadosNovoProduto();
                        String obs = visaoListaProdutos.leObservacoes();
                        controleListaProduto.adicionar(lista.getId(), p.getId(), dados[0], obs);
                        visaoListaProdutos.exibirMensagem("Produto adicionado!");
                    }
                } else {
                    System.out.println("Produto nao encontrado ou inativo!");
                }
                break;
            case 2:
                // Listar todos
                menuListagemProdutosParaAdicionar(lista);
                break;
            case 3:
                // Buscar por palavras e adicionar
                menuBuscaTermosParaAdicionar(lista);
                break;
            default:
                System.out.println("Opcao invalida!");
        }
    }

    private static void menuListagemProdutosParaAdicionar(Lista lista) throws Exception {
        ArrayList<Produto> produtos = controleProduto.listarAtivos();
        int paginaAtual = 1;
        
        do {
            int resultado = visaoProduto.exibirListagemPaginada(produtos, paginaAtual, 
                "> Inicio > Minhas listas > " + lista.getNome() + " > Produtos > Acrescentar produto > Listagem");
            
            if(resultado == -2) {
                return;  // retornar
            } else if(resultado == -3) {
                paginaAtual--;  // pagina anterior
            } else if(resultado == -4) {
                paginaAtual++;  // proxima pagina
            } else if(resultado >= 0) {
                // Produto selecionado
                Produto p = produtos.get(resultado);
                if(visaoListaProdutos.confirmaAdicaoProduto(p)) {
                    int[] dados = visaoListaProdutos.leDadosNovoProduto();
                    String obs = visaoListaProdutos.leObservacoes();
                    controleListaProduto.adicionar(lista.getId(), p.getId(), dados[0], obs);
                    visaoListaProdutos.exibirMensagem("Produto adicionado!");
                    return;
                }
            }
        } while(true);
    }

    private static void menuProdutos() throws Exception {
        do {
            System.out.println("\nPresenteFacil 2.0");
            System.out.println("-----------------");
            System.out.println("> Inicio > Produtos");
            System.out.println();
            System.out.println("(1) Buscar produtos por GTIN");
            System.out.println("(2) Listar todos os produtos");
            System.out.println("(3) Cadastrar um novo produto");
            System.out.println("(4) Buscar por palavras");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpcao: ");
            
            String entrada = scanner.nextLine().toUpperCase();
            
            switch(entrada) {
                case "1":
                    buscarProdutoPorGtin();
                    break;
                case "2":
                    menuListagemProdutos();
                    break;
                case "3":
                    cadastrarNovoProduto();
                    break;
                case "4":
                    buscarProdutosPorPalavras();
                    break;
                case "R":
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while(true);
    }

    private static void buscarProdutoPorGtin() throws Exception {
        String gtin = visaoProduto.lerGtin();
        Produto produto = controleProduto.lerPorGtin(gtin);
        
        if(produto != null) {
            menuDetalhesProduto(produto);
        } else {
            System.out.println("\nProduto nao encontrado!");
        }
    }

    private static void menuListagemProdutos() throws Exception {
        ArrayList<Produto> produtos = controleProduto.listarTodos();
        int paginaAtual = 1;
        
        do {
            int resultado = visaoProduto.exibirListagemPaginada(produtos, paginaAtual, 
                "> Inicio > Produtos > Listagem");
            
            if(resultado == -2) {
                return;  // retornar
            } else if(resultado == -3) {
                paginaAtual--;  // pagina anterior
            } else if(resultado == -4) {
                paginaAtual++;  // proxima pagina
            } else if(resultado >= 0) {
                // Produto selecionado
                Produto p = produtos.get(resultado);
                menuDetalhesProduto(p);
            }
        } while(true);
    }

    private static void menuDetalhesProduto(Produto produto) throws Exception {
        do {
            visaoProduto.mostrarProduto(produto);
            
            // Mostra em quais listas do usuario aparece
            ArrayList<ListaProduto> todasAssociacoes = controleListaProduto.buscarPorProduto(produto.getId());
            ArrayList<Lista> minhasListas = new ArrayList<>();
            int listasOutros = 0;
            
            for(ListaProduto lp : todasAssociacoes) {
                Lista lista = controleLista.buscarListaPorId(lp.getIdLista());
                if(lista != null) {
                    if(lista.getIdUsuario() == usuarioLogado.getId()) {
                        minhasListas.add(lista);
                    } else {
                        listasOutros++;
                    }
                }
            }
            
            if(!minhasListas.isEmpty()) {
                System.out.println("\nAparece nas minhas listas:");
                for(Lista l : minhasListas) {
                    System.out.println("- " + l.getNome());
                }
            }
            
            if(listasOutros > 0) {
                System.out.println("\nAparece tambem em mais " + listasOutros + " listas de outras pessoas.");
            }
            
            int opcao = visaoProduto.menuProduto(produto, listasOutros);
            
            switch(opcao) {
                case 0:
                    return;
                case 1:
                    Produto produtoAlterado = visaoProduto.lerAlteracoes(produto);
                    if(controleProduto.atualizar(produtoAlterado)) {
                        visaoProduto.exibirMensagem("Produto atualizado!");
                        produto = produtoAlterado;
                    }
                    break;
                case 2:
                    if(produto.isAtivo()) {
                        if(visaoProduto.confirmar("Deseja inativar este produto?")) {
                            if(controleProduto.inativar(produto.getId())) {
                                visaoProduto.exibirMensagem("Produto inativado!");
                                produto.setAtivo(false);
                            }
                        }
                    } else {
                        if(visaoProduto.confirmar("Deseja reativar este produto?")) {
                            if(controleProduto.reativar(produto.getId())) {
                                visaoProduto.exibirMensagem("Produto reativado!");
                                produto.setAtivo(true);
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while(true);
    }

    private static void cadastrarNovoProduto() throws Exception {
        Produto novoProduto = visaoProduto.lerNovoProduto();
        int id = controleProduto.criar(novoProduto);
        if(id > 0) {
            System.out.println("\nProduto cadastrado com sucesso!");
        } else {
            System.out.println("\nErro ao cadastrar produto!");
        }
    }
}
