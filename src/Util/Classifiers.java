package Util;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class Classifiers {

	/**
	 * ѵ��Logistic Regression ������
	 * 
	 * @param train
	 *            ѵ����
	 * @return ѵ���õķ�����
	 * 
	 * @throws Exception
	 */
	public static Classifier logistic_regression(Instances train) throws Exception {

		System.out.println("MaxEnt Training.......");

		Logistic logistic = new Logistic();

		// logistic.setRidge(0.8);

		logistic.buildClassifier(train);

		System.out.println("MaxEnt Training End.......");

		return logistic;

	}

	/**
	 * ѵ��SVM������
	 * 
	 * @param train
	 *            ѵ����
	 * @return ѵ���õķ�����
	 * @throws Exception
	 */
	public static Classifier SVM(Instances train) throws Exception {

		System.out.println("SVM Training.......");

		LibSVM libsvm = new LibSVM();

		libsvm.setCost(100);

		libsvm.buildClassifier(train);

		System.out.println("SVM Training End.......");

		return libsvm;

	}

	/**
	 * ѵ��SVM������, ���Ժ�
	 * 
	 * @param train
	 *            ѵ����
	 * @return ѵ���õķ�����
	 * @throws Exception
	 */
	public static Classifier SVM_Linear(Instances train) throws Exception {

		System.out.println("Linear SVM Training.......");

		LibLINEAR liblinear = new LibLINEAR();

		liblinear.buildClassifier(train);

		System.out.println("Linear SVM Training End.......");

		return liblinear;

	}

	/**
	 * ѵ�����ɭ�ַ�����
	 * 
	 * @param train
	 *            ѵ����
	 * @return ѵ���õķ�����
	 * @throws Exception
	 */
	public static Classifier random_forest(Instances train) throws Exception {

		System.out.println("RandomForest Training.......");

		RandomForest forest = new RandomForest();

		forest.setNumTrees(100);

		// forest.setMaxDepth(10);

		forest.buildClassifier(train);

		System.out.println("RandomForest Training End.......");

		return forest;

	}

	/**
	 * ѵ�����ر�Ҷ˹������
	 * 
	 * @param train
	 *            ѵ����
	 * @return ѵ���õķ�����
	 * @throws Exception
	 */
	public static Classifier naive_bayes(Instances train) throws Exception {

		System.out.println("Naive Bayes Training.......");

		NaiveBayes bayes = new NaiveBayes();

		bayes.buildClassifier(train);

		System.out.println("Naive Bayes Training End.......");

		return bayes;

	}

	/**
	 * ѵ��BP�����������
	 * 
	 * @param train
	 *            ѵ����
	 * @return ѵ���õķ�����
	 * @throws Exception
	 */
	public static Classifier BP(Instances train) throws Exception {

		System.out.println("BP Training.......");

		MultilayerPerceptron mlp = new MultilayerPerceptron();

		mlp.buildClassifier(train);

		System.out.println("BP Training End.......");

		return mlp;
	}

	/**
	 * ѵ��C4.5������������
	 * 
	 * @param train
	 *            ѵ����
	 * @return ѵ���õķ�����
	 * @throws Exception
	 */
	public static Classifier decision_tree(Instances train) throws Exception {

		System.out.println("C4.5 Training.......");

		J48 tree = new J48();

		tree.buildClassifier(train);

		System.out.println("C4.5 Training End.......");

		return tree;
	}

	/**
	 * ѵ��Adaboost��Ϸ�����
	 * 
	 * @param train
	 *            ѵ����
	 * @return ѵ���õķ�����
	 * @throws Exception
	 */
	public static Classifier Ada_boost(Instances train) throws Exception {

		System.out.println("Adaboost Training.......");

		AdaBoostM1 boost = new AdaBoostM1();

		boost.buildClassifier(train);

		System.out.println("Adaboost Training End.......");

		return boost;

	}

}
