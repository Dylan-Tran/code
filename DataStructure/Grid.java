import java.util.ArrayList;
import java.util.Collections;

public class Grid implements I2dAggregate {
	GridNode topLeft;

	public Grid() {
		this.topLeft = null;
	}

	public Grid(Grid G) {
		/* Creates a deep copy of the other grid. */
		if(G.topLeft == null) {
			this.topLeft = null;
		} else {
			this.topLeft = new GridNode(G.topLeft.data);
			GridNode firstRow = this.topLeft;
			GridNode firstRowG = G.topLeft;
			for(int i = 1; i < G.width(); i++) {
				firstRow.right = new GridNode(firstRowG.right.data);
				firstRow = firstRow.right;
				firstRowG = firstRowG.right;
			}
			
			GridNode current = this.topLeft;
			GridNode other = G.topLeft.down;
			for (int i = 1; i < G.height(); i++) {
				current.down = new GridNode(other.data);
				
				GridNode SweepTop = current.right;
				GridNode SweepRow = current.down;
				GridNode SweepG = other;
				
				for (int j = 1; j < G.width(); j++) {
					SweepRow.right = new GridNode(SweepG.right.data);
					SweepTop.down = SweepRow.right;
					SweepTop = SweepTop.right;
					SweepRow = SweepRow.right;
					SweepG = SweepG.right;
				}
				current = current.down;
				other = other.down;
			}
		}
	}

	public int width() {
		return this.topLeft == null ? 0 : this.topLeft.width();
	}

	public int height() {
		return this.topLeft == null ? 0 : this.topLeft.height();
	}

	public Grid concatenateAtBottom(Grid N) {
		if (this.topLeft == null) {
			Grid yo = new Grid(N);
			this.topLeft = yo.topLeft;
			return this;
		} else {
			GridNode current = this.topLeft;
			GridNode other = N.topLeft;

			// Get into position
			while (current.down != null) {
				current = current.down;
			}
			
			for (int i = 0; i < N.height(); i++) {
				// Start the new layer under
				current.down = new GridNode(other.data);
				
				GridNode SweepTop = current.right;
				GridNode Sweep = current.down;
				GridNode SweepN = other;
				
				for (int j = 1; j < this.width(); j++) {
					if(SweepN.right != null) {
						Sweep.right = new GridNode(SweepN.data);
						SweepTop.down = Sweep.right;
						SweepTop = SweepTop.right;
						Sweep = Sweep.right;
						SweepN = SweepN.right;
					} else {
						Sweep.right = new GridNode(false, new Object[] {null});
						SweepTop.down = Sweep.right;
						SweepTop = SweepTop.right;
						Sweep = Sweep.right;
					}
				}
				current = current.down;
				other = other.down;
			}
			return this;
		}

	}

	public boolean addAtBottom(GridNode N) {
		GridNode localCurrent = topLeft;
		if (this.topLeft == null) {
			this.topLeft = N;
			return true;
		}
		if (this.topLeft.width() == N.width()) {
			while (localCurrent.down != null) {
				localCurrent = localCurrent.down;
			}
			while (localCurrent != null && N != null) {
				localCurrent.down = N;
				localCurrent = localCurrent.right;
				N = N.right;
			}
			return true;
		}
		return false;
	}

	public Grid concatenateAtRight(Grid N) {
		/*
		 * returns the deep copy concatenation of this + N where N is concatenated to
		 * the right of this. If both Grids are not the same height, the extra rows
		 * should be filled in with GridNode(s) that have null for their data
		 */
		if (this.topLeft == null) {
			Grid yo = new Grid(N);
			this.topLeft = yo.topLeft;
			return this;
		} else {
			GridNode current = this.topLeft;
			GridNode other = N.topLeft;

			// Get into position
			while (current.right != null) {
				current = current.right;
			}
			
			for (int i = 0; i < N.width(); i++) {
				// Start the new layer under
				current.right= new GridNode(other.data);
				
				GridNode SweepLeft = current.down;
				GridNode SweepDown = current.right;
				GridNode SweepN = other;
				
				for (int j = 1; j < this.height(); j++) {
					System.out.println(j + " ");
					if(SweepN.down != null) {
						SweepDown.down = new GridNode(SweepN.data);
						SweepLeft.right = SweepDown.down;
						SweepLeft = SweepLeft.down;
						SweepDown = SweepDown.down;
						SweepN = SweepN.down;
					} else {
						SweepDown.down = new GridNode(false, new Object[] {null});
						SweepLeft.right = SweepDown.down;
						SweepLeft = SweepLeft.down;
						SweepDown = SweepDown.down;
					}
				}
				current = current.right;
				other = other.right;
			}
			return this;
		}
	}

	public boolean addAtRight(GridNode N) {
		if (this.topLeft == null) {
			this.topLeft = N;
			return true;
		}
		if (this.topLeft.height() == N.height()) {
			GridNode localCurrent = topLeft;
			while (localCurrent.right != null) {
				localCurrent = localCurrent.right;
			}
			while (localCurrent != null && N != null) {
				localCurrent.right = N;
				localCurrent = localCurrent.down;
				N = N.down;
			}
			return true;
		}
		return false;
	}

	public String toString() {
		return this.topLeft.toString();
	}

	public void trip() {
		GridNode current = this.topLeft;
		System.out.println(current.data.toString());
		current = current.right;
		System.out.println(current.data.toString());
		current = current.right;
		System.out.println(current.data.toString());
		current = current.down;
		System.out.println(current.data.toString());
		current = current.down;
		System.out.println(current.data.toString());
		current = current.down;
		System.out.println(current.data.toString());
		current = current.down;
		System.out.println(current.data.toString());
		current = current.down;
		System.out.println(current.data.toString());		
	}
	
	public Grid avg(boolean isTall) {
		Grid answer = new Grid();
		GridNode current = this.topLeft;
		if(isTall) {
			for(int i = 0; i < this.height(); i++) {
				double sum = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.width(); j++) {
					if(Sweep.data instanceof Integer) {
						sum += ((Integer) Sweep.data).doubleValue();
					} else if(Sweep.data instanceof Double) {
						sum += ((Double) Sweep.data).doubleValue();
					}
					
					Sweep = Sweep.right;
				}
				current = current.down;
				answer.addAtBottom(new GridNode(sum/this.width()));
			}
		} else {
			for(int i = 0; i < this.width(); i++) {
				double sum = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.height(); j++) {
					if(Sweep.data instanceof Integer) {
						sum += ((Integer) Sweep.data).doubleValue();
					} else if(Sweep.data instanceof Double) {
						sum += ((Double) Sweep.data).doubleValue();
					}
					
					Sweep = Sweep.down;
				}
				current = current.right;
				answer.addAtRight(new GridNode(sum/this.height()));
			}
		}
		return answer;
	}

	public Grid count(boolean isTall) {
		Grid answer = new Grid();
		GridNode current = this.topLeft;
		if(isTall) {
			for(int i = 0; i < this.height(); i++) {
				double counter = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.width(); j++) {
					if(Sweep.data instanceof Integer) {
						counter++;
					} else if(Sweep.data instanceof Double) {
						counter++;
					}
					
					Sweep = Sweep.right;
				}
				current = current.down;
				answer.addAtBottom(new GridNode(counter));
			}
		} else {
			for(int i = 0; i < this.width(); i++) {
				double counter = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.height(); j++) {
					if(Sweep.data instanceof Integer) {
						counter++;
					} else if(Sweep.data instanceof Double) {
						counter++;
					}
					
					Sweep = Sweep.down;
				}
				current = current.right;
				answer.addAtRight(new GridNode(counter));
			}
		}
		return answer;
	}

	
	public Grid min(boolean isTall) {
		Grid answer = new Grid();
		GridNode current = this.topLeft;
		ArrayList<Double> List = new ArrayList<>();
		
		if(isTall) {
			for(int i = 0; i < this.height(); i++) {
				double counter = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.width(); j++) {
					if(Sweep.data instanceof Integer) {
						List.add(((Integer) Sweep.data).doubleValue());
					} else if(Sweep.data instanceof Double) {
						List.add(((Double) Sweep.data).doubleValue());
					}
					Sweep = Sweep.right;
				}
				
				Collections.sort(List);
				current = current.down;
				answer.addAtBottom(new GridNode(List.get(0)));
				List.clear();
			}
		} else {
			for(int i = 0; i < this.width(); i++) {
				double counter = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.height(); j++) {
					if(Sweep.data instanceof Integer) {
						List.add(((Integer) Sweep.data).doubleValue());
					} else if(Sweep.data instanceof Double) {
						List.add(((Double) Sweep.data).doubleValue());
					}
					
					Sweep = Sweep.down;
				}
				Collections.sort(List);
				current = current.right;
				answer.addAtRight(new GridNode(List.get(0)));
				List.clear();
			}
		}
		return answer;
	}
	
	public Grid max(boolean isTall) {
		Grid answer = new Grid();
		GridNode current = this.topLeft;
		ArrayList<Double> List = new ArrayList<>();
		
		if(isTall) {
			for(int i = 0; i < this.height(); i++) {
				double counter = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.width(); j++) {
					if(Sweep.data instanceof Integer) {
						List.add(((Integer) Sweep.data).doubleValue());
					} else if(Sweep.data instanceof Double) {
						List.add(((Double) Sweep.data).doubleValue());
					}
					Sweep = Sweep.right;
				}
				
				Collections.sort(List);
				Collections.reverse(List);
				current = current.down;
				answer.addAtBottom(new GridNode(List.get(0)));
				List.clear();
			}
		} else {
			for(int i = 0; i < this.width(); i++) {
				double counter = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.height(); j++) {
					if(Sweep.data instanceof Integer) {
						List.add(((Integer) Sweep.data).doubleValue());
					} else if(Sweep.data instanceof Double) {
						List.add(((Double) Sweep.data).doubleValue());
					}
					
					Sweep = Sweep.down;
				}
				Collections.sort(List);
				Collections.reverse(List);
				current = current.right;
				answer.addAtRight(new GridNode(List.get(0)));
				List.clear();
			}
		}
		return answer;
	}
	

	public Grid sum(boolean isTall) {
		Grid answer = new Grid();
		GridNode current = this.topLeft;
		if(isTall) {
			for(int i = 0; i < this.height(); i++) {
				double sum = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.width(); j++) {
					if(Sweep.data instanceof Integer) {
						sum += ((Integer) Sweep.data).doubleValue();
					} else if(Sweep.data instanceof Double) {
						sum += ((Double) Sweep.data).doubleValue();
					}
					
					Sweep = Sweep.right;
				}
				current = current.down;
				answer.addAtBottom(new GridNode(sum));
			}
		} else {
			for(int i = 0; i < this.width(); i++) {
				double sum = 0;
				GridNode Sweep = current;
				for(int j = 0; j < this.height(); j++) {
					if(Sweep.data instanceof Integer) {
						sum += ((Integer) Sweep.data).doubleValue();
					} else if(Sweep.data instanceof Double) {
						sum += ((Double) Sweep.data).doubleValue();
					}
					
					Sweep = Sweep.down;
				}
				current = current.right;
				answer.addAtRight(new GridNode(sum));
			}
		}
		return answer;
	}	
	
	public static void main(String[] args) {
		GridNode test = new GridNode(new Object[] {null});
		//System.out.println(test.data == null);
		//System.out.println(test.toString());
		Grid x = new Grid();
		Grid y = new Grid();
		for (int i = 0; i < 13; i += 3) {
			x.addAtRight(new GridNode(true, new Integer[] { i, i + 1, i + 2 }));

		}
		for (int i = 9; i > 0; i -= 3) {
			y.addAtRight(new GridNode(true, new Integer[] { i, i - 1, i - 2 }));

		}
		System.out.println(x.toString());
		System.out.println(y.toString());

		x.concatenateAtBottom(y);
		x.concatenateAtBottom(y);
		//x.trip();
		
		Grid z = new Grid();
		
		System.out.println(x.toString());
		
		//System.out.println(z.toString());
		
		//z.trip();

		z.concatenateAtBottom(y);
		//System.out.println("One\n" + z.toString());
		z.concatenateAtBottom(y);
		
		System.out.println("two\n" +z.toString());
		
		z.concatenateAtRight(y);
		System.out.println("Three\n" +z.toString());
		
		z.concatenateAtRight(y);
		
		System.out.println("Four\n" +z.toString());
		
		Grid c = z.count(true);
		System.out.println(c.toString());
		
	}
}