import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Compresor<E extends Comparable<E>> {
    File archivo;
    ArbolH<E> huffman;


    public ArrayList<Character> leerFichero(File archivo) throws ClassNotFoundException, IOException, FileNotFoundException {
        FileReader fr;
        ArrayList<Character> aux = new ArrayList<Character>();
        int caract;
        fr = new FileReader(archivo);
        caract = fr.read();
        while(caract != -1) {
            aux.add((char) fr.read());
            caract = fr.read();
        }
        Collections.sort(aux);
        fr.close();
        return aux;
    }

    public ArrayList<NodoB<Character>> genDatos(File archivo) throws IOException, ClassNotFoundException {
            ArrayList<Character> aux = leerFichero(archivo);
            ArrayList<NodoB<Character>> lista = new ArrayList<NodoB<Character>>();
            for (int i = 0; i < aux.size(); i++) {
                int acum=1;
                int n=i+1;
                while(aux.get(i).compareTo(aux.get(n++))==0) {
                    i++;
                    acum++;
                }
                lista.add(new NodoB<Character>(aux.get(i), acum));
            }
            return lista;
    }

    public void buildTree(File archivo) throws IOException, ClassNotFoundException {
        PriorityQueue<NodoB> nodos = new PriorityQueue<>();
        nodos.addAll(genDatos(archivo));
        while(nodos.size() > 1) {
            NodoB temp = new NodoB();
            temp.setHijoIzq(nodos.poll());
            temp.setHijoDer(nodos.poll());
            nodos.add(temp);
        }
        huffman.setRaiz(nodos.poll());
    }

    public void cargar(File archivo) throws IOException, ClassNotFoundException {
        this.archivo = archivo;
        buildTree(archivo);
    }

    public void comprimir() {

    }

    public void descomprimir() {

    }

}
