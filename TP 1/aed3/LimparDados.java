package aed3;

import java.io.File;

public class LimparDados {
    public static void main(String[] args) {
        try {
            System.out.println("Limpando dados corrompidos...");
            
            // Remove a pasta dados
            File dadosDir = new File("dados");
            if (dadosDir.exists()) {
                deleteDirectory(dadosDir);
                System.out.println("Pasta 'dados' removida com sucesso!");
            } else {
                System.out.println("Pasta 'dados' nao existe.");
            }
            
            System.out.println("Dados limpos! Agora voce pode executar o programa normalmente.");
            
        } catch (Exception e) {
            System.out.println("Erro ao limpar dados: " + e.getMessage());
        }
    }
    
    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
