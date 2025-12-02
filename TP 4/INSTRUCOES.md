# Instruções – PresenteFácil 4.0 (TP4)

## Requisitos
- Navegador moderno (Chrome, Edge, Firefox). Não há dependências de backend.

## Como executar
1. Abra o arquivo `index.html` diretamente no navegador (duplo clique ou arraste para a janela do navegador).
2. Use o formulário “Cadastrar Produto” para inserir GTIN‑13, Nome e Descrição.
3. Utilize a tabela de “Produtos” para:
   - Buscar por GTIN ou nome
   - Editar um produto (botão “Editar”)
   - Excluir um produto (botão “Excluir” – marca lápide)
4. Acompanhe o painel “Visualização do Arquivo (simulado)”:
   - Mostra a posição do registro, a lápide, o tamanho (bytes) e os dados
   - O toggle “Mostrar excluídos” alterna a exibição de registros removidos

## Persistência (LocalStorage)
Chaves criadas:
- `pf4:header` – metadados do “arquivo” (ex.: último ID atribuído)
- `pf4:records` – vetor com registros `{ lapide, tamanho, dados }`
- `pf4:index:gtin` – índice direto `gtin13 -> id` (garante unicidade)

Para limpar os dados e começar do zero:
1. Abra as Ferramentas do Desenvolvedor (F12) → Application/Armazenamento → LocalStorage
2. Remova as chaves que começam com `pf4:` ou use “Clear All”
3. Recarregue a página

## Validações
- GTIN‑13: 13 dígitos numéricos obrigatórios e valor único
- Nome: obrigatório
- Descrição: opcional

## Fluxo sugerido de demonstração (vídeo)
1. Cadastrar 2–3 produtos com GTIN‑13 diferentes
2. Demonstrar a busca por GTIN e por nome
3. Editar o nome de um produto e salvar
4. Excluir um produto e marcar o toggle “Mostrar excluídos” para visualizar a lápide

## Dúvidas comuns
- “Por que ainda vejo o registro após excluir?”
  - A exclusão é lógica (lápide). O registro permanece no “arquivo” para simular um Arquivo Indexado.
- “O tamanho (bytes) é real?”
  - É um valor aproximado com base no JSON do registro, suficiente para a visualização didática.

## Compatibilidade
- Sistemas Operacionais: Windows, Linux, macOS
- Navegadores: últimas versões de Chrome/Edge/Firefox
- Codificação: UTF-8

---

PresenteFácil 4.0 – Visualização interativa do CRUD de Produtos (LocalStorage)


