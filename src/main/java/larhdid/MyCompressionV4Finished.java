package larhdid;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MyCompressionV4Finished {
    Map<Integer, String> charNewAscii = new TreeMap<>();//map to store the new ascii code for the characters

    //returns the dictionary
    public void compress(Map<Integer, Double> charOldAscii){
        List<Double> tab_results = new ArrayList<>();//results of each iteration ( the subtraction of numbers to get the Splitting point)
        List<Integer> temp = new ArrayList<>();//a temporary list to store the ascii while calculating;
        AtomicInteger border = new AtomicInteger(1);//Splitting point its one by default
        //i am using atomic because i am changing a variable inside collection.foreach
        charOldAscii.forEach((key, value)->{
            int curIndex = key;
            AtomicReference<Double> sum1 = new AtomicReference<>((double) 0);
            AtomicReference<Double> sum2 = new AtomicReference<>((double) 0);
            //break doesn't work in the foreach ( i should change the code to use takeWhile )
            //charOldAscii.keySet().stream().takeWhile(condition).foreach(...)
            if(border.get()>1)
                return;
            charOldAscii.forEach((key2, value2)->{
                if(key2<=key){
                    sum1.updateAndGet(v -> (v + charOldAscii.get(key2)));
                }
            });

            charOldAscii.forEach((key2, value2)->{
                if(key2>key){
                    sum2.updateAndGet(v -> (v + charOldAscii.get(key2)));
                }
            });

            double result_temp = sum1.get()-sum2.get();
            if(tab_results.size()==0){
                for(int i = 0; i<key ;i++){
                    tab_results.add(Double.MAX_VALUE);
                }
            }
            tab_results.add(key,result_temp>0?result_temp:-1*result_temp);
            //tab_results only has indexes : 0 and 1 at the moment
            //the condition is true and you're asking it to give you the value of index
            // curIndex-1 = 3-1 = 2
            // !important : you should check if the charOldAscii's size is == 2, we give the first 0 and the second 1
            if(tab_results.size()>1 && tab_results.get(key-1)<tab_results.get(key)){
                border.set(key);
            }

        });

        int finalBorder = border.get();
        Map<Integer, Double> char0Ascii = new TreeMap<>();
        Map<Integer, Double> char1Ascii = new TreeMap<>();
        charOldAscii.forEach((key, value)->{
            int curIndex = key;
            if(curIndex < finalBorder){
                //System.out.println(0);
                //collect 0 if you have more than 2 zeros you call compress
                char0Ascii.put(key,charOldAscii.get(key));
                charNewAscii.put(key,charNewAscii.getOrDefault(key,"")+"0");
            }else{
                //System.out.println(1);
                char1Ascii.put(key,charOldAscii.get(key));
                charNewAscii.put(key,charNewAscii.getOrDefault(key,"")+"1");
            }
        });
        if(char0Ascii.size()>1){
            this.compress(char0Ascii);
        }
        if(char1Ascii.size()>1){
            this.compress(char1Ascii);
        }
    }

    @Override
    public String toString() {
        return "MYCompressionV2{" +
                "charNewAscii=" + charNewAscii +
                '}';
    }

}
