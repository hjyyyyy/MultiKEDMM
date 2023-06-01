package MultiKEDMM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Evaluation.Accuracy;
import Evaluation.MacroF1;
import Evaluation.PMI;
import Util.BuildInstances;
import Util.Common;
import Util.Corpus;
import Util.Document;
import Util.ReadWriteFile;


public class resultMultiKEDMM
{
	
	public static void main(String[] args) throws Exception
	{
		
		String project_dir = "D:\\Code\\java\\MultiKEDMM";
		String data_root = project_dir+"\\data\\shortTextCorpus\\";
		
		String corpus_name = "StackOverflow" ;
		String corpus_root = data_root+corpus_name;
		String corpus_file = corpus_root+"\\"+corpus_name+"_corpus.txt";
		String corpus_label = corpus_root+"\\"+corpus_name+"_label.txt";
		String corpus_vocab = corpus_root+"\\"+corpus_name+"_vocab.txt";
		String corpus_sim = corpus_root+"\\"+corpus_name+"_entitySIM.txt"; //word similarity file computed by word embedding or multiple knowledge embedding
//		String corpus_sim = "D:\\Code\\java\\GPUDMM-master\\data\\corpusKE\\Vec\\Word2Vec\\cosineSIM\\StackOverflow_Word2VecCosineSim.txt";
		String wiki_dir = corpus_root+"\\"+"word_wiki\\"; 
//		String wiki_dir = "D:\\Code\\java\\GPUDMM-master\\data\\StackOverflow\\word_wiki\\";
		String save_root = project_dir+"\\result\\"; // save for topWord, pdz, pd, pzw file

		
		//Input
		ArrayList<Document> doc_list = Document.LoadCorpus(corpus_file,corpus_label);
		int num_iter = 1000,round_time = 5;
		double beta = 0.01;
		double weight = 0.3;  // hyper-parameter 
		double threshold = 0.7; // stackOverflow 0.8; snippet 0.7; Tweet 0.7; 
		int filterSize = 20;
		int TOP = 20;
		int[] Ks = new int[] {20,40,60,80};
		// K for SearchSnippet, StackOverflow,Biomedical {20,40,60,80}
		// K for GoogleNews, Tweet {100,150,200,250}
			
		
		StringBuilder sb = new StringBuilder();
		sb.append(corpus_name+"\n");
		sb.append("Parameter:\n");
		sb.append("alpha\t"+"50\\K"+"\n");
		sb.append("beta\t"+beta+"\n");
		sb.append("thres\t"+threshold+"\n");
		sb.append("weight\t"+weight+"\n");
		sb.append("filter\t"+filterSize+"\n");
		sb.append("----------Result-------------\n");
		for(int k=0;k<Ks.length;k++) 
		{	
			int K = Ks[k];
			double[] accuracy = new double[round_time];
			double[] pmi = new double[round_time];
			String flag = "";
			
			for (int round = 0; round < round_time; round += 1) 
			{	
				
				// set input for MultiKE-DMM
				double alpha = 1.0 * 50 / K;
				MultiKE_DMM multikeDmm = new MultiKE_DMM(doc_list, K, num_iter, beta, alpha, threshold);
				multikeDmm.word2idFileName = corpus_vocab;
				multikeDmm.topWords = TOP;
				multikeDmm.filterSize = filterSize;
				multikeDmm.roundIndex = round;
				multikeDmm.similarityFileName = corpus_sim;
				multikeDmm.weight = weight;
				
				/*
				 * train
				 */
				multikeDmm.initNewModel();
				multikeDmm.init_GSDMM();
				multikeDmm.run_iteration();
				
				//save model
				flag = corpus_name+round+"round_"+threshold+"thres_"+weight+"weight_"+filterSize+"filter_"+K+"K_"+TOP+"TOP_";
				flag = save_root+"\\" + flag;
				multikeDmm.saveModel(flag);
				
				/*
				 * Topic Coherence
				 */
				args = new String[4];
				args[0] = flag+"_TopWORD.txt";
				args[1] = corpus_vocab;
				args[2] = 5+"";  // compute PMI of TOP5 words in each topic
				args[3] = wiki_dir;
				pmi[round] = PMI.main(args);
				System.out.println("PMI : " + pmi[round]);
				
				/*
				 * Classification Accuracy
				 */
				args = new String[4];
				args[0] = corpus_label; // label file
				args[1] = flag+"_pdz.txt";// pdz file
				args[2] = flag; // save for test, train file
				args[3] = ""+K;
				accuracy[round] = Accuracy.main(args);
				System.out.println("Accuracy : " + accuracy[round]);
				
				
			} // FOR round
			sb.append("K\t"+K+"\n");
			sb.append("PMI_T5\t"+Common.mean(pmi)+"\t"+Common.Sample_STD_dev(pmi)+"\n");
			sb.append("Accuracy\t"+Common.mean(accuracy)+"\t"+Common.Sample_STD_dev(accuracy)+'\n');	
		} // FOR K
		
		ReadWriteFile.writeFile(save_root+"\\result"+".txt", sb.toString());	// save for PMI, Accuracy...
	}
}
