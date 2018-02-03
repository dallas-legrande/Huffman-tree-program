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
    public int iData;           // data item (key)
    public char dData;        // data item
    public int frequency = 1;   // number of times this node is used
    public Node leftChild;      // this Node's left child
    public Node rightChild;     // this Node's right child
    

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
        
//        if(this == o)
//        {
//            if(this.leftChild != null)
//            {
//                return -1;
//            }
//        }
        return frequency - o.frequency;
    }
}
