package Util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Util.Corpus;
import Util.ReadWriteFile;

public class WordWikiIndex {

	public static void main(String[] args) throws IOException {
		String[] corpusList = new String[] {"snippet","GoogleNews","StackOverflow","Tweet","Biomedical","PascalFlickr"};
		for(String corpus_name : corpusList) 
		{
			
			String project_dir = "D:\\Code\\java\\MultiKEDMM";
			String data_root = project_dir+"\\data\\shortTextCorpus\\";
			String corpus_root = data_root+corpus_name;
			String corpus_vocab = corpus_root+"\\"+corpus_name+"_vocab.txt";
			List<String> vocab = Corpus.getVocab(corpus_vocab);

			File[] files = new File("D:\\Code\\java\\MultiKEDMM\\data\\wiki_full\\").listFiles();

			
			Map<String, Set<String>> word_wikis = new HashMap<>();
			

			for (String word : vocab) {
				word_wikis.put(word, new HashSet<String>());
			}

			
			int count = 0;

			for (File file : files) {

				if (count >=  300000) // 300000
					break;

				String content = ReadWriteFile.getTextContent(file);

				for (String word : vocab) {

					if (content.contains(word)) {

						Set<String> wikis = word_wikis.get(word);
						wikis.add(file.getName());

					}
				}
				System.out.println(count);
				count++;
			}

				
			for(int i=0; i<vocab.size(); i=i+1) {
				StringBuilder word_sb = new StringBuilder();
				Set<String> wikis = word_wikis.get(vocab.get(i));
				for (String wiki : wikis) {
					word_sb.append(wiki + "\n");
				}
				String str = word_sb.toString();
				System.out.println(vocab.get(i));
				System.out.println(str);
				ReadWriteFile.writeFile(corpus_root+"\\word_wiki\\" + i, str);
			}
			System.out.println(corpus_name);
			
		}
		
	}
}
