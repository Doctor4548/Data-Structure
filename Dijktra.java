import java.io.*;

class DijktraSSS{
	int numNodes;
	int sourceNode;
	int minNode;
	int currentNode;
	int newCost;
	int[][] costMatrix;
	int[] fatherAry;
	int[] ToDoAry;
	int[] BestAry;
	
	DijktraSSS(int numbers){
		numNodes=numbers;
		costMatrix=new int[numbers+1][numbers+1];
		for(int i=0;i<numbers+1;i++) {
			for(int j=0;j<numbers+1;j++) {
				if(i==j) {
					costMatrix[i][j]=0;
				}
				else {
					costMatrix[i][j]=9999;
				}
			}
		}
		
		fatherAry=new int[numbers+1];
		for(int i=0;i<numbers+1;i++) {
			fatherAry[i]=i;
		}
		
		ToDoAry=new int[numbers+1];
		for(int i=0;i<numbers+1;i++) {
			ToDoAry[i]=1;
		}
		
		BestAry=new int[numbers+1];
		for(int i=0;i<numbers+1;i++) {
			BestAry[i]=9999;
		}
	}
	
	void loadCostMatrix(BufferedReader read) throws NumberFormatException, IOException{
		int row;
		int col;
		int cost;
		while(read.ready()) {
			String line=read.readLine();
			String part=line.replaceAll("\\s", "");
			row=Character.getNumericValue(part.charAt(0));
			col=Character.getNumericValue(part.charAt(1));
			cost=Integer.parseInt(part.substring(2, part.length()));
			costMatrix[row][col]=cost;
		}
	}
	
	void setBestAry(int sourceNode) {
		for(int i=1;i<BestAry.length;i++) {
			BestAry[i]=costMatrix[sourceNode][i];
		}
	}
	
	void setFatherAry(int sourceNode) {
		for(int i=1;i<fatherAry.length;i++) {
			fatherAry[i]=sourceNode;
		}
	}
	
	void setToDoAry(int sourceNode) {
		for(int i=1;i<ToDoAry.length;i++) {
			if(i==sourceNode) {
				ToDoAry[i]=0;
			}
			else {
				ToDoAry[i]=1;
			}
		}
	}
	
	int findMinNode() {
		int minCost=99999;
		minNode=0;
		int index=0;
		while(index<=numNodes) {
			if(ToDoAry[index]==1&&BestAry[index]<minCost) {
				minCost=BestAry[index];
				minNode=index;
			}
			index++;
		}
		return minNode;
	}
	
	int computerCost(int minNode, int Node) {
		return BestAry[minNode]+costMatrix[minNode][Node];
	}
	
	boolean checkToDoAry() {
		for(int i=1;i<=numNodes;i++) {
			if(ToDoAry[i]==1) {
				return true;
			}
		}
		return false;
	}
	
	void debugPrint(BufferedWriter debug) throws IOException {
		debug.write("Source Node is: "+sourceNode+"\n");
		debug.write("The matrix is: \n");
		for(int i=1;i<costMatrix.length;i++) {
			debug.write("|");
			for(int j=1;j<costMatrix.length;j++) {
				debug.write(" "+costMatrix[i][j]+" ");
			}
			debug.write("|\n");
		}
		debug.write("The father Array is: ");
		for(int i=1;i<fatherAry.length;i++) {
			debug.write(fatherAry[i]+" ");
		}
		debug.write("\n");
		debug.write("To Do List: ");
		for(int i=1;i<ToDoAry.length;i++) {
			debug.write(ToDoAry[i]+" ");
		}
		debug.write("\n");
		debug.write("The Best List: ");
		for(int i=1;i<BestAry.length;i++) {
			debug.write(BestAry[i]+" ");
		}
		debug.write(" ");
	}
	 
	void printShortestPath(int currentNode, int sourceNode, BufferedWriter SSSfile) throws IOException {
        int runningCost = 0, father = fatherAry[currentNode], child = currentNode;
        SSSfile.write("The path from " + sourceNode + " to " + child + ": "
                + " " + child); 
        while(father != sourceNode)
        {
            runningCost += costMatrix[father][child];
            SSSfile.write(" <- " + father);
            child = father;
            father = fatherAry[child];
        }

        runningCost += costMatrix[father][child];
        SSSfile.write(" <- " + sourceNode + ": cost = " + runningCost + System.lineSeparator());
    }
}


public class Dijktra {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader infile=new BufferedReader(new FileReader(args[0]));
		BufferedWriter SSSfile=new BufferedWriter(new FileWriter(args[1]));
		BufferedWriter debug=new BufferedWriter(new FileWriter(args[2]));
		
		int size=Integer.parseInt(infile.readLine()); 
		
		DijktraSSS sss=new DijktraSSS(size);
		sss.loadCostMatrix(infile);
		
		sss.sourceNode=1;
		
		while(sss.sourceNode<=sss.numNodes) {
			sss.setBestAry(sss.sourceNode);
			sss.setFatherAry(sss.sourceNode);
			sss.setToDoAry(sss.sourceNode);
			
			while(sss.checkToDoAry()) {
				sss.minNode=sss.findMinNode();
				sss.ToDoAry[sss.minNode]=0;
				sss.debugPrint(debug);
				
				int childNode=1;
				while(childNode<=sss.numNodes) {
					if(sss.ToDoAry[childNode]==1) {
						sss.newCost=sss.computerCost(sss.minNode, childNode);
						if(sss.newCost<sss.BestAry[childNode]) {
							sss.BestAry[childNode]=sss.newCost;
							sss.fatherAry[childNode]=sss.minNode;
							sss.debugPrint(debug);
						}
					}
					childNode++;
					}
			}

			sss.currentNode=1;
			
			while(sss.currentNode<=sss.numNodes) {
				sss.printShortestPath(sss.currentNode, sss.sourceNode, SSSfile);
				sss.currentNode++;}
			
			sss.sourceNode++;
		}
		
		infile.close();
		SSSfile.close();
		debug.close();
	}

	

}
