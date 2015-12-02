package com.ibaguo.mqa.solr;

import java.io.Serializable;

public class QAObjTagged implements Serializable{
	String question;
	String answer;
	String disease_name;
	String doctor;
	String position;
	String hospital;
	String resume;
	String tag;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getDisease_name() {
		return disease_name;
	}
	public void setDisease_name(String disease_name) {
		this.disease_name = disease_name;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	public String getTag() {
		return tag;
	}
//	@Override
//	public String toString() {
//		return "问题:" + question + "\n答案:" + answer + "\n疾病名称:" + disease_name + "\n医生:" + doctor + "\n职务:" + position + "\n医院:" + hospital + "\n医生简历:" + resume + "\nME分类:" + tag;
//	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	void from(QAObj obj,String tag){
		question = obj.getQuestion();
		answer = obj.getAnswer();
		disease_name = obj.getDisease_name();
		doctor = obj.getDoctor();
		position = obj.getPosition();
		hospital = obj.getHospital();
		resume = obj.getResume();
		this.tag = tag;
	}
	
}
