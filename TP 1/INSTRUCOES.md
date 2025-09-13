# Instruções de Uso - PresenteFácil 1.0

## Como Executar o Sistema

### 1. Compilação
```bash
javac -cp . aed3/*.java
```

### 2. Execução
```bash
java aed3.Principal
```

## Funcionalidades Disponíveis

### Menu Principal
- **(1) Login**: Acessar o sistema com email e senha
- **(2) Novo usuário**: Cadastrar um novo usuário
- **(S) Sair**: Encerrar o programa

### Menu do Usuário Logado
- **(1) Meus dados**: Gerenciar informações pessoais
- **(2) Minhas listas**: Criar e gerenciar listas de presentes
- **(3) Produtos**: Funcionalidade para TP2
- **(4) Buscar lista**: Buscar listas de outras pessoas por código
- **(S) Sair**: Fazer logout

### Menu de Dados do Usuário
- **(1) Visualizar dados**: Ver informações pessoais
- **(2) Alterar nome**: Modificar nome
- **(3) Alterar e-mail**: Modificar email
- **(4) Alterar senha**: Modificar senha
- **(5) Alterar pergunta secreta**: Modificar pergunta de recuperação
- **(6) Alterar resposta secreta**: Modificar resposta de recuperação
- **(7) Excluir usuário**: Remover conta
- **(R) Retornar**: Voltar ao menu anterior

### Menu de Listas
- **(1) Ver minhas listas**: Visualizar e gerenciar listas existentes
- **(2) Nova lista**: Criar nova lista de presentes
- **(R) Retornar**: Voltar ao menu anterior

### Menu de Detalhes da Lista
- **(1) Gerenciar produtos**: Funcionalidade para TP2
- **(2) Alterar dados da lista**: Modificar informações da lista
- **(3) Excluir lista**: Remover lista
- **(R) Retornar**: Voltar ao menu anterior

## Exemplo de Uso

1. **Primeiro uso**: Crie um novo usuário
2. **Login**: Acesse com email e senha
3. **Criar lista**: Vá em "Minhas listas" > "Nova lista"
4. **Compartilhar**: Use o código gerado para compartilhar a lista
5. **Buscar lista**: Use "Buscar lista" para ver listas de outras pessoas

## Estrutura de Dados

O sistema utiliza:
- **Arquivo Indexado**: Para armazenamento principal
- **Hash Extensível**: Para busca rápida por email e código
- **Árvore B+**: Para relacionamento 1:N entre usuários e listas

## Observações

- Senhas são armazenadas com hash SHA-256
- Códigos compartilháveis são gerados automaticamente
- Listas são ordenadas alfabeticamente
- Sistema preparado para expansão no TP2
