package Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Document {
  
  public String [] words;
  public int id;
  public String category;
  
  
  public Document(int docid, String category, String [] words){
    this.id = docid;
    this.category = category;
    this.words = words;
  }
  
  
  public static ArrayList<Document> LoadCorpus(String corpus , String corpus_label){
	  try 
	  {
		  String corpus_file = corpus ;
		  String corpus_category = corpus_label;
		  System.out.println(corpus_file);
		  System.out.println(corpus_category);
		  
		  FileInputStream fis = new FileInputStream(corpus_file);
	      InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	      BufferedReader reader = new BufferedReader(isr);
	      
	      FileInputStream fisc = new FileInputStream(corpus_category);
	      InputStreamReader isrc = new InputStreamReader(fisc, "UTF-8");
	      BufferedReader readerc = new BufferedReader(isrc);
	      
	      String line;
	      String linec;
	      int index=0;
	      ArrayList<Document> doc_list = new ArrayList();
	      while((line=reader.readLine())!=null &&(linec=readerc.readLine())!=null) 
	      {
	    	  line = line.trim();
	    	  String[] words = line.split(" ");
//	    	  System.out.print(words[0]);
	    	  linec = linec.trim();
			  String[] items = linec.split("\t");
			  String category = items[items.length-1];
    
	    	  int docid = index;
	    	  Document doc = new Document(docid,category,words);
//	    	  System.out.println(doc.category);
	    	  doc_list.add(doc);
	    	  index = index+1;
	    	  
	      }
	      return doc_list;
		  
	  }
	  catch(Exception e) {
		  System.out.println("Error while reading other file:" + e.getMessage());
	      e.printStackTrace();
	  }
	  return null;
  }

}
