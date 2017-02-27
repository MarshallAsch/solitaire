/* Class to sort the score info for when it is displayed in the high score panels
 * extends class ArrayList
 * 
 */


import java.util.*;
import java.lang.*;

public class Sort  extends ArrayList<ArrayList>
{
     private ArrayList<ArrayList> info;
     private ArrayList<ArrayList> temp = new ArrayList<ArrayList>();
     private int length, sortBy;
     
     //can sort by score, time, or number of moves
     public  Sort(ArrayList<ArrayList> input, int sortOption) {
          info = input;
          sortBy = sortOption;
          
          //validate input of the sorting options
          if (sortBy > 2 || sortBy < 0)
               sortBy = 0;
          length = input.get(1).size();
          
          //lists that contain the info 
          temp.add(new ArrayList<Integer>());
          temp.add(new ArrayList<Integer>());
          temp.add(new ArrayList<Integer>());
          temp.add(new ArrayList<String>());
          temp.add(new ArrayList<Character>());
          
          //fill temporary arraylists with meaningless values
          for (int i = 0; i < info.get(0).size(); i++)
          {
               temp.get(0).add(0);
               temp.get(1).add(0);
               temp.get(2).add(0);
               temp.get(3).add("name");
               temp.get(4).add('B');
          }
          
          //do the sort
          doMergeSort(0, length - 1);
          
          //add sorted to this
          for (int i = 0; i < 5; i++)
          {
               add(info.get(i));
          }     
     }
     
     //recursive fucntion #1 to split the list
     private  void doMergeSort(int lower, int higher) {
          
          if (lower < higher) {
               int middle = lower + (higher - lower) / 2;
               
               doMergeSort(lower, middle);   //split begining
               
               doMergeSort(middle + 1, higher);  //split end
               
               mergeParts(lower, middle, higher);  //merge
          }
     }
     
     
     //recursive function #2 to combine the list
     private  void mergeParts(int lower, int middle, int higher)
     {
          for (int i = lower; i <= higher; i++) {
               for (int j = 0; j < 5; j++)
               {
                    temp.get(j).set(i, info.get(j).get(i));
               }
          }
          int i = lower;
          int j = middle + 1;
          int k = lower;
          while (i <= middle && j <= higher)
          {
               if (((Integer)temp.get(sortBy).get(i)).compareTo(((Integer)temp.get(sortBy).get(j))) <= 0)
               {
                    for (int x = 0; x < 5; x++)
                    {
                         info.get(x).set(k, temp.get(x).get(i));   
                    }
                    
                    i++;
               }
               else 
               {
                    for (int x = 0; x < 5; x++)
                    {
                         info.get(x).set(k, temp.get(x).get(j));   
                    }
                    
                    j++;
               }
               k++;
          }
          while (i <= middle) 
          {
               for (int x = 0; x < 5; x++)
               {
                    info.get(x).set(k, temp.get(x).get(i));
               }
               
               k++;
               i++;
          }
     }
}