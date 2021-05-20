import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PruebaBinarios {

    public static void main(String[] args) {
        File file = new File("D:\\prueba2.txt");
        byte b = Byte.parseByte("10011", 2);
        try {
            FileWriter fw;
            BufferedWriter bw;
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("Archivo de prueba\n".getBytes(StandardCharsets.UTF_8));
            fos.write(b);
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytes);
            System.out.println(Arrays.toString(bytes));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
