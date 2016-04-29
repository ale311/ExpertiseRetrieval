package retrieval;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import scopusextraction.ScopusExtraction;
import tagmeextraction.TagMeExtraction;

public class Retrieval {

//	public static void main(String[] args) throws IOException {
//		// TODO Auto-generated method stub
//		Set<String> extracts = new HashSet<String>();
//		String[] author = new String[]{"Micarelli","Alessandro","Roma Tre"};
//		extracts = ScopusExtraction.estraiAbstract(author);
//		System.out.println("ho trovato un numero di documenti pari a: "+extracts.size());
//		
////		start della fase di estrazione delle parole chiave dai documenti trovati su scopus
//		Set<String> keyWords = new HashSet<String>();
////		ciclo all'interno dell'insieme di estratti e cerco le parole chiave
////		le inserisco a mucchio all'interno del mio insieme: non importa la provenienza (docuemnto)
////		l'importante è che sia relativo ad un particolare autore.
////		da chiarire l'uso del peso, sul dizionario, non sulle parole chiave all'interno dei paper
//		
//		for(String s : extracts){
//			for(String kw : TagMeExtraction.getJsonDocument(s)){
//				keyWords.add(kw);
//				
//			}
//		}
//		for(String s : keyWords){
//			System.out.println(s);
//		}
//	}

}
