package scopusextraction;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import javax.swing.plaf.synth.SynthSpinnerUI;
import javax.swing.text.html.HTML.Tag;

import org.neo4j.graphdb.GraphDatabaseService;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import neo4j.StartGraphDB;
import tagmeextraction.RelExtraction;
import tagmeextraction.TagMeExtraction;
import tagmeextraction.WPExtract;

public class LinkTag {
	static void inserisciTagDaAbstract (GraphDatabaseService graphDb, String extract, String TIPOLINK1, String TIPOLINK2, String TIPOLINK3 ) throws IOException{
		try {
			Map categories = TagMeExtraction.getCategories(extract);
			Map descriptions = TagMeExtraction.getDescription(extract);
//			prima fase: estraggo categorie e le linko ai nodi TAG
			for(Object k : categories.keySet()){
				String t = k.toString();
				String rel = RelExtraction.getRelBetweenTags(t, "nanoindentation");
				StartGraphDB.insertRelationProperty(graphDb, extract, "ABSTRACT", t, "TAG", TIPOLINK1, "weight", rel);
				for(String cate :  (HashSet<String>) categories.get(k)){
					StartGraphDB.insertRelation(graphDb, t, "TAG", cate, "CATEGORIA", TIPOLINK2);
				}
			}
//			seconda fase: estraggo le descrizioni delle Keywords e le linko ai nodi TAG
			for(Object k : descriptions.keySet()){
				String t = k.toString();
				String d = (String) descriptions.get(k);
				System.out.println("analisi del tag "+t);
				StartGraphDB.insertRelation(graphDb, t, "TAG", d, "DESCRIPTION", TIPOLINK3);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
}
