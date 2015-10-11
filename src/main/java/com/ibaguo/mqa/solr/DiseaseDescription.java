package com.ibaguo.mqa.solr;

import java.io.Serializable;

public class DiseaseDescription implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String name;
	String pathogeny = "";
	String symptom= "";
	String treatment= "";
	String other= "";
	public String getName() {
		return name;
	}
	public void appendName(String name) {
		this.name = name;
	}
	public String getPathogeny() {
		return pathogeny;
	}
	public void appendPathogeny(String pathogeny) {
		this.pathogeny += pathogeny;
	}
	public String getSymptom() {
		return symptom;
	}
	public void appendSymptom(String symptom) {
		this.symptom += symptom;
	}
	public String getTreatment() {
		return treatment;
	}
	public void appendTreatment(String treatment) {
		this.treatment += treatment;
	}
	public String getOther() {
		return other;
	}
	public void appendOther(String other) {
		this.other += other;
	}
	public DiseaseDescription(String name) {
		super();
		this.name = name;
	}
	@Override
	public String toString() {
		return "{\n\tname:" + name + "\n\tpathogeny:" + pathogeny + "\n\tsymptom:" + symptom + "\n\ttreatment:"
				+ treatment + "\n\tother:" + other + "}\n\n";
	}
	
}
