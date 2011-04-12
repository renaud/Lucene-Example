package ch.fhnw.lucene;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello Lucene: demonstrates indexing text
 * 
 * @author renaud.richardet@fhnw.ch
 */
public class Indexer {

	public static final String CONTENT_FIELD = "text";

	public static void main(String[] args) throws Exception {

		// create writer
		IndexWriter indexWriter = new IndexWriter(
				FSDirectory.open(new File("helloLuceneIndex")), 
				new StandardAnalyzer(Version.LUCENE_CURRENT), 
				true, 
				IndexWriter.MaxFieldLength.LIMITED);

		// sample content for indexing
		String[] texts = new String[] { //
				"The quick brown fox    jumps over the lazy dog",//
				"The slow  red   fox    jumps over the red dog",//
				"The quick brown rabbit jumps over the lazy dog"};

		// index
		for (String text : texts) {
			System.out.println("indexing:: " + text);
			Document doc = new Document();
			doc.add(new Field(CONTENT_FIELD, text, Field.Store.YES, Field.Index.ANALYZED));
			indexWriter.addDocument(doc);
		}
		
		// don't forget to close / optimize
		indexWriter.close();
	}
}