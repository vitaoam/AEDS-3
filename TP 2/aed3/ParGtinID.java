package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParGtinID implements RegistroHashExtensivel<ParGtinID> {
    
    private String gtin13;  // chave
    private int id;         // valor
    private final short TAMANHO = 150;  // tamanho fixo em bytes
    
    public ParGtinID() {
        this.gtin13 = "";
        this.id = -1;
    }
    
    public ParGtinID(String gtin13, int id) {
        this.gtin13 = gtin13;
        this.id = id;
    }
    
    public String getGtin13() {
        return gtin13;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public int hashCode() {
        return this.gtin13.hashCode();
    }
    
    public short size() {
        return this.TAMANHO;
    }
    
    public String toString() {
        return "(" + this.gtin13 + ";" + this.id + ")";
    }
    
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        byte[] ba = new byte[TAMANHO];
        dos.write(ba);  // preenche com zeros
        
        baos.reset();
        dos.writeUTF(this.gtin13);
        dos.writeInt(this.id);
        
        ba = baos.toByteArray();
        byte[] ba2 = new byte[TAMANHO];
        for(int i = 0; i < Math.min(ba.length, TAMANHO); i++) {
            ba2[i] = ba[i];
        }
        return ba2;
    }
    
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.gtin13 = dis.readUTF();
        this.id = dis.readInt();
    }
}

