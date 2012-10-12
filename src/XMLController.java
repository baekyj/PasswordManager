import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLController {
	private String inputFile = "C:/temp/TelewebAccConfig.xml";
	private String outputFile = "C:/temp/TelewebAccConfig.xml";

	public void doChange() throws Exception {
		Document doc = DocumentBuilderFactory.newInstance()
		.newDocumentBuilder().parse(new InputSource(inputFile));
		doc.setXmlStandalone(true);
		// locate the node(s)
		XPath xpath = XPathFactory.newInstance().newXPath();
		//NodeList nodes = (NodeList)xpath.evaluate
		//    ("//configs/config/name[text()='John']", doc, XPathConstants.NODESET);
		
		NodeList nodes = (NodeList)xpath.evaluate
		("//configs/config/hangmokName", doc, XPathConstants.NODESET);
		NodeList nodes2 = (NodeList)xpath.evaluate
		("//configs/config/hangmokValue", doc, XPathConstants.NODESET);
		//System.out.println("length:" + String.valueOf(nodes.getLength()));
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			//System.out.println("hangmokName" + String.valueOf(idx) + ":[" + nodes.item(idx).getTextContent() + "]"); //.getTextContent()
			if (nodes.item(idx).getTextContent().equals("jdbc.username")) {
				nodes2.item(idx).setTextContent("1111111111");
			}
		}
		System.out.println("value changed completed!");
		// make the change
		//for (int idx = 0; idx < nodes.getLength(); idx++) {
		//nodes.item(idx).setTextContent("John Paul");
		//}

		// save the result
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		xformer.transform
		(new DOMSource(doc), new StreamResult(new File(outputFile)));
	}
}