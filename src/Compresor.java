import java.io.File;
import java.util.*;

public class Compresor<E extends Comparable<E>> {
    File archivo;
    ArbolH<E> huffman;

    public ArrayList<E> crearLista(File archivo) {
        return new ArrayList<E>();
    }

    public ArrayList<E> genDatos(File archivo) {
        ArrayList<E> info = crearLista(archivo);
        return null;
    }

    public void buildTree(File archivo) {
        ArrayList<E> info = genDatos(archivo);
        PriorityQueue<NodoB> nodos = new PriorityQueue<>();
        for (int i = 0; i < info.size(); i++) {
            nodos.add(new NodoB(info.get(i)));
        }
        while(nodos.size() > 1) {
            NodoB temp = new NodoB();
            temp.setHijoIzq(nodos.poll());
            temp.setHijoDer(nodos.poll());
            nodos.add(temp);
        }
        huffman.setRaiz(nodos.poll());
    }

    public void cargar(File archivo) {
        this.archivo = archivo;
        buildTree(archivo);
    }

    public void comprimir() {

    }

    public void descomprimir() {

    }

}
