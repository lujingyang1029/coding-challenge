// example of program that calculates the  median degree of a
// venmo transaction graph
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package readfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author luj2
 */
public class median_degree {

    /**
     * @param args the command line arguments
     */


public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {
        // TODO code application logic here
	if (args.length != 2) {
		return;
	}

        String path = args[0];
        File file = new File(path);
        BufferedReader reader = null;
        String laststr ="";
        reader = new BufferedReader(new FileReader(file));
        String readline ="";
//        int line = 1;
//        StringBuilder a =  new StringBuilder();
        String tmp ="";
        ArrayList<Long>  list_time = new ArrayList<Long>(); // Save all the time within the time arrange
        ArrayList<String> list_actor = new ArrayList<String>(); // Save the corresponding actor list
        ArrayList<String> list_target = new ArrayList<String>(); // Save the corresponding target list
        Map<String, Integer> map = new HashMap<String, Integer>();  // The map is used to calculate the median
        long time_bound= 0; // timebound is used to keep the latest time.
        boolean flag= false; // checking whether the new transaction already exists
        PrintWriter writer = new PrintWriter(args[1], "UTF-8");



        while ((readline = reader.readLine()) != null) {
            tmp=readline;
           // System.out.println(tmp);
            JSONParser parser= new JSONParser();
            Object obj =  parser.parse(tmp);
            JSONObject jsonObject = (JSONObject) obj;
            String actor= (String) jsonObject.get("actor");
            String target= (String) jsonObject.get("target");
            String created_time= (String) jsonObject.get("created_time");
            DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date d1 = date_format.parse(created_time);
            if(map.isEmpty()==true){
                map.put(actor, 1);
                map.put(target,1);
                time_bound=d1.getTime();
                list_actor.add(actor); // sava actors in the ArrayList;
                list_target.add(target);// save target in the ArrayList;
                list_time.add(d1.getTime()); // save the time in the Arraylist;
            }else{
                if (d1.getTime()> time_bound  ) {
                    time_bound= d1.getTime();
                    //  Check whether there already exists the new input.
                    flag= false;
                    for(int l=0;l<list_time.size();l++){
                        if((list_actor.get(l).equals(actor)) && (list_target.get(l).equals(target))|| (list_actor.get(l).equals(target) && list_target.get(l).equals(actor)) ){
                            list_time.set(l, d1.getTime());
                            flag = true;
                        }
                    }
                    if(flag == false){
                        list_actor.add(actor); // sava actors in the ArrayList;
                        list_target.add(target);// save target in the ArrayList;
                        list_time.add(d1.getTime()); // save the time in the Arraylist;
                        // Update the Map!!!!
                        if (!map.containsKey(actor)) {
                            map.put(actor, 1);
                         }else {
                            // there exists the element;
                            int num = map.get(actor);
                            map.put(actor, ++num);
                        }
                        // Save the target in the Map becasue this action is within time range.
                        if (!map.containsKey(target)) {
                            map.put(target, 1);
                         }else {
                            // there exists the element;
                            int num = map.get(target);
                            map.put(target, ++num);
                        }
                        // delete the item outside the time range.
                        int up= list_time.size();
                        for(int i= 0; i<up;i++){
                            Long duration= time_bound-list_time.get(i);
                            Long totalSeconds = duration / 1000;// total seconds
                            if(totalSeconds>60){
                                // Update the map delete all the nodes those are out of timerange
                                int num1 = map.get(list_actor.get(i));
                                num1--;
                                map.put(list_actor.get(i), num1);
                            //    if (num1== 0) map.remove(actor);
                                int num2 = map.get(list_target.get(i));
                                num2--;
                                map.put(list_target.get(i), num2);
                            //    if (num2== 0) map.remove(target);

                                list_actor.remove(i);
                                list_target.remove(i);
                                list_time.remove(i);
                                i--;
                                up--;

                            }
                        }
                    }

                }else if(d1.getTime()>=time_bound-60 && d1.getTime()<=time_bound){
                    flag= false;
                    for(int l=0;l<list_time.size();l++){
                        if((list_actor.get(l).equals(actor)) && (list_target.get(l).equals(target))|| (list_actor.get(l).equals(target) && list_target.get(l).equals(actor)) ){
                            list_time.set(l, d1.getTime());
                            flag = true;
                        }
                    }
                    if(flag == false){
                        list_actor.add(actor);
                        list_target.add(target);
                        list_time.add(d1.getTime());
                        // Save the actor in the Map because this action is within time range.
                        if (!map.containsKey(actor)) {
                            map.put(actor, 1);
                         }else {
                            // there exists the element;
                            int num = map.get(actor);
                            map.put(actor, ++num);
                        }
                        // Save the target in the Map becasue this action is within time range.
                        if (!map.containsKey(target)) {
                            map.put(target, 1);
                         }else {
                            // there exists the element;
                            int num = map.get(target);
                            map.put(target, ++num);
                        }
                    }
                    //save the actor, target, and time in the arraylist

                }
            }
            while( map.values().remove(0));
            int[] arr1= new int[map.size()];
            int j=0;
            for (Integer value : map.values()) {
                arr1[j]=value;
                j++;
            }
            double res1 ;
            Arrays.sort(arr1);
            if(arr1.length%2 ==1){
                res1= (double) arr1[arr1.length/2];
            }else{
                res1= (double) (arr1[arr1.length/2]+arr1[arr1.length/2-1])/2;
            }
//            System.out.println(res1);

            DecimalFormat res_for = new DecimalFormat("0.00");
            String res_final = res_for.format(res1);
            writer.println(res_final);


/*
            Long duration= d2.getTime() - d1.getTime();
            Long totalSeconds = duration / 1000;// total seconds
            System.out.print(totalSeconds);
*/
        }

        reader.close();
        writer.close();


    }

}
