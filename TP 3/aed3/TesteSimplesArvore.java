package aed3;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class TesteSimplesArvore {
    public static void main(String[] args) {
        try {
            System.out.println("Teste simples da arvore B+...");
            
            // Cria uma árvore de teste
            Constructor<ParUsuarioLista> construtor = ParUsuarioLista.class.getConstructor();
            ArvoreBMais<ParUsuarioLista> arvore = new ArvoreBMais<ParUsuarioLista>(
                construtor, 4, "dados/teste_simples.db");
            
            // Cria um par de teste
            ParUsuarioLista par = new ParUsuarioLista(2, 3);
            System.out.println("Criando par: " + par);
            
            // Insere o par
            boolean sucesso = arvore.create(par);
            System.out.println("Inserção bem-sucedida: " + sucesso);
            
            // Busca o par exato
            System.out.println("Buscando par exato (2,3)...");
            ArrayList<ParUsuarioLista> resultadoExato = arvore.read(new ParUsuarioLista(2, 3));
            System.out.println("Encontrados " + resultadoExato.size() + " pares exatos:");
            for (ParUsuarioLista p : resultadoExato) {
                System.out.println("  " + p);
            }
            
            // Busca o par
            System.out.println("Buscando pares do usuario 2...");
            ArrayList<ParUsuarioLista> resultado = arvore.read(new ParUsuarioLista(2, 0));
            System.out.println("Encontrados " + resultado.size() + " pares:");
            for (ParUsuarioLista p : resultado) {
                System.out.println("  " + p);
            }
            
            arvore.close();
            System.out.println("Teste concluido!");
            
        } catch (Exception e) {
            System.out.println("Erro no teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
