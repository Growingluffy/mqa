package com.ibaguo.mqa.solr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibaguo.mqa.util.Utils;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.model.maxent.MaxEnt;
import com.ibaguo.nlp.seg.Segment;
import com.ibaguo.nlp.seg.common.Term;

public class Test {
	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// String content =
		// "平时无不适，昨天晚上聚餐，喝了些酒，有点高了；早上感觉有些头晕，肚子也难受，上了几次厕所，便溏；然后伴有恶心，面色苍白，呕吐，这是�?�么了？";
		// List<String> keywordList = MyNLP.extractKeyword(content, 3);
		// System.out.println(keywordList);
		//
//		 Segment segment = MyNLP.newSegment();
		// segment.enableIndexMode(true);
		// segment.enablePartOfSpeechTagging(false);
		// segment.enableNameRecognize(true);
		// segment.enablePlaceRecognize(true);
		// segment.enableOrganizationRecognize(true);
		// segment.enableTranslatedNameRecognize(false);
		// segment.enableCustomDictionary(false);
		// segment.enableJapaneseNameRecognize(false);
		// segment.enableAllNamedEntityRecognize(true);
//		 List<Term> termList = segment.seg("中文分词测试 这是一段普通的文本");
		// System.out.println(termList);

		// trainSaveMaxEnt();

		// loadPredictMaxEnt();

		// trainSaveQTMaxEnt();
//		trainSaveQTMaxEnt();
//		loadMQAPredictMaxEnt();
//		 for(Field f:NewDiseaseDescription.class.getDeclaredFields()){
//				String fieldName = f.getName();
//				if(fieldName.equals("serialVersionUID")){
//					continue;
//				}
//				System.out.println(fieldName);	
//		 }
//		 SynonymItem lst = CoreSynonymDictionary.get("腹泻");
//		 for(Synonym kk:Collections.sort(lst.synonymList)){
//			 System.out.println(kk.getRealWord());
//			 System.out.println(CoreSynonymDictionary.similarity("腹泻", kk.getRealWord()));
//		 }
//		System.out.println(CoreSynonymDictionary.get("香蕉"));
//		System.out.println(CoreSynonymDictionary.get("腹泻"));
		//
//		System.out.println("原因,病因 距离\t" + CoreSynonymDictionary.distance("原因","病因"));
//		System.out.println("处理,病因 距离\t" + CoreSynonymDictionary.distance("处理","病因"));
//		System.out.println("处理,治疗距离\t" + CoreSynonymDictionary.distance("处理","治疗"));
		// System.out.println("距离1\t" + CoreSynonymDictionary.distance("香蕉",
		// "�?"));
		// System.out.println("距离2\t" + CoreSynonymDictionary.distance("病痛",
		// "痢疾"));
		// System.out.println("距离3\t" + CoreSynonymDictionary.distance("腹泻",
		// "头疼"));
		// System.out.println("距离4\t" + CoreSynonymDictionary.distance("腹泻",
		// "胃病"));
		// System.out.println("距离5\t" + CoreSynonymDictionary.distance("腹泻",
		// "呕吐"));
		// System.out.println("距离6\t" + CoreSynonymDictionary.distance("晕车",
		// "呕吐"));
		// System.out.println("距离7\t" + CoreSynonymDictionary.distance("晕车",
		// "晕船"));
		// System.out.println("距离8\t" + CoreSynonymDictionary.distance("晕车",
		// "吃药"));
		// System.out.println("距离9\t" + CoreSynonymDictionary.distance("晕车",
		// "山路"));
		// System.out.println("距离X\t" + CoreSynonymDictionary.distance("香蕉",
		// "橙汁"));
//		loadSaveNewDZZMaxEnt();
//		loadTrainNewDZZMaxEnt();
//		System.out.println(loadPredictNewDZZMaxEnt("头晕恶心是怎么回事"));
//		int pres = 0;
//		MaxEnt maxEnt;
//		maxEnt = MaxEnt.loadModel("120Q-Train.dat2");
//		FileReader fr = new FileReader(new File("120-questions-4-train.txt"));
//		BufferedReader br = new BufferedReader(fr);
//		String tmp;
//		while((tmp=br.readLine())!=null){
//			String lable = tmp.split("\t")[0];
//			String qt = tmp.split("\t")[1];
//			List<Term> bb = MyNLP.segment(qt);
//			List<String> cc = new ArrayList<>();
//			for(Term t:bb){
//				cc.add(t.word);
//			}
//			String p = maxEnt.eval(cc);
//			if(p.equals(lable)){
//				pres++;
//				System.out.println(pres);
//			}
//		}
//		br.close();
//		System.out.println(pres*1.0/206817);
		
		load120();
//		writeAndFormatted();
	}

//	public static void trainSaveMaxEnt() throws IOException {
//		String path = "data/questions-train.txt";
//		MaxEnt maxEnt = new MaxEnt();
//		maxEnt.loadData(path);
//		maxEnt.train(5);
//		maxEnt.save("MaxEnt.dat");
//	}
	
	public static Map<String, String> newMap = new HashMap<>();
	static{
		String aa = "新分类		症状	5治疗	12并发症			2病因			13疾病分类			5治疗	13疾病分类		6注意事项					5治疗			4诊断方法		3症状	6注意事项												6注意事项	5治疗				5治疗		2病因				10预防	5治疗		15流行病学	2病因				5治疗	3症状			5治疗	10预防	4诊断方法				2病因	2病因	4诊断方法	5治疗	3症状	4诊断方法	5治疗	2病因	2病因	6注意事项		6注意事项			2病因	13疾病分类	4诊断方法	4诊断方法	13疾病分类	10预防	3症状	10预防	2病因		5治疗	5治疗			4诊断方法	5治疗	5治疗	4诊断方法	7副作用	17禁忌症和适应症		2病因	2病因		6注意事项		4诊断方法	5治疗	17禁忌症和适应症	7副作用	2病因		5治疗	3症状	4诊断方法	5治疗	5治疗		3症状	5治疗		3症状	3症状	7副作用	6注意事项	14预后	1概述			5治疗	5治疗	4诊断方法		2病因	5治疗	2病因	10预防	4诊断方法	2病因	15流行病学	2病因	5治疗		17禁忌症和适应症				6注意事项	5治疗				6注意事项						4诊断方法	5治疗			3症状	2病因				5治疗	5治疗		6注意事项			5治疗	5治疗	5治疗	5治疗	6注意事项	13疾病分类	5治疗	6注意事项		5治疗				5治疗	5治疗	5治疗	16药品			5治疗		5治疗	4诊断方法	6注意事项	5治疗	5治疗	12并发症	5治疗	5治疗	13疾病分类	5治疗		5治疗	13疾病分类	5治疗	5治疗	14预后		5治疗	5治疗	10预防	10预防			4诊断方法	6注意事项	4诊断方法	4诊断方法		6注意事项		5治疗		14预后			17禁忌症	3症状	2病因			2病因	6注意事项	3症状	6注意事项			4诊断方法	6注意事项	10预防	5治疗		5治疗		16药品	10预防	3症状			4诊断方法	5治疗		3症状		5治疗		2病因			10预防		7副作用	5治疗	5治疗				5治疗	4诊断方法	2病因	4诊断方法	4诊断方法	10预防	5治疗		4诊断方法		12并发症			13疾病分类						4诊断方法		5治疗								2病因			13疾病分类	2病因	4诊断方法	13疾病分类	2病因			注意事项";
		String bb = "名称	预处理的分类及优劣	发热	放疗历史	并发症	降压食品	子词条	致病原理	意义	建立骨髓库	银屑病的分型	能量的用途与需要	目的	中西医结合治疗	分型	种植覆盖义齿的维护	保健抗病功效	痰成分	脂肪与疾病	分期	原则	治疗原则	覆盖义齿类型选择	导致腹痛的疾病	诊断	分期、分子遗传学发现和治疗	临床表现	术后注意	睡眠异常与疾病	营养支持的方式	耐药相关定义	常见病	辨证要点	常选用的血管	术前同步放化疗的手术时机	重建方法	作用	套筒冠义齿	护理调适	术后注意事项	医院救治	临床常见病	光敏剂	认识误区	急救处理	操作要点	病因及病理特点	自体造血干细胞移植的步骤	病理分型	脊髓损伤的分级	预防糖尿病的五个要点	现场处理	打鼾的影响	结核菌的传播途径	原因	与疾病的关系	操作方法	五项达标	治疗及预后	造血干细胞异常	常见非有机磷农药	趋势	急救及搬运	预防调摄	审证要点	重要性	与传统放疗照射的区别	疗效判定	耳鸣病因	病理	证候分析	疗效	肝功能评价指标	脊柱脊髓损伤评分系统	放射性核素治疗与放疗的区别	病因	误诊漏诊防范	饮食及注意事项	锻炼步骤及方法	膳食指导	抵抗力	预处理及回输	窦性心动过缓的原因	脊柱骨折分类	鉴别要点	辅助检查	肿瘤类型	预防护理	常见的癌前病变	防治要点	原理	异物的种类	并发症的治疗	效果	脐血移植的优点	传染性	检测指标	光疗方法	预防与治疗	筛查项目	不良反应及对策	适应证	特色	病因与发病机制	病因及发病机制	缺点	测定体重时的注意事项	脂肪的功效	实验室检查	肺癌传统疗法	脐血移植适应证	并发症和后遗症	咳嗽的机制	效果评价	优点	手术指征	神经影像学检查	治疗	辨证施治	安全性	生物学行为	婴幼儿现场心肺复苏	“十字”原则的内容	特点	血肿特点	副作用	饮食调理	预后转归	概述	核医学的优势	减肥要科学	常用疗法介绍	家庭处理	诊断及鉴别诊断	失败原因	病因和发病机制	现代治疗趋势	导致发音失败的原因及处理方法	预防保健	睡眠解析	病因及分类	流行病学	打鼾原因	脐血移植的缺点	应用	适应证和禁忌证	手术程序	身高的影响因素	患者术前准备	检查后注意事项	方法	先进技术应用	肺结核	优势	前检查及注意事项	展望	抗癌食品	膳食纤维与健康	类型	肿瘤个性化治疗的核心	临床表现及诊断	我国结核病防治	人体的组成成分	骨髓穿刺失败原因	生活史	诱发因素	必要性	二者比较	腹痛常见疾病	施治要点	救治原则	分型标准	按压技术要求	适用部位	缺水可引致疾病	痰液处理	现场急救	缺牙修复	矮小症的治疗	饮食调护	种类	治疗要点	术后处理	效果及安全性	方法步骤与要求	误区原因与纠正	改善方法	梦游的特点	证候及辨证施治	操作步骤	紧急处理	常用药物	常见疾病	骨髓穿刺部位	饮食疗法	人体系统	防治	体温测量与正常波动	营养支持的作用	步骤与方法	综合治疗	可致疾病	种植治疗设计	处理方法	离断伤分类	手术方法	家庭小药箱	急救要点	肺癌分类	治疗模式	大发作救治	预后	缺牙的弊端	急救	心肺复苏开始宜早	饮食防癌方法	预防调护	部位及范围	来源	检查	护理	判断方法	肿瘤标志物检查	临床分型	养护方法	步骤	抢救	危害	预后与随访	种植患者的口腔维护	造血干细胞的分类与功能	禁忌证	常规临床应用项目	脑损伤过程	所致疾病	异常变化相关因素	心脑血管疾病的预防	水的功能与喝水	常见疾病及症状	术后护理	常见过敏性疾病	抗体与Ig	检查前准备工作	日常保健	预防方法	术前准备	原则内容	手术步骤	TNM分期系统	苯二氮?类镇静催眠药	锌的作用	白血病十大征象	形态	结核病疫情回升的主要原因	诊断与鉴别诊断	Richard（1998）前庭操方法	具体内容	临床分型及病理	营养素与癌症	常用止血方法	腹痛的感觉	病因病机	风险	误区	预防与调摄	中毒机制	不良反应	常备药中毒及救治	临床应用	致病性	基本原则	临床意义	原因及处理	鉴别诊断	疾病状态下的咳嗽反射	分类与诊断	诊断标准	预防	新辅助化疗后接受手术的时机	费用	诊断和鉴别诊断	临床病理类型	常见并发症	具体方法	杀鼠药分类	副银屑病分型	结构与功能	误诊的防控	即刻种植的优缺点	临床常见的误诊	目前较完善和实用的放疗技术	诊断依据	一级预防措施	治疗方法	作用机制	Ig的功能	评判标准	检查须知	固定方法	分期标准	肥胖判定	病因及常见疾病	传播方式	训练方法	分类	病理生理	相关检查	分类及分期	发病机制	复发	早餐营养学	注意事项";
		String[] aax = aa.split("\t");
		String[] bbx = bb.split("\t");
		for(int i=0;i<312;i++){
			if(aax[i].trim().equals("")){
				
			}else{
				String pinyinKey = MyNLP.convertToPinyinString(bbx[i].trim(), "", false);
				newMap.put(pinyinKey, aax[i].replaceAll("^\\d+", ""));
			}
		}
	}
	
	static String getNewType(String pinyinField){
		String newType = newMap.get(pinyinField);
		return newType!=null?newType:"";
	}
	public static List<QAObj> load300k(){
		Object temp=null;
        File file =new File("QAObj.dat");
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (List<QAObj>)temp;
	}
	
	public static void load120(){
		Object temp=null;
        File file =new File("QA120.dat");
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
       List<QA120> aa = (List<QA120>)temp;
       for (QA120 aaa:aa) {
		if(aaa.getName().equals("痔疮")){
			System.out.println(aaa.getGaishu());
		}
	}
	}
	
	public static List<String> load3k() throws FileNotFoundException{
		List<String> src = new ArrayList<>();
        FileReader file =new FileReader("3k.txt");
        BufferedReader in = new BufferedReader(file);
        try {
            String tmp;
			while((tmp = in.readLine())!=null){
            	src.add(tmp);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return src;
	}
	
//	public static List<QAObj2> update300k() throws IOException{
//		Map<String,Integer> q2id = new HashMap<>();
//		InputStream in = new FileInputStream(new File("pp.txt"));
//		InputStreamReader isr = new InputStreamReader(in,"UTF8");
//		BufferedReader br = new BufferedReader(isr);
//		String tmp;
//		while((tmp = br.readLine())!=null){
//			try {
//				q2id.put(tmp.split("~")[1],Integer.parseInt(tmp.split("~")[0]));
//			} catch (Exception e) {
//				System.out.println(tmp);
//			}
//		}
//		br.close();
//		List<QAObj> objs = load300k();
//		List<QAObj2> obj2s = new ArrayList<>();
//		for(QAObj obj:objs){
//			QAObj2 o2 = new QAObj2();
//			Integer id = q2id.get(obj.question);
//			if(id==null){
//				System.out.println(obj.question);
//				continue;
//			}
//			o2.from(obj,id.intValue());
//			obj2s.add(o2);
//		}
//		return obj2s;
//	}
	
	public static List<QAObjTagged> load300kTagged(){
		Object temp=null;
        File file =new File("QAObj2.dat");
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (List<QAObjTagged>)temp;
	}
	
	public static void writeAndFormatted(){
		List<QAObjTagged> objs = load300kTagged();
		int index = 0;
		for(QAObjTagged obj:objs){
			String fileName = String.format("%06d", index);
			String content = "\""+obj.question + "\"\t\"" + obj.answer.replaceAll("\n", "").trim() + "\"\t\"" + obj.disease_name + "\"\t\"" + obj.doctor + "\"\t\"" + obj.position + "\"\t\"" + obj.hospital + "\"\t\"" + obj.resume.replaceAll("\n", "").trim() + "\"\t\"" + obj.tag+"\"";
//			WriterThread wt = new WriterThread("qa-tagged/"+fileName,content);
//			wt.start();
			try {
				FileWriter fw = new FileWriter("qa-tagged/"+fileName);
				fw.write(content);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			index++;
		}
	}
	
//	public static void outPutDzzName(){
//		try {
//			List<QAObj> objs = load300k();
//			FileWriter file = new FileWriter("dzz-name.txt");
//			for(QAObj obj:objs){
//				file.write(obj.getDisease_name()+"\n");
//			}
//			file.flush();
//			file.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void loadPredict3k() throws IOException {
		MaxEnt maxEnt = MaxEnt.loadModel("NEWDZZTYP-Trained2.dat");
		Segment segment = MyNLP.newSegment();//启用分词器训�?
		segment.enableIndexMode(true);
		segment.enablePartOfSpeechTagging(false);
		segment.enableNameRecognize(true);
		segment.enablePlaceRecognize(true);
		segment.enableOrganizationRecognize(true);
		segment.enableTranslatedNameRecognize(false);
		segment.enableCustomDictionary(false);
		segment.enableJapaneseNameRecognize(false);
		segment.enableAllNamedEntityRecognize(true);
		
		List<String> objs = load3k();
		List<QAObjTagged> objTaggeds = new ArrayList<>();
		int index = 0;
		FileWriter fw = new FileWriter("predict3k.txt");
		for(String obj:objs){
			try {
//				QAObjTagged tagged = new QAObjTagged();
				List<Term> segs = segment.seg(MyNLP.extractSummary(obj, 1).get(0));
				List<String> toPred = new ArrayList<>();
				for(Term t:segs){
					toPred.add(t.word);
				}
				Map<String, Double> map = maxEnt.predict(toPred);
				String tag = maxEnt.eval(toPred);
//				tagged.from(obj, maxEnt.eval(toPred));
//				objTaggeds.add(tagged);
//				System.out.println(index++);
				fw.write(tag+"\n");
//				System.out.println(tag);
				fw.flush();
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
	}
	
	public static Map<String, Double> loadPredictNewDZZMaxEnt(String aa) throws IOException {
//		String path = "120-questions-4-train.txt";
//		MaxEnt maxEnt = new MaxEnt();
//		maxEnt.loadData(path,0,1,"\t");
//		maxEnt.save("120Q-PreTrain.dat");
		List<Term> bb = MyNLP.segment(aa);
		List<String> cc = new ArrayList<>();
		for(Term t:bb){
			cc.add(t.word);
		}
		MaxEnt maxEnt;
			maxEnt = MaxEnt.loadModel("120Q-Train.dat2");
			return maxEnt.predict(cc);
//		List<String> fieldList = new ArrayList<String>();
	}
	
	public static void loadTrainNewDZZMaxEnt() throws IOException {
//		String path = "120-questions-4-train.txt";
//		MaxEnt maxEnt = new MaxEnt();
//		maxEnt.loadData(path,0,1,"\t");
//		maxEnt.save("120Q-PreTrain.dat");
		MaxEnt maxEnt;
		for(int i=3;i<20;i++){
			maxEnt = MaxEnt.loadModel("120Q-Train.dat"+(i-1));
			maxEnt.train(1);
			maxEnt.save("120Q-Train.dat"+i);
		}
//		List<String> fieldList = new ArrayList<String>();
	}
	
	public static void loadSaveNewDZZMaxEnt() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String path = "data/NewDzz2Type.txt";
		MaxEnt maxEnt = new MaxEnt();
		maxEnt.loadData(path,0,1,"~,~");
		maxEnt.save("NEWDZZTYP2.dat");
	}
	
	public static void trainSaveQSNKWDZZMaxEnt() throws IOException {
		String path = "data/Disease2Ques";
		MaxEnt maxEnt = new MaxEnt();
		maxEnt.loadData(path,0,1,"\t");
		maxEnt.train(20);
		maxEnt.save("QSNKW-DZZ.dat");
	}

	public static void trainSaveQTMaxEnt() throws IOException {
		String path = "data/83.txt";
		MaxEnt maxEnt = new MaxEnt();
		maxEnt.loadCSV(path);
		maxEnt.train(50);
		maxEnt.save("QMaxEnt.dat");
	}

	public static void loadMQAPredictMaxEnt() throws IOException {
		MaxEnt maxEnt = MaxEnt.loadModel("QMaxEnt.dat");
		List<String> fieldList = new ArrayList<String>();
//		 fieldList.add("怎么办");
//		 fieldList.add("原因");
//		 fieldList.add("为什么");
		fieldList.add("头疼");
		// fieldList.add("喉咙肥大");
//		fieldList.add("如何");
//		fieldList.add("解决");
		Map<String, Double> result = maxEnt.predict(fieldList);
		System.out.println(Utils.mapToString(result));
		System.out.println(maxEnt.eval(fieldList));
	}

	public static void loadPredictMaxEnt() throws IOException {
		MaxEnt maxEnt = MaxEnt.loadModel("MaxEnt.dat");
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("When");
		fieldList.add("was");
		Map<String, Double> result = maxEnt.predict(fieldList);
		System.out.println(Utils.mapToString(result));
		System.out.println(maxEnt.eval(fieldList));
	}
}
