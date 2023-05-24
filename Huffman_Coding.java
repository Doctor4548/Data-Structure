import java.io.*;
class HNode{
	String chStr;
	int prob;
	String code;
	int numBits;
	int entropy;
	HNode left;
	HNode right;
	HNode next;
	HNode(String ch, int pro, String cod, int bit, int entr, HNode le, HNode ri, HNode ne){
		chStr= ch;
		prob=pro;
		code=cod;
		numBits=bit;
		entropy=entr;
		left=le;
		right=ri;
		next=ne;
	}
	public void printNode(HNode T, BufferedWriter outFile)throws IOException{
		if(T.next==null&&T.left==null&&T.right==null) {
			outFile.write("("+T.chStr+","+T.prob+","+T.code+","+T.numBits+","+T.entropy+","+null+","+null+","+null+") --》 ");
		}
		else if(T.next==null&&T.left==null) {
			outFile.write("("+T.chStr+","+T.prob+","+T.code+","+T.numBits+","+T.entropy+","+null+","+T.right+","+null+") --》 ");
		}
		else if(T.next==null&&T.right==null) {
			outFile.write("("+T.chStr+","+T.prob+","+T.code+","+T.numBits+","+T.entropy+","+T.left+","+null+","+null+") --》 ");
		}
		else if(T.left==null&&T.right==null) {
			outFile.write("("+T.chStr+","+T.prob+","+T.code+","+T.numBits+","+T.entropy+","+null+","+null+","+T.next.chStr+") --》  ");
		}
		else if(T.next==null) {
			outFile.write("("+T.chStr+","+T.prob+","+T.code+","+T.numBits+","+T.entropy+","+T.left.chStr+","+T.right.chStr+","+null+")  --》  ");
		}
		else if(T.left==null) {
			outFile.write("("+T.chStr+","+T.prob+","+T.code+","+T.numBits+","+T.entropy+","+null+","+T.right.chStr+","+T.next.chStr+")  --》  ");
		}
		else if(T.right==null) {
			outFile.write("("+T.chStr+","+T.prob+","+T.code+","+T.numBits+","+T.entropy+","+T.left.chStr+","+null+","+T.next.chStr+")  --》  ");
		}
		else {
			outFile.write("("+T.chStr+","+T.prob+","+T.code+","+T.numBits+","+T.entropy+","+T.left.chStr+","+T.right.chStr+","+T.next.chStr+")  --》  ");
		}
	}
}

class Huffman{
	HNode head;
	HNode root;
	int totalEntropy;
	Huffman(HNode listHead){
		head=listHead;
	}
	HNode findSpot(HNode newNode) {
		HNode cur=head;
		while(cur.next!=null&&cur.next.prob<newNode.prob) {
			cur=cur.next;
		}
		return cur;
	}
	
	void listInsert(HNode listHead, HNode newNode) {
		HNode spot=findSpot(newNode);
		newNode.next=spot.next;
		spot.next=newNode;
	}
	
	void constructHuffmanLList(BufferedReader inFile, BufferedWriter deBug, HNode listHead)throws IOException {
		deBug.write("Entering constructHuffmanLList method \n");
		String line;
		while((line=inFile.readLine())!=null) {
			String part=line;
			String chr=part.substring(0, 1);
			int prob=0;
			for(int i=1;i<part.length();i++) {
				if(part.charAt(i)!=' ') {
					prob=Integer.parseInt(part.substring(i));
					break;
				}
			}
			HNode newNode=new HNode(chr,prob,"",0,0,null,null,null);
			listInsert(listHead,newNode);
			printList(listHead, deBug);
		}
		deBug.write("Exit constructHuffmanLList method \n");
	}
	
	HNode constructHuffmanBinTree(HNode listHead,BufferedWriter deBug)throws IOException {
		deBug.write("Entering constructHuffmanBinTree method \n");
		while(listHead.next.next!=null) {
	        HNode newNode = new HNode("", 0, "", 0, 0, null, null, null);
	        newNode.prob = listHead.next.prob + listHead.next.next.prob;
	        newNode.chStr = listHead.next.chStr + listHead.next.next.chStr;
	        newNode.left = listHead.next;
	        newNode.right = listHead.next.next;
	        newNode.next = null;
	        listInsert(listHead, newNode);
	        listHead.next=listHead.next.next.next;
	        printList(listHead, deBug);
		}
		return listHead.next;
	}
	
	boolean isLeaf(HNode newNode) {
		if(newNode.left==null&&newNode.right==null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	void printEntropyTable(HNode T, String code, BufferedWriter output, int totalEntropy)throws IOException {
		if(isLeaf(T)) {
			T.code=code;
			T.numBits=code.length();
			T.entropy=T.prob*T.numBits;
			output.write(T.chStr+ " , "+T.prob+" , "+T.code+" , "+T.numBits+" , "+T.entropy+"\n");
			getEntropy(T.entropy);
		}
		else {
			printEntropyTable(T.left,code+"0",output,totalEntropy);
			printEntropyTable(T.right,code+"1",output,totalEntropy);
		}
	}
	
	void getEntropy(int i) {
		totalEntropy+=i;
	}
	
	void printList(HNode listHead,BufferedWriter output) throws IOException {
		HNode node = listHead;
		int nodecount=0;
		while(node!=null) {
			node.printNode(node, output);
			nodecount++;
			if(nodecount%3==0) {
				output.write("\n");
			}
			node=node.next;
		}
		output.write("\n");
	}
	void preorder(HNode T, BufferedWriter output)throws IOException {
		if(isLeaf(T)) {
			T.printNode(T,output);
		}
		else {
			T.printNode(T,output);
			preorder(T.left,output);
			preorder(T.right,output);
		}
	}
	
	void inorder(HNode T, BufferedWriter output)throws IOException {
		if(isLeaf(T)) {
			T.printNode(T,output);
		}
		else {
			inorder(T.left,output);
			T.printNode(T,output);
			inorder(T.right,output);
		}
	}
		
	
	void postorder(HNode T, BufferedWriter output)throws IOException {
		if(isLeaf(T)) {
			T.printNode(T,output);
		}
		else {
			postorder(T.left,output);
			postorder(T.right,output);
			T.printNode(T,output);
		}
	}
	
}

public class Huffman_Coding {

	public static void main(String[] args) throws IOException {
		BufferedReader read=new BufferedReader(new FileReader(args[0]));
		BufferedWriter output=new BufferedWriter(new FileWriter(args[1]));
		BufferedWriter debug=new BufferedWriter(new FileWriter(args[2]));
		HNode listHead=new HNode("dummy",0,"",0,0,null,null,null);
		Huffman tree=new Huffman(listHead);
		tree.constructHuffmanLList(read, debug, listHead);
		tree.printList(listHead, output);
		HNode root=tree.constructHuffmanBinTree(listHead, debug);
		output.write("printing the entropy Table \n");		
		int totalEntropy=0;
		tree.printEntropyTable(root,"", output, totalEntropy);
		totalEntropy=tree.totalEntropy;
		output.write("The Huffman Coding scheme’s entropy = "+(double)totalEntropy/100.00+" \n");
		output.write("Preorder: ");
		tree.preorder(root, output);
		output.write("\n");
		output.write("Inorder: ");
		tree.inorder(root, output);
		output.write("\n");
		output.write("Postorder: ");
		tree.postorder(root, output);
		read.close();
		output.close();
		debug.close();
		
	}

}
