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
        MyCompressionV4Finished v4 = new MyCompressionV4Finished();// next version you give a text in the constructor and based on it we get the percentages

        //instead of writing down the possibilities, we should generate them from a text
        System.out.println("Enter the text : ");
        text = s.nextLine();
        for(int i = 0; i<text.length(); i++){
            String key = String.valueOf(text.charAt(i));
            charOldAscii.put(key,charOldAscii.getOrDefault(key,0.)+1);
        }
//        charOldAscii.forEach((key,value)->{
//            charOldAscii.put(key,charOldAscii.get(key)/charOldAscii.size());
//        });

        AtomicInteger counter = new AtomicInteger();
        charOldAscii.forEach((key,value)->{
            counter.getAndIncrement();
            dictAscii.put(key,Integer.parseInt(String.valueOf(counter)));
            charOldAsciiTempSolution.put(Integer.parseInt(String.valueOf(counter)),charOldAscii.get(key)/charOldAscii.size());
        });

        System.out.println("Dictionary table"+dictAscii);
        //v4.compress(charOldAscii);
        v4.compress(charOldAsciiTempSolution);
        System.out.println(v4.toString());
    }
}
