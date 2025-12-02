// PresenteFácil 4.0 - CRUD de Produtos (LocalStorage)
// Armazenamento simula um "Arquivo Indexado" com:
// - cabecalho: lastId
// - registros: vetor de { lapide: boolean, tamanho: number, dados: { id, gtin13, nome, descricao } }
// - indice por GTIN-13 para unicidade
// Visualizacao mostra lapide e tamanho simulados.

(function(){
  "use strict";

  const STORAGE_KEYS = {
    HEADER: "pf4:header",
    RECORDS: "pf4:records",
    INDEX_GTIN: "pf4:index:gtin"
  };

  function readJSON(key, defaultValue){
    try {
      const raw = localStorage.getItem(key);
      if(!raw) return defaultValue;
      return JSON.parse(raw);
    } catch(e){
      return defaultValue;
    }
  }
  function writeJSON(key, value){
    localStorage.setItem(key, JSON.stringify(value));
  }

  function computeSizeBytes(data){
    // Tamanho aproximado em bytes da representacao JSON UTF-16 -> aqui usamos o comprimento de string como proxy
    // Para fins de visualizacao, isso eh suficiente.
    return JSON.stringify(data).length;
  }

  class ProductStore {
    constructor(){
      this.header = readJSON(STORAGE_KEYS.HEADER, { lastId: 0 });
      this.records = readJSON(STORAGE_KEYS.RECORDS, []);
      this.indexGtin = readJSON(STORAGE_KEYS.INDEX_GTIN, {}); // gtin -> id
    }
    persist(){
      writeJSON(STORAGE_KEYS.HEADER, this.header);
      writeJSON(STORAGE_KEYS.RECORDS, this.records);
      writeJSON(STORAGE_KEYS.INDEX_GTIN, this.indexGtin);
    }
    validateProduct(p, editingId = null){
      const errors = [];
      const gtin = (p.gtin13 || "").trim();
      const nome = (p.nome || "").trim();
      if(!/^\d{13}$/.test(gtin)){
        errors.push("GTIN-13 deve conter exatamente 13 dígitos.");
      }
      if(!nome){
        errors.push("Nome é obrigatório.");
      }
      const existsId = this.indexGtin[gtin];
      if(existsId && existsId !== editingId){
        errors.push("Já existe produto com este GTIN-13.");
      }
      return errors;
    }
    create(prod){
      const errors = this.validateProduct(prod, null);
      if(errors.length) return { ok: false, errors };
      const newId = ++this.header.lastId;
      const dados = { id: newId, gtin13: prod.gtin13.trim(), nome: prod.nome.trim(), descricao: (prod.descricao||"").trim() };
      const rec = { lapide: false, tamanho: computeSizeBytes(dados), dados };
      this.records.push(rec);
      this.indexGtin[dados.gtin13] = dados.id;
      this.persist();
      return { ok: true, id: newId };
    }
    update(id, prod){
      const errors = this.validateProduct(prod, id);
      if(errors.length) return { ok: false, errors };
      const rec = this.records.find(r => !r.lapide && r.dados.id === id);
      if(!rec) return { ok: false, errors: ["Produto não encontrado ou excluído."] };
      // Atualiza índice GTIN caso mude
      if(rec.dados.gtin13 !== prod.gtin13.trim()){
        delete this.indexGtin[rec.dados.gtin13];
        this.indexGtin[prod.gtin13.trim()] = id;
      }
      rec.dados.gtin13 = prod.gtin13.trim();
      rec.dados.nome = prod.nome.trim();
      rec.dados.descricao = (prod.descricao||"").trim();
      rec.tamanho = computeSizeBytes(rec.dados);
      this.persist();
      return { ok: true };
    }
    remove(id){
      const rec = this.records.find(r => !r.lapide && r.dados.id === id);
      if(!rec) return { ok: false, errors: ["Produto não encontrado ou já excluído."] };
      rec.lapide = true;
      // Remove do índice GTIN
      try { delete this.indexGtin[rec.dados.gtin13]; } catch(e){}
      this.persist();
      return { ok: true };
    }
    list(includeDeleted = false){
      const items = this.records
        .filter(r => includeDeleted ? true : !r.lapide)
        .map(r => r.dados);
      // ordena alfabeticamente por nome
      items.sort((a,b) => a.nome.localeCompare(b.nome, 'pt-BR', { sensitivity: 'base' }));
      return items;
    }
    search(query){
      const q = (query||"").trim().toLowerCase();
      if(!q) return this.list(false);
      const items = this.records
        .filter(r => !r.lapide)
        .map(r => r.dados)
        .filter(d => d.gtin13.includes(q) || d.nome.toLowerCase().includes(q));
      items.sort((a,b) => a.nome.localeCompare(b.nome, 'pt-BR', { sensitivity: 'base' }));
      return items;
    }
    getRecordView(includeDeleted){
      // Retorna uma visão dos registros com metadados para visualização
      const rows = [];
      for(let i=0;i<this.records.length;i++){
        const r = this.records[i];
        if(!includeDeleted && r.lapide) continue;
        rows.push({
          pos: i,
          lapide: r.lapide,
          tamanho: r.tamanho,
          id: r.dados.id,
          gtin13: r.dados.gtin13,
          nome: r.dados.nome
        });
      }
      return rows;
    }
  }

  // UI Controller
  const UI = {
    els: {},
    store: null,
    editingId: null,
    init(){
      this.cache();
      this.bind();
      this.refreshTables();
    },
    cache(){
      this.els.form = document.getElementById("product-form");
      this.els.inputId = document.getElementById("produto-id");
      this.els.inputGtin = document.getElementById("gtin13");
      this.els.inputNome = document.getElementById("nome");
      this.els.inputDesc = document.getElementById("descricao");
      this.els.btnSalvar = document.getElementById("btn-salvar");
      this.els.btnCancelar = document.getElementById("btn-cancelar");
      this.els.formTitle = document.getElementById("form-title");
      this.els.formMsg = document.getElementById("form-message");
      this.els.tbodyProdutos = document.getElementById("produtos-tbody");
      this.els.busca = document.getElementById("busca");
      this.els.btnBuscar = document.getElementById("btn-buscar");
      this.els.btnLimpar = document.getElementById("btn-limpar");
      this.els.toggleExcluidos = document.getElementById("toggle-excluidos");
      this.els.tbodyArquivo = document.getElementById("arquivo-tbody");
      this.els.formSection = document.getElementById("form-section");
    },
    bind(){
      this.els.form.addEventListener("submit", (e) => {
        e.preventDefault();
        this.onSubmit();
      });
      this.els.btnCancelar.addEventListener("click", () => this.resetForm());
      this.els.btnBuscar.addEventListener("click", () => this.applySearch());
      this.els.btnLimpar.addEventListener("click", () => {
        this.els.busca.value = "";
        this.applySearch();
      });
      this.els.busca.addEventListener("keydown", (e) => {
        if(e.key === "Enter"){ e.preventDefault(); this.applySearch(); }
      });
      this.els.toggleExcluidos.addEventListener("change", () => this.refreshArquivoTable());
    },
    setMessage(msg, type){
      this.els.formMsg.textContent = msg || "";
      this.els.formMsg.className = "message" + (type ? ` ${type}` : "");
    },
    onSubmit(){
      const payload = {
        gtin13: this.els.inputGtin.value,
        nome: this.els.inputNome.value,
        descricao: this.els.inputDesc.value
      };
      if(this.editingId){
        const res = this.store.update(this.editingId, payload);
        if(!res.ok){
          this.setMessage(res.errors.join(" "), "error");
          return;
        }
        this.setMessage("Produto atualizado com sucesso!", "success");
      } else {
        const res = this.store.create(payload);
        if(!res.ok){
          this.setMessage(res.errors.join(" "), "error");
          return;
        }
        this.setMessage("Produto criado com sucesso!", "success");
      }
      this.resetForm(false);
      this.refreshTables();
    },
    resetForm(clearMsg = true){
      this.editingId = null;
      this.els.inputId.value = "";
      this.els.inputGtin.value = "";
      this.els.inputNome.value = "";
      this.els.inputDesc.value = "";
      this.els.formTitle.textContent = "Cadastrar Produto";
      this.els.btnCancelar.hidden = true;
      if(clearMsg) this.setMessage("", "");
      this.els.inputGtin.focus();
    },
    editProduct(id){
      const rec = this.store.records.find(r => !r.lapide && r.dados.id === id);
      if(!rec) return;
      this.editingId = id;
      this.els.inputId.value = String(id);
      this.els.inputGtin.value = rec.dados.gtin13;
      this.els.inputNome.value = rec.dados.nome;
      this.els.inputDesc.value = rec.dados.descricao || "";
      this.els.formTitle.textContent = "Editar Produto";
      this.els.btnCancelar.hidden = false;
      this.els.inputNome.focus();
      this.setMessage("", "");
      this.els.formSection.scrollIntoView({ behavior: "smooth", block: "start" });
    },
    deleteProduct(id){
      if(!confirm("Deseja realmente excluir este produto?")) return;
      const res = this.store.remove(id);
      if(!res.ok){
        alert(res.errors.join(" "));
        return;
      }
      this.refreshTables();
    },
    applySearch(){
      const q = this.els.busca.value;
      const items = this.store.search(q);
      this.renderProdutos(items);
    },
    refreshTables(){
      this.renderProdutos(this.store.list(false));
      this.refreshArquivoTable();
    },
    renderProdutos(items){
      const tbody = this.els.tbodyProdutos;
      tbody.innerHTML = "";
      if(!items.length){
        const tr = document.createElement("tr");
        const td = document.createElement("td");
        td.colSpan = 4;
        td.innerHTML = "<span class='hint'>Nenhum produto encontrado.</span>";
        tr.appendChild(td);
        tbody.appendChild(tr);
        return;
      }
      for(const d of items){
        const tr = document.createElement("tr");
        const tdG = document.createElement("td"); tdG.textContent = d.gtin13;
        const tdN = document.createElement("td"); tdN.textContent = d.nome;
        const tdD = document.createElement("td"); tdD.textContent = d.descricao || "";
        const tdA = document.createElement("td");
        const btnE = document.createElement("button"); btnE.className = "btn"; btnE.textContent = "Editar";
        const btnX = document.createElement("button"); btnX.className = "btn danger"; btnX.textContent = "Excluir";
        btnE.addEventListener("click", () => this.editProduct(d.id));
        btnX.addEventListener("click", () => this.deleteProduct(d.id));
        tdA.appendChild(btnE);
        tdA.appendChild(document.createTextNode(" "));
        tdA.appendChild(btnX);
        tr.appendChild(tdG); tr.appendChild(tdN); tr.appendChild(tdD); tr.appendChild(tdA);
        tbody.appendChild(tr);
      }
    },
    refreshArquivoTable(){
      const includeDeleted = !!this.els.toggleExcluidos.checked;
      const rows = this.store.getRecordView(includeDeleted);
      const tbody = this.els.tbodyArquivo;
      tbody.innerHTML = "";
      if(!rows.length){
        const tr = document.createElement("tr");
        const td = document.createElement("td");
        td.colSpan = 6;
        td.innerHTML = "<span class='hint'>Nenhum registro no arquivo.</span>";
        tr.appendChild(td);
        tbody.appendChild(tr);
        return;
      }
      for(const r of rows){
        const tr = document.createElement("tr");
        const tdPos = document.createElement("td"); tdPos.textContent = String(r.pos);
        const tdLap = document.createElement("td"); tdLap.innerHTML = r.lapide ? "<span class='lapide-flag'>*</span> EXCL" : "&nbsp;";
        const tdTam = document.createElement("td"); tdTam.textContent = String(r.tamanho);
        const tdId  = document.createElement("td"); tdId.textContent = String(r.id);
        const tdG   = document.createElement("td"); tdG.textContent = r.gtin13;
        const tdN   = document.createElement("td"); tdN.textContent = r.nome;
        tr.appendChild(tdPos); tr.appendChild(tdLap); tr.appendChild(tdTam); tr.appendChild(tdId); tr.appendChild(tdG); tr.appendChild(tdN);
        tbody.appendChild(tr);
      }
    }
  };

  window.PF4 = {
    init(){
      UI.store = new ProductStore();
      UI.init();
    }
  };
})();


