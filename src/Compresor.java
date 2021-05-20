import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Compresor<E extends Comparable<E>> {
    File archivo;
    ArbolH<Character> huffman;
    HashMap<Character, String> codigos;
    String outDir;

    public Compresor() {
        this.codigos = new HashMap<>();
    }

    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }

    public ArrayList<Character> leerFichero(File archivo) throws ClassNotFoundException, IOException, FileNotFoundException {
        FileReader fr;
        BufferedReader br;
        ArrayList<Character> aux = new ArrayList<Character>();
        int caract;
        fr = new FileReader(archivo);
        br = new BufferedReader(fr);
        caract = br.read();
        while(caract != -1) {
            aux.add((char)caract);
            caract = br.read();
        }
        Collections.sort(aux);
        fr.close();
        return aux;
    }

    public ArrayList<NodoB<Character>> genDatos(File archivo) throws IOException, ClassNotFoundException {
        ArrayList<Character> aux = leerFichero(archivo);
        ArrayList<NodoB<Character>> lista = new ArrayList<NodoB<Character>>();
        char temp = aux.get(0);
        int cont = 1;
        for (int i = 1; i < aux.size(); i++) {
            if(i + 1 == aux.size() || aux.get(i) != temp) {
                lista.add(new NodoB<Character>(temp, cont));
                cont = 1;
                temp = aux.get(i);
            }
            else
                cont++;
        }
        if(temp != lista.get(lista.size() - 1).getLlave()) {
            lista.add(new NodoB<Character>(temp, 1));
        }
        return lista;
    }

    public void buildTree(File archivo) throws IOException, ClassNotFoundException {
        PriorityQueue<NodoB> nodos = new PriorityQueue<>();
        ArrayList<NodoB<Character>> datos = genDatos(archivo);
        nodos.addAll(datos);
        while(nodos.size() > 1) {
            NodoB temp = new NodoB();
            temp.setHijoIzq(nodos.poll());
            temp.setHijoDer(nodos.poll());
            temp.setWeight(temp.getHijoIzq().getWeight() + temp.getHijoDer().getWeight());
            nodos.add(temp);
        }
        huffman = new ArbolH<>(nodos.poll());
    }

    public void mapCodigos(NodoB<Character> nodo, String codigo) {
        if (nodo != null) {
            if(nodo.getHijoIzq() == null && nodo.getHijoDer() == null) {
                codigos.put(nodo.getLlave(), codigo);
            }
            mapCodigos(nodo.getHijoIzq(), codigo + '0');
            mapCodigos(nodo.getHijoDer(), codigo + '1');
        }
        return;
    }

    public void cargar(File archivo) throws IOException, ClassNotFoundException {
        this.archivo = archivo;
        buildTree(archivo);
        mapCodigos(huffman.getRaiz(), "");
    }

    public String codificar() throws IOException {
        FileReader fr;
        BufferedReader br;
        StringBuilder aux = new StringBuilder();
        int caract;
        fr = new FileReader(archivo);
        br = new BufferedReader(fr);
        caract = br.read();
        while(caract != -1) {
            String cod = codigos.get((char)caract);
            aux.append(cod);
            caract = br.read();
        }
        fr.close();
        System.out.println(aux);
        return aux.toString();
    }

    public void comprimir() throws IOException {
        String codigo = codificar();
        String [] split = codigo.split("(?<=\\G.{8})");
        char[] chars = new char[split.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char)Integer.parseInt(split[i], 2);
        }
        System.out.println(new String(chars)); //Linea de prueba
        FileWriter writer = new FileWriter(outDir);
        writer.write(chars);
        writer.close();
        //FileOutputStream fos = new FileOutputStream(new File(outDir));
        //fos.write(res.getBytes(StandardCharsets.UTF_8));
    }

    public void descomprimir() {

    }

    public static void main(String[] args) {
        Compresor<Character> compresor = new Compresor<>();
        try {
            compresor.setOutDir("D:\\prueba2.txt");
            compresor.cargar(new File("D:\\prueba.txt"));
            compresor.comprimir();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
