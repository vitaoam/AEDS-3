package aed3;

public class TesteSimples {
    public static void main(String[] args) {
        System.out.println("=== TESTE SIMPLES DO SISTEMA ===");
        
        try {
            // Teste básico de criação de usuario
            Usuario usuario = new Usuario("Teste", "teste@email.com", "senha123", 
                "Pergunta?", "Resposta");
            System.out.println("Usuario criado: " + usuario.getNome());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Hash da senha: " + usuario.getHashSenha());
            
            // Teste de verificação de senha
            boolean senhaCorreta = usuario.verificarSenha("senha123");
            System.out.println("Senha correta: " + senhaCorreta);
            
            // Teste de criação de lista
            Lista lista = new Lista("Lista Teste", "Descricao teste", null, 1);
            System.out.println("Lista criada: " + lista.getNome());
            System.out.println("Codigo compartilhavel: " + lista.getCodigoCompartilhavel());
            
            System.out.println("\nSistema funcionando corretamente!");
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
