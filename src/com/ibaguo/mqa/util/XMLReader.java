package com.ibaguo.mqa.util;

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

import com.ibaguo.mqa.solr.NewDiseaseDescription;
import com.ibaguo.mqa.solr.QA120;
import com.ibaguo.mqa.solr.QAObj;
import com.ibaguo.nlp.MyNLP;

public class XMLReader {

	public static void main(String args[]) {
		// Map<String, String> fields = fieldList();
		// for (String s : fields.keySet()) {
		// System.out.println("\"" + fields.get(s) + "\",");
		// }

		// List<NewDiseaseDescription> disease = getDisease();
		// System.out.println(disease);
		// File file =new File("NewDisease.dat");
		// FileOutputStream out;
		// try {
		// out = new FileOutputStream(file);
		// ObjectOutputStream objOut=new ObjectOutputStream(out);
		// objOut.writeObject(disease);
		// objOut.flush();
		// objOut.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		List<QA120> ojbs = get120Disease();
		File file = new File("QA120.dat");
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(ojbs);
			objOut.flush();
			objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, String> fieldList() {
		Map<String, String> fields = new HashMap<>();
		Element root = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (File f : new File("C:/Users/thyferny/Desktop/mqa/diseases").listFiles()) {
			// File f = new File("C:/Users/thyferny/Desktop/mqa/diseases/Ⅱ型免疫母细胞性淋巴腺病.xml");
			// documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
			try {
				Document dt = db.parse(f);
				root = dt.getDocumentElement();
				// System.out.println("根元素：" + root.getAttribute("name"));
				NodeList childNodes = root.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node node = childNodes.item(i);
					if (node.getAttributes() != null) {
						fields.put(MyNLP.convertToPinyinString(node.getAttributes().getNamedItem("key").getNodeValue().trim(), "", false), node.getAttributes().getNamedItem("key").getNodeValue().trim());
						// System.out.println();
						// System.out.println(node.getTextContent());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fields;
	}

	private static List<QAObj> getQAObj() {
		List<QAObj> diseases = new ArrayList<>();
		Element root = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int index = 0;
		for (File folder : new File("F:/qa").listFiles()) {
			for (File f : folder.listFiles()) {
				try {
					Document dt = db.parse(f);
					root = dt.getDocumentElement();
					QAObj disease = new QAObj();
					NodeList childNodes = root.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); i++) {
						Node node = childNodes.item(i);
						if (node.getAttributes() != null) {
							String fieldName = node.getAttributes().getNamedItem("key").getNodeValue().trim();
							String first = fieldName.substring(0, 1).toUpperCase();
							String rest = fieldName.substring(1, fieldName.length());
							fieldName = new StringBuffer(first).append(rest).toString();
							if ("Answer_html".equals(fieldName)) {
								continue;
							}
							Method method = QAObj.class.getMethod("set" + fieldName, String.class);
							method.invoke(disease, node.getTextContent());
						}
					}
					diseases.add(disease);
					System.out.println(index++);
				}

				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return diseases;
	}

	private static List<QA120> get120Disease() {
		List<QA120> diseases = new ArrayList<>();
		Element root = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (File f : new File("/Users/thyferny/Downloads/120-diseases").listFiles()) {
			try {
				Document dt = db.parse(f);
				root = dt.getDocumentElement();
				QA120 disease = new QA120();
				NodeList childNodes = root.getChildNodes();
				disease.setName(root.getAttribute("name").trim());
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node node = childNodes.item(i);
					if (node.getAttributes() != null) {
						String fieldName = MyNLP.convertToPinyinString(node.getAttributes().getNamedItem("key").getNodeValue().trim(), "", false);
						String first = fieldName.substring(0, 1).toUpperCase();
						String rest = fieldName.substring(1, fieldName.length());
						fieldName = new StringBuffer(first).append(rest).toString();
						Method method = QA120.class.getMethod("set" + fieldName, String.class);
						method.invoke(disease, node.getTextContent());
					}
				}
				diseases.add(disease);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return diseases;
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
		for (File f : new File("C:/Users/thyferny/Desktop/mqa/diseases").listFiles()) {
			try {
				Document dt = db.parse(f);
				root = dt.getDocumentElement();
				NewDiseaseDescription disease = new NewDiseaseDescription(root.getAttribute("name").trim());
				NodeList childNodes = root.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node node = childNodes.item(i);
					if (node.getAttributes() != null) {
						String fieldName = MyNLP.convertToPinyinString(node.getAttributes().getNamedItem("key").getNodeValue().trim(), "", false);
						String first = fieldName.substring(0, 1).toUpperCase();
						String rest = fieldName.substring(1, fieldName.length());
						fieldName = new StringBuffer(first).append(rest).toString();
						Method method = NewDiseaseDescription.class.getMethod("set" + fieldName, String.class);
						method.invoke(disease, node.getTextContent());
					}
				}
				diseases.add(disease);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return diseases;
	}
}