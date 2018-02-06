/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;

/**
 *
 * @author w71w233
 */
public class HuffmanCode{

    /**
     * @param args the command line arguments
     */
    
    private Node[] nodes = new Node[256];
    private char[] allChar = new char[256];
    private int index = 0;
    private Tree theTree = new Tree();
    private Tree huffmanTree = new Tree();
    private Node huffRoot;
    private Node n;
    PriorityQueue<Node> pq = new PriorityQueue<Node>();
    
    
    public static void main(String[] args) {
        
        HuffmanCode code = new HuffmanCode();
        code.readFile();
        code.createHuffTree();
        code.createCodeTable();
        code.encodeMessage();
      
    }
    
    //reads the file and prints it to the screen
    public void readFile()
    {
        try{
            //creates a file to output to
            File file = new File("output/output.txt");
            
            //opens a way to write to a file
            PrintWriter pw = new PrintWriter(file);
            
            //creates a path to show where the input file is located
            Path path =  Paths.get("./input/input.txt");
            
            //opens the bufferedreader to be able to read a file. The file location is held in the variable "path"
            try (BufferedReader reader = Files.newBufferedReader(path)){
                String line;

                //reads line by line until the line is null and the document is complete
                while ((line = reader.readLine()) != null)
                    {
                        //converts the line read into a character array
                        char[] stringToChar = line.toCharArray();
                        
                        //goes through the character array and creates a new node for each character
                        //node contains: the char as an int, the char, the frequency of that char
                        for(int i = 0; i < stringToChar.length; i++)
                        {
                            //creates a node and inserts it into the tree
                            //the idata is the character as an ascii number
                            //the ddata is the actual character
                            Node newNode = theTree.insert((int) stringToChar[i],stringToChar[i]);
                            if (newNode != null)
                            {     
                                nodes[index] = newNode;
                                index++;
                            }
                            
                        }
                        //adds ` after each line so you know where the line breaks are 10 is the ascii code for line feed
                        Node newNode = theTree.insert(10 ,' ');
                        if (newNode != null)
                            {     
                                nodes[index] = newNode;
                                index++;
                            }
                        
                        //prints out to the system
                        System.out.println(line);
                        
                        //prints to the output file - output
                        pw.println(line);
                    }
                
//                //displays all the nodes in the tree for testing purposes
//                   theTree.displayTree();
                   
                   //prints the total number of nodes
                   System.out.println(theTree.nodeCount);
                }
            catch (IOException x)
                {
                    System.err.format("IOException: %s%n",x);
                }
        
 
        fillQueue();    //fills the queue   
        printTable();   //prints the frequency table
        fillQueue();    //re-fills the queue

            //closes the printwriter and writes to the file
            pw.close();
            }
        catch (IOException e){
            System.out.println("File not found");
              }
    }
    
    // test priority queue
    public void printQueue(){
        
        while(!pq.isEmpty()){
            Node n = (Node) pq.poll();
            System.out.println(n.dData + " , ");
        }
        
    }
    
    //fills the queue with the nodes
    public void fillQueue()
    {
        int i = 0;
        while(nodes[i] != null)
        {
            nodes[i].leftChild = null;
            nodes[i].rightChild = null;
            pq.add(nodes[i]);
            i++;
        }
    }
    
    //prints the Frequency Table
    public void printTable(){
        System.out.println("Frequency Table");
        System.out.println("---------------");
        while(!pq.isEmpty()){
            Node n = (Node) pq.poll();
            System.out.println(n.dData + " | " + n.frequency);
        }     
    }
   
    //creates a huffman binary tree based on the frequency table
    //take the first two out of the queue
    //create a new parent node with first node popped as the left child
    //and the second node popped out at the right child
    //frequency of parent node = left child frequency plus right child frequency
    public void createHuffTree()
    {
        while(pq.size() > 1){
            Node leftChild = (Node) pq.poll();
            Node rightChild = (Node) pq.poll();
            Node parent = new Node();
            parent.frequency = (leftChild.frequency + rightChild.frequency);
            parent.leftChild = leftChild;
            parent.rightChild = rightChild;
            parent.iData = 0;
            leftChild.parent = parent;
            rightChild.parent = parent;
            
            // starts the binary code for each node if they are a left node the get a 0 if they are a right child node they get a 1
            if(leftChild.binaryCode == null)
            {   
                leftChild.binaryCode = ("0");
            }
            if(rightChild.binaryCode == null)
            {   
                rightChild.binaryCode = ("1");
            }
            
            pq.add(parent);
        }
        huffRoot = (Node) pq.poll();
        //huffmanTree.postOrder(huffRoot);
        //printTree(huffRoot);
        huffmanTree.displayTree(huffRoot);
        
    }
    
    //creates the code table based on the huffman binary tree
    public void createCodeTable()
    {
        // goes through all of the nodes and adds the binary codes of each of the parent nodes all the way to the root
        // each node then has the binary code for itself as part of the node
        int i = 0;
        while(nodes[i] != null)
            {
            n = nodes[i].parent;
            while(n != huffRoot)
            {
                if(n.binaryCode != null){
                nodes[i].binaryCode = n.binaryCode + nodes[i].binaryCode;
                n = n.parent;
                }
            }
            i++;
        }
        
        //prints the code table for testing
        System.out.println("Code Table");
        System.out.println("---------------");
        int k = 0;
        while(nodes[k] != null){
            System.out.println(nodes[k].dData + " | " + nodes[k].binaryCode);
            k++;
        }
    }
    public void printTree(Node root)
    {
        root = huffRoot;
        while(root != null)
                {
                   if(root.iData == 0)
                   {
                       System.out.println("NODE");
                   }
                   //System.out.println(root.dData);
                   if(root.leftChild != null)
                   {
                       System.out.println(root.leftChild.dData);
                   }
                   
                   if(root.leftChild != null)
                   {
                       System.out.println(root.rightChild.dData);
                   }
                   
                   root = root.leftChild;
                }
    }
    
    //reads the file and converts the characters to binary according to the code table
     public void encodeMessage(){
    
        String encodedMessage = "";
        
        Path path =  Paths.get("./input/input.txt");
             try (BufferedReader reader = Files.newBufferedReader(path)){
                String line;
                
                while((line = reader.readLine()) != null)
                    {
                        //converts the line read into a character array
                        char[] stringToChar = line.toCharArray();
                        
                        for(int i = 0; i < stringToChar.length; i++){
                            char tmp = stringToChar[i];
                            
                            String coded = encodeChar(tmp);
                            
                            System.out.print(coded);
                            
                            //System.out.print(tmp);
                            //encodedMessage += encodeChar(tmp);
                        }
                        
                    }
                
                
             }catch(IOException x)
                {
                    System.err.format("IOException: %s%n",x);
                }
        
             System.out.println(encodedMessage);
             
    }
    
    public String encodeChar(char c){

        for(int i = 0; i < nodes.length - 1; i++){
            
            if(nodes[i].dData == c){
                return nodes[i].binaryCode;
            }
        }
        
        return "not found";
    }
    
    //reads the encoded message by following the code table to reconstruct the message
    public void decodeMessage()
    {
        
    }

}
