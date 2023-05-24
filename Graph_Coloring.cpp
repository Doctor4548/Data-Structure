#include <iostream>
#include <fstream>
using namespace std;

class node{
public:
  int ID;
  node* next;
  node(int i){
    ID=i;
    next=nullptr;
  }
};

class coloring{
public:
  int numNodes;
  int numUncolor;
  node** hashTable;
  int* colorAry;

  coloring(ifstream& inFile){
    inFile >> numNodes;
    colorAry=new int[numNodes+1];
    hashTable = new node*[numNodes+1];
    for(int i=1;i<=numNodes;i++){
      hashTable[i]=nullptr;
      colorAry[i]=0;
    }
  }

  void loadGraph(ifstream& inFile){
    int id1, id2;
    while (inFile >> id1 >> id2) {
        hashInsert(id1, id2);
        hashInsert(id2, id1);
    }
    numUncolor = numNodes;    
  }

  void hashInsert(int id1, int id2){
    node* newNode = new node(id2);
    newNode->next = hashTable[id1];
    hashTable[id1] = newNode;    
  }

  void printHashTable(ofstream& outFile) {
    for (int i = 1; i <= numNodes; i++) {
        outFile << "hashTable [" << i << "] -> ";
        node* curr = hashTable[i];
        while (curr != nullptr) {
            outFile << curr->ID << " ";
            curr = curr->next;
        }
        outFile << endl;
    }
}

  void printAry(ofstream& file, int method) {
    file <<"Method "<<method<<" was used to color the graph. \n";
    file <<"Below is the result of the color assignments. \n";
    file<<endl;
    file<<numNodes<<endl;
    for(int i=1;i<=numNodes;i++){
      file<<i<<" "<<(char)(colorAry[i]); 
      file<<endl;
    }
}

  void method1(ofstream& output, ofstream& debug, int method){
    debug<<"enter Method1 \n";
    int newColor=64;
    while(numUncolor>0){
      newColor++;
      int nodeID=1;
      while(nodeID<=numNodes){
        if(colorAry[nodeID]==0){
          if(checkNeighbors(nodeID,newColor)){
            colorAry[nodeID]=newColor;
            numUncolor--;
          }
        }
        nodeID++;}
      printAry(debug, method);}
    printAry(output, method);
    debug<<"leaving Method1 \n";
  }

  void method2(ofstream& output, ofstream& debug, int method){
    debug<<"entering method2 \n";
    int lastUsedColor=64;
    int nextNodeID=0;
    while(nextNodeID<numNodes){
      nextNodeID++;
      int nextUsedColor=65;
      bool coloredFlag=false;
      while(coloredFlag==false&&nextUsedColor<=lastUsedColor){//
        if(lastUsedColor>64&&checkNeighbors(nextNodeID, nextUsedColor)){
          colorAry[nextNodeID]=nextUsedColor;
          coloredFlag=true;
        }
        else{
          nextUsedColor++;
        }
      }
      if(coloredFlag==false){
        lastUsedColor++;
        colorAry[nextNodeID]=lastUsedColor;
        debug << "lastUsedColor = " << lastUsedColor << endl;
      }
      printAry(debug, method);}
    printAry(output, method);
    debug<<"Leaving Method2 \n";
}


  bool checkNeighbors(int nodeID, int newColor){
    node* nextNode=hashTable[nodeID];  
    while(nextNode!=nullptr){
      if(nextNode==nullptr){
        return true;
      }
      if(colorAry[nextNode->ID]==newColor){
        return false;
      }
      nextNode=nextNode->next;}
    return true;
}

};

int main(int argc, char* argv[]) {

  ifstream infile;
  ofstream output, debug;

  infile.open(argv[1]);
  output.open(argv[3]);
  debug.open(argv[4]);
  
  //infile.open("input.txt");
  //output.open("output.txt");
  //debug.open("debug.txt");

  
  coloring color(infile);
  color.loadGraph(infile);
  color.printHashTable(debug);
  
  int whichMethod=atoi(argv[2]);
  switch(whichMethod){
    case 1:
      color.method1(output,debug, whichMethod);
      break;
    case 2:
      color.method2(output,debug, whichMethod);
      break;
    default:
      debug<<"Error: argv [2] only accept 1 or 2 \n";
      exit(1);
  }

  infile.close();
  output.close();
  debug.close();
}

