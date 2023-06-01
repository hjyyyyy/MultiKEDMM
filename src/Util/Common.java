package Util;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.special.Gamma;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import Jama.Matrix;
import jdistlib.math.Bessel;

import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
public class Common {
	
	/**
	 * ��������ĺ�
	 * @param data
	 *            ��������
	 * @return �����
	 */
	public static double sum(double[] data) {
		double sum=0;
		for(int i=0;i<data.length;i++) {
			sum+=data[i];
		}
		return sum;
	}
	
	/**
	 * ��������ľ�ֵ
	 * @param data
	 *            ��������
	 * @return ����ľ�ֵ
	 */
	public static double mean(double[] data) {
		double mean=0;
		mean = sum(data)/data.length;
		return mean;
	}
	
	/**
	 * ��������ķ���
	 * @param data
	 *            ��������
	 * @return ����ķ���
	 */
	public static double Sample_Variance(double[] data) 
	 {
        double variance = 0;
        for (int i = 0; i < data.length; i++) {
            variance = variance + (Math.pow((data[i] - mean(data)), 2));
        }
        variance = variance / data.length;
        return variance;
	 }

	 /**
		 * ��������ı�׼��
		 * @param data
		 *            ��������
		 * @return ����ı�׼��
		 */
	public static double Sample_STD_dev(double[] data) 
    {
        double std_dev;
        std_dev = Math.sqrt(Sample_Variance(data));
        return std_dev;
    }
	
	
	/**
	 * �������������Ԫ�ص��±�
	 * 
	 * @param array
	 *            ��������
	 * @return ���Ԫ�ص��±�
	 */
	public static int maxIndex(double[] array) {
		double max = array[0];
		int maxIndex = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}

		}
		return maxIndex;

	}

	/**
	 * ������������СԪ�ص��±�
	 * 
	 * @param array
	 *            ��������
	 * @return ��СԪ�ص��±�
	 */
	public static int minIndex(double[] array) {
		double min = array[0];
		int minIndex = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}

		}
		return minIndex;

	}

	/**
	 * ���������е���Сֵ
	 * 
	 * @param array
	 *            ��������
	 * @return
	 */
	public static double min(double[] array) {

		double min = array[0];

		for (int i = 0; i < array.length; i++) {
			if (array[i] < min)
				min = array[i];
		}

		return min;

	}

	/**
	 * ���ƾ���
	 * 
	 * @param array
	 *            ����
	 * @return
	 */
	public static double[][] makeCopy(double[][] array) {

		double[][] copy = new double[array.length][];

		for (int i = 0; i < copy.length; i++) {

			copy[i] = new double[array[i].length];

			for (int j = 0; j < copy[i].length; j++) {
				copy[i][j] = array[i][j];
			}
		}

		return copy;
	}

	/**
	 * 
	 * ��Ԫt�ֲ��ĸ����ܶȺ���
	 * 
	 * @param vector
	 * @param freedom
	 * @param mean
	 * @param sigma
	 * @return
	 */
	public static double MultivariateTdistribution(double[] vector, double freedom, double[] mean, double[][] sigma) {

		double dimension = vector.length;

		Matrix s = new Matrix(sigma);

		Matrix m = new Matrix(mean, 1);

		Matrix v = new Matrix(vector, 1);

		Matrix c = v.minus(m);

		Matrix inverse = s.inverse();

		Matrix temp = c.times(inverse).times(c.transpose());

		double det = s.det();

		double prob = Gamma.gamma((freedom + dimension) / 2)
				/ (Gamma.gamma(freedom / 2) * Math.pow(freedom, dimension / 2) * Math.pow(Math.PI, dimension / 2))
				* Math.pow(det, -0.5) * Math.pow(1 + temp.det() / freedom, -(dimension + freedom) / 2);

		return prob;

	}

	/**
	 * This function computes the lower triangular cholesky decomposition L' of
	 * matrix A' from L (the cholesky decomp of A) where A' = A - x*x^T. Based
	 * on the pseudocode in the wiki page
	 * https://en.wikipedia.org/wiki/Cholesky_decomposition#Rank-one_update
	 */
	public static void cholRank1Downdate(DenseMatrix64F L, DenseMatrix64F x, int dimension) {
		// L should be a square lower triangular matrix (although not checking
		// for triangularity here explicitly)
		// Data.D = 2;
		assert L.numCols == dimension;
		assert L.numRows == dimension;
		// x should be a vector
		assert x.numCols == 1;
		assert x.numRows == dimension;

		for (int k = 0; k < dimension; k++) {
			double r = Math.sqrt(L.get(k, k) * L.get(k, k) - x.get(k, 0) * x.get(k, 0));
			double c = r / (double) L.get(k, k);
			double s = x.get(k, 0) / L.get(k, k);
			L.set(k, k, r);
			for (int l = k + 1; l < dimension; l++) {
				double val = (L.get(l, k) - s * x.get(l, 0)) / (double) c;
				L.set(l, k, val);
				val = c * x.get(l, 0) - s * L.get(l, k);
				x.set(l, 0, val);
			}
		}
	}

	/**
	 * This function computes the lower triangular cholesky decomposition L' of
	 * matrix A' from L (the cholesky decomp of A) where A' = A + x*x^T. Based
	 * on the pseudocode in the wiki page
	 * https://en.wikipedia.org/wiki/Cholesky_decomposition#Rank-one_update
	 */
	public static void cholRank1Update(DenseMatrix64F L, DenseMatrix64F x, int dimension) {
		// L should be a square lower triangular matrix (although not checking
		// for triangularity here explicitly)
		// Data.D = 2;
		assert L.numCols == dimension;
		assert L.numRows == dimension;
		// x should be a vector
		assert x.numCols == 1;
		assert x.numRows == dimension;

		for (int k = 0; k < dimension; k++) {
			double r = Math.sqrt(Math.pow(L.get(k, k), 2) + Math.pow(x.get(k, 0), 2));
			double c = r / (double) L.get(k, k);
			double s = x.get(k, 0) / L.get(k, k);
			L.set(k, k, r);
			for (int l = k + 1; l < dimension; l++) {
				double val = (L.get(l, k) + s * x.get(l, 0)) / (double) c;
				L.set(l, k, val);
				val = c * x.get(l, 0) - s * val;
				x.set(l, 0, val);
			}
		}
	}

	/**
	 * mean of the data
	 * 
	 * @param data
	 * @param dimension
	 * @return
	 */
	public static DenseMatrix64F getSampleMean(DenseMatrix64F[] data, int dimension) {
		DenseMatrix64F mean = new DenseMatrix64F(dimension, 1);
		// initialized to 0

		for (DenseMatrix64F vec : data)
			CommonOps.addEquals(mean, vec);

		CommonOps.divide(data.length, mean);

		return mean;
	}

	/**
	 * binSearchArrayList
	 * 
	 * @param cumProb
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static int binSearchArrayList(List<Double> cumProb, double key, int start, int end) {
		if (start > end)
			return start;

		int mid = (start + end) / 2;
		if (key == cumProb.get(mid))
			return mid + 1;
		if (key < cumProb.get(mid))
			return binSearchArrayList(cumProb, key, start, mid - 1);
		if (key > cumProb.get(mid))
			return binSearchArrayList(cumProb, key, mid + 1, end);
		return -1;
	}

	/**
	 * C function
	 * 
	 * @param kappa_k
	 * @param M
	 * @return
	 */
	public static double C(double kappa_k, int M) {

		double result = Math.pow(kappa_k, (double) M / 2 - 1)
				/ (Math.pow(2 * Math.PI, (double) M / 2) * Bessel.i(kappa_k, (double) M / 2 - 1, false));

		return result;
	}

	/**
	 * l2 norm of a vector
	 * 
	 * @param vector
	 * @return
	 */
	public static double l2norm(double[] vector) {

		double norm = 0;

		for (int i = 0; i < vector.length; i++) {
			norm += vector[i] * vector[i];
		}

		norm = Math.sqrt(norm);

		return norm;
	}

	/**
	 * normalize a vector
	 * 
	 * @param vector
	 * @return
	 */
	public static double[] l2normalize(double[] vector) {

		double norm = 0;

		for (int i = 0; i < vector.length; i++) {
			norm += vector[i] * vector[i];
		}

		norm = Math.sqrt(norm);

		double[] result = new double[vector.length];

		for (int i = 0; i < vector.length; i++) {
			result[i] = vector[i] / norm;
		}

		return result;
	}

	/**
	 * ���������ӣ������Ľ��
	 * 
	 * @param x_1
	 * @param x_2
	 * @param flag
	 * @return
	 */
	public static double[] vector_add(double[] x_1, double[] x_2, int flag) {

		double[] result = new double[x_1.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = x_1[i] + flag * x_2[i];
		}

		return result;

	}

	/**
	 * �����log-Normal�ֲ�����һ����
	 * 
	 * @param mean
	 * @param shape
	 * @return
	 */
	public static double random_kappa_log_normal(double mean, double shape) {

		LogNormalDistribution lnd = new LogNormalDistribution(mean, shape);

		double y = lnd.sample();

		return y;
	}

	/**
	 * �����log-Normal�ֲ�����һ����
	 * 
	 * @param mean
	 * @param shape
	 * @param size
	 * @return
	 */
	public static double[] randoms_kappa_log_normal(double mean, double shape, int size) {

		LogNormalDistribution lnd = new LogNormalDistribution(mean, shape);

		double z[] = lnd.sample(size);

		return z;
	}

	/**
	 * ����log-norm��x��ĸ���ֵ
	 * 
	 * @param mean
	 * @param shape
	 * @param x
	 * @return
	 */
	public static double probability(double mean, double shape, double x) {

		LogNormalDistribution lnd = new LogNormalDistribution(mean, shape);

		return lnd.density(x);

	}

	/**
	 * ������������
	 * 
	 * @param vector
	 * @param factor
	 * @return
	 */
	public static double[] vector_times(double[] vector, double factor) {

		double[] result = new double[vector.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = factor * vector[i];
		}

		return result;

	}
	
	/**
     * Sample a value from a double array
     * 
     * @param probs
     * @return
     */
    public static int nextDiscrete(double[] probs)
    {
        double sum = 0.0;
        for (int i = 0; i < probs.length; i++)
            sum += probs[i];
        Random rg = new Random();
        double r = rg.nextDouble() * sum;

        sum = 0.0;
        for (int i = 0; i < probs.length; i++) {
            sum += probs[i];
            if (sum > r)
                return i;
        }
        return probs.length - 1;
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
