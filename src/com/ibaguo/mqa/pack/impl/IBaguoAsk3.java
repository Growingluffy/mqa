//package com.ibaguo.mqa.pack.impl;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.ibaguo.mqa.intefaces.AnswerSearcher;
//import com.ibaguo.mqa.intefaces.QuestionClassifier;
//import com.ibaguo.mqa.intefaces.QuestionToAnswer;
//import com.ibaguo.mqa.json.Doc;
//import com.ibaguo.mqa.json.DocRank;
//import com.ibaguo.nlp.MyNLP;
//import com.ibaguo.nlp.model.maxent.MaxEnt;
//import com.ibaguo.nlp.seg.common.Term;
//
//public class IBaguoAsk3 implements QuestionToAnswer {
//	static String[] TYPE;
//	public static Map<String, String> map = new HashMap<>();
//	public static Map<String, String> newMap = new HashMap<>();
//	public static Map<String, String> pinyinMap = new HashMap<>();
//	static Map<String, Integer> dzzName = new HashMap<>();
//
//	static {
//		String aa = "新分类		临床表现	5治疗	12并发症			2病因			13诊断			5治疗	13诊断		6注意事项					5治疗			4诊断		3临床表现	6注意事项												6注意事项	5治疗				5治疗		2病因				10预防	5治疗		15流行病学	2病因				5治疗	3临床表现			5治疗	10预防	4诊断				2病因	2病因	4诊断	5治疗	3临床表现	4诊断	5治疗	2病因	2病因	6注意事项		6注意事项			2病因	13诊断	4诊断	4诊断	13诊断	10预防	3临床表现	10预防	2病因		5治疗	5治疗			4诊断	5治疗	5治疗	4诊断	7副作用	17并发症		2病因	2病因		6注意事项		4诊断	5治疗	17并发症	7副作用	2病因		5治疗	3临床表现	4诊断	5治疗	5治疗		3临床表现	5治疗		3临床表现	3临床表现	7副作用	6注意事项	14预后	1概述			5治疗	5治疗	4诊断		2病因	5治疗	2病因	10预防	4诊断	2病因	15流行病学	2病因	5治疗		17并发症				6注意事项	5治疗				6注意事项						4诊断	5治疗			3临床表现	2病因				5治疗	5治疗		6注意事项			5治疗	5治疗	5治疗	5治疗	6注意事项	13诊断	5治疗	6注意事项		5治疗				5治疗	5治疗	5治疗	16治疗			5治疗		5治疗	4诊断	6注意事项	5治疗	5治疗	12并发症	5治疗	5治疗	13诊断	5治疗		5治疗	13诊断	5治疗	5治疗	14预后		5治疗	5治疗	10预防	10预防			4诊断	6注意事项	4诊断	4诊断		6注意事项		5治疗		14预后			17禁忌症	3临床表现	2病因			2病因	6注意事项	3临床表现	6注意事项			4诊断	6注意事项	10预防	5治疗		5治疗		16治疗	10预防	3临床表现			4诊断	5治疗		3临床表现		5治疗		2病因			10预防		7副作用	5治疗	5治疗				5治疗	4诊断	2病因	4诊断	4诊断	10预防	5治疗		4诊断		12并发症			13诊断						4诊断		5治疗								2病因			13诊断	2病因	4诊断	13诊断	2病因			注意事项";
//		String bb = "名称	预处理的分类及优劣	发热	放疗历史	并发症	降压食品	子词条	致病原理	意义	建立骨髓库	银屑病的分型	能量的用途与需要	目的	中西医结合治疗	分型	种植覆盖义齿的维护	保健抗病功效	痰成分	脂肪与疾病	分期	原则	治疗原则	覆盖义齿类型选择	导致腹痛的疾病	诊断	分期、分子遗传学发现和治疗	临床表现	术后注意	睡眠异常与疾病	营养支持的方式	耐药相关定义	常见病	辨证要点	常选用的血管	术前同步放化疗的手术时机	重建方法	作用	套筒冠义齿	护理调适	术后注意事项	医院救治	临床常见病	光敏剂	认识误区	急救处理	操作要点	病因及病理特点	自体造血干细胞移植的步骤	病理分型	脊髓损伤的分级	预防糖尿病的五个要点	现场处理	打鼾的影响	结核菌的传播途径	原因	与疾病的关系	操作方法	五项达标	治疗及预后	造血干细胞异常	常见非有机磷农药	趋势	急救及搬运	预防调摄	审证要点	重要性	与传统放疗照射的区别	疗效判定	耳鸣病因	病理	证候分析	疗效	肝功能评价指标	脊柱脊髓损伤评分系统	放射性核素治疗与放疗的区别	病因	误诊漏诊防范	饮食及注意事项	锻炼步骤及方法	膳食指导	抵抗力	预处理及回输	窦性心动过缓的原因	脊柱骨折分类	鉴别要点	辅助检查	肿瘤类型	预防护理	常见的癌前病变	防治要点	原理	异物的种类	并发症的治疗	效果	脐血移植的优点	传染性	检测指标	光疗方法	预防与治疗	筛查项目	不良反应及对策	适应证	特色	病因与发病机制	病因及发病机制	缺点	测定体重时的注意事项	脂肪的功效	实验室检查	肺癌传统疗法	脐血移植适应证	并发症和后遗症	咳嗽的机制	效果评价	优点	手术指征	神经影像学检查	治疗	辨证施治	安全性	生物学行为	婴幼儿现场心肺复苏	“十字”原则的内容	特点	血肿特点	副作用	饮食调理	预后转归	概述	核医学的优势	减肥要科学	常用疗法介绍	家庭处理	诊断及鉴别诊断	失败原因	病因和发病机制	现代治疗趋势	导致发音失败的原因及处理方法	预防保健	睡眠解析	病因及分类	流行病学	打鼾原因	脐血移植的缺点	应用	适应证和禁忌证	手术程序	身高的影响因素	患者术前准备	检查后注意事项	方法	先进技术应用	肺结核	优势	前检查及注意事项	展望	抗癌食品	膳食纤维与健康	类型	肿瘤个性化治疗的核心	临床表现及诊断	我国结核病防治	人体的组成成分	骨髓穿刺失败原因	生活史	诱发因素	必要性	二者比较	腹痛常见疾病	施治要点	救治原则	分型标准	按压技术要求	适用部位	缺水可引致疾病	痰液处理	现场急救	缺牙修复	矮小症的治疗	饮食调护	种类	治疗要点	术后处理	效果及安全性	方法步骤与要求	误区原因与纠正	改善方法	梦游的特点	证候及辨证施治	操作步骤	紧急处理	常用药物	常见疾病	骨髓穿刺部位	饮食疗法	人体系统	防治	体温测量与正常波动	营养支持的作用	步骤与方法	综合治疗	可致疾病	种植治疗设计	处理方法	离断伤分类	手术方法	家庭小药箱	急救要点	肺癌分类	治疗模式	大发作救治	预后	缺牙的弊端	急救	心肺复苏开始宜早	饮食防癌方法	预防调护	部位及范围	来源	检查	护理	判断方法	肿瘤标志物检查	临床分型	养护方法	步骤	抢救	危害	预后与随访	种植患者的口腔维护	造血干细胞的分类与功能	禁忌证	常规临床应用项目	脑损伤过程	所致疾病	异常变化相关因素	心脑血管疾病的预防	水的功能与喝水	常见疾病及症状	术后护理	常见过敏性疾病	抗体与Ig	检查前准备工作	日常保健	预防方法	术前准备	原则内容	手术步骤	TNM分期系统	苯二氮?类镇静催眠药	锌的作用	白血病十大征象	形态	结核病疫情回升的主要原因	诊断与鉴别诊断	Richard（1998）前庭操方法	具体内容	临床分型及病理	营养素与癌症	常用止血方法	腹痛的感觉	病因病机	风险	误区	预防与调摄	中毒机制	不良反应	常备药中毒及救治	临床应用	致病性	基本原则	临床意义	原因及处理	鉴别诊断	疾病状态下的咳嗽反射	分类与诊断	诊断标准	预防	新辅助化疗后接受手术的时机	费用	诊断和鉴别诊断	临床病理类型	常见并发症	具体方法	杀鼠药分类	副银屑病分型	结构与功能	误诊的防控	即刻种植的优缺点	临床常见的误诊	目前较完善和实用的放疗技术	诊断依据	一级预防措施	治疗方法	作用机制	Ig的功能	评判标准	检查须知	固定方法	分期标准	肥胖判定	病因及常见疾病	传播方式	训练方法	分类	病理生理	相关检查	分类及分期	发病机制	复发	早餐营养学	注意事项";
//		String[] aax = aa.split("\t");
//		String[] bbx = bb.split("\t");
//
//		for (int i = 0; i < 312; i++) {
//			if (aax[i].trim().equals("")) {
//
//			} else {
//				String pinyinKey = MyNLP.convertToPinyinString(bbx[i].trim(), "", false);
//				pinyinMap.put(pinyinKey, bbx[i]);
//				newMap.put(pinyinKey, aax[i].replaceAll("^\\d+", ""));
//			}
//		}
//
//		try {
//			FileReader fr = new FileReader(System.getProperty("user.dir") + "/dzz-name.txt");
//			BufferedReader br = new BufferedReader(fr);
//			String tmp;
//			while ((tmp = br.readLine()) != null) {
//				dzzName.put(tmp, 0);
//			}
//			br.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	static String getNewType(String pinyinField) {
//		String newType = newMap.get(pinyinField);
//		return newType != null ? newType : "";
//	}
//
//	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException,
//			IllegalArgumentException, InvocationTargetException, IOException {
//		String q = "日的副作用";
////		List<Doc> ans = new IBaguoAsk3().makeQa(q);
////		for (Doc doc : ans) {
////			System.out.println(doc);
////		}
//		System.out.println(getDzzName(q));
//	}
//
//	@Override
//	public List<Doc> makeQa(String q) {
//		// KeyWordExtract kwe = new NlpKeyWordExtract();
//
//		List<Term> ktList = MyNLP.segment(q);
//		List<String> kwList = new ArrayList<>();
//		for (Term word : ktList) {
//			kwList.add(word.word);
//		}
//		QuestionClassifier qc = MaxEnt.loadModel("NEWDZZTYP-Trained2.dat");
//		System.out.println(qc.predict(kwList));
//		Map<String, Double> map1 = qc.predict(kwList);
//		Map<String, Double> fixed = new HashMap<>();
//		fixed.put("治疗", 10.147392290249433);
//		fixed.put("并发症", 6.2199546485260773);
//		fixed.put("诊断", 10.97732426303855);
//		fixed.put("注意事项", 6.3287981859410431);
//		fixed.put("概述", 10.895691609977325);
//		fixed.put("流行病学", 7.1473922902494331);
//		fixed.put("副作用", 7.07936507936507936);
//		fixed.put("病因", 10.036281179138323);
//		fixed.put("预后", 8.572562358276644);
//		fixed.put("禁忌症", 7.1473922902494331);
//		fixed.put("预防", 8.6621315192743764);
//		fixed.put("临床表现", 10.285714285714286);
//		String questionType = qc.eval(kwList);
//		for(String kk:map1.keySet()){
//			if(map1.get(kk)*100>fixed.get(kk)){
//				questionType = kk;
//			}
//		}
//		System.out.println(questionType);
//		getDzzName(q);
//		List<Doc> ans = new ArrayList<>();
//
//		List<DocRank> qa2 = SolrSearcher2.search(q);
//		for (DocRank srr : qa2) {
//			Doc sr = srr.getDoc();
//			Doc aDoc = new Doc(sr.getName(), sr.getId());
//			Map<String, String> data = sr.getFieldVal();
//			for (String key : data.keySet()) {
//				if (key.equals("answer")) {
//					aDoc.putFieldVal("ANSWER", sr.getFieldVal().get(key));
//				}
//			}
//			ans.add(aDoc);
//		}
//
//		AnswerSearcher as = new SolrSearcher();
//		List<DocRank> kv = as.search(kwList.toArray(new String[kwList.size()]));
//		for (DocRank srr : kv) {
//			Doc sr = srr.getDoc();
//			Doc aDoc = new Doc(sr.getName(), sr.getId());
//			Map<String, String> data = sr.getFieldVal();
//			boolean hasFieldData = false;
//			for (String pinyinField : data.keySet()) {
//				String key = getNewType(pinyinField);
//				if (key.equals(questionType)) {
//					aDoc.putFieldVal(pinyinMap.get(pinyinField), sr.getFieldVal().get(pinyinField));
//					hasFieldData = true;
//				}
//			}
//			if (hasFieldData) {
//				ans.add(aDoc);
//			}
//		}
//		return ans;
//	}
//
//	private static String getDzzName(String q) {
//		String realDzzName = "";
//		int matchLength = 0;
//		double matchRate = 0;
//		boolean found = false;
//		for (String name : dzzName.keySet()) {
//			if (q.contains(name)) {
//				found = true;
//				realDzzName = name;
//				return realDzzName;
//			}
//		}
//		List<Term> ktList = MyNLP.segment(q);
//		if (!found) {
//			for (Term t : ktList) {
//				for (String name : dzzName.keySet()) {
//					if (name.indexOf(t.word) > -1) {
//						if (t.word.length() >= matchLength) {
//							matchLength = t.word.length();
//							double tmpMatchRate = matchLength * 1.0 / name.length();
//							if (tmpMatchRate > matchRate) {
//								realDzzName = name;
//								matchRate = tmpMatchRate;
//							}
//						}
//					}
//				}
//			}
//		}
////		System.out.println(realDzzName);
//		return realDzzName;
//	}
//}
