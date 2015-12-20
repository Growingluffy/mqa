package com.ibaguo.mqa.solr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Test2 {
public static void main(String[] args) throws IOException {
//	List<QAObj> objs = load300k();
//	String question = "双侧基底节区多发性腔隙性脑梗死伴软化严重吗？";
//	getSimilarQAObj(question,objs,5);
	InputStream in = new FileInputStream(new File("NEXT.txt"));
	InputStreamReader isr = new InputStreamReader(in,"UTF8");
	BufferedReader br = new BufferedReader(isr);
	Map<String,String> map1 = new HashMap<>();
	String tmp;
//	FileWriter fw = new FileWriter("pp.txt");
	while((tmp = br.readLine())!=null){
		try {
			map1.put(tmp.split("\t")[1], tmp.split(",")[0]);
//			fw.write(tmp.split(",")[0]+"~"+tmp.split(",")[1]+"\n");
//			fw.flush();
		} catch (Exception e) {
			System.out.println(tmp);
//			e.printStackTrace();
		}
	}
	
	in = new FileInputStream(new File("Q2T1214MDL-预测.txt"));
	isr = new InputStreamReader(in,"UTF8");
	br = new BufferedReader(isr);
	int total = 0;
	int right = 0;
	Map<String,String> map2 = new HashMap<>();
	while((tmp = br.readLine())!=null){
		try {
			if(tmp.split(",")[0].equals(map1.get(tmp.split("\t")[1]))){
				right++;
			}
			total++;
			map2.put(tmp.split("\t")[1], tmp.split(",")[0]);
		} catch (Exception e) {
			System.out.println(tmp);
//			e.printStackTrace();
		}
	}
	System.out.println(total);
	System.out.println(right);
	System.out.println(right*1.0/total);
	br.close();
}

}
