package larhdid;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MyCompressionV3 {
    Map<String, String> charNewAscii = new HashMap<>();//map to store the new ascii code for the characters

    //returns the dictionary
    public void compress(Map<String, Double> charOldAscii){
        List<Double> tab_results = new ArrayList<>();//results of each iteration ( the subtraction of numbers to get the Splitting point)
        List<Integer> temp = new ArrayList<>();//a temporary list to store the ascii while calculating;
        AtomicInteger border = new AtomicInteger(1);//Splitting point its one by default
        //i am using atomic because i am changing a variable inside collection.foreach
        AtomicBoolean flag = new AtomicBoolean(true);
        charOldAscii.forEach((key, value)->{
            if(flag.get()){
                int curIndex = Integer.parseInt(key.substring(1));
                AtomicReference<Double> sum1 = new AtomicReference<>((double) 0);
                AtomicReference<Double> sum2 = new AtomicReference<>((double) 0);
                //break doesn't work in the foreach ( i should change the code to use takeWhile )
                //charOldAscii.keySet().stream().takeWhile(condition).foreach(...)
                if(border.get()>1)
                    return;
                charOldAscii.forEach((key2, value2)->{
                    if(Integer.parseInt(key2.substring(1))<=Integer.parseInt(key.substring(1))){
                        sum1.updateAndGet(v -> (v + charOldAscii.get(key2)));
                    }
                });

                charOldAscii.forEach((key2, value2)->{
                    if(Integer.parseInt(key2.substring(1))>Integer.parseInt(key.substring(1))){
                        sum2.updateAndGet(v -> (v + charOldAscii.get(key2)));
                    }
                });

                double result_temp = sum1.get()-sum2.get();
                if(tab_results.size()==0){
                    for(int i = 0; i<curIndex ;i++){
                        tab_results.add(Double.MAX_VALUE);
                    }
                }
                tab_results.add(curIndex,result_temp>0?result_temp:-1*result_temp);
                //System.out.println(tab_results+" -- "+curIndex);
                //tab_results only has indexes : 0 and 1 at the moment
                //the condition is true and you're asking it to give you the value of index
                // curIndex-1 = 3-1 = 2
                // !important : you should check if the charOldAscii's size is == 2, we give the first 0 and the second 1
                if(tab_results.size()>1 && tab_results.get(curIndex-1)<tab_results.get(curIndex)){
                    border.set(curIndex);
                    flag.set(false);
                }

            }
            else
                return;
        });

        int finalBorder = border.get();
        Map<String, Double> char0Ascii = new HashMap<>();
        Map<String, Double> char1Ascii = new HashMap<>();
        charOldAscii.forEach((key, value)->{
            int curIndex = Integer.parseInt(key.substring(1));
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

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        Map<String, Double> charOldAscii = new HashMap<>();
        System.out.println("Size of array");
        int n = s.nextInt();

        MyCompressionV3 v3 = new MyCompressionV3();// next version you give a text in the constructor and based on it we get the percentages

        System.out.println("Enter the percentages : ");
        for(int i=0; i<n; i++){
            System.out.println(String.format("x%d", i));
            charOldAscii.put("x"+i,s.nextDouble());
        }

        v3.compress(charOldAscii);
        System.out.println(v3.toString());
    }
}
