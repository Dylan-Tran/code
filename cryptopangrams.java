//Didnt work
import java.util.*;
import java.io.*;
public class cryptopangrams {

	public static class pair {
		int x;
		int y;
		
		public pair(int one, int two){
			this.x = one;
			this.y = two;
		}
		
	}
	public static int match(pair one, pair two) {
		if(one.x == two.x) {
			return one.x;
		}
		if(one.x == two.y) {
			return one.x;
		}
		if(one.y == two.x) {
			return one.y;
		}
		if(one.y == two.y) {
			return one.y;
		}
		return 0;
	}
	
  public static void main(String[] args) {
    Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
    int t = in.nextInt();  // Scanner has functions to read ints, longs, strings, chars, etc.
    for (int j = 1; j <= t; ++j) {
      int n = in.nextInt();
      int L = in.nextInt();
      pair[] storage = new pair[L];
      ArrayList<Integer> List = new ArrayList<>();
      for(int k = 0; k < L; k++) {
    	  int temp = in.nextInt();
    	  for(int i = 2; i <= n; i++) {
    		  if(temp % i == 0) {
    			  int second = temp/i;
    			  storage[k] = new pair(i, second);
    			  if(!List.contains(i)) {
    				  List.add(i);
    			  }
    			  if(!List.contains(second)) {
    				  List.add(second);
    			  }
    			  break;
    		  }
    	  }
    	  
      }
      Collections.sort(List);
      String builder = "";
      for(int i = 0; i < storage.length-1; i++) {
    	  builder += (char) (List.indexOf(match(storage[i], storage[i+1]))+65);
      }
      
      //First
      if(storage[0].x == match(storage[0],storage[1])) {
    	  builder = (char) (List.indexOf(storage[0].y) + 65) + builder;
      } else {
    	  builder = (char) (List.indexOf(storage[0].x) + 65) + builder;
      }
      
      //End
      if(storage[storage.length-1].x == match(storage[storage.length-2],storage[storage.length-1])) {
    	  builder = builder + (char) (List.indexOf(storage[storage.length-1].y) + 65);
      } else {
    	  builder = builder + (char) (List.indexOf(storage[storage.length-1].x) + 65);
      }
      System.out.println("Case #" + j + ": "+  builder);
    }
  }
}