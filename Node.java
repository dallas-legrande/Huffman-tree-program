/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancode;

/**
 *
 * @author Dallas
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

    @Override
    public int compareTo(Node o) {
        return frequency - o.frequency;
    }
    public void addToBinary(String a)
    {
        binaryCode = binaryCode + (a);
    }
}
