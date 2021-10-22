package utils;

import java.io.*;
import java.util.Vector;

public class FileUtil {

    public static Vector<String> file2StrVec(File file) throws NumberFormatException {
        FileReader fr;
        BufferedReader br = null;
        Vector<String> strVec = new Vector<>();
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String str;
            while ((str = br.readLine()) != null){
                int index = Integer.parseInt(str.substring(0, str.indexOf('.')));
                str = str.substring(str.indexOf('.') + 1).replaceAll(" ", "");
                strVec.add(index - 1, str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return strVec;
    }

    public static void appendStr2File(String str, String path) throws IOException {
        FileWriter fw = null;
        try {
            File file = new File(path);
            if (!file.exists()) file.createNewFile();
            fw = new FileWriter(file, true);
            fw.write(str + "\n");
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (fw != null) fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        fw.write("");
        fw.flush();
        fw.close();
    }
}
