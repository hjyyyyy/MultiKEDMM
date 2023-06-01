package Evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import Util.BuildInstances;
import Util.Common;
import Util.Corpus;
import Util.Document;
import Util.ReadWriteFile;
import Util.Classifiers;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
public class Accuracy {
	
	public static double main(String[] args) throws Exception {

		String corpus_label = args[0];
		String pdz_file = args[1];
		String flag = args[2];
		int K = Integer.parseInt(args[3]);
		Map<String, double[]> vector_map = new HashMap<>();
		
		Map<String, String> train_or_test = new HashMap<>();

		Map<String, String> doc_label = new HashMap<>();
	
		// ∂¡±Í«©£¨—µ¡∑ªÚ≤‚ ‘
		File file = new File(corpus_label);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

		String line = "";

		int count = 0;

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split("\t");
			train_or_test.put(count + "", temp[1]);

			doc_label.put(count + "", temp[2]);

			count++;

		}

		reader.close();
		

		//  ∂¡Ãÿ’˜œÚ¡ø
		file = new File(pdz_file);
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		count = 0;

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split(" ");

			double[] vector = new double[temp.length];

			for (int i = 0; i < vector.length; i++) {

				vector[i] = Double.parseDouble(temp[i]);

			}

			vector_map.put(count + "", vector);

			count++;

		}
		reader.close();

		String training_data = BuildInstances.getTrainingSet(doc_label, train_or_test, vector_map, K);

		String test_data = BuildInstances.getTestSet(doc_label, train_or_test, vector_map, K);

		ReadWriteFile.writeFile(flag+"train_lda.arff", training_data);

		ReadWriteFile.writeFile(flag+"test_lda.arff", test_data);
		

		// ∂¡—µ¡∑ºØ
		file = new File(flag+"train_lda.arff");

		ArffLoader loader = new ArffLoader();
		loader.setFile(file);

		Instances train = loader.getDataSet();
		train.setClassIndex(train.numAttributes() - 1);


		// ∂¡≤‚ ‘ºØ
		file = new File(flag+"test_lda.arff");

		loader = new ArffLoader();
		loader.setFile(file);

		Instances test = loader.getDataSet();
		test.setClassIndex(train.numAttributes() - 1);

		// —µ¡∑
		Classifier classifier = Classifiers.SVM_Linear(train);
		int num_instances = test.numInstances();

		count = 0;

		for (int j = 0; j < num_instances; j++) {

			Instance test_instance = test.instance(j);

			int real_label = (int) test_instance.classValue();

			double class_value = classifier.classifyInstance(test_instance);

			int predict_result = (int) class_value;

			if (predict_result == real_label)
				count++;

		}

		double accuracy = (double) count / num_instances;
		return accuracy;
		
	}
	
	

}
