package com.ibaguo.mqa.pack.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibaguo.mqa.intefaces.AnswerSearcher;
import com.ibaguo.mqa.intefaces.QuestionClassifier;
import com.ibaguo.mqa.intefaces.QuestionToAnswer;
import com.ibaguo.mqa.json.Doc;
import com.ibaguo.mqa.json.DocRank;
import com.ibaguo.mqa.solr.NewDiseaseDescription;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.model.maxent.MaxEnt;
import com.ibaguo.nlp.seg.common.Term;

public class IBaguoAsk implements QuestionToAnswer {
	static String[] TYPE;
	public static Map<String, String> map = new HashMap<>();
	public static Map<String, String> newMap = new HashMap<>();
	public static Map<String, Integer> newMap2 = new HashMap<>();
	static{
		String aa = "新分类		临床表现	5治疗	12并发症			2病因			13诊断			5治疗	13诊断		6注意事项					5治疗			4诊断		3临床表现	6注意事项												6注意事项	5治疗				5治疗		2病因				10预防	5治疗		15流行病学	2病因				5治疗	3临床表现			5治疗	10预防	4诊断				2病因	2病因	4诊断	5治疗	3临床表现	4诊断	5治疗	2病因	2病因	6注意事项		6注意事项			2病因	13诊断	4诊断	4诊断	13诊断	10预防	3临床表现	10预防	2病因		5治疗	5治疗			4诊断	5治疗	5治疗	4诊断	7副作用	17并发症		2病因	2病因		6注意事项		4诊断	5治疗	17并发症	7副作用	2病因		5治疗	3临床表现	4诊断	5治疗	5治疗		3临床表现	5治疗		3临床表现	3临床表现	7副作用	6注意事项	14预后	1概述			5治疗	5治疗	4诊断		2病因	5治疗	2病因	10预防	4诊断	2病因	15流行病学	2病因	5治疗		17并发症				6注意事项	5治疗				6注意事项						4诊断	5治疗			3临床表现	2病因				5治疗	5治疗		6注意事项			5治疗	5治疗	5治疗	5治疗	6注意事项	13诊断	5治疗	6注意事项		5治疗				5治疗	5治疗	5治疗	16治疗			5治疗		5治疗	4诊断	6注意事项	5治疗	5治疗	12并发症	5治疗	5治疗	13诊断	5治疗		5治疗	13诊断	5治疗	5治疗	14预后		5治疗	5治疗	10预防	10预防			4诊断	6注意事项	4诊断	4诊断		6注意事项		5治疗		14预后			17禁忌症	3临床表现	2病因			2病因	6注意事项	3临床表现	6注意事项			4诊断	6注意事项	10预防	5治疗		5治疗		16治疗	10预防	3临床表现			4诊断	5治疗		3临床表现		5治疗		2病因			10预防		7副作用	5治疗	5治疗				5治疗	4诊断	2病因	4诊断	4诊断	10预防	5治疗		4诊断		12并发症			13诊断						4诊断		5治疗								2病因			13诊断	2病因	4诊断	13诊断	2病因			注意事项";
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
//	static {
//		TYPE = new String[] { "实验室检查", "预后与随访", "治疗", "分类及分期", "病因和发病机制", "诊断依据", "预防与治疗", "具体内容", "抗癌食品", "现代治疗趋势",
//				"治疗原则", "鉴别要点", "肿瘤类型", "固定方法", "光疗方法", "预防调护", "常见非有机磷农药", "常见过敏性疾病", "所致疾病", "与疾病的关系", "常规临床应用项目",
//				"临床表现", "病因及常见疾病", "临床分型", "防治", "核医学的优势", "风险", "患者术前准备", "脐血移植的优点", "证候分析", "苯二氮䓬类镇静催眠药", "家庭小药箱",
//				"抗体与Ig", "分类与诊断", "身高的影响因素", "优势", "分型标准", "脐血移植的缺点", "步骤", "预处理的分类及优劣", "分型", "具体方法", "异物的种类", "抢救",
//				"减肥要科学", "发病机制", "种植患者的口腔维护", "概述", "窦性心动过缓的原因", "脊髓损伤的分级", "临床应用", "饮食调理", "检查", "放射性核素治疗与放疗的区别",
//				"造血干细胞异常", "常用疗法介绍", "临床分型及病理", "Ig的功能", "检查须知", "子词条", "并发症的治疗", "致病性", "诊断标准", "血肿特点", "常备药中毒及救治",
//				"心肺复苏开始宜早", "预后", "审证要点", "常见并发症", "造血干细胞的分类与功能", "保健抗病功效", "方法步骤与要求", "日常保健", "白血病十大征象", "费用", "不良反应",
//				"预防糖尿病的五个要点", "腹痛常见疾病", "急救及搬运", "Richard（1998）前庭操方法", "病理", "趋势", "形态", "“十字”原则的内容", "重要性",
//				"结核病疫情回升的主要原因", "疗效", "检查后注意事项", "早餐营养学", "目的", "并发症", "术前同步放化疗的手术时机", "治疗及预后", "神经影像学检查", "急救", "抵抗力",
//				"二者比较", "常见病", "结构与功能", "检测指标", "人体系统", "新辅助化疗后接受手术的时机", "中西医结合治疗", "操作要点", "应用", "展望", "复发", "缺水可引致疾病",
//				"咳嗽的机制", "打鼾的影响", "误区", "导致腹痛的疾病", "家庭处理", "术后注意", "疾病状态下的咳嗽反射", "效果及安全性", "预后转归", "护理调适", "锌的作用", "作用",
//				"操作步骤", "误区原因与纠正", "救治原则", "病因与发病机制", "痰成分", "膳食纤维与健康", "我国结核病防治", "病因", "五项达标", "重建方法", "覆盖义齿类型选择",
//				"发热", "婴幼儿现场心肺复苏", "护理", "预防护理", "综合治疗", "睡眠异常与疾病", "病因及分类", "梦游的特点", "预处理及回输", "银屑病的分型", "病因及病理特点",
//				"不良反应及对策", "副银屑病分型", "误诊的防控", "注意事项", "原因及处理", "种植覆盖义齿的维护", "饮食及注意事项", "辨证施治", "病理分型", "诊断与鉴别诊断",
//				"目前较完善和实用的放疗技术", "水的功能与喝水", "检查前准备工作", "脑损伤过程", "肺癌传统疗法", "必要性", "生物学行为", "诊断", "分期、分子遗传学发现和治疗",
//				"能量的用途与需要", "种植治疗设计", "手术方法", "临床表现及诊断", "营养素与癌症", "预防调摄", "分类", "预防保健", "营养支持的方式", "肝功能评价指标",
//				"病因及发病机制", "放疗历史", "光敏剂", "分期", "来源", "脂肪与疾病", "常选用的血管", "治疗模式", "中毒机制", "意义", "步骤与方法", "处理方法",
//				"TNM分期系统", "原则", "可致疾病", "适应证和禁忌证", "辅助检查", "脂肪的功效", "肿瘤标志物检查", "临床常见病", "常用止血方法", "特色", "急救要点",
//				"腹痛的感觉", "临床常见的误诊", "常见疾病", "效果评价", "原因", "病因病机", "预防", "术后护理", "安全性", "失败原因", "缺牙修复", "人体的组成成分",
//				"临床意义", "评判标准", "效果", "按压技术要求", "杀鼠药分类", "养护方法", "脐血移植适应证", "缺点", "打鼾原因", "睡眠解析", "缺牙的弊端", "治疗方法",
//				"现场处理", "自体造血干细胞移植的步骤", "传染性", "分期标准", "骨髓穿刺部位", "种类", "筛查项目", "副作用", "预防与调摄", "体温测量与正常波动", "适应证",
//				"急救处理", "紧急处理", "流行病学", "操作方法", "类型", "现场急救", "预防方法", "与传统放疗照射的区别", "肺结核", "方法", "常见的癌前病变",
//				"脊柱脊髓损伤评分系统", "大发作救治", "导致发音失败的原因及处理方法", "认识误区", "传播方式", "诊断及鉴别诊断", "致病原理", "手术指征", "训练方法", "病理生理",
//				"术后注意事项", "饮食调护", "医院救治", "并发症和后遗症", "辨证要点", "相关检查", "优点", "一级预防措施", "危害", "施治要点", "肺癌分类", "套筒冠义齿",
//				"常用药物", "证候及辨证施治", "饮食防癌方法", "耐药相关定义", "建立骨髓库", "作用机制", "适用部位", "膳食指导", "营养支持的作用", "前检查及注意事项", "术后处理",
//				"结核菌的传播途径", "特点", "禁忌证", "常见疾病及症状", "即刻种植的优缺点", "离断伤分类", "耳鸣病因", "先进技术应用", "基本原则", "诱发因素", "生活史",
//				"异常变化相关因素", "锻炼步骤及方法", "防治要点", "临床病理类型", "改善方法", "原理", "鉴别诊断", "术前准备", "判断方法", "饮食疗法", "肿瘤个性化治疗的核心",
//				"部位及范围", "骨髓穿刺失败原因", "疗效判定", "肥胖判定", "治疗要点", "测定体重时的注意事项", "矮小症的治疗", "心脑血管疾病的预防", "脊柱骨折分类", "诊断和鉴别诊断",
//				"手术步骤", "降压食品", "痰液处理", "原则内容", "误诊漏诊防范", "手术程序" };
//		
//		System.out.println("\t\t\t\t\t症状\t病因\t治疗\t其他\t学术\t药物\t病症\t医院\t医生");
//		DecimalFormat df   = new DecimalFormat("######0.00");  
//		for (String key : TYPE) {
//			System.out.print(key+"\t\t\t\t\t");
//			String pinyinKey = MyNLP.convertToPinyinString(key, "", false);
//			Map<String, Double> similars = new HashMap<>();
//			similars.put("症状", 0.0);
//			similars.put("病因", 0.0);
//			similars.put("治疗", 0.0);
//			similars.put("其他", 0.0);
//			similars.put("学术", 0.0);
//			similars.put("药物", 0.0);
//			similars.put("病症", 0.0);
//			similars.put("医院", 0.0);
//			similars.put("医生", 0.0);
//			
//			List<Term> terms = MyNLP.segment(key);
//			for (String type : similars.keySet()) {
//				for (Term t : terms) {
//					double s = CoreSynonymDictionary.similarity(type, t.word);
//					if (s > similars.get(type)) {
//						similars.put(type, s);
//					}
//				}
//			}
//			for(String kk:similars.keySet()){
//				System.out.print(df.format(similars.get(kk))+"\t");
//			}
//			double max = 0;
//			String retType = "";
//			for (String type : similars.keySet()) {
//				if (similars.get(type) > max) {
//					max = similars.get(type);
//					retType = type;
//				}
//			}
//			System.out.print(retType);
//			System.out.println();
//			map.put(pinyinKey, retType);
//		}
//	}

//	static String getType(String pinyinField) {
//		return map.get(pinyinField);
//	}
	
	static String getNewType(String pinyinField){
		String newType = newMap.get(pinyinField);
		return newType!=null?newType:"";
	}
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		getNewType("jiancha");
//		System.out.println(newMap2);
		
		Object temp = null;
		File file = new File("NewDisease.dat");
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<NewDiseaseDescription> dds = (List<NewDiseaseDescription>) temp;
		int i=0;
		FileWriter fw = new FileWriter("data/NewDzz2Type.txt");
		for (NewDiseaseDescription dd : dds) {
			for(Field f:NewDiseaseDescription.class.getDeclaredFields()){
				String fieldName = f.getName();
				if(fieldName.equals("serialVersionUID")){
					continue;
				}
				String nt = getNewType(fieldName);
				String first = fieldName.substring(0, 1).toUpperCase();
				String rest = fieldName.substring(1, fieldName.length());
				String upperCaseFieldName = new StringBuffer(first).append(rest).toString(); 
				Method method = NewDiseaseDescription.class.getMethod("get"+upperCaseFieldName, null);
//				System.out.println(fieldName+":"+method.invoke(dd, null));
				if(!method.invoke(dd, null).equals("")&&!nt.equals("")){
					if(newMap2.get(nt)==null){
						newMap2.put(nt, 0);
					}
					newMap2.put(nt, newMap2.get(nt)+1);
					i++;
					fw.write(nt+"~,~"+method.invoke(dd, null).toString().replaceAll("\n", "").trim()+"\n");
				}
			}
			fw.flush();
		}
		fw.close();
		System.out.println(newMap2);
		for(String key:newMap2.keySet()){
			System.out.println(key+newMap2.get(key)*100.0/i);
		}
	}

	@Override
	public List<Doc> makeQa(String q) {
//		KeyWordExtract kwe = new NlpKeyWordExtract();
		List<Term> ktList = MyNLP.segment(q);
		List<String> kwList = new ArrayList<>();
		for(Term word:ktList){
			kwList.add(word.word);
		}
		QuestionClassifier qc = MaxEnt.loadModel("QMaxEnt.dat");
		String questionType = qc.eval(kwList);
		AnswerSearcher as = new SolrSearcher();
		List<DocRank> kv = as.search(kwList.toArray(new String[kwList.size()]));
		List<Doc> ans = new ArrayList<>();
		
		for(DocRank srr:kv){
			Doc sr = srr.getDoc();
			Doc aDoc = new Doc(sr.getName(),sr.getId());
			Map<String, String> data = sr.getFieldVal();
			for(String pinyinField:data.keySet()){
				String key = getNewType(pinyinField);
				if(key.equals(questionType)){
					aDoc.putFieldVal(key, sr.getFieldVal().get(pinyinField));
//					ans.add(sr.getFieldVal().get(pinyinField));
				}
			}
			ans.add(aDoc);
		}
		return ans;
	}
}
