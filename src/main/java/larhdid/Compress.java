package larhdid;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Compress {

    public static void main(String[] args) throws IOException {

        String filePath = Paths.get("").toAbsolutePath().toString()+"/myText.txt";
        boolean shouldCreateFile = true;
        if(args.length>0){
            filePath=args[0];
            shouldCreateFile = false;
        }

        File myObj = new File(filePath);
        if (!myObj.isFile() && !myObj.canWrite() && shouldCreateFile) {
            myObj.createNewFile();
            System.out.println("Enter text : ");
            FileWriter writer = new FileWriter(myObj);
            Scanner s = new Scanner(System.in);
            writer.write(s.nextLine());
            writer.close();
        }

        Map<String, Double> charOldAscii = new TreeMap<>();
        Map<Integer, Double> charOldAsciiTempSolution = new TreeMap<>();
        Map<String, Integer> dictAscii = new TreeMap<>();
        Map<String, String> dictJson = new TreeMap<>();
        String text="";
        String newText = "";
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            text+=data;
        }
        myReader.close();
        InputStream inputStream = new FileInputStream(filePath);
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
//        System.out.println("Dictionary table"+dictAscii);
        v4.compress(charOldAsciiTempSolution);
//        System.out.println(v4.toString());

        for(int i = 0; i< text.length();i++){
            newText += v4.charNewAscii.get(dictAscii.get(String.valueOf(text.charAt(i))));
        }
        dictAscii.forEach((key,value)->{
            dictJson.put(key,v4.charNewAscii.get(value));
        });
        String test = "";
        String newText2 = newText;
        if(newText.length()%MyCompressionV4Finished.numberPOfBits>0){
            for (int i = 0 ; i<(MyCompressionV4Finished.numberPOfBits-newText.length()%MyCompressionV4Finished.numberPOfBits); i++){
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
        System.out.println("compressed data : "+newText);
        for(int i = 0 ; i<newText2.length();i += MyCompressionV4Finished.numberPOfBits){//each character is 10bits
            //int charCode = Integer.parseInt(newText.substring(i,i+10), 2);
            int charCode = Integer.parseUnsignedInt(newText2.substring(i,i+MyCompressionV4Finished.numberPOfBits), 2);
            // Then if you want the corresponding character as a string:
            test+= Character.toString((char) charCode);
        }

        FileWriter newFile = new FileWriter(Paths.get("").toAbsolutePath().toString()+"/compressed.txt");
        newFile.write(test);
        newFile.close();
        FileWriter newFileDict = new FileWriter(Paths.get("").toAbsolutePath().toString()+"/dict.txt");
        newFileDict.write(newText.length()+":n\n");
        dictJson.forEach((key,value)->{
            try {
                newFileDict.append(key+":"+value+"\n");
                System.out.println(key+":"+value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        newFileDict.close();


    }
}
