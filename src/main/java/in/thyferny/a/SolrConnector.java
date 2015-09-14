package in.thyferny.a;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import in.thyferny.nlp.model.maxent.MaxEnt;

public class SolrConnector {
public static void main(String[] args) throws SolrServerException, IOException {
	
	Object temp=null;
    File file =new File("Disease.dat");
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
    List<DiseaseDescription> dds = (List<DiseaseDescription>)temp;
    
	HttpSolrClient solr = new HttpSolrClient("http://127.0.0.1:8983/solr/solr-qa");
    solr.setConnectionTimeout(100);
    solr.setDefaultMaxConnectionsPerHost(100);
    solr.setMaxTotalConnections(100);
    
    List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();  
    for (DiseaseDescription dd:dds) {  
        SolrInputDocument doc = new SolrInputDocument();  
        doc.addField("id", UUID.randomUUID().toString());
        doc.addField("name",dd.getName()); 
        doc.addField("pathogeny",dd.getPathogeny());  
        doc.addField("symptom",dd.getSymptom());  
        doc.addField("treatment",dd.getTreatment());  
        doc.addField("other", dd.getOther());  
        docs.add(doc);  
    }  
    solr.add(docs);
    solr.commit();
}
}
