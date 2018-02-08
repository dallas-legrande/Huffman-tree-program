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

public class Node implements Comparable<Node>{
    public int iData;           // char as an ascii number (key)
    public char dData;        // char
    public int frequency = 1;   // number of times this node is used
    public Node leftChild;      // this Node's left child
    public Node rightChild;     // this Node's right child
    public String binaryCode;   // this nodes binary number according to the huffman tree
    public Node parent;

    public void displayNode() { // display ourself
            System.out.print('{');
            System.out.print(iData);
            System.out.print(", ");
            System.out.print(dData);
            System.out.print("} ");		
    }
    public void increaseCount()
    {
        frequency++;
    }
    public int getCount()
    {
        return frequency;
    }

    // used with the priority queue
    // this tells the queue to base the priority on the node with the lowest frequency
    @Override
    public int compareTo(Node o) {
        return frequency - o.frequency;
    }
    public void addToBinary(String a)
    {
        binaryCode = binaryCode + (a);
    }
}
