/**
 *
 * This class starts reading a file and inserts a hashtag in hashtable and fibonacci heap.
 */
import java.io.*;
import java.util.*;
import java.io.FileReader;
import java.lang.*;

public class ReadFile{

    //creating object of file that refers to a new output file 	
    File file = new File("output_file.txt");


    // This will reference one line at a time
    String line = null;

    Hashtable<String, Node> hashtags = new Hashtable<String, Node>();
    MaxFibonacciHeap mfh = new MaxFibonacciHeap();


    public void ReadFileText(String fileName) throws Exception{


        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);


            //Output Writer 
            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            while ((line = bufferedReader.readLine()) != null) {
                if(line.charAt(0)==35) {
		   
		   //Split line read based on space
                    String[] splitres = line.split("\\s");

                   //HashTag Insert with IncreaseKey (i.e hashtag repeated in input file)
                    if(hashtags.containsKey(splitres[0]))
                    {
                        Node n = hashtags.get(splitres[0]);
                        int increasekey = Integer.parseInt(splitres[1]);
                        mfh.increaseKey(n,increasekey);
                        n = hashtags.get(splitres[0]);
                    }
                    //HashTag Insert 1st time
                    else
                    {
                        int freq = Integer.parseInt(splitres[1]);
                        Node n = mfh.insert(new Node(freq));
                        hashtags.put(splitres[0],n);
                    }
                }
                else if(line.equals("stop") || line.equals("STOP"))
                {
                    out.close();
                    // File End
                }

		//when a query is read perform RemoveMax on the fibonacci heap "query" no of times
                else
                {
                    line=line.trim();
                    int query = Integer.parseInt(line);
                    Node[] myListOfMaxNodes = new Node[20];

                    for (int i=0; i<query; i++)
                    {
			//store removed node to reinsert it later
                        myListOfMaxNodes[i] = mfh.RemoveMax();

                        //get key for the given value from hashtable
			String key = null;
                        Node value = myListOfMaxNodes[i];
                        for(Map.Entry entry: hashtags.entrySet()) {
                            if (value.equals(entry.getValue())) {
                                key = entry.getKey().toString();
                                break; //breaking because its one to one map
                            }
                        }
			 //write hashtag to outputfile
                            out.write(key.substring(1,key.length()));

                            if(query-1 != i )
                            {
                                out.write(",");
                            }
                            else if(query-1 == i)
                            {
                                out.write("\n");
                            }

                    }

		     //reinsert removed hashtags into the fibonacci heap
                    for (int i=0; i<query; i++)
                    {
                        Node n = mfh.insert(myListOfMaxNodes[i]);
                    }

                }


            }
            bufferedReader.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");

        }

    }
}

