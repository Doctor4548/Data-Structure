#include <iostream>
#include<fstream>
#include<string>
#include<cmath>
using namespace std;
class listNode{
public: 
    int data;
    listNode *next;
    listNode(){
        data=0;
        next=NULL;
    }
    listNode(int data){
        this->data=data;
    }
};

class LLQueue{
public:
    listNode *head;
    listNode *tail;
    LLQueue(){
        head=tail=NULL;
    }
    bool isEmpty(){
        return (head==NULL&&tail==NULL);
    }
    void insertQ(listNode *newNode){
        newNode->next=NULL;
        if(isEmpty()){
            head=newNode;
            tail=newNode;
        }
        else{
            tail->next=newNode;
            tail=newNode;
        }
    }
    
    listNode *deleteQ(){
        if(isEmpty()){
            return head;
        }
        listNode *first=head;
        head=head->next;
        first->next=NULL;
        if(head==NULL){
            tail=NULL;
        }
        return first;
    }
 
    void printQueue(int index, ofstream &output){
        output<<"front("+to_string(index)+")->";
        listNode *spot=head;
        while(spot->next!=NULL){
            output<<"("+to_string(spot->data)+","+to_string(spot->next->data)+")->";
            spot=spot->next;
        }
        output<<"("+to_string(spot->data)+",NULL)->NULL";
        output<<endl;
    }
};

class Radixsort{
public:
    static const int tableSize=10;
    LLQueue *hashTable[2][tableSize];
    int data1;
    int data2;
    int currentTable=1;
    int previousTable=0;
    int maxDigits;
    int offSet;
    int digitPosition;
    int currentDigit;
    LLQueue *queue;
    int hashIndex;
    int tableIndex=0;
    
    Radixsort(){
        for(int r=0; r<2; r++){
            for(int c=0;c<10;c++){
                queue = new LLQueue();
                hashTable[r][c] = queue;}
        }
    }
    
    void preProcessing(ifstream &input, ofstream &deBug){
        int negativeNum=0;
        int positiveNum=0;
        while(!input.eof()){
            input>>data1;
            if(input.good()==true){
                if(data1<negativeNum){
                    negativeNum=data1;}
                else if(data1>positiveNum){
                    positiveNum=data1; }}
        }
        if(negativeNum<0){
            offSet=abs(negativeNum);//get absolute value of smallest
        }
        else{
            offSet=0;
        }
        positiveNum+=offSet;
        string len=to_string(positiveNum);
        maxDigits=len.length();
        deBug<<"The smallest Negative number is "<<negativeNum<<endl;
        deBug<<"And the absolute value of it is "<<offSet<<endl;
        deBug<<"The largest number is "<<positiveNum<<endl;
        deBug<<"It has "<<maxDigits<<" Digits"<<endl;}
        
    int getLength(int d){
        string l=to_string(d);
        return l.length();
    }
    
    int getDigit(int d, int position){
        if(position==0){
            return d%10;
        }
        else{
            d=d/10;
            return getDigit(d,position-1);
        }
    }
    
    void printTable(LLQueue **table, ofstream &output){
      for(int i=0;i<10;i++){
        if(table[i]->head!=NULL){
            table[i]->printQueue(i,output);}
      }
    }
    
    void printSortedData(LLQueue **t, ofstream &output){
        output<<"sorted Data:"<<endl;
        for(int j=0;j<10;j++){
            listNode *cur=t[j]->head;
            while(cur!=NULL){  
                for(int i=0;i<10&&cur!=NULL;i++){
                    output<<cur->data-offSet<<" ";
                    cur=cur->next;}
                output<<endl;
            }
        }
    }
        
    void RSort(ifstream &input, ofstream &output, ofstream &deBug){
        deBug<<"*** Perform RSort: "<<endl;
        digitPosition=0;
        currentTable=0;
        while(!input.eof()){
            input>>data2;
            data2+=offSet;
            if(input.good()==true){
                listNode *newNode=new listNode(data2);
                hashIndex=getDigit(data2,digitPosition);
                hashTable[currentTable][hashIndex]->insertQ(newNode);}
        }
        while(digitPosition<maxDigits){
            digitPosition++;
            previousTable=currentTable;
            currentTable=(currentTable+1)%2;
            tableIndex=0;
            deBug<<"digit Position: "<<digitPosition<<endl;
            deBug<<"current Table: "<<currentTable<<endl;
            deBug<<"previous table: "<<previousTable<<endl;
            output<<"*** Printing hashTable: "<<previousTable<<endl;
            printTable(hashTable[previousTable],output);

            while(tableIndex<=tableSize-1){
                while(!hashTable[previousTable][tableIndex]->isEmpty()){
                    listNode *newNode=hashTable[previousTable][tableIndex]->deleteQ();
                    hashIndex=getDigit(newNode->data,digitPosition);
                    hashTable[currentTable][hashIndex]->insertQ(newNode);
                }
                tableIndex++;}
            printTable(hashTable[previousTable],output);
        }
                
        printSortedData(hashTable[currentTable],output);
    }
};

int main(int argc, char* argv[]){
    ifstream inFile;
    ofstream outFile;
    ofstream deBugFile;
    inFile.open(argv[1]);
    outFile.open(argv[2]);
    deBugFile.open(argv[3]);
    
    
    if(inFile.fail()){
        cout<<"file 1 Error opening"<<endl;
        exit(1);
    }
    Radixsort *sort=new Radixsort();

    sort->preProcessing(inFile,deBugFile);
    inFile.close();
    inFile.open(argv[1]);
    sort->RSort(inFile,outFile,deBugFile);

    inFile.close();
    outFile.close();
    deBugFile.close();
    return 0;
}