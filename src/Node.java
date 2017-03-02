/**
 *
 * This class defines the structure of any node in the MaxFibonacciHeap
 */


public class Node 
{
    
    Node child = null;
    Node parent = null;
    Node left = this;
    Node right = this;
    
    int degree = 0;
    boolean childCut = false;
    int frequency=-1;
    
    public Node (int frequency)
    {
        this.frequency = frequency;
    }
    
}
