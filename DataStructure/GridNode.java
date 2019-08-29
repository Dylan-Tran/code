public class GridNode {
    GridNode right;
    GridNode down;
    Object data;
    
    public GridNode(Object data)
    {
   	 this.data = data;
    }
    
    public GridNode(boolean isTall, Object ... data)
    {   			 
   	 this.data = data[0];
   	 GridNode current = this;
   	 for(int i = 1;i < data.length;i++)
   	 {
   		 if(isTall)
   		 {
   			 current.down = new GridNode(data[i]);   			 
   			 current = current.down;
   		 }
   		 else
   		 {
   			 current.right = new GridNode(data[i]);   			 
   			 current = current.right;
   		 }
   	 }
    }
   	 
    public int width()
    {
   	 GridNode current = this;
   	 int count = 0;
   	 while(current != null)
   	 {
   		 count++;
   		 current = current.right;
   	 }   	 
   	 return count;
    }
    public int height()
    {
   	 GridNode current = this;
   	 int count = 0;
   	 while(current != null)
   	 {
   		 count++;
   		 current = current.down;
   	 }   	 
   	 return count;
    }
        
    public String toString(){
      	 StringBuilder S = new StringBuilder();
      	 GridNode row = this;
      	 GridNode col = this;
      	 while(col != null)
      	 {
      		 while(row != null)
      		 {
      			if(row.data == null){
      				S.append("NULL");
   				} else{
   					S.append(row.data.toString());
      			}
      			
      			S.append(" ");
      			row = row.right;
      		 }
      		 S.append("\n");
      		 col = col.down;
      		 row = col;
      	 }
      	 return S.toString();   	 
       }    
}