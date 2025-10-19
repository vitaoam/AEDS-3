# Relatório está anexado em pdf na página inicial na formatação SBC

# Link do video publicado no youtube https://youtu.be/UgL8zFW7V-0

# PresenteFácil 2.0 - Sistema de Gestão de Listas de Presentes (com Produtos)

## Participantes do Trabalho Prático
- Vitor Alexandre Moreira Amaral
- Pedro Henrique Bellone  
- Gustavo Vinicius Elias Souza Silva

## Resumo

Este trabalho apresenta a implementação do sistema PresenteFácil 2.0, evolução direta do TP1. Nesta sprint foram adicionadas as funcionalidades de gestão de produtos e a associação N:N entre listas e produtos. Os produtos são identificados por GTIN‑13, podem ser pesquisados por código, listados com paginação e associados às listas com quantidade e observações. Foram utilizados arquivos indexados, tabelas hash extensíveis e árvores B+ para garantir eficiência e integridade.

## Abstract

This work presents PresenteFácil 2.0, which extends TP1 by adding product management and an N:N relationship between lists and products. Products are identified by GTIN‑13, can be searched by code, listed with pagination, and associated to lists with quantity and notes. Indexed files, extensible hash tables and B+ trees were used to ensure efficiency and integrity.

## 1. Introdução

O PresenteFácil 2.0 mantém todo o escopo do TP1 (usuários, listas, busca por código) e adiciona:

- CRUD de produtos (GTIN‑13, nome, descrição, ativo/inativo)
- Relacionamento N:N Lista–Produto com quantidade e observações
- Busca por GTIN‑13 e listagem paginada (10 itens/página)
- Inativação/reativação de produtos
- Garantia de integridade (sem duplicidade e remoção em cascata ao excluir lista)

## 2. Descrição do Sistema

### 2.1 Funcionalidades Implementadas

#### 2.1.1 Sistema de Produtos
- **Cadastro/Alteração** de produtos (GTIN‑13 único)
- **Busca por GTIN‑13** e **listagem paginada** por nome
- **Inativar/Reativar** produto (soft delete)

#### 2.1.2 Associação ListaProduto (N:N)
- **Adicionar** produto à lista com quantidade e observações
- **Alterar** quantidade/observações
- **Remover** produto da lista
- **Integridade**: evita produto duplicado na mesma lista; remove associações ao excluir lista

#### 2.1.3 Funcionalidades do TP1 mantidas
- Usuários, autenticação, recuperação de senha
- Listas com código compartilhável, ordenação por nome e busca por código

### 2.2 Estruturas de Dados Utilizadas

#### 2.2.1 Arquivo Indexado
Base para armazenamento de `Usuario`, `Lista`, `Produto` e `ListaProduto`.

#### 2.2.2 Hash Extensível
Índices diretos:
- `(email.hashCode() → idUsuario)`
- `(codigoCompartilhavel.hashCode() → idLista)`
- `(gtin13.hashCode() → idProduto)`

#### 2.2.3 Árvore B+
Índices indiretos:
- `ParUsuarioLista` (idUsuario, idLista) – relacionamento 1:N do TP1
- `ParListaProduto` (idLista, idListaProduto) – listas → associações
- `ParProdutoLista` (idProduto, idListaProduto) – produtos → associações

## 3. Arquitetura do Sistema

### 3.1 Padrão MVC

#### 3.1.1 Model (Modelo)
- **Entidades**: `Usuario`, `Lista`, `Produto`, `ListaProduto`
- **Pares/Índices**: `ParUsuarioLista`, `ParEmailID`, `ParCodigoID`, `ParGtinID`, `ParListaProduto`, `ParProdutoLista`
- **Classes Base**: `Arquivo`, `HashExtensivel`, `ArvoreBMais`
- **Interfaces**: `Registro`, `RegistroHashExtensivel`, `RegistroArvoreBMais`

#### 3.1.2 View (Visão)
- `VisaoUsuario`, `VisaoLista`, `VisaoProduto`, `VisaoListaComProdutos`

#### 3.1.3 Controller (Controle)
- `ControleUsuario`, `ControleLista`, `ControleProduto`, `ControleListaProduto`

### 3.2 Classes Implementadas

#### 3.2.1 Entidades
- `Produto`: produto do sistema
- `ListaProduto`: associação N:N com quantidade e observações
- `ParGtinID`, `ParListaProduto`, `ParProdutoLista`: índices

#### 3.2.2 Controllers
- `ControleProduto`: CRUD + inativação de produtos
- `ControleListaProduto`: gestão das associações N:N

#### 3.2.3 Views
- `VisaoProduto`: leitura/mostra de produtos + paginação
- `VisaoListaComProdutos`: menus de gerenciamento em listas

## 4. Interface do Usuário

O sistema apresenta uma interface textual com menus:

1. **Menu Principal**: Login, cadastro e saída
2. **Menu do Usuário Logado**: Meus dados, Minhas listas, Produtos, Buscar lista
3. **Minhas listas**: Visualização/edição/exclusão de lista; Gerenciar produtos
4. **Produtos**: Buscar por GTIN‑13, listar todos (paginado) e cadastrar produto

## 5. Operações Especiais Implementadas

1. **Geração de Código Compartilhável** (TP1) – 10 caracteres
2. **Inativação de Produtos** – não exclui fisicamente
3. **Listagem Paginada** – 10 por página, ordenação por nome
4. **Integridade N:N** – sem duplicidade e remoção em cascata ao excluir lista

## 6. Checklist de Requisitos

### 6.1 Há um CRUD de produtos (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?
**SIM** – `ControleProduto` com Hash por GTIN‑13 e operações completas.

### 6.2 Há um CRUD da entidade de associação ListaProduto (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?
**SIM** – `ControleListaProduto` com árvores B+ bidirecionais e operações completas.

### 6.3 A visão de produtos está corretamente implementada e permite consultas às listas em que o produto aparece?
**SIM** – mostra listas do usuário e quantidade de listas de outras pessoas; permite inativar/reativar e editar dados.

### 6.4 A visão de listas funciona corretamente e permite a gestão dos produtos na lista?
**SIM** – adicionar, alterar quantidade/observações e remover produto.

### 6.5 A integridade do relacionamento entre listas e produtos está mantida em todas as operações?
**SIM** – valida duplicidade, mantém índices e remove associações ao excluir lista.

### 6.6 O trabalho compila corretamente?
**SIM** – compilação sem erros.

### 6.7 O trabalho está completo e funcionando sem erros de execução?
**SIM** – menus e operações testados.

### 6.8 O trabalho é original e não a cópia de um trabalho de outro grupo?
**SIM** – implementação própria baseada nos requisitos.

## 7. Estrutura de Arquivos

```
NOSSO TRABALHO TP 2/
├── aed3/
│   ├── Arquivo.java
│   ├── ArvoreBMais.java
│   ├── HashExtensivel.java
│   ├── Registro.java
│   ├── RegistroHashExtensivel.java
│   ├── RegistroArvoreBMais.java
│   ├── ParIDEndereco.java
│   ├── Usuario.java
│   ├── Lista.java
│   ├── Produto.java
│   ├── ListaProduto.java
│   ├── ParEmailID.java
│   ├── ParCodigoID.java
│   ├── ParGtinID.java
│   ├── ParUsuarioLista.java
│   ├── ParListaProduto.java
│   ├── ParProdutoLista.java
│   ├── ControleUsuario.java
│   ├── ControleLista.java
│   ├── ControleProduto.java
│   ├── ControleListaProduto.java
│   ├── VisaoUsuario.java
│   ├── VisaoLista.java
│   ├── VisaoProduto.java
│   ├── VisaoListaComProdutos.java
│   └── Principal.java
```

## 8. Como Executar

1. Compilar o projeto:
   ```bash
   cd "NOSSO TRABALHO TP 2"
   javac -cp . aed3/*.java
   ```

2. Executar o programa:
   ```bash
   java aed3.Principal
   ```

3. Seguir as instruções da interface para:
   - Criar um novo usuário
   - Fazer login
   - Gerenciar listas e produtos

## 9. Conclusões

O PresenteFácil 2.0 entrega a gestão de produtos e o relacionamento N:N com listas, mantendo desempenho e integridade com Arquivo Indexado, Hash Extensível e Árvores B+. A solução está pronta para evoluir (TP3: busca avançada de produtos).

## 10. Referências

- Material de aula sobre Arquivos Indexados
- Material de aula sobre Tabelas Hash Extensíveis  
- Material de aula sobre Árvores B+
- Material de aula sobre Relacionamentos N:N
- GS1 – GTIN‑13

