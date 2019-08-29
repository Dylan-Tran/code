import java.util.*;
import java.io.*;
public class maze {
  public static void main(String[] args) {
    Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
    int t = in.nextInt();  // Scanner has functions to read ints, longs, strings, chars, etc.
    for (int j = 1; j <= t; ++j) {
    	int size = in.nextInt();
    	String path = in.next();
    	int counter = 0;
    	String builder = "";
    	for(int i = 0; i < path.length(); i++) {
    		if(path.charAt(i) == 'E') {
    			builder += "S";
    		} else {
    			builder += "E";
    		}
      }
      
      System.out.println("Case #" + j + ": "+  builder);
    }
  }
}