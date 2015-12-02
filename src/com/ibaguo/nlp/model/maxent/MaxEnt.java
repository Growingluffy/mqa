package com.ibaguo.nlp.model.maxent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibaguo.mqa.intefaces.QuestionClassifier;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.seg.Segment;
import com.ibaguo.nlp.seg.common.Term;

public class MaxEnt implements Serializable,QuestionClassifier
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Instance> instanceList = new ArrayList<Instance>();
    
	private List<Feature> featureList = new ArrayList<Feature>();
    
	private List<Integer> featureCountList = new ArrayList<Integer>();
    
	private List<String> labels = new ArrayList<String>();
    
	private double[] weight;
    
	private int C;

    public void save(String path)
    {
        File file =new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(this);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static MaxEnt loadModel(String path)
    {
        Object temp=null;
        File file =new File(path);
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
        return (MaxEnt)temp;
    }
    
    public MaxEnt initFromFile(String path){
    	return loadModel(path);
    }
    
    public void loadCSV(String path) throws IOException
    {
    	Segment segment = MyNLP.newSegment();
		segment.enableIndexMode(true);
		segment.enablePartOfSpeechTagging(false);
		segment.enableNameRecognize(true);
		segment.enablePlaceRecognize(true);
		segment.enableOrganizationRecognize(true);
		segment.enableTranslatedNameRecognize(false);
		segment.enableCustomDictionary(false);
		segment.enableJapaneseNameRecognize(false);
		segment.enableAllNamedEntityRecognize(true);
		
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line = br.readLine();
        while (line != null)
        {
        	String[] csvArray = line.split(";");
        	if(csvArray.length<4){
        		line = br.readLine();
        		continue;
        	}
        	String content = csvArray[1];
        	String title = csvArray[2];
        	String type = csvArray[3];
        	if(type==null||type.equals("")){
        		line = br.readLine();
        		continue;
        	}
        	List<Term> segs = segment.seg(content+";"+title);
            String label = type;
            int length = segs.size();
            List<String> fieldList = new ArrayList<String>();
            for (int i = 0; i < length; ++i)
            {
            	if(segs.get(i).word.trim().equals("")){
            		continue;
            	}
                fieldList.add(segs.get(i).word);
                Feature feature = new Feature(label, segs.get(i).word);
                int index = featureList.indexOf(feature);
                if (index == -1)
                {
                    featureList.add(feature);
                    featureCountList.add(1);
                }
                else
                {
                    featureCountList.set(index, featureCountList.get(index) + 1);
                }
            }
            if (fieldList.size() > C) C = fieldList.size();
            Instance instance = new Instance(label, fieldList);
            instanceList.add(instance);
            if (labels.indexOf(label) == -1) labels.add(label);
            line = br.readLine();
        }
        
//        File fl = new File("d:/tmp2Chuge_fl");
//        FileWriter fw1 = new FileWriter(fl);
//        for(int i=0;i<featureList.size();i++){
//        	fw1.write(featureList.get(i).toString()+"\t"+featureCountList.get(i)+"\n");
//        }
//        fw1.flush();
//        fw1.close();
        br.close();
    }
    
    public void loadData(String srcFilePath,int lablePos,int sentPos,String seperator) throws IOException
    {
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
		
        BufferedReader br = new BufferedReader(new FileReader(new File(srcFilePath)));
        String line = br.readLine();
        int progressIndex=0;
        while (line != null)
        {
        	try{
        	List<Term> segs = segment.seg(MyNLP.extractSummary(line.split(seperator)[sentPos], 1).get(0));
            String label = line.split(seperator)[lablePos];
            int length = segs.size();
            List<String> fieldList = new ArrayList<String>();
            for (int i = 0; i < length; ++i)
            {
            	if(segs.get(i).word.trim().equals("")){
            		continue;
            	}
                fieldList.add(segs.get(i).word);
                Feature feature = new Feature(label, segs.get(i).word);
                int index = featureList.indexOf(feature);
                if (index == -1)
                {
                    featureList.add(feature);
                    featureCountList.add(1);
                }
                else
                {
                    featureCountList.set(index, featureCountList.get(index) + 1);
                }
            }
            if (fieldList.size() > C) C = fieldList.size();
            Instance instance = new Instance(label, fieldList);
            instanceList.add(instance);
            if (labels.indexOf(label) == -1) labels.add(label);
            line = br.readLine();
            progressIndex++;
            System.out.println("Progress"+progressIndex+"/306866");
        	}
        	catch(Exception e){
        		e.printStackTrace();
        	}
        }
        br.close();
    }
    
    public void train(int maxIt)
    {
        int size = featureList.size();
        weight = new double[size];               // 特征权重
        double[] empiricalE = new double[size];   // 经验期望
        double[] modelE = new double[size];       // 模型期望

        for (int i = 0; i < size; ++i)
        {
            empiricalE[i] = (double) featureCountList.get(i) / instanceList.size();
        }

        double[] lastWeight = new double[weight.length];  // 上次迭代的权�?
        for (int i = 0; i < maxIt; ++i)
        {
//        	long start = System.currentTimeMillis();
        	System.out.print("Iteration " + i+":");
            computeModeE(modelE);
            for (int w = 0; w < weight.length; w++)
            {
                lastWeight[w] = weight[w];
                weight[w] += 1.0 / C * Math.log(empiricalE[w] / modelE[w]);
            }
//            long end = System.currentTimeMillis();
//            System.out.println(end-start+"ms");
            if (checkConvergence(lastWeight, weight)) break;
        }
    }

    
    @SuppressWarnings("unchecked")
	public Map<String, Double> predict(List<String> fieldList)
    {
        double[] prob = calProb(fieldList);
        Map<String, Double> retMap = new HashMap<String, Double>();
        for (int i = 0; i < prob.length; ++i)
        {
        	retMap.put(labels.get(i), prob[i]);
        }
        return retMap;
    }
    
    public String eval(List<String> fieldList)
    {
    	String maxProbVal = "";
    	double maxProb=0;
        double[] prob = calProb(fieldList);
        for (int i = 0; i < prob.length; ++i)
        {
        	if(prob[i]>maxProb){
        		maxProbVal = labels.get(i);
        		maxProb = prob[i];
        	}
        }
        return maxProbVal;
    }

    
    public boolean checkConvergence(double[] w1, double[] w2)
    {
        for (int i = 0; i < w1.length; ++i)
        {
            if (Math.abs(w1[i] - w2[i]) >= 0.01)    // convergence
                return false;
        }
        return true;
    }

    
    public void computeModeE(double[] modelE)
    {
        Arrays.fill(modelE, 0.0f);
        for (int i = 0; i < instanceList.size(); ++i)
        {
            List<String> fieldList = instanceList.get(i).fieldList;
            //计算当前样本X对应�?有类别的概率
            double[] pro = calProb(fieldList);
            for (int j = 0; j < fieldList.size(); j++)
            {
                for (int k = 0; k < labels.size(); k++)
                {
                    Feature feature = new Feature(labels.get(k), fieldList.get(j));
                    int index = featureList.indexOf(feature);
                    if (index != -1)
                        modelE[index] += pro[k] * (1.0 / instanceList.size());
                }
            }
          System.out.println(i);
        }
    }

    
    public double[] calProb(List<String> fieldList)
    {
        double[] p = new double[labels.size()];
        double sum = 0;  // 正则化因子，保证概率和为1
        for (int i = 0; i < labels.size(); ++i)
        {
            double weightSum = 0;
            for (String field : fieldList)
            {
                Feature feature = new Feature(labels.get(i), field);
                int index = featureList.indexOf(feature);
                if (index != -1)
                    weightSum += weight[index];
            }
            p[i] = Math.exp(weightSum);
            sum += p[i];
        }
        for (int i = 0; i < p.length; ++i)
        {
            p[i] /= sum;
        }
        return p;
    }

    
    class Instance implements Serializable
    {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		String label;
        
        List<String> fieldList = new ArrayList<String>();

        public Instance(String label, List<String> fieldList)
        {
            this.label = label;
            this.fieldList = fieldList;
        }

		@Override
		public String toString() {
			return label + "\t"+Arrays.toString(fieldList.toArray(new String[fieldList.size()]));
		}
        
    }

    
    class Feature implements Serializable
    {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		String label;
        
        String value;

        
        public Feature(String label, String value)
        {
            this.label = label;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj)
        {
            Feature feature = (Feature) obj;
            if (this.label.equals(feature.label) && this.value.equals(feature.value))
                return true;
            return false;
        }

        @Override
        public String toString()
        {
            return "[" + label + ", " + value + "]";
        }

    }
}