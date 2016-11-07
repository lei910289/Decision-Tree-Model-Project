import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class entropy {
	public static double getEntropy(ArrayList<ArrayList<String>> dataSet) {
		int size = dataSet.get(0).size();
		HashMap<String, Integer> hash = getUniqueResult(dataSet, size - 1);
		ArrayList<String> resultList = getKey(dataSet, size - 1);
		double sum = 0;
		for (String s : resultList) {
			int num = hash.get(s);
			double pro = (double)num / dataSet.size();
			sum -= pro * ((double)Math.log(pro) / (double)Math.log(2));
		}
		return sum;
	}

	public static double getGain(ArrayList<ArrayList<String>> dataSet, int attributeIndex) {
		ArrayList<String> attributeValue = getKey(dataSet, attributeIndex);
		HashMap<String, Integer> hash = getUniqueResult(dataSet, attributeIndex);
		int size = dataSet.size();
		double splitEntropy = 0;
		for (String value : attributeValue) {
			ArrayList<ArrayList<String>> splitDataSet = getSplitDataSet(dataSet, attributeIndex, value);
			double entropy = getEntropy(splitDataSet);
			double pro = (double)hash.get(value) / (double)size;
			splitEntropy += pro * entropy;
		}
		double gain = getEntropy(dataSet) - splitEntropy;
		return gain;
	}
	
	
	
	public static ArrayList<ArrayList<String>> getSplitDataSet(ArrayList<ArrayList<String>> dataSet, int attributeIndex, String value) {
		ArrayList<ArrayList<String>> tmpDataSet = new ArrayList<>();
		for (ArrayList<String> tmp : dataSet) {
			if (tmp.get(attributeIndex).equals(value)) {
				tmpDataSet.add(tmp);
			}
		}
		ArrayList<ArrayList<String>> splitDataSet = new ArrayList<>();
		for (ArrayList<String> tmp : tmpDataSet) {
			ArrayList<String> tmpResult = new ArrayList<>();
			int size = tmp.size();
			for (int i = 0; i < size; i++) {
				if (i == attributeIndex) {
					continue;
				}
				tmpResult.add(tmp.get(i));
			}
			splitDataSet.add(tmpResult);
		}
		return splitDataSet;
	}


	// return a map from result type to its type
	public static HashMap<String, Integer> getUniqueResult(ArrayList<ArrayList<String>> dataSet, int attributeIndex) {
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		for(ArrayList<String> tmp : dataSet) {
			String keyValue = tmp.get(attributeIndex);
			if (hash.containsKey(keyValue)) {
				hash.put(keyValue, hash.get(keyValue) + 1);
			} else {
				hash.put(keyValue, 1);
			}
		}
		return hash;
	}

	// return an arrayList of result type
	public static ArrayList<String> getKey(ArrayList<ArrayList<String>> dataSet, int attributeIndex) {
		ArrayList<String> resultList = new ArrayList<String>();
		for (ArrayList<String> tmp : dataSet) {
			if (resultList.contains(tmp.get(attributeIndex))) {
				continue;
			} else {
				resultList.add(tmp.get(attributeIndex));
			}
		}
		return resultList;
	}
	
	public static ArrayList<String> getSplitAttributeName(ArrayList<String> attributeName, int attributeIndex) {
		ArrayList<String> splitAttributeName = new ArrayList<>();
		for (int i = 0; i < attributeName.size(); i++) {
			if (i == attributeIndex) {
				continue;
			}
			splitAttributeName.add(attributeName.get(i));
		}
		return splitAttributeName;
	}
	

}
