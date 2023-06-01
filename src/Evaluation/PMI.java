package Evaluation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Util.Corpus;
import Util.ReadWriteFile;

public class PMI
{
	
//	public static final int wiki_docs = 4776093;

	public static final int wiki_docs = 300000;
	public static String wiki_dir;

	/**
	 * 算一个主题的PMI (Newman et al., 2010)，文档共生
	 * 
	 * @param topic
	 * @return
	 * @throws IOException
	 */
	public static double pmi(String[] topic,int top) throws IOException
	{

		
		Set<Set<String>> word_pairs = new HashSet<>();

		for (String word_i : topic) {

			for (String word_j : topic) {

				if (!word_i.equals(word_j)) {

					Set<String> word_pair = new HashSet<>();

					word_pair.add(word_i);

					word_pair.add(word_j);

					word_pairs.add(word_pair);
					

				}
			}
		}

		List<Set<String>> word_pair_list = new ArrayList<>(word_pairs);

		/*
		 * 词出现的维基词条集合
		 */

		Map<String, Set<String>> word_wikis = new HashMap<>();

		for (String word : topic) {
//			System.out.println(wiki_dir + word);
			String content = ReadWriteFile.getTextContent(new File(wiki_dir + word));

			String[] lines = content.split("\n");

			Set<String> wikis = new HashSet<>();

			for (String line : lines) {
				wikis.add(line);
			}

			word_wikis.put(word, wikis);

		}

		int length = word_pair_list.size();

		int[] count_1 = new int[length];

		int[] count_2 = new int[length];

		int[] count = new int[length];

		for (int index = 0; index < length; index++) {

			Set<String> pair = word_pair_list.get(index);

			List<String> two_words = new ArrayList<>(pair);

			String word_1 = two_words.get(0);

			String word_2 = two_words.get(1);

			Set<String> wikis_1 = word_wikis.get(word_1);

			Set<String> wikis_2 = word_wikis.get(word_2);

			count_1[index] = wikis_1.size();

			count_2[index] = wikis_2.size();

			for (String wiki : wikis_1) {
				if (wikis_2.contains(wiki))
					count[index]++;
			}

		}

		double[] pmi = new double[length];

		for (int index = 0; index < length; index++) {

			double p_i = (double) count_1[index] / wiki_docs;

			double p_j = (double) count_2[index] / wiki_docs;

			double p_i_j = (double) count[index] / wiki_docs;
			if(p_i==0||p_j==0||p_i_j==0) {
				pmi[index] = 0;
			}
			else {
				pmi[index] = Math.log(p_i_j / (p_i * p_j));
			
			}

		}

		double topic_pmi = 0;

		for (int i = 0; i < length; i++) {
			topic_pmi += pmi[i];
		}
		topic_pmi = 2*topic_pmi/(top*(top-1));
		return topic_pmi;
	}

	
	public static double main(String[] args) throws IOException 
	{
		
		
		PMI.wiki_dir = args[3];
		String topic_file = args[0];

		String content = ReadWriteFile.getTextContent(new File(topic_file));
		System.out.println(content);
		String[] lines = content.split("\n");

		List<String> vocab = Corpus.getVocab(args[1]);

		List<String[]> topics = new ArrayList<>();
		int TOP =  Integer.parseInt(args[2]);
		for (String line : lines) 
		{
			
			String[] words = line.trim().split(" "); //

			String[] topWords = new String[TOP];
			for(int i=0;i<TOP;i++)
			{
				topWords[i] = words[i];
			}
			topics.add(topWords);
			

		}


		StringBuilder sb = new StringBuilder();
		int topic_num = topics.size();

		System.out.println("主题数: " + topic_num);

		double pmi_total = 0;

		for (String[] topic : topics)
		{

			StringBuilder topic_str = new StringBuilder();

			for (String word : topic) 
			{


				topic_str.append(word + "\t");
			}

			double pmi = pmi(topic,TOP);
			
			topic_str.append(pmi);

			sb.append(topic_str.toString().trim() + "\n");

			pmi_total += pmi;
		}

		double average_pmi = pmi_total / topic_num;

		sb.append(average_pmi);

		System.out.println(average_pmi);
		return average_pmi;
		

	}
}
