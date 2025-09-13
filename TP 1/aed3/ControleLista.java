package aed3;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ControleLista {
    private Arquivo<Lista> arquivoLista;
    private HashExtensivel<ParCodigoID> indiceCodigo;
    private ArvoreBMais<ParUsuarioLista> arvoreUsuarioLista;
    private VisaoLista visao;

    public ControleLista() throws Exception {
        Constructor<Lista> construtorLista = Lista.class.getConstructor();
        this.arquivoLista = new Arquivo<>("listas", construtorLista);
        
        Constructor<ParCodigoID> construtorParCodigoID = ParCodigoID.class.getConstructor();
        this.indiceCodigo = new HashExtensivel<>(
            construtorParCodigoID,
            4,
            ".\\dados\\listas\\listas_codigo.d.db",
            ".\\dados\\listas\\listas_codigo.c.db"
        );
        
        Constructor<ParUsuarioLista> construtorParUsuarioLista = ParUsuarioLista.class.getConstructor();
        this.arvoreUsuarioLista = new ArvoreBMais<ParUsuarioLista>(
            construtorParUsuarioLista,
            4,
            ".\\dados\\listas\\arvore_usuario_lista.db"
        );
        
        this.visao = new VisaoLista();
    }

    public int criarLista(Lista lista) throws Exception {
        // Verifica se o codigo compartilhavel ja existe
        String codigo = lista.getCodigoCompartilhavel();
        while (buscarListaPorCodigo(codigo) != null) {
            // Gera um novo codigo se ja existir
            lista.setCodigoCompartilhavel(gerarNovoCodigo());
            codigo = lista.getCodigoCompartilhavel();
        }
        
        // Cria a lista
        int id = arquivoLista.create(lista);
        
        // Atualiza o indice de codigo compartilhavel
        indiceCodigo.create(new ParCodigoID(lista.getCodigoCompartilhavel(), id));
        
        // Atualiza a arvore B+ para o relacionamento 1:N
        arvoreUsuarioLista.create(new ParUsuarioLista(lista.getIdUsuario(), id));
        
        visao.mostraMensagem("Lista criada com sucesso! ID: " + id);
        return id;
    }
    
    private String gerarNovoCodigo() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = (int) (Math.random() * caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }

    public Lista buscarListaPorId(int id) throws Exception {
        return arquivoLista.read(id);
    }

    public Lista buscarListaPorCodigo(String codigo) throws Exception {
        try {
            ParCodigoID parCodigoID = indiceCodigo.read(codigo.hashCode());
            if (parCodigoID != null) {
                return arquivoLista.read(parCodigoID.getId());
            }
        } catch (Exception e) {
            // Se houver erro na busca, retorna null
        }
        return null;
    }

    public ArrayList<Lista> buscarListasPorUsuario(int idUsuario) throws Exception {
        ArrayList<Lista> listas = new ArrayList<>();
        
        try {
            // Busca por range usando diferentes estratégias
            ArrayList<ParUsuarioLista> pares = new ArrayList<>();
            
            // Tenta buscar com diferentes valores de idLista
            for (int i = 0; i < 100; i++) { // Busca até ID 100
                try {
                    ArrayList<ParUsuarioLista> resultado = arvoreUsuarioLista.read(new ParUsuarioLista(idUsuario, i));
                    for (ParUsuarioLista par : resultado) {
                        if (par.getIdUsuario() == idUsuario && !pares.contains(par)) {
                            pares.add(par);
                        }
                    }
                } catch (Exception e) {
                    // Ignora erros de busca individual
                }
            }
            
            for (ParUsuarioLista par : pares) {
                Lista lista = arquivoLista.read(par.getIdLista());
                if (lista != null) {
                    listas.add(lista);
                }
            }
            
            // Ordena as listas por nome
            Collections.sort(listas, new Comparator<Lista>() {
                @Override
                public int compare(Lista l1, Lista l2) {
                    return l1.getNome().compareToIgnoreCase(l2.getNome());
                }
            });
            
        } catch (Exception e) {
            // Se houver erro na busca, retorna lista vazia
        }
        
        return listas;
    }

    public boolean alterarLista(Lista lista) throws Exception {
        return arquivoLista.update(lista);
    }

    public boolean excluirLista(int id) throws Exception {
        Lista lista = arquivoLista.read(id);
        if (lista != null) {
            // Remove do índice de codigo
            indiceCodigo.delete(lista.getCodigoCompartilhavel().hashCode());
            
            // Remove da árvore B+
            arvoreUsuarioLista.delete(new ParUsuarioLista(lista.getIdUsuario(), id));
            
            // Exclui a lista
            return arquivoLista.delete(id);
        }
        return false;
    }

    public void menuListas(Usuario usuario) throws Exception {
        int opcao;
        do {
            ArrayList<Lista> listas = buscarListasPorUsuario(usuario.getId());
            visao.mostraBreadcrumb("Inicio > Minhas listas");
            visao.mostraListas(listas);
            visao.mostraMenuListas();
            opcao = visao.leOpcaoMenuListas();
            
            switch (opcao) {
                case 1: // Nova lista
                    Lista novaLista = visao.leLista(usuario.getId());
                    criarLista(novaLista);
                    break;
                    
                case 0:
                    visao.mostraErro("Opcao invalida!");
                    break;
            }
        } while (opcao != -1);
    }

    public void menuDetalhesLista(Lista lista) throws Exception {
        int opcao;
        do {
            visao.mostraBreadcrumb("Inicio > Minhas listas > " + lista.getNome());
            visao.mostraLista(lista);
            visao.mostraMenuDetalhesLista();
            opcao = visao.leOpcaoMenuDetalhesLista();
            
            switch (opcao) {
                case 1: // Gerenciar produtos da lista
                    visao.mostraMensagem("Funcionalidade sera implementada no TP2!");
                    break;
                    
                case 2: // Alterar dados da lista
                    Lista listaAlterada = visao.leDadosAlteracaoLista(lista);
                    if (alterarLista(listaAlterada)) {
                        visao.mostraMensagem("Lista alterada com sucesso!");
                        lista = listaAlterada; // Atualiza a referência
                    } else {
                        visao.mostraErro("Erro ao alterar lista!");
                    }
                    break;
                    
                case 3: // Excluir lista
                    if (visao.confirmaExclusaoLista(lista.getNome())) {
                        if (excluirLista(lista.getId())) {
                            visao.mostraMensagem("Lista excluida com sucesso!");
                            return; // Sai do menu
                        } else {
                            visao.mostraErro("Erro ao excluir lista!");
                        }
                    }
                    break;
                    
                case 0:
                    visao.mostraErro("Opcao invalida!");
                    break;
            }
        } while (opcao != -1);
    }

    public void menuSelecaoLista(Usuario usuario) throws Exception {
        ArrayList<Lista> listas = buscarListasPorUsuario(usuario.getId());
        if (listas.isEmpty()) {
            visao.mostraMensagem("Você ainda nao possui listas cadastradas.");
            return;
        }
        
        visao.mostraBreadcrumb("Inicio > Minhas listas");
        visao.mostraListas(listas);
        
        int numeroLista = visao.leNumeroLista(listas.size());
        if (numeroLista >= 0) {
            Lista listaSelecionada = listas.get(numeroLista);
            menuDetalhesLista(listaSelecionada);
        } else {
            visao.mostraErro("Numero de lista inválido!");
        }
    }

    public void buscarListaPorCodigo() throws Exception {
        visao.mostraBreadcrumb("Inicio > Buscar lista");
        String codigo = visao.leCodigoCompartilhavel();
        Lista lista = buscarListaPorCodigo(codigo);
        
        if (lista != null) {
            visao.mostraMensagem("Lista encontrada!");
            visao.mostraLista(lista);
        } else {
            visao.mostraErro("Lista não encontrada com o codigo informado!");
        }
    }

    public void close() throws Exception {
        arquivoLista.close();
        indiceCodigo.close();
        arvoreUsuarioLista.close();
        visao.close();
    }
}
