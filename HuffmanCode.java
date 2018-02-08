package huffmancode;


/**
 * Authors: Dallas LeGrande, Selene Smith
 * Date: 2/6/18
 * 
 * Overview: Program #1 - Huffman Codes
 * Program used to:
 * 1. Accept a text message via input file (/input/input.txt)
 * 2. Construct frequency table for characters in the message
 * 3. Create a Huffman Tree for the message
 * 4. Create a code table from the Huffman Tree
 * 5. Encode the original message into binary
 * 6. Decode the encoded message and write the decoded message as output
 *      to an output file (/output/output.txt)
 * 
 * Credit to Robert Lafore, Data Structures and Algorithms in Java (2 ed.)
 * for the TreeApp.java code used in parts of this program
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;

public class HuffmanCode{

    private Node[] nodes = new Node[256];
    private char[] allChar = new char[256];
    private int index = 0;
    private Tree theTree = new Tree();
    private Tree huffmanTree = new Tree();
    private Node huffRoot;
    private Node n;
    PriorityQueue<Node> pq = new PriorityQueue<Node>();
    
    private String encodedMessage = "";
    private String decodedMessage = "";
    
    
    public static void main(String[] args) throws FileNotFoundException {
        
        HuffmanCode code = new HuffmanCode();
        code.readFile();
        code.createHuffTree();
        code.createCodeTable();
        code.encodeMessage();
        code.decodeMessage();
      
    }
    
    //reads the file and prints it to the screen
    public void readFile()
    {    
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

                }// ends the for loop
                //adds a line feed after each line so you know where the line breaks are. 10 is the ascii code for line feed
                Node newNode = theTree.insert(10 ,' ');
                if (newNode != null)
                    {     
                        nodes[index] = newNode;
                        index++;
                    }

                //prints out to the system
                System.out.println(line);
                }// ends the while loop

               //prints the total number of nodes if need for reference
               //System.out.println(theTree.nodeCount);
            }
        catch (IOException x)
            {
                System.err.format("IOException: %s%n",x);
            }
    fillQueue();    //fills the queue   
    printTable();   //prints the frequency table
    fillQueue();    //re-fills the queue
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
        System.out.println();
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
            
            // starts the binary code for each node with whether they are a left or right child
            if(leftChild.binaryCode == null)
            {   
                leftChild.binaryCode = ("0");
            }
            if(rightChild.binaryCode == null)
            {   
                rightChild.binaryCode = ("1");
            }
            //adds parent back into the queue
            pq.add(parent);
        }
        
        //This is the root of the Huffman tree
        huffRoot = (Node) pq.poll();
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
        
        //prints the code table
        System.out.println();
        System.out.println("Code Table");
        System.out.println("---------------");
        int k = 0;
        while(nodes[k] != null){
            System.out.println(nodes[k].dData + " | " + nodes[k].binaryCode);
            k++;
        }
    }
    
    // method to print the tree... not needed for our purposes, just for testing
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
        System.out.println();
        System.out.println("Encoded Message \n-----------");
        
        // read in the input file to encode by character
        Path path =  Paths.get("./input/input.txt");
        try (BufferedReader reader = Files.newBufferedReader(path)){
            String line;
            while((line = reader.readLine()) != null){
                   //converts the line read into a character array
                char[] stringToChar = line.toCharArray();
                for(int i = 0; i < stringToChar.length; i++){
                    char tmp = stringToChar[i];

                    //sends the ascii code to the encodeChar method
                    //once the char is returned it gets added to the string
                    String coded = encodeChar( (int) tmp);

                    //adds the char to the whole message
                    encodedMessage += coded;
                }// end of for loop

                //adds the new line character to the encoded message
                String newLineChar = encodeChar(10);
                encodedMessage += newLineChar;
               }  // end of while loop
        }catch(IOException x)
           {
               System.err.format("IOException: %s%n",x);
           }
        System.out.println(encodedMessage);    
    }
    
    // method to encode indivual characters into strings of binary
    public String encodeChar(int id)
    {
        for(int i = 0; i < nodes.length; i++)
        {
            if(nodes[i].iData == id){
                return nodes[i].binaryCode;
            }
        }
        return "not found";  // hail mary return
    }
    
    //reads the encoded message by following the code table to reconstruct the message
    public void decodeMessage() throws FileNotFoundException
    {
        // start writing to output files
        File file = new File("output/output.txt");

        //opens a way to write to a file
        PrintWriter pw = new PrintWriter(file);
        
        //PRINTS THE OUTPUT TO THE CONSOLE IF NEEDED
        //System.out.println();
        //System.out.println("Decoded Message \n-----------");
        Node n = huffRoot;
        
        // loop thru encoded message
        for(int i = 0; i < encodedMessage.length(); ){
            Node tmp = n;
            
            // chooses path to follow down tree -> 0 for right, 1 for left
            while(tmp.leftChild != null && tmp.rightChild != null && i < encodedMessage.length()){
                if(encodedMessage.charAt(i) == '1'){
                    tmp = tmp.rightChild;
                } 
                else{
                    tmp = tmp.leftChild;
                }
                i++;
            }
            decodedMessage+= tmp.dData;
            
            /*if the node is the line space...
              print the string that contains the message to the console, print to the file
              next start a new line, then erase the string with the message so the next line can be started fresh
            */
            if(tmp.iData == 10){
                
                
                //System.out.print(decodedMessage); //PRINTS THE DECODED MESSAGE TO THE CONSOLE
                pw.print(decodedMessage);
                pw.println();
                //System.out.println(); //PRINTS A NEW LINE TO THE CONSOLE
                decodedMessage = "";
            }
        }
        pw.close();  // close printwriter
    } 
}
