package aed3;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ControleUsuario {
    private Arquivo<Usuario> arquivoUsuario;
    private HashExtensivel<ParEmailID> indiceEmail;
    private VisaoUsuario visao;

    public ControleUsuario() throws Exception {
        Constructor<Usuario> construtorUsuario = Usuario.class.getConstructor();
        this.arquivoUsuario = new Arquivo<>("usuarios", construtorUsuario);
        
        Constructor<ParEmailID> construtorParEmailID = ParEmailID.class.getConstructor();
        this.indiceEmail = new HashExtensivel<>(
            construtorParEmailID,
            4,
            ".\\dados\\usuarios\\usuarios_email.d.db",
            ".\\dados\\usuarios\\usuarios_email.c.db"
        );
        
        this.visao = new VisaoUsuario();
    }

    public int criarUsuario(Usuario usuario) throws Exception {
        // Verifica se ja existe usuario com este email
        if (buscarUsuarioPorEmail(usuario.getEmail()) != null) {
            throw new Exception("Já existe um usuario cadastrado com este email!");
        }

        // Cria o usuario
        int id = arquivoUsuario.create(usuario);
        
        // Atualiza o índice de email
        indiceEmail.create(new ParEmailID(usuario.getEmail(), id));
        
        visao.mostraMensagem("Usuario criado com sucesso! ID: " + id);
        return id;
    }

    public Usuario buscarUsuarioPorId(int id) throws Exception {
        return arquivoUsuario.read(id);
    }

    public Usuario buscarUsuarioPorEmail(String email) throws Exception {
        ParEmailID parEmailID = indiceEmail.read(email.hashCode());
        if (parEmailID != null) {
            return arquivoUsuario.read(parEmailID.getId());
        }
        return null;
    }

    public Usuario autenticarUsuario(String email, String senha) throws Exception {
        Usuario usuario = buscarUsuarioPorEmail(email);
        if (usuario != null && usuario.verificarSenha(senha)) {
            return usuario;
        }
        return null;
    }

    public boolean alterarUsuario(Usuario usuario) throws Exception {
        // Se o email foi alterado, precisa atualizar o índice
        Usuario usuarioAntigo = arquivoUsuario.read(usuario.getId());
        if (usuarioAntigo != null && !usuarioAntigo.getEmail().equals(usuario.getEmail())) {
            // Verifica se ja existe outro usuario com o novo email
            if (buscarUsuarioPorEmail(usuario.getEmail()) != null) {
                throw new Exception("Já existe um usuario cadastrado com este email!");
            }
            
            // Remove o índice antigo e cria o novo
            indiceEmail.delete(usuarioAntigo.getEmail().hashCode());
            indiceEmail.create(new ParEmailID(usuario.getEmail(), usuario.getId()));
        }
        
        return arquivoUsuario.update(usuario);
    }

    public boolean excluirUsuario(int id) throws Exception {
        Usuario usuario = arquivoUsuario.read(id);
        if (usuario != null) {
            // Remove do índice de email
            indiceEmail.delete(usuario.getEmail().hashCode());
            
            // Exclui o usuario
            return arquivoUsuario.delete(id);
        }
        return false;
    }

    public Usuario recuperarSenha(String email, String perguntaSecreta, String respostaSecreta) throws Exception {
        Usuario usuario = buscarUsuarioPorEmail(email);
        if (usuario != null && 
            usuario.getPerguntaSecreta().equals(perguntaSecreta) && 
            usuario.getRespostaSecreta().equals(respostaSecreta)) {
            return usuario;
        }
        return null;
    }

    public void menuUsuario(Usuario usuario) throws Exception {
        int opcao;
        do {
            visao.mostraMenuUsuario();
            opcao = visao.leOpcaoMenuUsuario();
            
            switch (opcao) {
                case 1: // Visualizar dados
                    visao.mostraUsuario(usuario);
                    break;
                    
                case 2: // Alterar nome
                    String novoNome = visao.leNovoNome();
                    usuario.setNome(novoNome);
                    if (alterarUsuario(usuario)) {
                        visao.mostraMensagem("Nome alterado com sucesso!");
                    } else {
                        visao.mostraErro("Erro ao alterar nome!");
                    }
                    break;
                    
                case 3: // Alterar email
                    String novoEmail = visao.leNovoEmail();
                    usuario.setEmail(novoEmail);
                    if (alterarUsuario(usuario)) {
                        visao.mostraMensagem("E-mail alterado com sucesso!");
                    } else {
                        visao.mostraErro("Erro ao alterar email!");
                    }
                    break;
                    
                case 4: // Alterar senha
                    String novaSenha = visao.leNovaSenha();
                    usuario.alterarSenha(novaSenha);
                    if (alterarUsuario(usuario)) {
                        visao.mostraMensagem("Senha alterada com sucesso!");
                    } else {
                        visao.mostraErro("Erro ao alterar senha!");
                    }
                    break;
                    
                case 5: // Alterar pergunta secreta
                    String novaPergunta = visao.leNovaPerguntaSecreta();
                    usuario.setPerguntaSecreta(novaPergunta);
                    if (alterarUsuario(usuario)) {
                        visao.mostraMensagem("Pergunta secreta alterada com sucesso!");
                    } else {
                        visao.mostraErro("Erro ao alterar pergunta secreta!");
                    }
                    break;
                    
                case 6: // Alterar resposta secreta
                    String novaResposta = visao.leNovaRespostaSecreta();
                    usuario.setRespostaSecreta(novaResposta);
                    if (alterarUsuario(usuario)) {
                        visao.mostraMensagem("Resposta secreta alterada com sucesso!");
                    } else {
                        visao.mostraErro("Erro ao alterar resposta secreta!");
                    }
                    break;
                    
                case 7: // Excluir usuario
                    if (visao.confirmaExclusao(usuario.getNome())) {
                        if (excluirUsuario(usuario.getId())) {
                            visao.mostraMensagem("Usuario excluido com sucesso!");
                            return; // Sai do menu
                        } else {
                            visao.mostraErro("Erro ao excluir usuario!");
                        }
                    }
                    break;
                    
                case 0:
                    visao.mostraErro("Opcao invalida!");
                    break;
            }
        } while (opcao != -1);
    }

    public void close() throws Exception {
        arquivoUsuario.close();
        indiceEmail.close();
        visao.close();
    }
}
