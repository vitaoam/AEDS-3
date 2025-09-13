# PresenteFácil 1.0 - Sistema de Gestão de Listas de Presentes

## Participantes do Trabalho Prático
- [Vitor Alexandre Moreira Amaral]
- [Pedro Henrique Bellone]
- [Gustavo Vinicius Elias Souza Silva]
## Descrição do Sistema

O PresenteFácil é um sistema para gestão de listas de sugestões de presentes que as pessoas gostariam de receber. O sistema permite que usuários cadastrados criem múltiplas listas de presentes, cada uma com produtos específicos, e compartilhem essas listas com outras pessoas através de códigos únicos.

### Funcionalidades Implementadas

#### 1. Sistema de Autenticação
- **Login**: Acesso ao sistema mediante email e senha
- **Cadastro**: Criação de novos usuários com validação de email único
- **Recuperação de Senha**: Sistema baseado em pergunta e resposta secreta
- **Hash de Senha**: Senhas são armazenadas usando SHA-256

#### 2. Gestão de Usuários
- **CRUD Completo**: Criar, ler, atualizar e excluir usuários
- **Índice por Email**: Busca rápida de usuários por email usando Hash Extensível
- **Validações**: Verificação de email único e integridade dos dados

#### 3. Gestão de Listas
- **CRUD Completo**: Criar, ler, atualizar e excluir listas
- **Código Compartilhável**: Geração automática de códigos alfanuméricos de 10 caracteres
- **Relacionamento 1:N**: Cada lista pertence a um usuário, um usuário pode ter várias listas
- **Índice por Código**: Busca rápida de listas por código compartilhável

#### 4. Estruturas de Dados Utilizadas
- **Arquivo Indexado**: Base para armazenamento de registros
- **Hash Extensível**: Índices para busca por email e código compartilhável
- **Árvore B+**: Relacionamento 1:N entre usuários e listas
- **Lista de Excluídos**: Reutilização de espaço em arquivos

### Classes Implementadas

#### Entidades
- `Usuario`: Representa um usuário do sistema
- `Lista`: Representa uma lista de presentes
- `ParUsuarioLista`: Par (idUsuario, idLista) para relacionamento 1:N
- `ParEmailID`: Par (email, id) para índice de usuários
- `ParCodigoID`: Par (codigo, id) para índice de listas

#### Classes Base (aed3)
- `Arquivo`: CRUD genérico com índice direto
- `HashExtensivel`: Tabela hash extensível
- `ArvoreBMais`: Árvore B+ para relacionamentos
- `Registro`: Interface para registros
- `RegistroHashExtensivel`: Interface para hash extensível
- `RegistroArvoreBMais`: Interface para árvore B+

#### Visão (MVC)
- `VisaoUsuario`: Interface de usuário para operações de usuário
- `VisaoLista`: Interface de usuário para operações de lista

#### Controle (MVC)
- `ControleUsuario`: Lógica de negócio para usuários
- `ControleLista`: Lógica de negócio para listas

#### Programa Principal
- `Principal`: Interface principal do sistema

### Interface do Usuário

O sistema apresenta uma interface textual intuitiva com:

1. **Menu Principal**: Login, cadastro e saída
2. **Menu do Usuário Logado**: Acesso a dados pessoais, listas e busca
3. **Menu de Listas**: Visualização, criação e gerenciamento de listas
4. **Menu de Detalhes da Lista**: Edição, exclusão e visualização de códigos

### Operações Especiais Implementadas

1. **Geração de Código Compartilhável**: Algoritmo que gera códigos únicos de 10 caracteres
2. **Validação de Integridade**: Verificação de relacionamentos antes de exclusões
3. **Ordenação de Listas**: Listas são apresentadas em ordem alfabética
4. **Breadcrumb**: Navegação contextual mostrando localização no sistema
5. **Validação de Datas**: Formato brasileiro (dd/mm/aaaa) com tratamento de erros

## Checklist de Requisitos

### ✅ Há um CRUD de usuários (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?
**SIM** - Implementado na classe `ControleUsuario` com:
- Arquivo indexado para armazenamento
- Hash Extensível para busca por email
- Operações CRUD completas

### ✅ Há um CRUD de listas (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?
**SIM** - Implementado na classe `ControleLista` com:
- Arquivo indexado para armazenamento
- Hash Extensível para busca por código compartilhável
- Operações CRUD completas

### ✅ As listas de presentes estão vinculadas aos usuários usando o idUsuario como chave estrangeira?
**SIM** - Cada lista possui o campo `idUsuario` que referencia o usuário proprietário

### ✅ Há uma árvore B+ que registre o relacionamento 1:N entre usuários e listas?
**SIM** - Implementada usando a classe `ParUsuarioLista` que armazena pares (idUsuario, idLista)

### ✅ Há uma visualização das listas de outras pessoas por meio de um código NanoID?
**SIM** - Implementada busca por código compartilhável de 10 caracteres alfanuméricos

### ✅ O trabalho compila corretamente?
**SIM** - Todas as classes compilam sem erros

### ✅ O trabalho está completo e funcionando sem erros de execução?
**SIM** - Sistema funcional com todas as operações implementadas

### ✅ O trabalho é original e não a cópia de um trabalho de outro grupo?
**SIM** - Implementação original baseada nos requisitos fornecidos

## Estrutura de Arquivos

```
NOSSO TRABALHO/
├── aed3/
│   ├── Arquivo.java
│   ├── ArvoreBMais.java
│   ├── HashExtensivel.java
│   ├── ParIDEndereco.java
│   ├── Registro.java
│   ├── RegistroArvoreBMais.java
│   └── RegistroHashExtensivel.java
├── ControleLista.java
├── ControleUsuario.java
├── Lista.java
├── ParCodigoID.java
├── ParEmailID.java
├── ParUsuarioLista.java
├── Principal.java
├── README.md
├── Usuario.java
├── VisaoLista.java
└── VisaoUsuario.java
```

## Como Executar

1. Compilar o projeto:
   ```bash
   javac -cp . *.java aed3/*.java
   ```

2. Executar o programa:
   ```bash
   java Principal
   ```

3. Seguir as instruções da interface para:
   - Criar um novo usuário
   - Fazer login
   - Gerenciar listas de presentes

## Observações

- O sistema está preparado para o TP2, onde será implementada a gestão de produtos
- Todas as estruturas de dados solicitadas foram implementadas
- O padrão MVC foi seguido rigorosamente
- O sistema é robusto com tratamento de erros e validações
