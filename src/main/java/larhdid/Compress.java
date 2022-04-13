package larhdid;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Compress {

    public static void main(String[] args) throws IOException {

        String filePath = "/myText.txt";
        boolean shouldCreateFile = true;
        if(args.length>0){
            filePath=args[0];
            shouldCreateFile = false;
        }

        File myObj = new File(Paths.get("").toAbsolutePath().toString()+filePath);
        if (shouldCreateFile) {
            myObj.createNewFile();
            FileWriter writer = new FileWriter(myObj);
            Scanner s = new Scanner(System.in);
            writer.write(s.nextLine());
            writer.close();
        }

        Map<String, Double> charOldAscii = new TreeMap<>();
        Map<Integer, Double> charOldAsciiTempSolution = new TreeMap<>();
        Map<String, Integer> dictAscii = new TreeMap<>();
        Map<String, String> dictJson = new TreeMap<>();
        //JSONObject dictJson = new JSONObject();
        String text="";
        String newText = "";
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            text+=data;
        }
        myReader.close();
        InputStream inputStream = new FileInputStream(Paths.get("").toAbsolutePath().toString()+"/myText.txt");
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
        dictAscii.forEach((key,value)->{
            dictJson.put(key,v4.charNewAscii.get(value));
        });
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
        if(newText.length()%16>0){
            for (int i = 0 ; i<(16-newText.length()%16); i++){
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
        for(int i = 0 ; i<newText2.length();i += 16){//each character is 32bits
            //int charCode = Integer.parseInt(newText.substring(i,i+32), 2);
            int charCode = Integer.parseUnsignedInt(newText2.substring(i,i+16), 2);
            // Then if you want the corresponding character as a string:
            test+= Character.toString((char) charCode);
        }
        System.out.println("test : "+test+"\nDictJson : "+dictJson.toString());
        FileWriter newFile = new FileWriter(Paths.get("").toAbsolutePath().toString()+"/compressed.txt");
        newFile.write(test);
        newFile.close();
        FileWriter newFileDict = new FileWriter(Paths.get("").toAbsolutePath().toString()+"/dict.txt");
        newFileDict.write(newText.length()+":number\n");
        dictJson.forEach((key,value)->{
            try {
                newFileDict.append(key+":"+value+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        newFileDict.close();


    }
}
