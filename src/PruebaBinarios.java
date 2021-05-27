import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PruebaBinarios {

    public static void write(File file, BufferedWriter writer) throws IOException {
        writer.write("hola");
    }

    public static void main(String[] args) {
        char c = '1';
        int i = 3;
        System.out.println((char)Integer.parseInt("1000000000000", 2));
        System.out.println('“');
        System.out.println(Integer.toBinaryString('"'));
        c = (char)3;
        System.out.println(c);
        File file = new File("D:\\prueba2.txt");
        try {

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            FileReader re = new FileReader("D:\\prueba.zap");
            System.out.println((int)re.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte b = Byte.parseByte("10011", 2);

        String p = "Miguel";
        String u = p.substring(1, 6);
        System.out.println(u);
    }
}
