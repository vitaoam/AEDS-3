# Relatório está anexado em pdf na página inicial na formatação SBC

# PresenteFácil 1.0 - Sistema de Gestão de Listas de Presentes

## Participantes do Trabalho Prático
- Vitor Alexandre Moreira Amaral
- Pedro Henrique Bellone  
- Gustavo Vinicius Elias Souza Silva

## Resumo

Este trabalho apresenta a implementação do sistema PresenteFácil 1.0, um sistema para gestão de listas de sugestões de presentes que as pessoas gostariam de receber. O sistema permite que usuários cadastrados criem múltiplas listas de presentes e compartilhem essas listas com outras pessoas através de códigos únicos. A implementação utiliza estruturas de dados avançadas como arquivos indexados, tabelas hash extensíveis e árvores B+ para garantir eficiência nas operações de busca e armazenamento.

## Abstract

This work presents the implementation of the PresenteFácil 1.0 system, a gift suggestion list management system that allows people to create and share gift lists. The system enables registered users to create multiple gift lists and share them with others through unique codes. The implementation uses advanced data structures such as indexed files, extensible hash tables, and B+ trees to ensure efficiency in search and storage operations.

## 1. Introdução

O sistema PresenteFácil 1.0 foi desenvolvido como parte do primeiro trabalho prático da disciplina de Algoritmos e Estruturas de Dados III. O objetivo é criar um sistema completo de gestão de listas de presentes que demonstre o uso eficiente de estruturas de dados persistentes e índices para otimizar operações de busca e armazenamento.

## 2. Descrição do Sistema

### 2.1 Funcionalidades Implementadas

#### 2.1.1 Sistema de Autenticação
- **Login**: Acesso ao sistema mediante email e senha
- **Cadastro**: Criação de novos usuários com validação de email único
- **Recuperação de Senha**: Sistema baseado em pergunta e resposta secreta
- **Hash de Senha**: Senhas são armazenadas usando SHA-256

#### 2.1.2 Gestão de Usuários
- **CRUD Completo**: Criar, ler, atualizar e excluir usuários
- **Índice por Email**: Busca rápida de usuários por email usando Hash Extensível
- **Validações**: Verificação de email único e integridade dos dados

#### 2.1.3 Gestão de Listas
- **CRUD Completo**: Criar, ler, atualizar e excluir listas
- **Código Compartilhável**: Geração automática de códigos alfanuméricos de 10 caracteres
- **Relacionamento 1:N**: Cada lista pertence a um usuário, um usuário pode ter várias listas
- **Índice por Código**: Busca rápida de listas por código compartilhável

### 2.2 Estruturas de Dados Utilizadas

#### 2.2.1 Arquivo Indexado
Base para armazenamento de registros com:
- Lápide (byte indicando se o registro é válido)
- Indicador de tamanho do registro
- Vetor de bytes com os dados da entidade

#### 2.2.2 Hash Extensível
Utilizado para índices diretos:
- Mapeamento (email.hashCode() → idUsuario)
- Mapeamento (codigoCompartilhavel.hashCode() → idLista)

#### 2.2.3 Árvore B+
Utilizada para relacionamento 1:N:
- Armazenamento de pares (idUsuario, idLista)
- Suporte a consultas por faixa de valores

## 3. Arquitetura do Sistema

### 3.1 Padrão MVC

O sistema segue rigorosamente o padrão Model-View-Controller:

#### 3.1.1 Model (Modelo)
- **Entidades**: `Usuario`, `Lista`, `ParUsuarioLista`, `ParEmailID`, `ParCodigoID`
- **Classes Base**: `Arquivo`, `HashExtensivel`, `ArvoreBMais`
- **Interfaces**: `Registro`, `RegistroHashExtensivel`, `RegistroArvoreBMais`

#### 3.1.2 View (Visão)
- **VisaoUsuario**: Interface de usuário para operações de usuário
- **VisaoLista**: Interface de usuário para operações de lista

#### 3.1.3 Controller (Controle)
- **ControleUsuario**: Lógica de negócio para usuários
- **ControleLista**: Lógica de negócio para listas

### 3.2 Classes Implementadas

#### 3.2.1 Entidades
- `Usuario`: Representa um usuário do sistema com atributos nome, email, hash da senha, pergunta e resposta secreta
- `Lista`: Representa uma lista de presentes com nome, descrição, datas e código compartilhável
- `ParUsuarioLista`: Par (idUsuario, idLista) para relacionamento 1:N
- `ParEmailID`: Par (email, id) para índice de usuários
- `ParCodigoID`: Par (codigo, id) para índice de listas

#### 3.2.2 Classes Base (aed3)
- `Arquivo`: CRUD genérico com índice direto
- `HashExtensivel`: Tabela hash extensível
- `ArvoreBMais`: Árvore B+ para relacionamentos
- `Registro`: Interface para registros
- `RegistroHashExtensivel`: Interface para hash extensível
- `RegistroArvoreBMais`: Interface para árvore B+

## 4. Interface do Usuário

O sistema apresenta uma interface textual intuitiva com:

1. **Menu Principal**: Login, cadastro e saída
2. **Menu do Usuário Logado**: Acesso a dados pessoais, listas e busca
3. **Menu de Listas**: Visualização, criação e gerenciamento de listas
4. **Menu de Detalhes da Lista**: Edição, exclusão e visualização de códigos

## 5. Operações Especiais Implementadas

1. **Geração de Código Compartilhável**: Algoritmo que gera códigos únicos de 10 caracteres usando NanoID
2. **Validação de Integridade**: Verificação de relacionamentos antes de exclusões
3. **Ordenação de Listas**: Listas são apresentadas em ordem alfabética
4. **Breadcrumb**: Navegação contextual mostrando localização no sistema
5. **Validação de Datas**: Formato brasileiro (dd/mm/aaaa) com tratamento de erros

## 6. Checklist de Requisitos

### 6.1 Há um CRUD de usuários (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?
**SIM** - Implementado na classe `ControleUsuario` com:
- Arquivo indexado para armazenamento
- Hash Extensível para busca por email
- Operações CRUD completas

### 6.2 Há um CRUD de listas (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?
**SIM** - Implementado na classe `ControleLista` com:
- Arquivo indexado para armazenamento
- Hash Extensível para busca por código compartilhável
- Operações CRUD completas

### 6.3 As listas de presentes estão vinculadas aos usuários usando o idUsuario como chave estrangeira?
**SIM** - Cada lista possui o campo `idUsuario` que referencia o usuário proprietário

### 6.4 Há uma árvore B+ que registre o relacionamento 1:N entre usuários e listas?
**SIM** - Implementada usando a classe `ParUsuarioLista` que armazena pares (idUsuario, idLista)

### 6.5 Há uma visualização das listas de outras pessoas por meio de um código NanoID?
**SIM** - Implementada busca por código compartilhável de 10 caracteres alfanuméricos

### 6.6 O trabalho compila corretamente?
**SIM** - Todas as classes compilam sem erros

### 6.7 O trabalho está completo e funcionando sem erros de execução?
**SIM** - Sistema funcional com todas as operações implementadas

### 6.8 O trabalho é original e não a cópia de um trabalho de outro grupo?
**SIM** - Implementação original baseada nos requisitos fornecidos

## 7. Estrutura de Arquivos

```
NOSSO TRABALHO TP 1/
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

## 8. Como Executar

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

## 9. Conclusões

O sistema PresenteFácil 1.0 foi implementado com sucesso, demonstrando o uso eficiente de estruturas de dados persistentes e índices. Todas as funcionalidades solicitadas foram implementadas e testadas, incluindo:

- Sistema completo de autenticação com hash de senhas
- CRUD completo para usuários e listas
- Relacionamento 1:N implementado com árvore B+
- Busca eficiente por email e código compartilhável
- Interface textual intuitiva seguindo padrão MVC

O sistema está preparado para o TP2, onde será implementada a gestão de produtos nas listas.

## 10. Referências

- Material de aula sobre Arquivos Indexados
- Material de aula sobre Tabelas Hash Extensíveis  
- Material de aula sobre Árvores B+
- Documentação da biblioteca NanoID para geração de códigos únicos

