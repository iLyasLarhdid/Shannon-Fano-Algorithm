package larhdid;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MyCompressionV4Finished {
    Map<Integer, String> charNewAscii = new TreeMap<>();//map to store the new ascii code for the characters
    public static int numberPOfBits = 16;



    public void compress(Map<Integer, Double> charOldAscii){
        List<Double> tab_results = new ArrayList<>();//results of each iteration ( the subtraction of numbers to get the Splitting point)
        AtomicInteger border = new AtomicInteger(1);//Splitting point its one by default
        //i am using atomic because i am changing a variable inside collection.foreach
        AtomicBoolean flag = new AtomicBoolean(true);
        charOldAscii.forEach((key, value)->{
            if (flag.get()){
                int curIndex = key;
                AtomicReference<Double> sum1 = new AtomicReference<>((double) 0);
                AtomicReference<Double> sum2 = new AtomicReference<>((double) 0);
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
                if(tab_results.size()>1 && tab_results.get(key-1)<tab_results.get(key)){
                    border.set(key);
                    flag.set(false);
                }
            }

        });

        int finalBorder = border.get();
        Map<Integer, Double> char0Ascii = new TreeMap<>();
        Map<Integer, Double> char1Ascii = new TreeMap<>();
        charOldAscii.forEach((key, value)->{
            int curIndex = key;
            if(curIndex < finalBorder){
                char0Ascii.put(key,charOldAscii.get(key));
                charNewAscii.put(key,charNewAscii.getOrDefault(key,"")+"0");
            }else{
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
        return "MYCompressionV4{" +
                "charNewAscii=" + charNewAscii +
                '}';
    }




























    // convert the result+dictionary into binary and send it to over the network https://stackoverflow.com/questions/4211705/binary-to-text-in-java
    // get the data and decode it back to the text
}
