package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParListaProduto implements RegistroArvoreBMais<ParListaProduto> {
    
    private int idLista;         // chave primaria
    private int idListaProduto;  // chave secundaria
    private final short TAMANHO = 8;  // tamanho fixo em bytes (2 inteiros)
    
    public ParListaProduto() {
        this.idLista = -1;
        this.idListaProduto = -1;
    }
    
    public ParListaProduto(int idLista, int idListaProduto) {
        this.idLista = idLista;
        this.idListaProduto = idListaProduto;
    }
    
    public int getIdLista() {
        return idLista;
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
        dos.writeInt(this.idLista);
        dos.writeInt(this.idListaProduto);
        return baos.toByteArray();
    }
    
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idLista = dis.readInt();
        this.idListaProduto = dis.readInt();
    }
    
    @Override
    public int compareTo(ParListaProduto outro) {
        if(this.idLista != outro.idLista) {
            return Integer.compare(this.idLista, outro.idLista);
        } else {
            return Integer.compare(this.idListaProduto, outro.idListaProduto);
        }
    }
    
    @Override
    public ParListaProduto clone() {
        return new ParListaProduto(this.idLista, this.idListaProduto);
    }
    
    public String toString() {
        return "(" + this.idLista + ";" + this.idListaProduto + ")";
    }
}

