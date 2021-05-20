import java.io.*;
import java.util.Arrays;

public class PruebaBinarios {

    public static void main(String[] args) {
        File file = new File("D:\\prueba2.txt");
        byte b = Byte.parseByte("10011", 2);
        String s = "1001010101010";
        String [] str = s.split("(?<=\\G.{8})");
        System.out.println(Arrays.toString(str));
        try {
            FileWriter fw;
            BufferedWriter bw;
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            FileOutputStream fos = new FileOutputStream(file);
            //bw.write(b); bw.newLine();
            bw.close();
            fos.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
