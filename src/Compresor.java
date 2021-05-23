import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Compresor<E extends Comparable<E>> {
    File archivo;
    ArbolH<Character> huffman;
    int nCaracteres;
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
        //A;adir nodo extra
        return lista;
    }

    public void buildTree(File archivo) throws IOException, ClassNotFoundException {
        PriorityQueue<NodoB> nodos = new PriorityQueue<>();
        nodos.add(new NodoB<Character>((char)3, 1));
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

    public void cargarComprimir(File archivo) throws IOException, ClassNotFoundException {
        this.archivo = archivo;
        buildTree(archivo);
        mapCodigos(huffman.getRaiz(), "");
    }

    public void cargarDescomprimir(File archivo) {
        this.archivo = archivo;
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
        aux.append(codigos.get((char)3));
        fr.close();
        System.out.println(aux);
        return aux.toString();
    }

    public void comprimir() throws IOException {
        String codigo = codificar();
        String [] split = codigo.split("(?<=\\G.{8})");
        while (split[split.length - 1].length() < 8) {
            split[split.length - 1] += '0';
        }
        char[] chars = new char[split.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char)Integer.parseInt(split[i], 2);
        }
        System.out.println(new String(chars)); //Linea de prueba
        FileWriter writer = new FileWriter(outDir);
        BufferedWriter bWriter = new BufferedWriter(writer);
        //for (int i = 0; i < frecuencias.size(); i++) {
            //bWriter.write(String.valueOf(frecuencias.get(i).getLlave()));
            //bWriter.write(String.valueOf(frecuencias.get(i).getWeight()));
            //bWriter.newLine();
        //}
        bWriter.write(chars);
        bWriter.close();
    }

    public String fileToBin () throws IOException {
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder res = new StringBuilder();
        int caract = br.read();
        while(caract != -1) {
            String temp = Integer.toBinaryString(caract);
            while(temp.length() < 8) {
                temp = '0' + temp;
            }
            res.append(temp);
            caract = br.read();
        }
        br.close();
        return res.toString();
    }

    public String decodificar(String codigo) throws IOException {
        StringBuilder res = new StringBuilder();
        NodoB<Character> temp = huffman.getRaiz();
        for (int i = 0; i < codigo.length(); i++) {
            if(codigo.charAt(i) == '0')
                temp = temp.getHijoIzq();
            else
                temp = temp.getHijoDer();

            if(temp.getHijoIzq() == null && temp.getHijoDer() == null) {
                if (temp.getLlave() == (char)3) {
                    break; //Cambiar ciclo por while y borrar break
                }
                res.append(temp.getLlave());
                temp = huffman.getRaiz();
            }
        }
        return res.toString();
    }

    public void descomprimir() throws IOException {
        String res = decodificar(fileToBin());
        FileWriter fw = new FileWriter(outDir);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(res);
        bw.close();
    }

    public String treeToString(NodoB<Character> nodo, String codigo) {
        if (nodo != null) {
            if(nodo.getHijoIzq() == null && nodo.getHijoDer() == null) {
                String aux = Integer.toBinaryString(nodo.getLlave());
                while(aux.length() < 8) {
                    aux = '0' + aux;
                }
                codigo += '1' + aux;
            }
            else {
                codigo = treeToString(nodo.getHijoIzq(), codigo + '0');
                codigo = treeToString(nodo.getHijoDer(), codigo + '0');
            }
        }
        return codigo;
    }

    public String treeToString() {
        return treeToString(huffman.getRaiz(), "");
    }

    public char[] treeToBinary() {
        String tree = treeToString();
        String[] bytes = tree.split("(?<=\\G.{8})");
        while(bytes[bytes.length - 1].length() < 8) {
            bytes[bytes.length - 1] += '0';
        }
        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = (char)Integer.parseInt(bytes[i], 2);
        }
        return chars;
    }

    public void writeTree(String dir) throws IOException {
        char[] tree = treeToBinary();
        FileWriter fw = new FileWriter(dir + "huffman.tree");
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(codigos.size());
        writer.write(tree);
        writer.close();
        fw.close();
    }

    public String readTree(File archivo) throws IOException {
        FileReader fr = new FileReader(archivo);
        BufferedReader reader = new BufferedReader(fr);
        StringBuilder tree = new StringBuilder();
        nCaracteres = reader.read();
        int caract = reader.read();
        while(caract != -1) {
            String temp = Integer.toBinaryString(caract);
            while(temp.length() < 8) {
                temp = '0' + temp;
            }
            tree.append(temp);
            caract = reader.read();
        }
        return tree.toString();
    }

    public String decodeTree(File archivo) throws IOException {
        String codigo = readTree(archivo);
        StringBuilder tree = new StringBuilder();
        int n = 0;
        int i = 0;
        while(n < nCaracteres) {
            if(codigo.charAt(i) == '0') {
                tree.append(0);
                i++;
            }
            else {
                tree.append(1);
                String temp = codigo.substring(i + 1, i + 9);
                char caract = (char)Integer.parseInt(temp, 2);
                tree.append(caract);
                i += 9;
                n++;
            }
        }
        return tree.toString();
    }

    public void treeFromFile(File archivo) throws IOException {
        String codigo = decodeTree(archivo);
        Stack<NodoB<Character>> stack = new Stack<>();
        NodoB<Character> root = new NodoB<>();
        for (int i = 0; i < codigo.length(); i++) {
            if(codigo.charAt(i) == 0) {
                NodoB<Character> aux = new NodoB<>();
                if(root.getHijoIzq() != null)
                    root.setHijoDer(aux);
                else
                    root.setHijoIzq(aux);
                stack.add(root);
                root = aux;
            }
            else {
                NodoB<Character> aux = new NodoB<>(codigo.charAt(++i));
                if(root.getHijoIzq() != null)
                    root.setHijoDer(aux);
                else
                    root.setHijoIzq(aux);
                root = stack.pop();
            }
        }

    }


    public static void main(String[] args) {
        Compresor<Character> compresor = new Compresor<>();
        try {
            compresor.setOutDir("D:\\prueba.zap");
            compresor.cargarComprimir(new File("D:\\prueba.txt"));
            compresor.writeTree("D:\\");
            System.out.println(compresor.treeToString());
            System.out.println(compresor.decodeTree(new File("D:\\huffman.tree")));
            compresor.comprimir();



            compresor.setOutDir("D:\\descomprimido.txt");
            compresor.cargarDescomprimir(new File("D:\\prueba.zap"));
            compresor.descomprimir();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
