package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReadWriteFile {

	/**
	 * ��ָ������д��ָ���ļ�
	 * 
	 * @param file
	 * @param content
	 * @throws IOException
	 */
	public static void writeFile(String file, String content) throws IOException {

		File result = new File(file);
		OutputStream out = new FileOutputStream(result, false);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));

		bw.write(content);

		bw.close();
		out.close();
		
	}

	/**
	 * 
	 * ��ȡ�ļ�����
	 * 
	 * @param f
	 *            �ļ�
	 * @return
	 * @throws IOException
	 */
	public static String getTextContent(File f) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

		String line = "";

		StringBuilder content = new StringBuilder();

		while ((line = reader.readLine()) != null) {

			content.append(line + "\n");
		}
		reader.close();

		return content.toString();
	}

	/**
	 * ���ʱ�д���ļ�
	 * 
	 * @param vocab
	 * @param filename
	 * @throws IOException
	 */
	public static void writeList(List<String> vocab, String filename) throws IOException {

		StringBuilder sb = new StringBuilder();

		for (String word : vocab) {

			sb.append(word + "\n");
		}

		writeFile(filename, sb.toString());

	}

	/**
	 * ��ȡ�ʼ��ϣ�һ��һ����
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getWordSet(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		Set<String> word_set = new HashSet<>();

		while ((line = reader.readLine()) != null) {

			word_set.add(line);
		}
		reader.close();

		return word_set;
	}

}
