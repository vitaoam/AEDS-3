package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParProdutoLista implements RegistroArvoreBMais<ParProdutoLista> {
    
    private int idProduto;       // chave primaria
    private int idListaProduto;  // chave secundaria
    private final short TAMANHO = 8;  // tamanho fixo em bytes (2 inteiros)
    
    public ParProdutoLista() {
        this.idProduto = -1;
        this.idListaProduto = -1;
    }
    
    public ParProdutoLista(int idProduto, int idListaProduto) {
        this.idProduto = idProduto;
        this.idListaProduto = idListaProduto;
    }
    
    public int getIdProduto() {
        return idProduto;
    }
    
    public int getIdListaProduto() {
        return idListaProduto;
    }
    
    @Override
    public short size() {
        return this.TAMANHO;
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idProduto);
        dos.writeInt(this.idListaProduto);
        return baos.toByteArray();
    }
    
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idProduto = dis.readInt();
        this.idListaProduto = dis.readInt();
    }
    
    @Override
    public int compareTo(ParProdutoLista outro) {
        if(this.idProduto != outro.idProduto) {
            return Integer.compare(this.idProduto, outro.idProduto);
        } else {
            return Integer.compare(this.idListaProduto, outro.idListaProduto);
        }
    }
    
    @Override
    public ParProdutoLista clone() {
        return new ParProdutoLista(this.idProduto, this.idListaProduto);
    }
    
    public String toString() {
        return "(" + this.idProduto + ";" + this.idListaProduto + ")";
    }
}

