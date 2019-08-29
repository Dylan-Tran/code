import java.util.*;
import java.io.*;
public class foregone {
  public static void main(String[] args) {
    Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
    int t = in.nextInt();  // Scanner has functions to read ints, longs, strings, chars, etc.
    for (int i = 1; i <= t; ++i) {
      long n = in.nextInt();
      long a = 0;
      long b = 0;
      String temp = Long.toString(n);
      for(int j = 0; j < temp.length(); j++) {
    	  if(Integer.parseInt(temp.substring(j, j + 1)) == 4) {
    		  a += 3*Math.pow(10, temp.length()-j-1);
    		  b += 1*Math.pow(10, temp.length()-j-1);
    	  } else {
    		  a += Integer.parseInt(temp.substring(j, j + 1))*(int)Math.pow(10, temp.length()-j-1);
    		  
    	  }
      }
      
      System.out.printf("Case #%d: %d %d\n", i, a,b);
    }
  }
}