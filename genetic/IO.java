package genetic;

import java.io.*;

public abstract class IO {

    public static String readFile(String filename) {

        String result = "";
        FileReader file = null;

        try {
            file = new FileReader(filename);
            BufferedReader reader = new BufferedReader(file);
            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
        return result;
    }

    public static void writeToFile(String filename, String str) {
        try {
            File file = new File(filename);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(str);
            output.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}