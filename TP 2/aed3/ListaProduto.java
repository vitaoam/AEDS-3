package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ListaProduto implements Registro {
    
    private int id;
    private int idLista;     // chave estrangeira para Lista
    private int idProduto;   // chave estrangeira para Produto
    private int quantidade;
    private String observacoes;
    
    public ListaProduto() {
        this.id = -1;
        this.idLista = -1;
        this.idProduto = -1;
        this.quantidade = 1;
        this.observacoes = "";
    }
    
    public ListaProduto(int idLista, int idProduto, int quantidade, String observacoes) {
        this.id = -1;
        this.idLista = idLista;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.observacoes = observacoes;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public int getIdLista() {
        return idLista;
    }
    
    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }
    
    public int getIdProduto() {
        return idProduto;
    }
    
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.idLista);
        dos.writeInt(this.idProduto);
        dos.writeInt(this.quantidade);
        dos.writeUTF(this.observacoes);
        return baos.toByteArray();
    }
    
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.idLista = dis.readInt();
        this.idProduto = dis.readInt();
        this.quantidade = dis.readInt();
        this.observacoes = dis.readUTF();
    }
    
    public String toString() {
        return "\nID: " + this.id +
               "\nID Lista: " + this.idLista +
               "\nID Produto: " + this.idProduto +
               "\nQuantidade: " + this.quantidade +
               "\nObservacoes: " + this.observacoes;
    }
}

