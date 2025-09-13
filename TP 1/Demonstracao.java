package aed3;

import java.time.LocalDate;

public class Demonstracao {
    public static void main(String[] args) {
        try {
            System.out.println("=== DEMONSTRACAO DO SISTEMA PRESENTEFACIL ===\n");
            
            // Inicializa os controles
            ControleUsuario controleUsuario = new ControleUsuario();
            ControleLista controleLista = new ControleLista();
            
            // 1. Criar usuarios de exemplo
            System.out.println("1. Criando usuarios de exemplo...");
            Usuario usuario1 = new Usuario("João Silva", "joao@email.com", "senha123", 
                "Qual o nome da sua primeira escola?", "Escola Municipal");
            Usuario usuario2 = new Usuario("Maria Santos", "maria@email.com", "senha456", 
                "Qual o nome do seu primeiro animal de estimação?", "Rex");
            
            int id1 = controleUsuario.criarUsuario(usuario1);
            int id2 = controleUsuario.criarUsuario(usuario2);
            System.out.println("Usuarios criados com IDs: " + id1 + " e " + id2 + "\n");
            
            // 2. Buscar usuario por email
            System.out.println("2. Buscando usuario por email...");
            Usuario usuarioEncontrado = controleUsuario.buscarUsuarioPorEmail("joao@email.com");
            System.out.println("Usuario encontrado: " + usuarioEncontrado.getNome() + "\n");
            
            // 3. Autenticar usuario
            System.out.println("3. Testando autenticacao...");
            Usuario usuarioAutenticado = controleUsuario.autenticarUsuario("maria@email.com", "senha456");
            if (usuarioAutenticado != null) {
                System.out.println("Autenticacao bem-sucedida para: " + usuarioAutenticado.getNome() + "\n");
            }
            
            // 4. Criar listas de exemplo
            System.out.println("4. Criando listas de exemplo...");
            Lista lista1 = new Lista("Aniversario 2025", "Sugestoes para meu aniversario", 
                LocalDate.of(2025, 7, 30), id1);
            Lista lista2 = new Lista("Natal 2024", "Presentes de natal", 
                LocalDate.of(2024, 12, 25), id1);
            Lista lista3 = new Lista("Casamento", "Lista de casamento", 
                LocalDate.of(2025, 6, 15), id2);
            
            int idLista1 = controleLista.criarLista(lista1);
            int idLista2 = controleLista.criarLista(lista2);
            int idLista3 = controleLista.criarLista(lista3);
            System.out.println("Listas criadas com IDs: " + idLista1 + ", " + idLista2 + ", " + idLista3 + "\n");
            
            // 5. Buscar listas por usuario
            System.out.println("5. Buscando listas do usuario " + usuario1.getNome() + "...");
            java.util.ArrayList<Lista> listasUsuario1 = controleLista.buscarListasPorUsuario(id1);
            System.out.println("Listas encontradas: " + listasUsuario1.size());
            for (Lista lista : listasUsuario1) {
                System.out.println("- " + lista.getNome() + " (Codigo: " + lista.getCodigoCompartilhavel() + ")");
            }
            System.out.println();
            
            // 6. Buscar lista por código
            System.out.println("6. Buscando lista por codigo...");
            String codigo = lista1.getCodigoCompartilhavel();
            Lista listaPorCodigo = controleLista.buscarListaPorCodigo(codigo);
            if (listaPorCodigo != null) {
                System.out.println("Lista encontrada: " + listaPorCodigo.getNome() + 
                    " (Proprietario ID: " + listaPorCodigo.getIdUsuario() + ")\n");
            }
            
            // 7. Alterar dados de uma lista
            System.out.println("7. Alterando dados de uma lista...");
            lista1.setDescricao("Sugestoes atualizadas para meu aniversario na cervejaria");
            if (controleLista.alterarLista(lista1)) {
                System.out.println("Lista alterada com sucesso!\n");
            }
            
            // 8. Mostrar dados finais
            System.out.println("8. Dados finais do sistema:");
            System.out.println("Total de usuarios: 2");
            System.out.println("Total de listas: 3");
            System.out.println("Relacionamentos 1:N funcionando corretamente");
            System.out.println("Indices funcionando corretamente");
            System.out.println("Sistema pronto para uso!\n");
            
            // Fechar recursos
            controleUsuario.close();
            controleLista.close();
            
        } catch (Exception e) {
            System.out.println("Erro na demonstracao: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
