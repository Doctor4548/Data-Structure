#include <iostream>
#include <fstream>
using namespace std;

class edge{
public:
    int nU;
    int nW;
    int cost;
    edge* next;
    
    edge(int n1, int n2, int c){
        nU=n1;
        nW=n2;
        cost=c;
        next=NULL;
    }
    
    void printEdge(ofstream &output){
        output<<"("<<nU<<","<<nW<<","<<cost<<")-> ";
    }
};

class KruskalMST{
public:
    int N;
    int *whichSet;
    int numSet;
    int totalMSTCost;
    edge *edgeList;
    edge *mstList;
    
    KruskalMST(int n){
        N=n;
        whichSet=new int[n+1];
        for(int i=0;i<=n;i++){
            whichSet[i] = i;
        }
        numSet=n;
        totalMSTCost=0;
        edgeList=new edge(0,0,0);
        mstList=new edge(0,0,0);
    }
    
    void insertEdge(edge *list, edge *newEdge){
        edge *cur=list;
        while(cur->next!=NULL&&newEdge->cost>cur->next->cost){
            cur=cur->next;
        }
        newEdge->next=cur->next;
        cur->next=newEdge;
    }
    
    edge* removeEdge(){
        edge *head=edgeList->next;
        edgeList->next=head->next;
        head->next=NULL;
        return head;
    }
    
    void merge2Sets(int Ni, int Nj){
        if(whichSet[Ni]<whichSet[Nj]){
            updateWhichSet(whichSet[Nj],whichSet[Ni]);
        }
        else{
            updateWhichSet(whichSet[Ni],whichSet[Nj]);
        }
    }
    
    void updateWhichSet(int a, int b){
        for(int i=1; i<=N; i++){
            if(whichSet[i]==a){
                whichSet[i]=b;
            }
        }
    }
    
    void printAry(ofstream& debug){
        debug<<"In WhichSet: ";
        for(int i=1;i<=N;i++){
            debug<<whichSet[i]<<" ";
        }
        debug<<"\n";
    }
    
    void printList(edge* list, ofstream& output){
        edge* cur=list->next;
        output<<"ListHead -> ";
        while(cur!=NULL){
            cur->printEdge(output);
            cur=cur->next;
        }
        output<<"NULL \n";
    }
};

int main(int argc, char** argv){
    ifstream input;
    input.open(argv[1]);    
    ofstream output;
    output.open(argv[2]);  
    ofstream debug;
    debug.open(argv[3]); 
    

    int N,u,w,cost;
    
    input>>N;
    KruskalMST kruskal(N);//
    
    debug<<"Printing the input graph: \n";
    
    while(input>>u>>w>>cost){
        edge* newEdge=new edge(u,w,cost);
        debug<<"newEdge from inFile is ";
        newEdge->printEdge(debug);
        
        kruskal.insertEdge(kruskal.edgeList, newEdge);
        debug<<"\n Printing edgeList after insert the new edge: ";
        kruskal.printList(kruskal.edgeList,debug);
    }
     
    debug << "At the end of printing all edges of the input graph: ";
    do {
        edge* nextEdge = kruskal.removeEdge();
        while (kruskal.whichSet[nextEdge->nU] == kruskal.whichSet[nextEdge->nW]) {
            nextEdge = kruskal.removeEdge();
        }
    
        debug << "\nthe next Edge is ";
        nextEdge->printEdge(debug);
        kruskal.insertEdge(kruskal.mstList,nextEdge);
        kruskal.totalMSTCost += nextEdge->cost;
        kruskal.merge2Sets(nextEdge->nU, nextEdge->nW);
        kruskal.numSet--;
        debug << "numSets is " << kruskal.numSet;
    
        debug << "\nPrinting whichSet array:\n";
        kruskal.printAry(debug);
        debug << "\nPrinting the remaining edgeList:\n";
        kruskal.printList(kruskal.edgeList, debug);
        debug << "\nPrinting the growing MST list:\n";
        kruskal.printList(kruskal.mstList, debug);
    } while (kruskal.numSet > 1);
    
    kruskal.printList(kruskal.mstList, output);
    output<<"The total cost of a Kruskal's MST is: "<<kruskal.totalMSTCost<<endl;
    input.close();
    output.close();
    debug.close();
    }

