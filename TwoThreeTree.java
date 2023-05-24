import java.io.*;

class treeNode{
	int key1;
	int key2;
	int rank;
	treeNode child1;
	treeNode child2;
	treeNode child3;
	treeNode father;
	
	treeNode(int k1, int k2, int ra, treeNode c1, treeNode c2, treeNode c3, treeNode fa){
		key1=k1;
		key2=k2;
		rank=ra;
		child1=c1;
		child2=c2;
		child3=c3;
		father=fa;
	}
	
	void printNode(treeNode Tnode, BufferedWriter output) throws IOException {
		if(Tnode.father==null&&Tnode.child1==null) {
			output.write("("+Tnode.key1+" , "+Tnode.key2+" , "+Tnode.rank+" , "+null+" , "+null+" , "+null+" , "+null+") \n");
		}
		else if(Tnode.child1==null) {
			output.write("("+Tnode.key1+" , "+Tnode.key2+" , "+Tnode.rank+" , "+null+" , "+null+" , "+null+" , "+Tnode.father.key1+") \n");
		}
		else if(Tnode.father==null&&Tnode.child3==null) {
			output.write("("+Tnode.key1+" , "+Tnode.key2+" , "+Tnode.rank+" , "+Tnode.child1.key1+" , "+Tnode.child2.key1+" , "+null+" , "+null+") \n");
		}
		else if(Tnode.child3==null) {
			output.write("("+Tnode.key1+" , "+Tnode.key2+" , "+Tnode.rank+" , "+Tnode.child1.key1+" , "+Tnode.child2.key1+" , "+null+" , "+Tnode.father.key1+") \n");
		}
		else if(Tnode.father==null) {
			output.write("("+Tnode.key1+" , "+Tnode.key2+" , "+Tnode.rank+" , "+Tnode.child1.key1+" , "+Tnode.child2.key1+" , "+Tnode.child3.key1+" , "+null+") \n");
		}
		else {
			output.write("("+Tnode.key1+" , "+Tnode.key2+" , "+Tnode.rank+" , "+Tnode.child1.key1+" , "+Tnode.child2.key1+" , "+Tnode.child3.key1+" , "+Tnode.father.key1+") \n");
		}
	}
}

class tree{
	treeNode root;
	
	void swap(int data1, int data2) {
		int temp=data1;
		data1=data2;
		data2=temp;
	}
	
	int countKids(treeNode Tnode) {
		if(Tnode.child3==null) {
			return 2;
		}
		else {
			return 3;
		}
	}
	
	boolean isLeaf(treeNode Tnode) {
		if(Tnode.child1==null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	treeNode initialTree(BufferedReader inFile, BufferedWriter deBug) throws IOException {
		deBug.write("Entering initialTree () method \n");
		root=new treeNode(-1,-1,-1,null,null,null,null);
		String word1[]=inFile.readLine().split(" ");
		String word2[]=inFile.readLine().split(" ");
	
		int data1=Integer.parseInt(word1[0]);
		int data2=Integer.parseInt(word2[0]);		
		
		deBug.write("Before swap data1 and data2 are: "+"data1="+data1+", data2="+data2+"\n");
		if(data2<data1) {
			int temp=data1;
			data1=data2;
			data2=temp;
		}
		deBug.write("After swap data1 and data2 are: "+"data1="+data1+", data2="+data2+"\n");
		treeNode node1=new treeNode(data1,-1,1,null,null,null,root);
		treeNode node2=new treeNode(data2,-1,2,null,null,null,root);
		root.key1=data2;
		root.child1=node1;
		root.child2=node2;
		deBug.write("Root node: \n");
		root.printNode(root, deBug);
		deBug.write("Exiting initialTree() method \n");
		return root;
	}
	
	treeNode findSpot(treeNode spot, int data, BufferedWriter deBug) throws IOException {
		deBug.write("Enter findSpot() method \n");
		deBug.write("Spot's key1 and key2 are: "+"Key1="+spot.key1+", Key2="+spot.key2+"\n");
		if(spot.child1==null) {
			deBug.write("In findSpot () You are at leaf level, you are too far down the tree!! \n");
			return null;
		}
		if(data==spot.key1||data==spot.key2) {
			deBug.write("In findSpot (): data is already in Spot’s keys, no need to search further! \n");
			return spot;//spot
		}
		if(isLeaf(spot.child1)) {
			if(data==spot.child1.key1||data==spot.child2.key1) {
				deBug.write("In findSpot(): data is already in the leaf node \n");
				return null;
			}
			else {
				return spot;
			}
		}
		else {
			if(data<spot.key1) {
				return findSpot(spot.child1,data,deBug);
			}
			else if(spot.key2==-1||data<spot.key2) {
				return findSpot(spot.child2,data,deBug);
			}
			else if(spot.key2!=-1&&data>spot.key2) {
				return findSpot(spot.child3,data,deBug);
			}
			else {
				deBug.write("In findSpot (), something is wrong about data \n");
				return null;
			}
		}
	}
	
	int findMinLeaf(treeNode Tnode) {
		if(Tnode==null) {
			return-1;
		}
		if(Tnode.child1==null) {
			return Tnode.key1;
		}
		else {
			return findMinLeaf(Tnode.child1);
		}
	}
	
	void updateKeys(treeNode Tnode, BufferedWriter deBug) throws IOException {
		deBug.write("Entering updateKeys () method. \n");
		if(Tnode==null) {
			return;
		}
		Tnode.key1=findMinLeaf(Tnode.child2);
		Tnode.key2=findMinLeaf(Tnode.child3);
		if(Tnode.rank>1) {
			updateKeys(Tnode.father,deBug);
		}
		deBug.write("Leaving updateKeys () method. \n");
	}
	
	void spotHas2KidsCase(treeNode spot, treeNode newNode, BufferedWriter deBug) throws IOException {
		deBug.write("Entering spotHas2kidCase () method \n");
		deBug.write("In spotHas2kidCase () method; Spot’s rank is "+spot.rank+" \n");
		if(newNode.key1<spot.child2.key1) {
			spot.child3=spot.child2;
			spot.child2=newNode;
		}
		else {
			spot.child3=newNode;
		}
		if(spot.child2.key1<spot.child1.key1) {
			treeNode tmpNode=spot.child1;
			spot.child1=spot.child2;
			spot.child2=tmpNode;
		}
		spot.child1.father=spot;
		spot.child1.rank=1;
		spot.child2.father=spot;
		spot.child2.rank=2;
		spot.child3.father=spot;
		spot.child3.rank=3;
		updateKeys(spot, deBug);
		if(spot.rank>1) {//
			updateKeys(spot,deBug);
		}
		deBug.write("Leaving spotHas2kidCase() method \n");
	}
	
	treeNode makeNewRoot(treeNode spot, treeNode sibling, BufferedWriter deBug) throws IOException {
		deBug.write("Entering makeNewRoot() method. \n");
		treeNode newRoot=new treeNode(-1,-1,-1,null,null,null,null);
		newRoot.child1=spot;
		newRoot.child2=sibling;
		newRoot.child3=null;
		newRoot.key1=findMinLeaf(sibling);
		newRoot.key2=-1;
		spot.father=newRoot;
		spot.rank=1;
		sibling.father=newRoot;
		sibling.rank=2;
		deBug.write("Leaving makeNewRoot() method \n");
		return newRoot;
	}
	
	void spotHas3KidsCase(treeNode spot, treeNode newNode, BufferedWriter deBug) throws IOException  {
		deBug.write("Entering spotHas3KidsCase() method \n");
		deBug.write("In spotHas3KidCase() method; Spot's rank is "+spot.rank+" \n");
		treeNode sibling=new treeNode(-1,-1,5,null,null,null,null);
		if(newNode.key1>spot.child3.key1) {
			sibling.child1=spot.child3;
			sibling.child2=newNode;
			spot.child3=null;
		}
		else if(newNode.key1<=spot.child3.key1) {
			sibling.child2=spot.child3;
			spot.child3=newNode;	
		}
		if(spot.child3!=null) {
			if(spot.child3.key1>spot.child2.key1) {
				sibling.child1=spot.child3;
				spot.child3=null;
			}
			else {
				sibling.child1=spot.child2;
				spot.child2=newNode;
			}
		}
		else if(spot.child2.key1<spot.child1.key1) {
			treeNode tmpNode=spot.child1;
			spot.child1=spot.child2;
			spot.child2=tmpNode;
		}
		
		spot.child1.father=spot;
		spot.child1.rank=1;
		spot.child2.father=spot;
		spot.child2.rank=2;
		spot.child3=null;
		
		sibling.child1.father=sibling;
		sibling.child1.rank=1;
		sibling.child2.father=sibling;
		sibling.child2.rank=2;
		sibling.child3=null;
		
		updateKeys(spot,deBug);
		updateKeys(sibling, deBug);
		if(spot.rank==-1&&spot.father==null) {
			root=makeNewRoot(spot,sibling,deBug);
		}
		else {
			treeInsert(spot.father,sibling,deBug);
		}
		if(spot.rank>1) {
			updateKeys(spot.father, deBug);
		}
		deBug.write("Leaving spotHas3KidsCase() method \n");
	}

	void treeInsert(treeNode spot, treeNode newNode, BufferedWriter deBug) throws IOException {
		deBug.write("Entering treeInsert () method \n");
		if(spot==null) {
			deBug.write("In treeInsert (), Spot is null, something is wrong \n");
			return;
		}
		else {
			deBug.write("In treeInsert (). Printing Spot and newNode info \n");
			spot.printNode(spot, deBug);
			newNode.printNode(newNode,deBug);
		}
		
		treeNode spot2=findSpot(root,newNode.key1,deBug);
		
		int count;
		if(spot2.key2==-1) {
			count=2;
		}
		else {
			count=3;
		}
		deBug.write("In treeInsert () method; Spot kids count is "+count+"\n");
		if(count==2) {
			spotHas2KidsCase(spot2,newNode,deBug);
		}
		else if(count==3) {
			spotHas3KidsCase(spot2,newNode,deBug);
		}
		deBug.write("Leaving treeInsert () method \n");
	}

	
	void preOrder(treeNode rootNode, BufferedWriter output) throws IOException {//
		
		rootNode.printNode(rootNode, output);
		

		if(rootNode.child1!=null) {
			preOrder(rootNode.child1,output);}
		if(rootNode.child2!=null) {
			preOrder(rootNode.child2,output);}
		if(rootNode.child3!=null) {
			preOrder(rootNode.child3,output);}
	}
	
	void build23Tree(BufferedReader infile, treeNode rootNode, BufferedWriter deBug, BufferedWriter output) throws IOException {
		deBug.write("Entering build23Tree () method \n");
		String line;
		while((line=infile.readLine())!=null) {
			String word[]=line.split(" ");
			int data=Integer.parseInt(word[0]);
			//System.out.println(data);
			treeNode spot=findSpot(rootNode,data,deBug);
			while(spot==null) {
				spot=findSpot(root,data,deBug);
				data=Integer.parseInt(infile.readLine());
				
			}
			deBug.write("in build23Tree (), printing Spot info. \n");
			spot.printNode(spot, deBug);
			treeNode leafNode=new treeNode(data,-1,5,null,null,null,null);
			treeInsert(spot,leafNode,deBug);}
			
			deBug.write("In build23Tree(); printing preOrder() after one treeInsert \n");
			preOrder(root,output);
			preOrder(root,deBug);
		deBug.close();}
	
	int treeDepth(treeNode rootNode) {//
		if(rootNode==null) {
			return 0;
		}
		int ch1=treeDepth(rootNode.child1);
		int ch2=treeDepth(rootNode.child2);
		int ch3=treeDepth(rootNode.child3);
		return 1+Math.max(ch1, Math.max(ch2, ch3));
	}
}

public class TwoThreeTree {

	public static void main(String[] args) throws IOException {
		BufferedReader input=new BufferedReader(new FileReader(args[0]));
		BufferedWriter treeFile=new BufferedWriter(new FileWriter(args[1]));
		BufferedWriter debug=new BufferedWriter(new FileWriter(args[2]));
		
		tree tree=new tree();
		treeNode root=tree.initialTree(input,debug);
		tree.build23Tree(input, root, debug, treeFile);
		
		treeFile.write("The Depth of this tree is "+tree.treeDepth(tree.root));

		
		input.close();
		treeFile.close();
		debug.close();
	}
}

