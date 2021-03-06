package larhdid;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MyApp {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Map<String, Double> charOldAscii = new TreeMap<>();
        Map<Integer, Double> charOldAsciiTempSolution = new TreeMap<>();
        Map<String, Integer> dictAscii = new TreeMap<>();
        String text;
        String newText="";
        MyCompressionV4Finished v4 = new MyCompressionV4Finished();

        System.out.println("Enter the text : ");
        text = s.nextLine();
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
    }
}
