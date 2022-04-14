package larhdid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Decompress {
    public static int longestValueInMap(Map<String,String> dictMap){
        AtomicInteger a = new AtomicInteger();
        dictMap.forEach((key,value)->{
            if(!key.equals("number") && key.length()> a.get())
                a.set(key.length());
        });
        return a.get();
    }
    public static String convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%16s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();

    }
    public static void main(String[] args) throws IOException {
        int longestValue = 0;
        String encodedText = "";
        String decodedText = "";
        String finalText = "";
        Map<String,String> dictMap = new HashMap<>();
        //the reverse process
        File encodedFile = new File(Paths.get("").toAbsolutePath().toString()+"/compressed.txt");
        Scanner encodedReader = new Scanner(encodedFile);
        while (encodedReader.hasNextLine()) {
            encodedText += encodedReader.nextLine();
        }
        // decoding process
        File decodedFile = new File(Paths.get("").toAbsolutePath().toString()+"/dict.txt");
        Scanner decodedReader = new Scanner(decodedFile);
        while (decodedReader.hasNextLine()) {
            decodedText = decodedReader.nextLine();
            dictMap.put(decodedText.split("[:]")[1],decodedText.split("[:]")[0]);
        }
        System.out.println(encodedText+"\n---------\n"+dictMap);

        String encodedBits = convertStringToBinary(encodedText);
        System.out.println("encodedBits : "+encodedBits);

        longestValue = longestValueInMap(dictMap);
        System.out.println(longestValue);
        int i = 0;
        while(i<Integer.parseInt(dictMap.get("number"))){
            int jHolder = 0;
            for(int j = i+1; j<=i+longestValue ; j++){
                String sub = encodedBits.substring(i,j);
                if(dictMap.containsKey(sub)){
                    finalText+=dictMap.get(sub);
                    jHolder = j;
                    j = i+longestValue+1;
                }
            }
            i=jHolder;
        }
        System.out.println("text : "+finalText);
        FileWriter newFileDict = new FileWriter(Paths.get("").toAbsolutePath().toString()+"/decodedText.txt");
        newFileDict.write(finalText);
        newFileDict.close();
        /*Arrays.stream("0110100101101100011110010110000101110011".split("(?<=\\G.{8})")).forEach(s1 -> System.out.print((char) Integer.parseInt(s1, 2)));
        System.out.print('\n');*/
    }
}
