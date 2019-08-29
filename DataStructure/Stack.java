public class Stack {
	private Node top = null;
	
	public void push(Object inp) {
		Node temp = new Node(inp);
		if(this.top == null){
			this.top = temp;
	   	}else{
	   		temp.next = this.top;
	   		this.top = temp;
	   	 }
	}
	
	public Object pop() {
		Object temp;
		if(this.top == null) {
			temp = null;
		} else {
			temp = this.top.data;
			this.top = this.top.next;
		}
		return temp;
	}
	
	public int size() {
		if(this.top == null) {
			return 0;
		} else {
			int count = 1;
			Node curr = this.top;
			while(curr.next != null) {
				curr = curr.next;
				count++;
			}
			return count;
		}
		
	}
	
	public boolean contains(Object search) {
		if(this.top == null) {
			return false;
		} else {
			Node curr = this.top;
			while(curr.next != null) {
				if(curr.data.equals(search)) {
					return true;
				}
				curr = curr.next;
			}
		}
		return false;
	}
	
	public Object removeFromBottom() {
		if(this.top == null) {
			return null;
		} else {
			Node curr = this.top;
			while(curr.next.next != null) {
				curr = curr.next;
			}
			Object a = curr.next.data;
			curr.next = null;
			
			return a;
		}
	}
	
	public boolean containsSubStack(Stack wow) {
		if(this.top == null) {
			return false;
		} else {
			Node curr = this.top;
			Node curr2 = wow.top;
			int counter = 0;
			while(curr != null) {
				if(curr.data == curr2.data) {
					counter++;
					curr2 = curr2.next;
					if(counter == wow.size()) {
						return true;
					}
				} else {
					counter = 0;
					curr2 = wow.top;
				}
				
				curr = curr.next;
			}
			return false;
		}
	}
	
	public Stack concatenate(Stack concat) {
		Stack newStack = new Stack();
		Stack first = new Stack();
		Stack sec = new Stack();	
		Node curr = this.top;
		Node curr2 = concat.top;
		
		while(curr != null) {
			first.push(curr.data);
			curr = curr.next;
		}
		
		while(curr2 != null) {
			sec.push(curr2.data);
			curr2 = curr2.next;
		}
		
		while(first.size() != 0) {
			newStack.push(first.pop());
		}
		
		while(sec.size() != 0) {
			newStack.push(sec.pop());
		}

		return newStack;
	}
	
	public void print() {
		Node curr = this.top;
		while(curr != null) {
			System.out.print(curr.data + " ");
			curr = curr.next;
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		Stack S = new Stack();
		S.push(1);
		S.push(2);
		S.push(3);

		Stack f = new Stack();
		f.push(5);
		f.push(7);
		f.push(9);
		f.push(11);

		System.out.println(S.size());
		System.out.println(f.size());
		
		S.print();
		f.print();
		
		Stack j = S.concatenate(f);
		
		j.print();

		S.print();
		f.print();
		
		System.out.println(j.containsSubStack(S));
		System.out.println(S.containsSubStack(f));
		
		System.out.println(S.removeFromBottom());
		S.print();
	}
}