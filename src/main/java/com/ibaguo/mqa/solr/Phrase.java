package com.ibaguo.mqa.solr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Phrase {
	public static void main(String[] args) throws IOException {
		List<DiseaseDescription> dds = new ArrayList<>();
		File f = new File("data/《默克家庭诊疗手册》.txt");
		InputStreamReader isr = new InputStreamReader(new FileInputStream(f),"GBK");
		BufferedReader br = new BufferedReader(isr);
		String tmp = "";
		DiseaseDescription dd = null;
		int contentType=0;
		while((tmp=br.readLine())!=null){
			if(tmp.contains("★")){
				if(dd!=null){
					dds.add(dd);
				}
				dd = new DiseaseDescription(tmp.replaceAll("★", "").trim());
				contentType=0;
				continue;
			}
			if(dd!=null){
				  String regex = "\\【(.*?)\\】";
				  Pattern p = Pattern.compile(regex);
				  Matcher m = p.matcher(tmp);
				  if(m.find()){
					  if(m.group(1).contains("病因")){
						  contentType=1;
					  }
					  if(m.group(1).contains("症状")||m.group(1).contains("诊断")){
						  contentType=2;
					  }
					  if(m.group(1).contains("治疗")||m.group(1).contains("预防")){
						  contentType=3;
					  }
				  }
				  String content = tmp.replaceAll("\\【(.*?)\\】", "").trim();
				  switch(contentType){
				  case 0:
					  dd.appendOther(content);
					  break;
				  case 1:
					  dd.appendPathogeny(content);
					  break;
				  case 2:
					  dd.appendSymptom(content);
					  break;
				  case 3:
					  dd.appendTreatment(content);
					  break;
				  }
			}
		}
		br.close();
		File file =new File("Disease.dat");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(dds);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
