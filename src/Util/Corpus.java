package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Liang Yao
 * @email yaoliang@zju.edu.cn
 * 
 */
public class Corpus {

	/**
	 * 
	 * ��ȡ�ʱ�һ��һ����
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static List<String> getVocab(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		List<String> vocab = new ArrayList<String>();


		while((line=reader.readLine())!=null) {
			line = line.trim();
			vocab.add(line);
		}
		reader.close();
		return vocab;
	}

	/**
	 * ��ȡ�ĵ�����һ��һƪ����
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static int[][] getDocuments(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		List<String> docsLines = new ArrayList<String>();

		while ((line = reader.readLine()) != null) {
			if (line.trim().length() > 0)
				docsLines.add(line);
		}
		reader.close();

		int[][] docs = new int[docsLines.size()][];

		for (int d = 0; d < docs.length; d++) {

			String doc = docsLines.get(d);
			String[] tokens = doc.trim().split(" ");

			docs[d] = new int[tokens.length];

			for (int n = 0; n < tokens.length; n++) {
				int wordid = Integer.parseInt(tokens[n]);
				docs[d][n] = wordid;
			}

		}

		return docs;
	}

	/**
	 * ��ȡÿ���ĵ���ʵ������
	 * 
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static String[][] getEntities(File[] files) throws IOException {

		
		String[][] entities = new String[files.length][];
		for (File file : files) {

			int doc_no = Integer.parseInt(file.getName());

			String content = ReadWriteFile.getTextContent(file);

			String[] lines = content.split("\n");

			
			entities[doc_no] = new String[lines.length];

			System.out.println(entities[doc_no].length);

			System.out.println(file);

			for (int i = 0; i < entities[doc_no].length; i++) {


				entities[doc_no][i] = lines[i];

			}

		}

		return entities;
		

	}

	/**
	 * ����ĳ���ʵ��ĵ�����
	 * 
	 * @param documents
	 * @param word
	 * @return
	 */
	public static int DocumentFrequency(ArrayList<int[]>  documents, int word) {
		int count = 0;
		for (int i = 0; i < documents.size(); i++) {
			int[] termIDArray = documents.get(i);
			for (int j = 0; j < termIDArray.length; j++) {
				if (termIDArray[j] == word) {
					count++;
					break;
				}

			}
		}
		return count;
	}

	/**
	 * �����ʹ������ĵ�����
	 * 
	 * @param documents
	 * @param word_i
	 * @param word_j
	 * @return
	 */
	public static int DocumentFrequency(ArrayList<int[]>  documents, int word_i, int word_j) {
		int count = 0;
		for (int i = 0; i < documents.size(); i++) {

			boolean exsit_i = false;
			boolean exsit_j = false;
			int[] termIDArray = documents.get(i);
			for (int j = 0; j < termIDArray.length; j++) {
				if (termIDArray[j] == word_i) {
					exsit_i = true;
					break;
				}
			}

			for (int j = 0; j < termIDArray.length; j++) {
				if (termIDArray[j] == word_j) {
					exsit_j = true;
					break;
				}
			}
			if (exsit_i && exsit_j)
				count++;

		}
		return count;
	}

	/**
	 * ���������ʵĵ㻥��ϢPMI
	 * 
	 * @param documents
	 *            ���Ͽ�
	 * @param word_i
	 * @param word_j
	 * @return
	 */
	public static double PMI(ArrayList<int[]> documents, int word_i, int word_j) {

		int count_i = DocumentFrequency(documents, word_i);

		int count_j = DocumentFrequency(documents, word_j);

		int co_occur = DocumentFrequency(documents, word_i, word_j);

		double p_i = (double) count_i / documents.size();

		double p_j = (double) count_j / documents.size();

		double p_i_j = (double) co_occur / documents.size();

		double pmi = Math.log(p_i_j / (p_i * p_j));

		return pmi;
	}

	/**
	 * ���������һ���� (EMNLP'11)
	 * 
	 * @param top_words
	 *            �����top words
	 * @param documents
	 *            �ĵ���
	 * @return ����һ����
	 */
	public static double coherence(int[] top_words, ArrayList<int[]>  documents) {

		double coherence_score = 0.0;
		for (int m = 1; m < top_words.length; m++) {
			for (int l = 0; l < m; l++) {

				if (top_words[m] != top_words[l])
					coherence_score += Math.log((double) (DocumentFrequency(documents, top_words[m], top_words[l]) + 1)
							/ DocumentFrequency(documents, top_words[l]));
				else
					coherence_score += Math.log((double) 2 / DocumentFrequency(documents, top_words[l]));

			}
		}
		return coherence_score;
	}

	/**
	 * K�������ƽ������һ����
	 * 
	 * @param docs
	 * @param phi
	 * @param top_words_size
	 * @return
	 */
	public static double average_coherence(ArrayList<int[]> docs, double[][] phi, int top_words_size) {

		double total_coherence = 0;

		for (double[] phi_t : phi) {

			int[] top_words = new int[top_words_size];

			for (int i = 0; i < top_words_size; i++) {

				int max_index = Common.maxIndex(phi_t);
				top_words[i] = max_index;
				phi_t[max_index] = 0;

			}

			double coherence = coherence(top_words, docs);
			total_coherence += coherence;
		}
		double average_coherence = total_coherence / phi.length;
		return average_coherence;
	}

	/**
	 * LDA����ģ�͵�Training perplexity
	 * 
	 * @param theta
	 *            �ĵ�-����ֲ�
	 * @param phi
	 *            ����-�ʷֲ�
	 * @param docs
	 *            �ĵ���
	 * @return
	 */
	public static double perplexity(double[][] theta, double[][] phi, int[][] docs) {
		double perplexity = 0.0;

		int total_length = 0;
		for (int i = 0; i < docs.length; i++) {
			total_length += docs[i].length;
		}

		for (int i = 0; i < docs.length; i++) {

			for (int j = 0; j < docs[i].length; j++) {

				double prob = 0.0;
				for (int k = 0; k < phi.length; k++) {
					prob += theta[i][k] * phi[k][docs[i][j]];
				}

				perplexity += Math.log(prob);

			}
		}

		perplexity = Math.exp(-1 * perplexity / total_length);

		return perplexity;
	}

	/**
	 * ��ȡһ�������ĵ����е�IDF
	 * 
	 * @param documents
	 * @param word
	 * @return
	 */
	public static double IDF(int[][] documents, int word) {
		int count = 0;

		for (int[] document : documents) {

			for (int e : document) {
				if (e == word) {
					count++;
					break;
				}

			}
		}
		return Math.log((double) documents.length / count);
	}

	/**
	 * ͳ�ƴ����ĵ��еĴ�Ƶ
	 * 
	 * @param documents
	 * @param word
	 * @return
	 */
	public static int TF(int[] document, int word) {
		int count = 0;
		for (int e : document) {
			if (e == word)
				count++;
		}
		return count;
	}

}
