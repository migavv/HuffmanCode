import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Compresor {
    File archivo;
    ArbolH<Character> huffman;
    int nCaracteres;
    int treeSize;
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
        br.close();
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
        String salida = new String(chars);
        System.out.println(new String(chars)); //Linea de prueba
        FileWriter writer = new FileWriter(outDir);
        BufferedWriter bWriter = new BufferedWriter(writer);
        writeTree(outDir, bWriter);
        bWriter.write(chars);
        bWriter.close();
    }

    public String fileToBin () throws IOException {
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder res = new StringBuilder();
        readTree(br);
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
        int i = 0;
        while (i < codigo.length()) {
            if(codigo.charAt(i) == '0')
                temp = temp.getHijoIzq();
            else
                temp = temp.getHijoDer();

            if(temp.getHijoIzq() == null && temp.getHijoDer() == null) {
                if (temp.getLlave() == (char)3) {
                    return res.toString();
                }
                res.append(temp.getLlave());
                temp = huffman.getRaiz();
            }
            i++;
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

    public StringBuilder treeToString(NodoB<Character> nodo, StringBuilder codigo) {
        if (nodo != null) {
            codigo = treeToString(nodo.getHijoIzq(), codigo);
            codigo = treeToString(nodo.getHijoDer(), codigo);
            if(nodo.getHijoIzq() == null && nodo.getHijoDer() == null) {
                char ch = nodo.getLlave();
                codigo.append('1').append(ch);//codigo += '1' + ch;
            }
            else
                codigo.append('0');//codigo += '0';
        }
        //codigo += '0';
        treeSize = codigo.length();
        return codigo;
    }

    public StringBuilder treeToString() {
        return treeToString(huffman.getRaiz(), new StringBuilder());
    }

    public char[] treeToBinary() {
        String tree = treeToString().toString();
        String[] bytes = tree.split("(?<=\\G.{8})");
        while(bytes[bytes.length - 1].length() < 8) {
            bytes[bytes.length - 1] += '0';
        }
        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = (char)Integer.parseInt(bytes[i], 2);
        }
        treeSize = chars.length;
        return chars;
    }

    public void writeTree(String dir, BufferedWriter writer) throws IOException {
        String tree = treeToString().toString();
        System.out.println(tree);
        writer.write(treeSize);
        writer.write(tree);
    }

    public String readTree(BufferedReader reader) throws IOException {
        StringBuilder tree = new StringBuilder();
        int c = 0;
        treeSize = reader.read();
        int caract = reader.read();
        while(c <  treeSize) {
            tree.append((char)caract);
            c++;
            if(c < treeSize)
                caract = reader.read();
        }
        return tree.toString();
    }

    public String decodeTree(BufferedReader reader) throws IOException {
        String codigo = readTree(reader);
        StringBuilder tree = new StringBuilder();
        int n = 0;
        int i = 0;
        while(n < nCaracteres - 1) {
            if(codigo.charAt(i) == '0') {
                tree.append(0);
                i++;
                n++;
            }
            else {
                tree.append(1);
                String temp = codigo.substring(i + 1, i + 9);
                char caract = (char)Integer.parseInt(temp, 2);
                tree.append(caract);
                i += 9;
            }
        }
        return tree.toString();
    }

    public void treeFromFile(BufferedReader reader) throws IOException {
        String codigo = decodeTree(reader);
        Stack<NodoB<Character>> stack = new Stack<>();
        int i = 0;
        while (i < codigo.length()) {
            if(codigo.charAt(i) == '1') {
                Character caract = codigo.charAt(++i);
                stack.add(new NodoB<Character>(caract));
            }
            else {
                if(stack.size() > 1) {
                    NodoB<Character> aux = new NodoB<>();
                    aux.setHijoDer(stack.pop());
                    aux.setHijoIzq(stack.pop());
                    stack.add(aux);
                }
            }
            i++;
        }
        while(stack.size() > 1) {
            NodoB<Character> aux = new NodoB<>();
            aux.setHijoDer(stack.pop());
            aux.setHijoIzq(stack.pop());
            stack.add(aux);
        }
        huffman = new ArbolH<>(stack.pop());
    }


    public static void main(String[] args) {
        Compresor compresor = new Compresor();
        try {
            compresor.setOutDir("D:\\prueba.zap");
            compresor.cargarComprimir(new File("D:\\prueba.txt"));
            System.out.println(compresor.treeToString());
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
