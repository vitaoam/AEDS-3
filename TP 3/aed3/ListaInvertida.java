/*********
 * LISTA INVERTIDA
 * String termo, int dado
 * 
 * Implementado pelo Prof. Marcos Kutova
 * v1.0 - 2020
 */
package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ListaInvertida {

  String nomeArquivoDicionario;
  String nomeArquivoBlocos;
  RandomAccessFile arqDicionario;
  RandomAccessFile arqBlocos;
  int quantidadeDadosPorBloco;

  class Bloco {

    short quantidade;
    short quantidadeMaxima;
    ElementoLista[] elementos;
    long proximo;
    short bytesPorBloco;

    public Bloco(int qtdmax) throws Exception {
      quantidade = 0;
      quantidadeMaxima = (short) qtdmax;
      elementos = new ElementoLista[quantidadeMaxima];
      proximo = -1;
      bytesPorBloco = (short) (2 + (4+4) * quantidadeMaxima + 8);
    }

    public byte[] toByteArray() throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeShort(quantidade);
      int i = 0;
      while (i < quantidade) {
        dos.writeInt(elementos[i].getId());
        dos.writeFloat(elementos[i].getFrequencia());
        i++;
      }
      while (i < quantidadeMaxima) {
        dos.writeInt(-1);
        dos.writeFloat(-1);
        i++;
      }
      dos.writeLong(proximo);
      return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);
      quantidade = dis.readShort();
      int i = 0;
      while (i < quantidadeMaxima) {
        elementos[i] = new ElementoLista(dis.readInt(), dis.readFloat());
        i++;
      }
      proximo = dis.readLong();
    }

    public boolean create(ElementoLista e) {
      if (full())
        return false;
      int i = quantidade - 1;
      while (i >= 0 && e.getId() < elementos[i].getId()) {
        elementos[i + 1] = elementos[i];
        i--;
      }
      i++;
      elementos[i] = e.clone();
      quantidade++;
      return true;
    }

    public boolean test(int id) {
      if (empty())
        return false;
      int i = 0;
      while (i < quantidade && id > elementos[i].getId())
        i++;
      if (i < quantidade && id == elementos[i].getId())
        return true;
      else
        return false;
    }

    public ElementoLista read(int id) {
      if (empty())
        return null;
      int i = 0;
      while (i < quantidade && id > elementos[i].getId())
        i++;
      if (i < quantidade && id == elementos[i].getId())
        return elementos[i].clone();
      else
        return null;
    }

    public boolean update(ElementoLista e) {
      if (empty())
        return false;
      int i = 0;
      while (i < quantidade && e.getId() > elementos[i].getId())
        i++;
      if (e.getId() == elementos[i].getId()) {
        elementos[i] = e.clone();
        return true;
      } else
        return false;
    }

    public boolean delete(int id) {
      if (empty())
        return false;
      int i = 0;
      while (i < quantidade && id > elementos[i].getId())
        i++;
      if (id == elementos[i].getId()) {
        while (i < quantidade - 1) {
          elementos[i] = elementos[i + 1];
          i++;
        }
        quantidade--;
        return true;
      } else
        return false;
    }

    public ElementoLista[] list() {
      ElementoLista[] lista = new ElementoLista[quantidade];
      for (int i = 0; i < quantidade; i++)
        lista[i] = elementos[i].clone();
      return lista;
    }

    public boolean empty() {
      return quantidade == 0;
    }

    public boolean full() {
      return quantidade == quantidadeMaxima;
    }

    public long next() {
      return proximo;
    }

    public void setNext(long p) {
      proximo = p;
    }

    public int size() {
      return bytesPorBloco;
    }
  }

  public ListaInvertida(int n, String nd, String nc) throws Exception {
    quantidadeDadosPorBloco = n;
    nomeArquivoDicionario = nd;
    nomeArquivoBlocos = nc;

    arqDicionario = new RandomAccessFile(nomeArquivoDicionario, "rw");
    if(arqDicionario.length()<4) {
      arqDicionario.seek(0);
      arqDicionario.writeInt(0);
    }
    arqBlocos = new RandomAccessFile(nomeArquivoBlocos, "rw");
  }

  public void incrementaEntidades() throws Exception {
    arqDicionario.seek(0);
    int n = arqDicionario.readInt();
    arqDicionario.seek(0);
    arqDicionario.writeInt(n+1);
  }

  public void decrementaEntidades() throws Exception {
    arqDicionario.seek(0);
    int n = arqDicionario.readInt();
    arqDicionario.seek(0);
    arqDicionario.writeInt(n-1);
  }

  public int numeroEntidades() throws Exception {
    arqDicionario.seek(0);
    return arqDicionario.readInt();
  }

  public boolean create(String c, ElementoLista e) throws Exception {
    ElementoLista[] lista = read(c);
    for (int i = 0; i < lista.length; i++)
      if (lista[i].getId() == e.getId())
        return false;

    String termo = "";
    long endereco = -1;

    boolean existe = false;
    arqDicionario.seek(4);
    while (arqDicionario.getFilePointer() != arqDicionario.length()) {
      termo = arqDicionario.readUTF();
      endereco = arqDicionario.readLong();
      if (termo.compareTo(c) == 0) {
        existe = true;
        break;
      }
    }

    if (!existe) {
      Bloco b = new Bloco(quantidadeDadosPorBloco);
      endereco = arqBlocos.length();
      arqBlocos.seek(endereco);
      arqBlocos.write(b.toByteArray());
      arqDicionario.seek(arqDicionario.length());
      arqDicionario.writeUTF(c);
      arqDicionario.writeLong(endereco);
    }

    Bloco b = new Bloco(quantidadeDadosPorBloco);
    byte[] bd;
    while (endereco != -1) {
      long proximo = -1;
      arqBlocos.seek(endereco);
      bd = new byte[b.size()];
      arqBlocos.read(bd);
      b.fromByteArray(bd);
      if (!b.full()) {
        b.create(e);
      } else {
        proximo = b.next();
        if (proximo == -1) {
          Bloco b1 = new Bloco(quantidadeDadosPorBloco);
          proximo = arqBlocos.length();
          arqBlocos.seek(proximo);
          arqBlocos.write(b1.toByteArray());
          b.setNext(proximo);
        }
      }
      arqBlocos.seek(endereco);
      arqBlocos.write(b.toByteArray());
      endereco = proximo;
    }
    return true;
  }

  public ElementoLista[] read(String c) throws Exception {
    ArrayList<ElementoLista> lista = new ArrayList<>();
    String termo = "";
    long endereco = -1;
    boolean existe = false;
    arqDicionario.seek(4);
    while (arqDicionario.getFilePointer() != arqDicionario.length()) {
      termo = arqDicionario.readUTF();
      endereco = arqDicionario.readLong();
      if (termo.compareTo(c) == 0) {
        existe = true;
        break;
      }
    }
    if (!existe)
      return new ElementoLista[0];
    Bloco b = new Bloco(quantidadeDadosPorBloco);
    byte[] bd;
    while (endereco != -1) {
      arqBlocos.seek(endereco);
      bd = new byte[b.size()];
      arqBlocos.read(bd);
      b.fromByteArray(bd);
      ElementoLista[] lb = b.list();
      for (int i = 0; i < lb.length; i++)
        lista.add(lb[i]);
      endereco = b.next();
    }
    lista.sort(null);
    ElementoLista[] resposta = new ElementoLista[lista.size()];
    for (int j = 0; j < lista.size(); j++)
      resposta[j] = (ElementoLista) lista.get(j);
    return resposta;
  }

  public ElementoLista read(String c, int id) throws Exception {
    String termo = "";
    long endereco = -1;
    boolean existe = false;
    arqDicionario.seek(4);
    while (arqDicionario.getFilePointer() != arqDicionario.length()) {
      termo = arqDicionario.readUTF();
      endereco = arqDicionario.readLong();
      if (termo.compareTo(c) == 0) {
        existe = true;
        break;
      }
    }
    if (!existe)
      return null;
    Bloco b = new Bloco(quantidadeDadosPorBloco);
    byte[] bd;
    while (endereco != -1) {
      arqBlocos.seek(endereco);
      bd = new byte[b.size()];
      arqBlocos.read(bd);
      b.fromByteArray(bd);
      if (b.test(id)) 
        return b.read(id);
      endereco = b.next();
    }
    return null;
  }

  public boolean update(String c, ElementoLista e) throws Exception {
    String termo = "";
    long endereco = -1;
    boolean existe = false;
    arqDicionario.seek(4);
    while (arqDicionario.getFilePointer() != arqDicionario.length()) {
      termo = arqDicionario.readUTF();
      endereco = arqDicionario.readLong();
      if (termo.compareTo(c) == 0) {
        existe = true;
        break;
      }
    }
    if (!existe)
      return false;
    Bloco b = new Bloco(quantidadeDadosPorBloco);
    byte[] bd;
    while (endereco != -1) {
      arqBlocos.seek(endereco);
      bd = new byte[b.size()];
      arqBlocos.read(bd);
      b.fromByteArray(bd);
      if (b.test(e.getId())) {
        b.update(e);
        arqBlocos.seek(endereco);
        arqBlocos.write(b.toByteArray());
        return true;
      }
      endereco = b.next();
    }
    return false;
  }

  public boolean delete(String c, int id) throws Exception {
    String termo = "";
    long endereco = -1;
    boolean existe = false;
    arqDicionario.seek(4);
    while (arqDicionario.getFilePointer() != arqDicionario.length()) {
      termo = arqDicionario.readUTF();
      endereco = arqDicionario.readLong();
      if (termo.compareTo(c) == 0) {
        existe = true;
        break;
      }
    }
    if (!existe)
      return false;
    Bloco b = new Bloco(quantidadeDadosPorBloco);
    byte[] bd;
    while (endereco != -1) {
      arqBlocos.seek(endereco);
      bd = new byte[b.size()];
      arqBlocos.read(bd);
      b.fromByteArray(bd);
      if (b.test(id)) {
        b.delete(id);
        arqBlocos.seek(endereco);
        arqBlocos.write(b.toByteArray());
        return true;
      }
      endereco = b.next();
    }
    return false;
  }
}


