/**
 *
 * This class is the starting execution point for the project where we create an object 
 * of ReadFile class. We read the name of input file that is provided as command line
 * argument while running this program and call the ReadFileText method to start reading the file
 */
public class hashtagcounter{

    public static void main(String[] args) throws Exception{
       
        ReadFile rf = new ReadFile();
        if(args[0]=="")
            System.out.println("Please enter a valid file name");
        else{          
// this call will Start reading the input file and pass transfer to ReadFileText method of ReadFile class        
		rf.ReadFileText(args[0]);
        }
        }
    
}
