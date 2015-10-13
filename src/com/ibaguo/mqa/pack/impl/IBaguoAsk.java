package com.ibaguo.mqa.pack.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibaguo.mqa.intefaces.AnswerSearcher;
import com.ibaguo.mqa.intefaces.KeyWordExtract;
import com.ibaguo.mqa.intefaces.QuestionClassifier;
import com.ibaguo.mqa.intefaces.QuestionToAnswer;
import com.ibaguo.mqa.solr.SolrReturn;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.dictionary.CoreSynonymDictionary;
import com.ibaguo.nlp.model.maxent.MaxEnt;
import com.ibaguo.nlp.seg.common.Term;

public class IBaguoAsk implements QuestionToAnswer {
	static String[] TYPE;
	public static Map<String, String> map = new HashMap<>();
	static {
		TYPE = new String[] { "实验室检查", "预后与随访", "治疗", "分类及分期", "病因和发病机制", "诊断依据", "预防与治疗", "具体内容", "抗癌食品", "现代治疗趋势",
				"治疗原则", "鉴别要点", "肿瘤类型", "固定方法", "光疗方法", "预防调护", "常见非有机磷农药", "常见过敏性疾病", "所致疾病", "与疾病的关系", "常规临床应用项目",
				"临床表现", "病因及常见疾病", "临床分型", "防治", "核医学的优势", "风险", "患者术前准备", "脐血移植的优点", "证候分析", "苯二氮䓬类镇静催眠药", "家庭小药箱",
				"抗体与Ig", "分类与诊断", "身高的影响因素", "优势", "分型标准", "脐血移植的缺点", "步骤", "预处理的分类及优劣", "分型", "具体方法", "异物的种类", "抢救",
				"减肥要科学", "发病机制", "种植患者的口腔维护", "概述", "窦性心动过缓的原因", "脊髓损伤的分级", "临床应用", "饮食调理", "检查", "放射性核素治疗与放疗的区别",
				"造血干细胞异常", "常用疗法介绍", "临床分型及病理", "Ig的功能", "检查须知", "子词条", "并发症的治疗", "致病性", "诊断标准", "血肿特点", "常备药中毒及救治",
				"心肺复苏开始宜早", "预后", "审证要点", "常见并发症", "造血干细胞的分类与功能", "保健抗病功效", "方法步骤与要求", "日常保健", "白血病十大征象", "费用", "不良反应",
				"预防糖尿病的五个要点", "腹痛常见疾病", "急救及搬运", "Richard（1998）前庭操方法", "病理", "趋势", "形态", "“十字”原则的内容", "重要性",
				"结核病疫情回升的主要原因", "疗效", "检查后注意事项", "早餐营养学", "目的", "并发症", "术前同步放化疗的手术时机", "治疗及预后", "神经影像学检查", "急救", "抵抗力",
				"二者比较", "常见病", "结构与功能", "检测指标", "人体系统", "新辅助化疗后接受手术的时机", "中西医结合治疗", "操作要点", "应用", "展望", "复发", "缺水可引致疾病",
				"咳嗽的机制", "打鼾的影响", "误区", "导致腹痛的疾病", "家庭处理", "术后注意", "疾病状态下的咳嗽反射", "效果及安全性", "预后转归", "护理调适", "锌的作用", "作用",
				"操作步骤", "误区原因与纠正", "救治原则", "病因与发病机制", "痰成分", "膳食纤维与健康", "我国结核病防治", "病因", "五项达标", "重建方法", "覆盖义齿类型选择",
				"发热", "婴幼儿现场心肺复苏", "护理", "预防护理", "综合治疗", "睡眠异常与疾病", "病因及分类", "梦游的特点", "预处理及回输", "银屑病的分型", "病因及病理特点",
				"不良反应及对策", "副银屑病分型", "误诊的防控", "注意事项", "原因及处理", "种植覆盖义齿的维护", "饮食及注意事项", "辨证施治", "病理分型", "诊断与鉴别诊断",
				"目前较完善和实用的放疗技术", "水的功能与喝水", "检查前准备工作", "脑损伤过程", "肺癌传统疗法", "必要性", "生物学行为", "诊断", "分期、分子遗传学发现和治疗",
				"能量的用途与需要", "种植治疗设计", "手术方法", "临床表现及诊断", "营养素与癌症", "预防调摄", "分类", "预防保健", "营养支持的方式", "肝功能评价指标",
				"病因及发病机制", "放疗历史", "光敏剂", "分期", "来源", "脂肪与疾病", "常选用的血管", "治疗模式", "中毒机制", "意义", "步骤与方法", "处理方法",
				"TNM分期系统", "原则", "可致疾病", "适应证和禁忌证", "辅助检查", "脂肪的功效", "肿瘤标志物检查", "临床常见病", "常用止血方法", "特色", "急救要点",
				"腹痛的感觉", "临床常见的误诊", "常见疾病", "效果评价", "原因", "病因病机", "预防", "术后护理", "安全性", "失败原因", "缺牙修复", "人体的组成成分",
				"临床意义", "评判标准", "效果", "按压技术要求", "杀鼠药分类", "养护方法", "脐血移植适应证", "缺点", "打鼾原因", "睡眠解析", "缺牙的弊端", "治疗方法",
				"现场处理", "自体造血干细胞移植的步骤", "传染性", "分期标准", "骨髓穿刺部位", "种类", "筛查项目", "副作用", "预防与调摄", "体温测量与正常波动", "适应证",
				"急救处理", "紧急处理", "流行病学", "操作方法", "类型", "现场急救", "预防方法", "与传统放疗照射的区别", "肺结核", "方法", "常见的癌前病变",
				"脊柱脊髓损伤评分系统", "大发作救治", "导致发音失败的原因及处理方法", "认识误区", "传播方式", "诊断及鉴别诊断", "致病原理", "手术指征", "训练方法", "病理生理",
				"术后注意事项", "饮食调护", "医院救治", "并发症和后遗症", "辨证要点", "相关检查", "优点", "一级预防措施", "危害", "施治要点", "肺癌分类", "套筒冠义齿",
				"常用药物", "证候及辨证施治", "饮食防癌方法", "耐药相关定义", "建立骨髓库", "作用机制", "适用部位", "膳食指导", "营养支持的作用", "前检查及注意事项", "术后处理",
				"结核菌的传播途径", "特点", "禁忌证", "常见疾病及症状", "即刻种植的优缺点", "离断伤分类", "耳鸣病因", "先进技术应用", "基本原则", "诱发因素", "生活史",
				"异常变化相关因素", "锻炼步骤及方法", "防治要点", "临床病理类型", "改善方法", "原理", "鉴别诊断", "术前准备", "判断方法", "饮食疗法", "肿瘤个性化治疗的核心",
				"部位及范围", "骨髓穿刺失败原因", "疗效判定", "肥胖判定", "治疗要点", "测定体重时的注意事项", "矮小症的治疗", "心脑血管疾病的预防", "脊柱骨折分类", "诊断和鉴别诊断",
				"手术步骤", "降压食品", "痰液处理", "原则内容", "误诊漏诊防范", "手术程序" };
		for (String key : TYPE) {
			String pinyinKey = MyNLP.convertToPinyinString(key, "", false);
			Map<String, Double> similars = new HashMap<>();
			similars.put("症状", 0.0);
			similars.put("病因", 0.0);
			similars.put("治疗", 0.0);
			similars.put("其他", 0.0);
			List<Term> terms = MyNLP.segment(key);
			for (String type : similars.keySet()) {
				for (Term t : terms) {
					double s = CoreSynonymDictionary.similarity(type, t.word);
					if (s > similars.get(type)) {
						similars.put(type, s);
					}
				}
			}
			double max = 0;
			String retType = "";
			for (String type : similars.keySet()) {
				if (similars.get(type) > max) {
					max = similars.get(type);
					retType = type;
				}
			}
			map.put(pinyinKey, retType);
		}
	}

	static String getType(String pinyinField) {
		return map.get(pinyinField);
	}

	public static void main(String[] args) {
		new IBaguoAsk().makeQa("溃疡病穿孔有哪些表现？");
	}

	@Override
	public List<String> makeQa(String q) {
		KeyWordExtract kwe = new NlpKeyWordExtract();
		List<String> kwList = kwe.extractKeyword(q, 3);
		QuestionClassifier qc = MaxEnt.loadModel("QMaxEnt.dat");
		String questionType = qc.eval(kwList);
		AnswerSearcher as = new SolrSearcher();
		List<SolrReturn> kv = as.search(kwList, true);
		List<String> ans = new ArrayList<>();
		
		for(SolrReturn sr:kv){
//			System.out.println(sr.name);
			Map<String, String> data = sr.getData();
			for(String pinyinField:data.keySet()){
				String key = getType(pinyinField);
				if(key.equals(questionType)){
					ans.add(sr.getData().get(pinyinField));
//					System.out.println(key+"\t"+sr.getData().get(pinyinField));
				}
			}
		}
		return ans;
	}
}
