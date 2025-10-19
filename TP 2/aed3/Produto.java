package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Produto implements Registro {
    
    private int id;
    private String gtin13;  // GTIN-13 (antigo EAN-13)
    private String nome;
    private String descricao;
    private boolean ativo;  // true = ativo, false = inativado
    
    public Produto() {
        this.id = -1;
        this.gtin13 = "";
        this.nome = "";
        this.descricao = "";
        this.ativo = true;
    }
    
    public Produto(String gtin13, String nome, String descricao) {
        this.id = -1;
        this.gtin13 = gtin13;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = true;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public String getGtin13() {
        return gtin13;
    }
    
    public void setGtin13(String gtin13) {
        this.gtin13 = gtin13;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.gtin13);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeBoolean(this.ativo);
        return baos.toByteArray();
    }
    
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.gtin13 = dis.readUTF();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.ativo = dis.readBoolean();
    }
    
    public String toString() {
        return "\nID: " + this.id +
               "\nGTIN-13: " + this.gtin13 +
               "\nNome: " + this.nome +
               "\nDescricao: " + this.descricao +
               "\nAtivo: " + (this.ativo ? "Sim" : "Nao (INATIVADO)");
    }
}

