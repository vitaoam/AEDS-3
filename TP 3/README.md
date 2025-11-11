# Link do video publicado no youtube: https://youtu.be/A0SaOqF-jxY

# PresenteFácil 3.0 - Sistema de Gestão de Listas de Presentes (busca por palavras)

## Participantes do Trabalho Prático
- Vitor Alexandre Moreira Amaral
- Pedro Henrique Bellone  
- Gustavo Vinicius Elias Souza Silva

## Resumo

Este trabalho apresenta a implementação do sistema PresenteFácil 3.0, evolução direta do TP2. Nesta sprint foi implementada a busca de produtos por palavras usando índice invertido com ranqueamento TF×IDF, além da integração dessa busca no menu de produtos e no fluxo de acrescentar produto às listas. O sistema mantém GTIN‑13, paginação, relacionamento N:N (Lista–Produto), inativação e integridade referencial, utilizando Arquivo Indexado, Tabela Hash Extensível e Árvores B+.

## Abstract

This work presents PresenteFácil 3.0, extending TP2 by adding word-based product search using an inverted index with TF×IDF ranking, integrated both in the product menu and in the “add product to list” flow. The system keeps GTIN‑13, pagination, N:N association, inactivation and data integrity using Indexed Files, Extensible Hash Tables and B+ Trees.

## 1. Introdução

O PresenteFácil 3.0 mantém todo o escopo do TP2 (usuários, listas, GTIN‑13, N:N, paginação) e adiciona:

- Índice invertido de termos dos nomes dos produtos
- Normalização de termos (minúsculas/sem acentos) e remoção de stopwords (pt‑BR)
- Ranqueamento de resultados por TF×IDF
- Busca por palavras no menu de Produtos e no fluxo “Acrescentar produto”

## 2. Descrição do Sistema

### 2.1 Funcionalidades Implementadas

#### 2.1.1 Busca por Palavras (TP3)
- **Índice invertido** com listas de `(idProduto; TF)` por termo
- **TF×IDF** na consulta: agrega termos e soma os scores por produto
- **Normalização** (minúsculas/sem acento) e **stopwords** (pt‑BR) removidas
- **Integração**:
  - Menu Produtos → “Buscar por palavras” (ordenado por relevância)
  - Nas listas → “Acrescentar produto” → “Buscar por palavras”

#### 2.1.2 Sistema de Produtos (TP2)
- **Cadastro/Alteração** de produtos (GTIN‑13 único)
- **Busca por GTIN‑13** e **listagem paginada** por nome
- **Inativar/Reativar** produto (soft delete)
- **Reindexação** automática no índice invertido ao criar/alterar nome

#### 2.1.3 Associação ListaProduto (N:N) (TP2)
- **Adicionar** produto à lista com quantidade e observações
- **Alterar** quantidade/observações e **remover** produto da lista
- **Integridade**: evita duplicidade na mesma lista; remove associações ao excluir lista

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
- `ParUsuarioLista` (idUsuario, idLista) – relacionamento 1:N
- `ParListaProduto` (idLista, idListaProduto) – listas → associações
- `ParProdutoLista` (idProduto, idListaProduto) – produtos → associações

#### 2.2.4 Índice Invertido (TP3)
- `ListaInvertida`: dicionário (termo → ponteiro) e blocos encadeados com `ElementoLista(id, TF)`
- Persistência em `dados/lista_invertida/{dicionario.listainv.db, blocos.listainv.db}`

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
4. **Produtos**: Buscar por GTIN‑13, listar todos (paginado), cadastrar produto e buscar por palavras

## 5. Operações Especiais Implementadas

1. **Geração de Código Compartilhável** (TP1) – 10 caracteres
2. **Inativação de Produtos** (TP2) – não exclui fisicamente
3. **Listagem Paginada** (TP2) – 10 por página, ordenação por nome
4. **Integridade N:N** (TP2) – sem duplicidade e remoção em cascata ao excluir lista
5. **Busca por Palavras** (TP3) – índice invertido + TF×IDF

## 6. Checklist de Requisitos

### 6.1 O índice invertido com os termos dos nomes dos produtos foi criado usando a classe ListaInvertida?
**SIM** – termos normalizados e frequências (TF) gravadas; N (total de produtos) mantido.

### 6.2 É possível buscar produtos por palavras no menu de manutenção de produtos?
**SIM** – Menu Produtos → “Buscar por palavras”, com ranqueamento TF×IDF.

### 6.3 É possível buscar produtos por palavras na hora de acrescentá-los às listas dos usuários?
**SIM** – Gerenciar produtos → Acrescentar → “Buscar por palavras” (ordenado por relevância).

### 6.4 O trabalho compila corretamente?
**SIM** – compilação sem erros.

### 6.5 O trabalho está completo e funcionando sem erros de execução?
**SIM** – operações testadas para TP3, mantendo TP1/TP2.

### 6.6 O trabalho é original e não a cópia de um trabalho de outro grupo?
**SIM** – implementação própria baseada nos requisitos.

## 7. Estrutura de Arquivos

```
NOSSO TRABALHO TP 3/
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
│   ├── ElementoLista.java            [TP3]
│   ├── ListaInvertida.java           [TP3]
│   ├── TextUtils.java                [TP3]
│   ├── ControleUsuario.java
│   ├── ControleLista.java
│   ├── ControleProduto.java
│   ├── ControleListaProduto.java
│   ├── VisaoUsuario.java
│   ├── VisaoLista.java
│   ├── VisaoProduto.java
│   ├── VisaoListaComProdutos.java
│   └── Principal.java
├── dados/
│   └── lista_invertida/
│       ├── dicionario.listainv.db
│       └── blocos.listainv.db
```

## 8. Como Executar

1. Compilar o projeto:
   ```bash
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

O PresenteFácil 3.0 entrega a busca por palavras com índice invertido e TF×IDF, integrada tanto ao menu de produtos quanto ao fluxo de acrescentar produto nas listas, mantendo o ecossistema do TP2 (GTIN‑13, N:N e paginação). A solução fica preparada para evoluções como filtros por categoria, boosting de campos e sugestões de termos.

## 10. Referências

- Material de aula sobre Arquivos Indexados
- Material de aula sobre Tabelas Hash Extensíveis  
- Material de aula sobre Árvores B+
- Material de aula sobre Relacionamentos N:N e Índice Invertido
- GS1 – GTIN‑13

