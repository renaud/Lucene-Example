package ch.fhnw.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello Lucene: demonstate index search. try it with:<br/>
 * <li>jumps</li>
 * <li>jum, then jum*</li>
 * <li>jumsp then jumsp~</li>
 * <li>fox then fox NOT red</li>
 * @author renaud.richardet@fhnw.ch
 */
public class Searcher {

	public static void main(String[] args) throws Exception {

		IndexReader reader = IndexReader.open(FSDirectory.open(new File("helloLuceneIndex")), true);
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, Indexer.CONTENT_FIELD, analyzer);

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		while (true) {

			// prompt the user
			System.out.println("\nEnter query: ");
			String queryString = in.readLine();
			if (queryString == null || queryString.length() == -1)
				break;
			queryString = queryString.trim();
			if (queryString.length() == 0)
				break;

			Query query = parser.parse(queryString);
			TopDocs results = searcher.search(query, 10);

			System.out.println(results.totalHits + " hits for '" + queryString + "'");
			ScoreDoc[] hits = results.scoreDocs;
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				System.out.printf("%5.3f %s\n", hit.score, doc.get("text"));
			}
		}

		searcher.close();
	}
}