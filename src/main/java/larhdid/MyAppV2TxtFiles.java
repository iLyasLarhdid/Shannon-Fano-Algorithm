package larhdid;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MyAppV2TxtFiles {


    public static String convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();

    }

    public static void main(String[] args) throws IOException {

        //todo : instead of saving bits in String we should use java.util.BitSet
        File myObj = new File(Paths.get("src/main/java/larhdid").toAbsolutePath().toString()+"/myText.txt");
        Map<String, Double> charOldAscii = new TreeMap<>();
        Map<Integer, Double> charOldAsciiTempSolution = new TreeMap<>();
        Map<String, Integer> dictAscii = new TreeMap<>();
        String text="";
        String newText = "";
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            text+=data;
        }
        myReader.close();
        InputStream inputStream = new FileInputStream(Paths.get("src/main/java/larhdid").toAbsolutePath().toString()+"/myText.txt");
        System.out.println("input stream "+inputStream.read());
        MyCompressionV4Finished v4 = new MyCompressionV4Finished();

        for(int i = 0; i<text.length(); i++){
            String key = String.valueOf(text.charAt(i));
            charOldAscii.put(key,charOldAscii.getOrDefault(key,0.)+1);
        }

        AtomicInteger counter = new AtomicInteger();
        charOldAscii.forEach((key,value)->{
            counter.getAndIncrement();
            dictAscii.put(key,Integer.parseInt(String.valueOf(counter)));
            charOldAsciiTempSolution.put(Integer.parseInt(String.valueOf(counter)),charOldAscii.get(key)/charOldAscii.size());
        });

        System.out.println("Probabilities "+charOldAsciiTempSolution);
        System.out.println("Dictionary table"+dictAscii);
        v4.compress(charOldAsciiTempSolution);
        System.out.println(v4.toString());

        for(int i = 0; i< text.length();i++){
            newText += v4.charNewAscii.get(dictAscii.get(String.valueOf(text.charAt(i))));
        }

        System.out.println("new file text : ("+newText+") length : "+newText.length());
        // You can use Integer.parseInt with a radix of 2 (binary) to convert the binary string to an integer:
        // add (8-bits.length()%32) zeros to the end if the length of our bits%32 > 0
        // in our dictionary we should have the number of zeros we have added at the end of our string so that we can delete them
        // this is a temporary solution but we are going to fix it some how because we always know the last element ends with one ...
        // if we have zero at the end we trace it back to its last occurrence and we delete them
        // this is a better approach as we dont have to send number of zeros in the dictionary
        /*byte[] data = text.substring(0,101).getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(data));*/
//        System.out.println(Character.MIN_RADIX);
//        System.out.println(Character.MAX_RADIX);
        String test = "";
        String newText2 = newText;
        if(newText.length()%32>0){
            for (int i = 0 ; i<(32-newText.length()%32); i++){
                newText2+="0";
            }
        }
        /*String fileName = "out.bin";
        FileOutputStream fileOs = new FileOutputStream(fileName);
        ObjectOutputStream os = new ObjectOutputStream(fileOs);
        os.write();
        os.close();*/
//        int charCode2 = Integer.parseInt("-11000101010100101000110000010001", 2);
//        // Then if you want the corresponding character as a string:
//        System.out.println(Character.toString((char) charCode2));
        System.out.println(newText2);
        for(int i = 0 ; i<newText2.length();i += 32){//each character is 32bits
            //int charCode = Integer.parseInt(newText.substring(i,i+32), 2);
            int charCode = Integer.parseUnsignedInt(newText2.substring(i,i+32), 2);
            // Then if you want the corresponding character as a string:
            test+= Character.toString((char) charCode);
        }
        System.out.println("test : "+test);
        FileWriter newFile = new FileWriter(Paths.get("src/main/java/larhdid").toAbsolutePath().toString()+"/myResult.txt");
        newFile.write(test);
        newFile.close();
        /*System.out.println(convertStringToBinary(text));
        Arrays.stream("0110100101101100011110010110000101110011".split("(?<=\\G.{8})")).forEach(s1 -> System.out.print((char) Integer.parseInt(s1, 2)));
        System.out.print('\n');*/
    }
}
