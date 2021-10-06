

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CopyXMLFile {

    public static void main(String[] args) {

        int copies = Integer.parseInt(args[0]);
        int SeqNr = Integer.parseInt(args[1]);
        String destinationFolder = args[2];
        File baseXMLFilePath = new File(args[3]);
        String filename = args[4];
        File destXMLFilePath;

        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(baseXMLFilePath.getPath());

            Node inv = doc.getFirstChild();
            Node invSum = doc.getElementsByTagName("INVOICENUM").item(0);
            for (int i = 1; i <= copies; i++){
                invSum.setTextContent(String.valueOf(SeqNr));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(baseXMLFilePath);
                transformer.transform(source,result);
                destXMLFilePath = new File(destinationFolder+"\\"+filename+"_"+SeqNr+".xml");
                copyFiles(baseXMLFilePath, destXMLFilePath);
                SeqNr++;

            }
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException pce) {
            pce.printStackTrace();
        }


    }

    private static void copyFiles(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
