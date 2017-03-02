/**
 *
 * This class has the implementation for the MaxFibonacciHeap which includes operations
 * like insert, RemoveMax, Meld, CascadingCut.
 */

import java.util.*;
import java.lang.*;
import java.math.*;

public class MaxFibonacciHeap{
    
    //Global Variables declared here maximumNode will hold the node with highest value in heap at all times
    
    Node maximumNode;
    boolean updateHeap;
    
    //Constructor of MaxHeap
    public MaxFibonacciHeap(){
        maximumNode = null;
        updateHeap = false;
    }
    

//This method inserts a new node into the max fibonacci heap
    public Node insert(Node n)
    {
	//check if the node being inserted is the 1st node in the heap or not, insert it and update the maximumNode
        if(maximumNode != null)
        {
	    //insert the current node at top level circular list of the heap	
            insertAtTopLevel(n);
            return n;
        }
        else 
        {
            maximumNode = n;
            return maximumNode;
        }
        
    
    }

//Updates the left and right pointers of node being added
    private void updateLinksAtAnyLevelToAddNewNode(Node n, Node toBeLeft, Node toBeRight)
    {
        n.left = toBeLeft;
        n.right = toBeRight;
        toBeLeft.right = n;
        toBeRight.left = n;


        
    }

// Compares the current node with the maximumNode and updates the value of maximumNode if necessary  
    private void compareToUpdateMaximumNode(Node n) {
        if(maximumNode.frequency < n.frequency)
            maximumNode = n;
    }

// Inserts the current node at the top level list of the heap
    private void insertAtTopLevel(Node n){
	//check if the parent of current node is null. If not it is set to null here
        if(n.parent != null) {
            n.parent = null;
        }
        updateLinksAtAnyLevelToAddNewNode(n, maximumNode, maximumNode.right);
        compareToUpdateMaximumNode(n);
    }

// Updates the frequency of the current node with amount specified by the argument.
// Check if there is a need to perform cascadingCut, if yes perform it.
    public void increaseKey(Node n, int x)
    {
        //Increase the frequency of the node n
        n.frequency += x;
        
        //Check if node n is now maximumNode at root level or it has changed the heap structure at some other level
        if(n.parent == null)
        {
            compareToUpdateMaximumNode(n);
        }
        
        else{
	//check if increase key violated the heap property i.e. value of current node became greater that its parent
            if(n.parent.frequency < n.frequency)
            {
                Node nparent = n.parent;

                UpdateChildNode(n);

		//remove current node from its sibling list to insert it at top level
                n.left.right=n.right;
                n.right.left=n.left;
                Node temp = n.parent;
                Node temp2 =n;
		//update the degree of the parent if it is affected by removal of current node
                while(temp!=null){
			
		    // get the maximum degree from sibling list of the current node
                    int check = getSiblingsDegree(temp);
                    temp.degree = check +1 ;

                    temp=temp.parent;
                    temp2=temp2.parent;

                }

                insertAtTopLevel(n);
//one time call to cascadingcut
                CascadingCut(nparent);
            }
        }
        
        
    }

// Traverses all the sibling nodes of the current node to return the node with maximum degree.
    private int getSiblingsDegree(Node n) {
        int tempDeg=n.child.degree;
        for(Node i=n.child;i.right!=n.child;i=i.right){
            if(i.degree>tempDeg)
                tempDeg = i.degree;
        }


        return tempDeg;
    }

// Update the child pointer of a node when its child is removed to be inserted at the root level
    private void UpdateChildNode(Node n) {
  
        if(n.parent!= null){
	    //check if child pointer of parent node points to current node. If yes update it to point to left or right sibling
            if(n.parent.child == n)
            {
                if(n.left != null)
                    n.parent.child = n.left;
                else if(n.right != null)
                    n.parent.child = n.right;
                else
                    n.parent.child = null;
            }

            
        }    
        
    }

// Check for the boolean childcut and perform cascading cut operation starting at the current node
// Recurssively perform cascading cut on parent of current node till either we reach a root level node 
// or the childcut value becomes false for a node 

    private void CascadingCut(Node nparent) throws NullPointerException{
        if( nparent != null){
            if(nparent.parent != null){
                if(nparent.childCut == true){
                    nparent.left.right = nparent.right;
                    nparent.right.left = nparent.left;

                    Node temp1 = nparent.parent;
                    updateHeap = true;
                    UpdateChildNode(nparent);

                    Node temp = nparent.parent;
                    Node temp2 = nparent;
                    while(temp!=null){
                        int check = getSiblingsDegree(temp);
                        temp.degree = check +1 ;

                        temp=temp.parent;
                        temp2=temp2.parent;

                    }
                    insertAtTopLevel(nparent);
		    
// recursive call to cascading cut 
                    CascadingCut(temp1);
                }
                else
                {
                    nparent.childCut = true;
                }
            }
            
            else
                nparent.childCut = false;
                
        }
    }

// This method updates the heap structure after a RemoveMax operation
    private void UpdateHeap() throws NullPointerException{
        Node x;
        int topLevelNodes;
        ArrayList<Node> degrees = new ArrayList<Node>();
        
        if(updateHeap == true){
            int temp=1;
            if(maximumNode == null)
                temp = 0;
            else{
                

		//finding the number of nodes in top level list
                for(Node i = maximumNode;i.right != maximumNode; i = i.right)
                    temp++;
            }
            topLevelNodes = temp;
            Node m;
            int i;
            
            for(m = maximumNode, i = 0; i<topLevelNodes; m=m.right, i++){
                int thisdegree = m.degree;
                if((degrees.size() <= thisdegree) ) {
                    while ((degrees.size() <= thisdegree))
                        degrees.add(degrees.size(),new Node(-1));

                    degrees.set(thisdegree, m);
                    continue;
                }
                if(degrees.get(thisdegree).degree==m.degree){
                    continue;
                }

                for(;degrees.get(thisdegree).frequency!= -1;){
                    
                    // Logic for Meld starts here
		    // Compares two nodes of same degree and makes the node with smaller
		    // frequency the child node with higher frequency
                    if(m.frequency > degrees.get(thisdegree).frequency){
                        degrees.get(thisdegree).left.right = degrees.get(thisdegree).right;
                        degrees.get(thisdegree).right.left = degrees.get(thisdegree).left;

                        if(m.child != null){
                            degrees.get(thisdegree).right.left = degrees.get(thisdegree).left;
                            degrees.get(thisdegree).left.right = degrees.get(thisdegree).right;
                            updateLinksAtAnyLevelToAddNewNode(degrees.get(thisdegree) , m.child, m.child.right);
                        }
                        else{

                            degrees.get(thisdegree).right.left = degrees.get(thisdegree).left;
                            degrees.get(thisdegree).left.right = degrees.get(thisdegree).right;

                            m.child = degrees.get(thisdegree);
                            degrees.get(thisdegree).left = degrees.get(thisdegree);
                            degrees.get(thisdegree).right = degrees.get(thisdegree);
                        }
                        
                        degrees.get(thisdegree).parent = m;
                        m.degree++;
                    }
                    
                    else{

                        
                            degrees.get(thisdegree).left.right = degrees.get(thisdegree).right;
                            degrees.get(thisdegree).right.left = degrees.get(thisdegree).left;

                            updateLinksAtAnyLevelToAddNewNode(degrees.get(thisdegree), m, m.right);

                        if(degrees.get(thisdegree).child != null){
                            m.right.left = m.left;
                            m.left.right = m.right;
                            updateLinksAtAnyLevelToAddNewNode(m , degrees.get(thisdegree).child, degrees.get(thisdegree).child.right);
                        }
                        else{
                            m.right.left = m.left;
                            m.left.right = m.right;

                            degrees.get(thisdegree).child = m;
                            m.left = m;
                            m.right = m;
                        }
                        
                        m.parent = degrees.get(thisdegree);
                        degrees.get(thisdegree).degree++;
                        m = degrees.get(thisdegree);

                    }
                    
                    degrees.set(thisdegree, new Node(-1));
                    thisdegree++;

                    if(degrees.size() <= thisdegree){
                        degrees.add(degrees.size(),new Node(-1));
                    }

                }
                degrees.set(thisdegree, m);
            }
            
            updateHeap = false;
        }
        
    }
    
// RemoveMax initiates removal of the maximumNode of the heap.
// We update the maximumNode of the heap when a node is removed from the heap.
// All child nodes of the maximumNode are inserted at the top level list of heap.   
    public Node RemoveMax() throws Exception{

        Node MaxNode = maximumNode;
        Node m=maximumNode.right;
        Node tm=maximumNode.right;
        if(maximumNode==maximumNode.right && maximumNode.degree!=0)
        {
            ArrayList<Node> ListOfChildNodes = new ArrayList<Node>();
            Node strt = MaxNode.child;
            Node curr = strt;
            //int nchild=0;
            if(strt.left == strt && strt.right==strt)
            {
		// Make a list of all child nodes of the node being removed
                ListOfChildNodes.add(curr);
            }
            else {
                ListOfChildNodes.add(curr);
                while (curr.right != strt) {
                    ListOfChildNodes.add(curr.right);
		    curr = curr.right;
                }
            }
	    // Insert the child nodes at top level list
            for(Node i:ListOfChildNodes){
                insertAtTopLevel(i);
            }

            MaxNode.child = null;
            MaxNode.degree = 0;

            m=maximumNode.right;
            tm=maximumNode.right;
            while(m!=maximumNode){
                if(tm.frequency < m.frequency)
                    tm = m;
                m=m.right;
            }
		
            maximumNode=tm; //maximumNode of the heap updated

           
        }
        else {
            if (maximumNode == maximumNode.right) {
                if (maximumNode.degree == 0) {
                    maximumNode = null;
                }

            }
            else {
                while (m != maximumNode) {
                    if (tm.frequency < m.frequency)
                        tm = m;
                    m = m.right;
                }
                maximumNode = tm; //maximumNode of the heap updated
            }
        }

        
        RemoveNode(MaxNode);

// Reset all pointers of the removed MaxNode 
        MaxNode.right = null;
        MaxNode.left = null;
        MaxNode.child = null;
        MaxNode.parent = null;
        MaxNode.childCut = false;
        MaxNode.degree = 0;

// Return the MaxNode of the heap
        return MaxNode;
    }

// This node helps execution of the RemoveMax()
// Removes the maxnode actually from the heap by updating its pointers
// Check for all its child nodes and insert them at root level list 
// Gives a call to UpdateHeap to check if heap properties have been violated
// and if it needs to be restructured 
    private void RemoveNode(Node MaxNode) throws Exception{
        
        MaxNode.right.left = MaxNode.left;
        MaxNode.left.right = MaxNode.right;

        updateHeap=true;
        if(MaxNode.degree != 0)
        {
            updateHeap = true;
            ArrayList<Node> ListOfChildNodes = new ArrayList<Node>();
            Node strt = MaxNode.child;
            Node curr = strt;
            //int nchild=0;
            if(strt.left == strt && strt.right==strt)
            {
                ListOfChildNodes.add(curr);
            }
            else {
                ListOfChildNodes.add(curr);
                while (curr.right != strt) {
                    ListOfChildNodes.add(curr.right);
                    //
                    curr = curr.right;
                }
            }
            for(Node i:ListOfChildNodes){
                insertAtTopLevel(i);
            }
      
        }
        
        UpdateChildNode(MaxNode);
        CascadingCut(MaxNode.parent);
        UpdateHeap();
            
    }
    
}
