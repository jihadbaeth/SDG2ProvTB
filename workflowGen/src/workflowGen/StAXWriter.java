package workflowGen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class StAXWriter {

	public static void main(String []args)
	{
	    XMLInputFactory inFactory = XMLInputFactory.newInstance();
	    XMLEventReader eventReader;
		try {
			eventReader = inFactory.createXMLEventReader(new FileInputStream("/home/jihad/Desktop/DAX/100K/dax_1_100K_.dax"));
		    XMLOutputFactory factory = XMLOutputFactory.newInstance();
		    XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter("/home/jihad/Desktop/DAX/100K/dax_1_100K_.dax"));
		    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		    while (eventReader.hasNext()) {
		        XMLEvent event = eventReader.nextEvent();
		        writer.add(event);
		        if (event.getEventType() == XMLEvent.START_ELEMENT) {
//		        	System.out.println(event.asStartElement().getName().toString());
		            if (event.asStartElement().getName().toString().equalsIgnoreCase("{http://pegasus.isi.edu/schema/DAX}job")) {
		            	writer.add(eventFactory.createAttribute("runtime", "13.85"));
//		                writer.add(eventFactory.createStartElement("", null, "index"));
//		                writer.add(eventFactory.createEndElement("", null, "index"));
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

	}

}
