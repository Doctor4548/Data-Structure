import java.io.*;

class listNode {
	String data;
	listNode next;
	listNode(String d){
		data=d;
		next=null;
	}

}


class LList {
	listNode listHead;
	listNode middleNode;
	
	LList(){
		listHead=new listNode("dummy");
	}
 
    public void constructLL(BufferedReader  inFile, BufferedWriter deBugFile) throws IOException {
        deBugFile.write("In constructLL method");
        deBugFile.newLine();
        String []dataArray;
        
        while (inFile.ready()) {
            String data = inFile.readLine();
            dataArray = data.split("\\s+");
            for(String word: dataArray) {
            	listNode newNode = new listNode(word);
                listInsert(newNode, deBugFile);
                printList(deBugFile);
            }
        }
    }
    
    public void listInsert(listNode newNode, BufferedWriter deBugFile) throws IOException {
        deBugFile.write("In listInsert method");
        deBugFile.newLine();
        listNode spot = findSpot(newNode);
        deBugFile.write("Returns from findSpot where Spot.data is " + spot.data);
        deBugFile.newLine();
        newNode.next = spot.next;
        spot.next = newNode;
    }

    public listNode findSpot(listNode newNode) {
        listNode curr = listHead;
        while (curr.next != null && curr.next.data.compareTo(newNode.data) < 0) {
            curr = curr.next;
        }
        return curr;
    }
 
    public void printList(BufferedWriter outFile) throws IOException {
        listNode curr = listHead;

        while(curr.next!=null) {
        	outFile.write(" (");
        	for(int i=0;i<5&&curr.next!=null;i++) {
        		curr=curr.next;
        		outFile.write(curr.data+" ");

        	}
        	outFile.write(")-->");
        	outFile.newLine();
        }
    }


    public listNode findMiddleNode(BufferedWriter deBugFile) throws IOException {
        deBugFile.write("In findMiddleNode method\n");
        listNode walker1 = listHead.next;
        listNode walker2 = listHead.next;
        while (walker2 != null && walker2.next != null) {
            walker1 = walker1.next;
            walker2 = walker2.next.next;
            deBugFile.write("walker1's data is " + walker1.data + "\n");
        }
        return walker1;
    }
    
}


public class LinkedList {

	   public static void main(String[] args) throws Exception {


	       BufferedReader reader = new BufferedReader(new FileReader(args[0]));
	        BufferedWriter outWriter = new BufferedWriter(new FileWriter(args[1]));
	        BufferedWriter deBugWriter = new BufferedWriter(new FileWriter(args[2]));
	        
	        LList linkedList = new LList();

	        linkedList.constructLL(reader, deBugWriter);
	        linkedList.printList(outWriter);
	        listNode middleNode = linkedList.findMiddleNode(deBugWriter);

	        if (middleNode != null) {
	            outWriter.write("The word in the middle of list is: " + middleNode.data);
	            outWriter.newLine();
	        }

	        reader.close();
	        outWriter.close();
	        deBugWriter.close();
	    }
}
