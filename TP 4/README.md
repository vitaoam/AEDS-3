Link do video: https://youtu.be/4GF6kAHMowo

# PresenteFácil 4.0 (TP4) – CRUD de Produtos Web (LocalStorage)

## Participantes do Trabalho Prático
- Vitor Alexandre Moreira Amaral  
- Pedro Henrique Bellone  
- Gustavo Vinicius Elias Souza Silva

## Resumo

Este trabalho apresenta o PresenteFácil 4.0, uma página web desenvolvida com HTML, CSS e JavaScript puro que ilustra, de forma interativa, as operações de CRUD de produtos armazenados em um “Arquivo Indexado” simulado sobre o LocalStorage do navegador. A aplicação permite cadastrar, consultar, alterar e excluir produtos identificados por GTIN‑13, garantindo unicidade por meio de um índice direto em memória e exibindo, em paralelo, uma visualização da estrutura de armazenamento com lápide e tamanho aproximado de cada registro. A solução foi avaliada por alunos de Algoritmos e Estruturas de Dados III quanto à sua utilidade didática e usabilidade, obtendo médias acima de 4,0 em todos os itens avaliados.

## Abstract

This work presents PresenteFácil 4.0, a web page built with plain HTML, CSS and JavaScript that visually demonstrates CRUD operations over products stored in a simulated Indexed File using the browser’s LocalStorage. The application supports creating, reading, updating and deleting products identified by GTIN‑13, enforcing uniqueness through an in‑memory direct index and showing, side by side, a visualization of the underlying storage structure with tombstones and approximate record sizes. The tool was evaluated by Algorithms and Data Structures students regarding usefulness and usability, achieving average scores above 4.0 in all items.

## 1. Introdução

O TP4 complementa os trabalhos anteriores (TP1–TP3) com uma atividade extensionista focada em visualização didática. Enquanto os primeiros trabalhos implementam as estruturas de dados (Arquivo Indexado, Tabela Hash Extensível, Árvores B+ e Lista Invertida) em Java, este trabalho cria uma interface web simples para que outros alunos possam entender visualmente como um CRUD de produtos se comporta em um arquivo. Toda a solução é implementada apenas com HTML, CSS e JavaScript, utilizando o LocalStorage como meio de persistência.

## 2. Descrição da Aplicação

### 2.1 Funcionalidades Implementadas

- **CRUD de Produtos**: criação, listagem, busca, edição e exclusão de produtos (GTIN‑13, nome, descrição)
- **Validações**: GTIN‑13 com 13 dígitos numéricos e unicidade garantida por índice direto
- **Busca**: por GTIN‑13 ou por nome (case‑insensitive)
- **Visualização do Arquivo (simulado)**: exibe posição do registro, lápide, tamanho aproximado (bytes), id, GTIN‑13 e nome
- **Persistência Local**: todos os dados são armazenados no LocalStorage do navegador, sem backend

### 2.2 Armazenamento em LocalStorage

A aplicação simula um Arquivo Indexado com as seguintes estruturas:

- **`pf4:header`**: objeto com metadados, incluindo o `lastId` utilizado
- **`pf4:records`**: vetor de registros, cada um com `{ lapide, tamanho, dados }`, em que:
  - `lapide` indica se o registro foi logicamente excluído
  - `tamanho` é o tamanho aproximado da representação do registro em bytes
  - `dados` contém `{ id, gtin13, nome, descricao }`
- **`pf4:index:gtin`**: mapa direto `gtin13 → id` para garantir unicidade

Essa modelagem permite aproximar o comportamento de um arquivo indexado com lápide, sem manipular bytes reais, mas deixando o aluno visualizar claramente registros ativos e excluídos.

## 3. Arquitetura da Página Web

### 3.1 Estrutura de Arquivos

```text
NOSSO TRABALHO TP 4/
├── index.html       # Interface principal da aplicação
├── styles.css       # Estilos (tema escuro, layout responsivo)
├── app.js           # Lógica do CRUD e simulação do arquivo em LocalStorage
└── README.md        # Relatório/resumo do TP4
```

### 3.2 Tecnologias Utilizadas

- **HTML5**: estrutura da página e componentes de formulário/tabelas
- **CSS3**: layout em duas colunas, tema escuro, responsividade e usabilidade
- **JavaScript (ES6+)**: controle de estado, manipulação do DOM, acesso ao LocalStorage
- **LocalStorage**: persistência dos dados da simulação no navegador do usuário

## 4. Interface do Usuário

A interface é dividida em duas colunas principais:

1. **Coluna Esquerda**  
   - Formulário “Cadastrar Produto” (ou “Editar Produto”), com campos para GTIN‑13, Nome e Descrição.  
   - Tabela de “Produtos” com busca por GTIN ou Nome e botões de “Editar” e “Excluir”.

2. **Coluna Direita**  
   - Painel “Visualização do Arquivo (simulado)”, exibindo:
     - Posição do registro no vetor
     - Lápide (`* EXCL` para registros excluídos logicamente)
     - Tamanho aproximado em bytes
     - ID interno, GTIN‑13 e Nome
   - Controle “Mostrar excluídos” para alternar a exibição de registros com lápide.

Essa organização permite ao aluno realizar o CRUD normalmente e, ao mesmo tempo, enxergar o impacto de cada operação na estrutura de armazenamento.

## 5. Operações Especiais Implementadas

1. **Validação de GTIN‑13** – Apenas 13 dígitos numéricos são aceitos, evitando cadastros inválidos.  
2. **Unicidade de GTIN‑13** – Um índice direto `gtin13 → id` em LocalStorage impede produtos duplicados.  
3. **Exclusão Lógica (Lápide)** – Ao excluir, o registro permanece no vetor, mas recebe lápide, simulando um arquivo com reaproveitamento futuro de espaço.  
4. **Visualização do Tamanho de Registro** – O tamanho aproximado em bytes é calculado a partir do JSON do registro, reforçando a ideia de custo de armazenamento.  
5. **Busca Integrada** – A caixa de busca filtra produtos por GTIN‑13 ou Nome, apoiando a exploração e o uso didático em sala.

## 6. Checklist de Requisitos

- A página web com a visualização interativa do CRUD de produtos foi criada? **Sim**  
- Há um vídeo de até 3 minutos demonstrando o uso da visualização? **Sim**  
- O trabalho foi criado apenas com HTML, CSS e JS? **Sim**  
- O relatório do trabalho foi entregue no APC? **Sim**  
- O trabalho está completo e funcionando sem erros de execução? **Sim**  
- O trabalho é original e não a cópia de um trabalho de outro grupo? **Sim**  

## 7. Como Executar

1. Abrir o arquivo `index.html` em um navegador moderno (Chrome, Edge ou Firefox).  
2. No formulário “Cadastrar Produto”, informar GTIN‑13, Nome e Descrição e clicar em **Salvar**.  
3. Usar a tabela de “Produtos” para:
   - Buscar por GTIN‑13 ou Nome
   - Editar um produto existente
   - Excluir um produto (aparecerá lápide no painel da direita)  
4. Observar, no painel “Visualização do Arquivo (simulado)”, como cada operação afeta:
   - Lápide
   - Tamanho aproximado do registro
   - Lista de registros ativos e excluídos

## 8. Resultados da Avaliação com Usuários

Após a implementação, a aplicação foi avaliada por alunos que já cursaram ou estão cursando AEDs III, utilizando um questionário em escala Likert (1–Discordo totalmente … 5–Concordo totalmente). As afirmações avaliadas foram:

1. A aplicação me ajuda a realizar as tarefas de cadastro de forma eficiente.  
2. As funções principais (incluir, consultar, alterar, excluir) são fáceis de encontrar e usar.  
3. As mensagens exibidas são claras e úteis.  
4. A aplicação é intuitiva, mesmo para quem a usa pela primeira vez.  
5. De modo geral, estou satisfeito(a) com a experiência de uso.

Tabela de resultados (10 respondentes):

| Item | Afirmação resumida                      | Média |
|------|-----------------------------------------|-------|
| 1    | Eficiência da aplicação                 | 4,6   |
| 2    | Facilidade de uso das funções           | 4,3   |
| 3    | Clareza das mensagens                   | 4,4   |
| 4    | Intuitividade da interface              | 4,1   |
| 5    | Satisfação geral                        | 4,5   |

De forma geral, os alunos avaliaram a visualização como **útil** para compreender o funcionamento de um CRUD em arquivo, com médias acima de 4,0 em todos os itens. Os melhores resultados apareceram em eficiência e satisfação geral, enquanto a intuitividade recebeu uma média um pouco menor, indicando espaço para melhorias em rótulos e organização visual dos botões. A partir desses dados, planejamos revisar textos de ajuda e destacar ainda mais o painel de visualização do arquivo, reforçando o caráter didático da ferramenta.

## 9. Conclusões

O PresenteFácil 4.0 cumpre o papel de ferramenta extensionista ao apoiar outros alunos na visualização concreta das operações de CRUD e da estrutura de um arquivo indexado, utilizando apenas tecnologias de front‑end. A combinação de formulário, tabela de produtos e painel de visualização do arquivo facilita a conexão entre teoria e prática, tornando conceitos como lápide, tamanho de registro e índices diretos mais acessíveis. Os resultados da avaliação indicam boa aceitação em termos de utilidade e usabilidade, com pequenas oportunidades de melhoria na intuitividade da interface.
