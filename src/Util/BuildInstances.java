package Util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BuildInstances {

	/**
	 * ��������label��ѵ��\���� �������鹹��ѵ����
	 * 
	 * @param doc_label
	 * @param doc_train_test
	 * @param vector_map
	 * @param dimension
	 * @return
	 */
	public static String getTrainingSet(Map<String, String> doc_label, Map<String, String> doc_train_test,
		Map<String, double[]> vector_map, int dimension) {

		
		StringBuilder sb = new StringBuilder("@relation topic\n");

		Set<String> label_set = new HashSet<>();

		for (String doc : doc_label.keySet()) {

			label_set.add(doc_label.get(doc));

		}

		StringBuilder labels = new StringBuilder();

		for (String label : label_set) {

			labels.append(label + ",");

		}

		for (int i = 0; i < dimension; i++)
			sb.append("@attribute feature_" + i + " numeric\n");
		sb.append("@attribute 'class' {" + labels.toString().substring(0, labels.toString().length() - 1)
				+ "}\n\n@data\n");

		for (String doc : vector_map.keySet()) {

			double[] vector = vector_map.get(doc);

			if (doc_train_test.get(doc).contains("train")) {

				for (double e : vector) {

					sb.append(e + ",");

				}
				sb.append(doc_label.get(doc) + "\n");

			}

		}

		return sb.toString();
	}

	/**
	 * ��������label��ѵ��\���� �������鹹����Լ�
	 * 
	 * @param doc_label
	 * @param doc_train_test
	 * @param vector_map
	 * @param dimension
	 * @return
	 */
	public static String getTestSet(Map<String, String> doc_label, Map<String, String> doc_train_test,
			Map<String, double[]> vector_map, int dimension) {

		StringBuilder sb = new StringBuilder("@relation topic\n");

		Set<String> label_set = new HashSet<>();

		for (String doc : doc_label.keySet()) {

			label_set.add(doc_label.get(doc));

		}

		StringBuilder labels = new StringBuilder();

		for (String label : label_set) {

			labels.append(label + ",");

		}

		for (int i = 0; i < dimension; i++)
			sb.append("@attribute feature_" + i + " numeric\n");
		sb.append("@attribute 'class' {" + labels.toString().substring(0, labels.toString().length() - 1)
				+ "}\n\n@data\n");

		for (String doc : vector_map.keySet()) {

			double[] vector = vector_map.get(doc);

			if (doc_train_test.get(doc).contains("test")) {

				for (double e : vector) {

					sb.append(e + ",");

				}
				sb.append(doc_label.get(doc) + "\n");

			}

		}

		return sb.toString();
	}

	
}
