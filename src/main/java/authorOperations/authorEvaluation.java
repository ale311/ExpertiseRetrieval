package authorOperations;

import java.util.HashMap;
import java.util.HashSet;
import neo4j.*;

public class authorEvaluation {
	public static void main (String [] args){
		getScoreWithPresence("6603406713");
		getScoreWithWeight("6603406713");
	}
	
	private static double getScoreWithPresence(String author){
		double result = 0;
		HashSet<String> keywordsKA;
		HashSet<String> keywordsCA;
		keywordsKA = ExtractInfoN4J.getOnlyTags();
		keywordsCA = AuthorExtraction.authorExtractionOnlytag(author);
		result = calcolaCoOccurrency(keywordsCA, keywordsKA);
		return result;
	}
	
	private static double getScoreWithWeight(String author){
		double result = 0;
		HashMap<String,Double> keywordsKA;
		HashMap<String, Double> keywordsCA;
		keywordsKA = ExtractInfoN4J.getTagsAndWeight();
		keywordsCA = AuthorExtraction.authorExtractionTagWeight(author);
		result = calcolaWeightedOccurrency(keywordsCA,keywordsKA);
		return result;
	}
	
	private static double calcolaCoOccurrency(HashSet<String> keywordsCA, HashSet<String> keywordsKA) {
		// TODO Auto-generated method stub
		HashSet<String> temp = new HashSet<>();
		for(String s : keywordsCA){
			if(keywordsKA.contains(s)){
				temp.add(s);
			}
		}
		double dimCAKA = temp.size();
		System.out.println(dimCAKA);
		double dimKA = keywordsKA.size();
		System.out.println(dimKA);
		double result = dimCAKA/dimKA;
		System.out.println(result);
		return result;
	}
	
	
	private static double calcolaWeightedOccurrency(HashMap<String, Double> keywordsCA,
			HashMap<String, Double> keywordsKA) {
		// TODO Auto-generated method stub
		double result = 0;
		HashMap<String, Double> temp = new HashMap<>();
		for(String s : keywordsCA.keySet()){
			if(keywordsKA.containsKey(s)){
				temp.put(s, keywordsCA.get(s));
			}
		}
		double weightedKA = calcolaWeightSet(keywordsKA);
		double weightedCA = calcolaWeightSet(temp);
		result = weightedCA/weightedKA;
		System.out.println(result);
		return result;
	}

	private static double calcolaWeightSet(HashMap<String, Double> hm) {
		// TODO Auto-generated method stub
		double result = 0;
		for(String s : hm.keySet()){
			result = result + hm.get(s);
		}
		return result;
	}
}
