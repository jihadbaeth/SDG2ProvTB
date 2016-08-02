package workflowGen;

import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

public class Util {

	public static Random random = new Random();

	public static void main(String[] args) {
		double[] values =createNDDataset(1000);
		double [] probabilityTable=probabilityTable(normalize(values));

		System.out.println("Start...");
		int tossCount = 1000;
		int credibleCount = 0;
		int notCredibletailsCount = 0;
		for (int i=0; i< tossCount; i++) {
			System.out.println(probabilityTable[(int) chooseWithChance(probabilityTable)]);
		    if (chooseWithChance(probabilityTable) > 500)
		    	credibleCount++;
		    else
		    	notCredibletailsCount++;
		}

		System.out.println("credible"+ credibleCount);
		System.out.println("not credible:"+ notCredibletailsCount);

	}

	public static String[] listOfAffected(Map<String,String> successor, String tweetID)
	{
		String[] listOfAffected = new String[successor.size()];
		int index =0;
		boolean done = false;
		String temp= successor.get(tweetID);
		listOfAffected[index]=temp;

		while(!done)
		{
			String key ="";
			String val ="";
			String temp2= successor.get(temp);

			if(successor.get(temp2)!=null)
			{
				listOfAffected[index]=temp2;
				index++;
				System.out.println("Affectd: "+temp2);
				temp=temp2;

			}
			else{
				done=true;
				break;
			}
		}
		for (String string : listOfAffected) {
			if (string!=null)
			System.out.println("List of Affected "+string);
		}

		return null;

	}

	public static double [] probabilityTable(double [] dataSet)
	{
		double [] probabilityTable=new double[dataSet.length];
		double sum = 0;
		for (int i = 0; i <= 999; i++) {
			// System.out.println(normalized[i]);
			sum = sum + dataSet[i];

		}
		double probSum = 0;
		for (int i = 0; i <= 999; i++) {
			double probability = (dataSet[i]) / sum;
			System.out.println(probability);
			probabilityTable[i]=probability;
			probSum = probSum + probabilityTable[i];
		}
		 System.out.println("sum equals: "+ probSum);
		// normalize(values).toString();
		 Arrays.sort(probabilityTable);

		 return probabilityTable;
	}
	public static double []createNDDataset(int numberOfInstances)
	{
		double[] values = new double[1000];
		for (int i = 0; i <= 999; i++) {
			Random randomno = new Random();
			values[i] = (randomno.nextGaussian() * 25 + 50);
			// System.out.println(values[i]);

		}
		return values;
	}

	public static double[] normalize(double[] val) {
		List<Double> b = Arrays.asList(ArrayUtils.toObject(val));
		double max = Collections.max(b);
		double min = Collections.min(b);
		// System.out.println(Collections.max(b));
		// System.out.println(Collections.min(b));
		double[] normalizedVal = new double[val.length + 1];
		for (int i = 0; i < val.length; i++) {
			normalizedVal[i] = (val[i] - min) / (max - min);

		}
		// normalized = (x-min(x))/(max(x)-min(x))
		return normalizedVal;

	}


	public static double chooseWithChance(double[] args) {
		/*
		 * This method takes number of chances and randomly chooses one of them
		 * considering their chance to be chosen. e.g. chooseWithChance(0,99)
		 * will most probably (%99) return 1 chooseWithChance(99,1) will most
		 * probably (%99) return 0 chooseWithChance(0,100) will always return 1.
		 * chooseWithChance(100,0) will always return 0. chooseWithChance(67,0)
		 * will always return 0.
		 */
		int argCount = args.length;
		double sumOfChances = 0;

		for (int i = 0; i < argCount; i++) {
			sumOfChances += args[i];
		}

		double randomDouble = random.nextDouble() * sumOfChances;

		while (sumOfChances > randomDouble) {
			sumOfChances -= args[argCount - 1];
			argCount--;
		}
		return argCount - 1;
	}
}
