/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

class Tree {
	private Node root;                 // first Node of Tree
        //public Node root;
        public static int nodeCount = 0;    // counts the total number of nodes created
	
	public Tree() {                    // constructor
		root = null;                   // no nodes in tree yet
	}
	
	public Node find(int key) {      // find node with given key
		Node current = root;         // (assumes non-empty tree)
		while (current.iData != key) {          // while no match
			if (key < current.iData) {          // go left?
				current =  current.leftChild; 
			}
			else {                              // or go right?
				current =  current.rightChild;
			}
			if(current == null)                 // if no child
			{                                   // didn't find it
				return null;              
			}			
		}
		return current;                         // found it
	}  //end find()
	
	
	public Node insert(int id, char dd) {
		Node newNode = new Node();    // make new Node
		newNode.iData = id;           // insert data
		newNode.dData = dd;
		newNode.leftChild = null;
		newNode.rightChild = null;
		if(root == null) {            // no node in root
			root = newNode;
                        nodeCount++;            //updates the total node count
                        return root;
		}
		else {                        // root occupied
			Node current = root;      // start at root  
			Node parent;
			while (true) {            // exits internally			
				parent = current;
                                
                                //if a node already exists with the same idata then it increases 
                                // the frequency of the pre-existing node and deletes the duplicate node
                                if(newNode.iData == current.iData)
                                {
                                    current.increaseCount();    //increases the frequency count
                                    newNode = null;             //erases the duplicate node
                                    return (newNode);
                                }
                                else if (id < current.iData) {              // go left?
					current =  current.leftChild;
					if(current == null) {             // if the end of the line        
						parent.leftChild = newNode;   // insert on left
                                                nodeCount++;                //increase the total node count
						return newNode;                    
					}
				} //end if go left
				else {                                // or go right?
					current =  current.rightChild;      
					if(current == null)               // if the end of the line
					{                                 // insert on right
						parent.rightChild = newNode;
                                                nodeCount++;        //increase the total node count
						return newNode;                    
					}
				}
			}
		}
	} // end insert()

	
	public boolean delete(int key) {             // delete node with given key
		Node current = root;		             // (assumes non-empty list)
		Node parent = root;
		boolean isLeftChild = true;

		while (current.iData != key) {           // search for Node
			parent = current;
			if (key < current.iData) {           // go left?
				isLeftChild = true;
				current =  current.leftChild;
			}
			else {                               // or go right?
				isLeftChild = false;
				current =  current.rightChild;
			}
			if(current == null) {                // end of the line,                             
				return false;                    // didn't find it
			}			
		}
		//found the node to delete

		//if no children, simply delete it
		if (current.leftChild == null && current.rightChild == null) {
			if (current == root) {              // if root,
				root = null;                    // tree is empty
			}
			else if (isLeftChild) {
				parent.leftChild = null;        // disconnect
			}                                   // from parent
			else {
				parent.rightChild = null;
			}
		}
		//if no right child, replace with left subtree
		else if (current.rightChild == null) {  
			if (current == root) {
				root = current.leftChild;
			}
			else if (isLeftChild) {
				parent.leftChild = current.leftChild;
			}			
			else {
				parent.rightChild = current.leftChild;
			}
		}

		//if no left child, replace with right subtree
		else if (current.leftChild == null) {  
			if (current == root) {
				root = current.rightChild;
			}
			else if (isLeftChild) {
				parent.leftChild = current.rightChild;
			}			
			else {
				parent.rightChild = current.rightChild;
			}
		}

		else { // two children, so replace with inorder successor
			   // get successor of node to delete (current)
			Node successor = getSuccessor(current);

			// connect parent of current to successor instead
			if (current == root) {
				root = successor;
			}
			else if (isLeftChild) {
				parent.leftChild = successor;
			}
			else {
				parent.rightChild = successor;
			}

			//connect successor to current's left child
			successor.leftChild = current.leftChild;
		} // end else two children
		// (successor cannot have a left child)
		return true;              // success
	}// end delete()

	
	//returns node with next-highest value after delNode
	//goes right child, then right child's left descendants
	private Node getSuccessor(Node delNode) {
		Node successorParent = delNode;
		Node successor = delNode;
		Node current = delNode.rightChild;        // go to the right child
		while (current != null) {                 // until no more
			successorParent = successor;          // left children
			successor = current;
			current = current.leftChild;
		}

		if (successor != delNode.rightChild) {    // if successor not right child,
			//make connections
			successorParent.leftChild = successor.rightChild;
			successor.rightChild = delNode.rightChild;
		}
		return successor;
	}

	
	public void traverse(int traverseType) {
		switch (traverseType) {
		case 1:
			System.out.print("\nPreorder traversal: ");
			preOrder(root);
			break;
		case 2:
			System.out.print("\nInorder traversal: ");
			inOrder(root);
			break;
		case 3:
			System.out.print("\nPostorder traversal: ");
			postOrder(root);
			break;
		default:
			System.out.print("Invalid traversal type\n");
			break;
		}
		System.out.println();
	}

	
	private void preOrder(Node localRoot) {
		if (localRoot != null) {
			System.out.print(localRoot.iData + " ");	
			preOrder(localRoot.leftChild);
			preOrder(localRoot.rightChild);	
		}
	}

	
	private void inOrder(Node localRoot) {
		if (localRoot != null) {
			inOrder(localRoot.leftChild);
			System.out.print(localRoot.iData + " ");
			inOrder(localRoot.rightChild);		
		}
	}
        public Node returnRoot()
        {
            return root;
        }
	
	public Node postOrder(Node localRoot) {
		if (localRoot != null) {
			postOrder(localRoot.leftChild);
			postOrder(localRoot.rightChild);
                        System.out.print(localRoot.dData + " ");		
                        return localRoot;
		}
                return localRoot;
	}

        
        
        public void displayTree(Node localRoot){
            
            Queue<Node> lvl = new LinkedList<>();
            Queue<Node> nextLvl = new LinkedList<>();
            int lvlCounter = 0;
            
            // put local root into queue
            lvl.add(localRoot);
            
            System.out.println("Huffman Tree\n-------------------");            
            
            while(!lvl.isEmpty()){
                Iterator<Node> lvlItr = lvl.iterator();
                
                while(lvlItr.hasNext()){
                                        
                    Node node = lvlItr.next();
                    
                    if(node.leftChild != null){
                        nextLvl.add(node.leftChild);
                    }
                    if(node.rightChild != null){
                        nextLvl.add(node.rightChild);
                    }
                    if(node.leftChild == null && node.rightChild == null){
                        //node is leaf
                    }
                                       
                    System.out.print(" " + node.dData + " ");   
                }
                
                System.out.println("");
                
                lvl = nextLvl;
                nextLvl = new LinkedList<Node>();
                lvlCounter++;
                
                
            }
        }
        
	//alternate method for printing the tree
	public void alternateDisplayTree(Node localRoot) {
		Stack<Node> globalStack = new Stack<Node>();
		globalStack.push(localRoot);
		int nBlanks = 288+32+64+64+64+64;
		boolean isRowEmpty = false;
		System.out.println(
				".................................................................");
		while (isRowEmpty==false) {
			Stack<Node> localStack = new Stack<Node>();
			isRowEmpty = true;
			
			for (int j = 0; j < nBlanks; j++) {
				System.out.print(' ');
			}

			while (globalStack.isEmpty()==false) {
				Node temp = (Node) globalStack.pop();
				if (temp != null) {
                                        if(temp.dData == 0)
                                        {
                                            System.out.print("NODE");
                                            localStack.push(temp.leftChild);
                                            localStack.push(temp.rightChild);
                                            if (temp.leftChild != null ||
                                                            temp.rightChild != null) {
                                                    isRowEmpty = false;
                                            }
                                        }
                                        else{
					System.out.print(temp.dData);
					localStack.push(temp.leftChild);
					localStack.push(temp.rightChild);
					if (temp.leftChild != null ||
							temp.rightChild != null) {
						isRowEmpty = false;
					}
                                        }
				}
				else {
					System.out.print("--");
					localStack.push(null);
					localStack.push(null);
				}

				for (int j = 0; j < nBlanks*2-2; j++) {
					System.out.print(' ');
				}
			} 
			System.out.println();

                            nBlanks /= 2;

			while (localStack.isEmpty()==false) {
				globalStack.push(localStack.pop());
			} // end while isRowEmpty is false
                        for(int k = 0; k < 1000; k++){
                            System.out.print(".");
                        }
                        System.out.println();
			
		}// end while globalStack not empty
	} // end displayTree()
        
        //finds the minimum value node in the tree
        public Node findMin(){
            Node min = root;         // (assumes non-empty tree)
		while (min.leftChild != null) {          // while min has a left child
			min = min.leftChild;		
                }
                return min;
        }
        
        //finds the maximum value node in the tree
        public Node findMax(){
            Node max = root;
            while (max.rightChild != null) {          // while min has a left child
			max = max.rightChild;		
                }
            return max;
        }
}// end class Tree
////////////////////////////////////////////////////////////////
