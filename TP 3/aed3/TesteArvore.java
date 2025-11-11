package aed3;

import java.util.ArrayList;

public class TesteArvore {
    public static void main(String[] args) {
        try {
            System.out.println("Testando arvore B+...");
            
            // Cria uma árvore de teste
            ArvoreBMais<ParUsuarioLista> arvore = new ArvoreBMais<ParUsuarioLista>(
                ParUsuarioLista.class.getConstructor(), 0, "dados/teste_arvore.db");
            
            // Cria alguns pares de teste
            ParUsuarioLista par1 = new ParUsuarioLista(1, 1);
            ParUsuarioLista par2 = new ParUsuarioLista(1, 2);
            ParUsuarioLista par3 = new ParUsuarioLista(2, 3);
            
            // Insere os pares
            System.out.println("Inserindo par1: " + par1);
            arvore.create(par1);
            
            System.out.println("Inserindo par2: " + par2);
            arvore.create(par2);
            
            System.out.println("Inserindo par3: " + par3);
            arvore.create(par3);
            
            // Busca todos os pares do usuário 1
            System.out.println("\nBuscando pares do usuario 1...");
            ArrayList<ParUsuarioLista> resultado = arvore.read(new ParUsuarioLista(1, 0));
            
            System.out.println("Encontrados " + resultado.size() + " pares:");
            for (ParUsuarioLista par : resultado) {
                System.out.println("  " + par);
            }
            
            arvore.close();
            System.out.println("\nTeste concluido com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro no teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
