package in.thyferny.a;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import in.thyferny.nlp.MyNLP;

public class XMLReader {

	public static void main(String args[]) {
		Map<String, String> fields = fieldList();
		for(String s :fields.keySet()){
//			<field name="symptom" type="text_mqa" indexed="true" stored="true" omitNorms="true"/>
//			System.out.println("<field name=\""+s+"\" type=\"text_mqa\" indexed=\"true\" stored=\"true\" omitNorms=\"true\"/>");
			System.out.println("<copyField source=\""+s+"\" dest=\"text\"/>");
		}
//		List<NewDiseaseDescription> disease = getDisease();
//		System.out.println(disease);
//		File file =new File("NewDisease.dat");
//        FileOutputStream out;
//        try {
//            out = new FileOutputStream(file);
//            ObjectOutputStream objOut=new ObjectOutputStream(out);
//            objOut.writeObject(disease);
//            objOut.flush();
//            objOut.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
	}

	private static Map<String, String> fieldList() {
		Map<String,String> fields = new HashMap<>();
		Element root = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(File f:new File("C:/Users/thyferny/Desktop/mqa/diseases").listFiles()){
//		File f = new File("C:/Users/thyferny/Desktop/mqa/diseases/Ⅱ型免疫母细胞性淋巴腺病.xml");
		// documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
		try {
			Document dt = db.parse(f);
			root = dt.getDocumentElement();
//			System.out.println("根元素：" + root.getAttribute("name"));
			NodeList childNodes = root.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				if(node.getAttributes()!=null){
					fields.put(MyNLP.convertToPinyinString(node.getAttributes().getNamedItem("key").getNodeValue().trim(), "", false), node.getAttributes().getNamedItem("key").getNodeValue().trim());
//					System.out.println();
//					System.out.println(node.getTextContent());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		}
		return fields;
	}
	
	private static List<NewDiseaseDescription> getDisease() {
		List<NewDiseaseDescription> diseases = new ArrayList<>();
		Element root = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(File f:new File("C:/Users/thyferny/Desktop/mqa/diseases").listFiles()){
//		File f = new File("C:/Users/thyferny/Desktop/mqa/diseases/Ⅱ型免疫母细胞性淋巴腺病.xml");
		// documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
		try {
			Document dt = db.parse(f);
			root = dt.getDocumentElement();
//			System.out.println("根元素：" + root.getAttribute("name"));
			NewDiseaseDescription disease = new NewDiseaseDescription(root.getAttribute("name").trim());
			NodeList childNodes = root.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				if(node.getAttributes()!=null){
					String fieldName = MyNLP.convertToPinyinString(node.getAttributes().getNamedItem("key").getNodeValue().trim(), "", false);
					String first = fieldName.substring(0, 1).toUpperCase();
					String rest = fieldName.substring(1, fieldName.length());
					fieldName = new StringBuffer(first).append(rest).toString(); 
//					fields.put(MyNLP.convertToPinyinString(node.getAttributes().getNamedItem("key").getNodeValue().trim(), "", false), node.getAttributes().getNamedItem("key").getNodeValue().trim());
//					System.out.println();
//					System.out.println(node.getTextContent());
					Method method = NewDiseaseDescription.class.getMethod("set"+fieldName, String.class);
					method.invoke(disease, node.getTextContent());
				}
			}
			diseases.add(disease);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		}
		return diseases;
	}
}