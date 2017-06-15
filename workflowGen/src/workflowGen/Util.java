package workflowGen;

import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Util {

	public static Random random = new Random();

	public static void main(String[] args) {
		double[] values =createNDDataset(1);
		double [] probabilityTable=probabilityTable(normalize(values));

		System.out.println("Start...");
		int tossCount = 100;
		int credibleCount = 0;
		int notCredibletailsCount = 0;
		System.out.println("Starting...");

		for (int i=0; i< tossCount; i++) {
			//System.out.println(probabilityTable[(int) chooseWithChance(probabilityTable)]);
			System.out.println(chooseWithChance(probabilityTable));
		    if (chooseWithChance(probabilityTable) > (probabilityTable.length)/2)
		    {
		    	credibleCount++;
		    }
		    else
		    {
		    	System.out.println(chooseWithChance(probabilityTable));
		    	notCredibletailsCount++;
		    }
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
		if(listOfAffected.length>index)
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
//				System.out.println("Affectd: "+temp2);
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
		for (int i = 0; i < dataSet.length; i++) {
			// System.out.println(normalized[i]);
			sum = sum + dataSet[i];

		}
		double probSum = 0;
		for (int i = 0; i < dataSet.length; i++) {
			double probability = (dataSet[i]) / sum;
//			System.out.println(probability);
			probabilityTable[i]=probability;
			probSum = probSum + probabilityTable[i];
		}
//		 System.out.println("sum equals: "+ probSum);
		// normalize(values).toString();
		 Arrays.sort(probabilityTable);

		 return probabilityTable;
	}
	public static double []createNDDataset(int numberOfInstances)
	{
		double[] values = new double[numberOfInstances];
		for (int i = 0; i < numberOfInstances; i++) {
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

	public static void addMissingAtt(String path) {

	    XMLInputFactory inFactory = XMLInputFactory.newInstance();
	    XMLEventReader eventReader;
		try {
			eventReader = inFactory.createXMLEventReader(new FileInputStream(path));
		    XMLOutputFactory factory = XMLOutputFactory.newInstance();
		    XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(path+".dax"));
		    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		    while (eventReader.hasNext()) {
		        XMLEvent event = eventReader.nextEvent();
		        writer.add(event);
		        if (event.getEventType() == XMLEvent.START_ELEMENT) {
//		        	System.out.println(event.asStartElement().getName().toString());
		            if (event.asStartElement().getName().toString().equalsIgnoreCase("{http://pegasus.isi.edu/schema/DAX}job")) {
		            	writer.add(eventFactory.createAttribute("runtime", "13.85"));

		            }
		            if (event.asStartElement().getName().toString().equalsIgnoreCase("{http://pegasus.isi.edu/schema/DAX}uses")) {
		            	writer.add(eventFactory.createAttribute("size", "4222080"));

		            }
		        }
		    }
		    writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


//		try {
//			File inputFile = new File(path);
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(inputFile);
//			doc.getDocumentElement().normalize();
////			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//			NodeList nList = doc.getElementsByTagName("job");
////			System.out.println("----------------------------");
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//				Node nNode = nList.item(temp);
////				System.out.println("\nCurrent Element :" + nNode.getNodeName());
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//					Element eElement = (Element) nNode;
//					eElement.setAttribute("runtime", "13.85");
//					NodeList nodeList = nNode.getChildNodes();
//					for (int j = 0; j < nodeList.getLength(); j++) {
//						Node childNode = nodeList.item(j);
//						if (childNode.getNodeType() == Node.ELEMENT_NODE) {
//
//							//Element element = (Element) childNode;
//
//							if (childNode.getNodeType() == Node.ELEMENT_NODE) {
//								if (childNode.getNodeName().equals("metadata")) {
////									System.out.println("Timestamp:" + " " + childNode.getTextContent());
//								}
//								if (childNode.getNodeName().equals("uses")) {
////									System.out.print(element.getAttribute("name") + " ");
////									System.out.println(element.getAttribute("link"));
//									((Element)childNode).setAttribute("size","4222080");
//								}
//							}
//						}
//					}
//
//				}
//
//		        DOMSource source = new DOMSource(doc);
//
//		        StreamResult result = new StreamResult(new File(path));
//		        Transformer transformer = TransformerFactory.newInstance().newTransformer();
//		        transformer.transform(source, result);
//
//			}
//			System.out.println("All attributes are added...");
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//

	}
}
