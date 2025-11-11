# Instruções de Uso - PresenteFácil 3.0

## Como Executar o Sistema

### 1. Compilação
```bash
javac -cp . aed3/*.java
```

### 2. Execução
```bash
java aed3.Principal
```

## Novidades do TP3

- Busca de produtos por palavras com índice invertido e ranqueamento TF×IDF
- Normalização de texto (minúsculas, sem acentos) e remoção de stopwords (pt‑BR)
- Busca por palavras disponível no menu Produtos e no fluxo “Acrescentar produto” dentro das listas

## Verificação da Instalação do Java

Para verificar se o Java está corretamente instalado:

```bash
java -version
javac -version
```

Ambos os comandos devem exibir a versão do Java instalada (8 ou superior).

## Estrutura de Pastas

Após a compilação, a estrutura será:

```
NOSSO TRABALHO TP 3/
├── aed3/
│   ├── *.java (arquivos fonte)
│   └── *.class (arquivos compilados)
├── dados/ (criada automaticamente na primeira execução)
│   ├── usuarios/
│   ├── listas/
│   ├── produtos/
│   ├── lista_produto/
│   └── lista_invertida/
├── README.md
└── INSTRUCOES.md
```

## Primeiro Uso

### 1. Criar um Usuário
```
Menu Principal
> (2) Novo usuario
Preencha: Nome, E-mail, Senha, Pergunta secreta, Resposta secreta
```

### 2. Fazer Login
```
Menu Principal
> (1) Login
Digite: E-mail e Senha
```

### 3. Criar uma Lista
```
Menu Usuario Logado
> (2) Minhas listas
> (2) Nova lista
Preencha: Nome, Descricao, Data limite (opcional)
```

### 4. Cadastrar Produtos
```
Menu Usuario Logado
> (3) Produtos
> (3) Cadastrar um novo produto
Preencha: GTIN-13, Nome, Descricao
```

### 5. Adicionar Produtos à Lista
```
Menu Usuario Logado
> (2) Minhas listas
> (1) Ver minhas listas
> [Selecione uma lista]
> (1) Gerenciar produtos da lista
> (A) Acrescentar produto
```

## Funcionalidades Principais

### Menu Principal
- **(1) Login**: Acessar o sistema com e-mail e senha
- **(2) Novo usuario**: Cadastrar novo usuário
- **(S) Sair**: Encerrar o programa

### Menu Usuário Logado
- **(1) Meus dados**: Gerenciar informações pessoais
- **(2) Minhas listas**: Criar e gerenciar listas de presentes
- **(3) Produtos**: Gerenciar produtos do sistema
- **(4) Buscar lista**: Buscar listas de outras pessoas por código
- **(5) Buscar produtos por palavras**: Busca avançada (TF×IDF)
- **(S) Sair**: Fazer logout

### Menu Produtos
- **(1) Buscar produtos por GTIN**: Busca direta por código GTIN-13
- **(2) Listar todos os produtos**: Listagem paginada (10 por página)
- **(3) Cadastrar um novo produto**: Adicionar novo produto ao sistema
- **(4) Buscar por palavras**: Busca por termos com relevância (TF×IDF)
- **(R) Retornar**: Voltar ao menu anterior

### Menu Minhas Listas
- **(1) Ver minhas listas**: Visualizar listas cadastradas
- **(2) Nova lista**: Criar nova lista de presentes
- **(R) Retornar**: Voltar ao menu anterior

### Menu Detalhes da Lista
- **(1) Gerenciar produtos da lista**: Adicionar/remover produtos
- **(2) Alterar dados da lista**: Modificar nome, descrição, data
- **(3) Excluir lista**: Remover lista (e suas associações)
- **(R) Retornar**: Voltar ao menu anterior

### Menu Produtos da Lista
- **[Número]**: Selecionar produto para ver detalhes
- **(A) Acrescentar produto**: Adicionar novo produto
- **(R) Retornar**: Voltar ao menu anterior

### Menu “Acrescentar Produto” (dentro da Lista)
- **(1) Buscar produtos por GTIN**
- **(2) Listar todos os produtos**
- **(3) Buscar produtos por palavras** (TF×IDF; apenas produtos ativos)
- **(R) Retornar**

### Menu Detalhes do Produto na Lista
- **(1) Alterar a quantidade**: Modificar quantidade desejada
- **(2) Alterar as observacoes**: Adicionar/modificar observações
- **(3) Remover o produto desta lista**: Excluir da lista
- **(R) Retornar**: Voltar ao menu anterior

## Navegação

O sistema utiliza **breadcrumbs** para mostrar sua localização:
```
PresenteFacil 3.0
-----------------
> Inicio > Minhas listas > Aniversario 2025 > Produtos
```

## Funcionalidades Especiais

### Paginação
- Listas com mais de 10 produtos são paginadas
- Use **(A) Pagina anterior** e **(P) Proxima pagina** para navegar

### GTIN-13
- Código único de 13 dígitos para identificar produtos
- O sistema não permite GTIN-13 duplicado

### Inativação de Produtos
- Produtos podem ser **inativados** ao invés de excluídos
- Produtos inativos:
  - Permanecem nas listas existentes
  - Não aparecem para novas adições
  - Podem ser reativados

### Busca por Palavras (TF×IDF) – TP3
- Busca por termos no nome dos produtos
- Termos são normalizados (minúsculas, sem acentos) e stopwords (pt‑BR) são removidas
- Resultados ranqueados por TF×IDF (apenas produtos ativos)
- Disponível no menu Produtos e no fluxo “Acrescentar produto”

### Código Compartilhável
- Cada lista possui um código único de 10 caracteres
- Use o código para compartilhar listas com outras pessoas
- Busque listas de outros usando **(4) Buscar lista**

## Observações Importantes

1. **Dados Persistentes**: Todos os dados são armazenados na pasta `dados/`
2. **Primeira Execução**: A pasta `dados/` é criada automaticamente
3. **Senhas**: São armazenadas com hash SHA-256 para segurança
4. **Integridade**: O sistema impede operações que violem a integridade referencial
5. **Produtos Duplicados**: Não é possível adicionar o mesmo produto duas vezes na mesma lista

## Limpeza de Dados (Para Testes)

Para reiniciar o sistema com dados limpos:

### Windows
```cmd
rmdir /s /q dados
```

### Linux/Mac
```bash
rm -rf dados/
```

**Atenção**: Isso apagará TODOS os dados do sistema!

## Resolução de Problemas

### Erro: "Could not find or load main class aed3.Principal"
**Solução**: Certifique-se de estar na pasta "NOSSO TRABALHO TP 3" e que os arquivos foram compilados corretamente.

### Erro: "javac não é reconhecido..."
**Solução**: Java não está instalado ou não está no PATH. Instale o JDK e configure as variáveis de ambiente.

### Erro: "Exception... Access denied"
**Solução**: Execute o terminal/CMD com permissões adequadas ou escolha uma pasta com permissão de escrita.

### Arquivos .class não são criados
**Solução**: Verifique se há erros de compilação. Corrija os erros e compile novamente.

## Exemplo de Fluxo Completo

```
1. java aed3.Principal
2. (2) Novo usuario → Cadastrar
3. (1) Login → Fazer login
4. (3) Produtos → (3) Cadastrar um novo produto
   - GTIN-13: 7898742841332
   - Nome: Sanduicheira Oster
   - Descricao: Grill Eletrico 2 em 1
5. (2) Minhas listas → (2) Nova lista
   - Nome: Aniversario 2025
   - Descricao: Sugestoes para aniversario
6. (2) Minhas listas → (1) Ver minhas listas → [1]
7. (1) Gerenciar produtos
8. (A) Acrescentar produto → (3) Buscar por palavras
   - Termos: sanduicheira inox
   - Selecionar o produto sugerido
   - GTIN-13: 7898742841332
   - Confirmar: S
   - Quantidade: 1
   - Observacoes: Pode ser de outra marca
9. Produto adicionado com sucesso!
```

## Suporte

Para dúvidas ou problemas:
1. Verifique se seguiu todos os passos corretamente
2. Leia as mensagens de erro com atenção
3. Consulte o README.md para detalhes técnicos
4. Entre em contato com o grupo do trabalho

## Compatibilidade

- **Java**: JDK 8, 11, 17, 21
- **Sistemas Operacionais**: Windows 10/11, Linux, macOS
- **Codificação**: UTF-8 (padrão)
- **Terminal**: Qualquer terminal que suporte Java

---

**PresenteFácil 3.0** - Sistema de Gestão de Listas de Presentes
Desenvolvido como parte do TP3 de Algoritmos e Estruturas de Dados III

