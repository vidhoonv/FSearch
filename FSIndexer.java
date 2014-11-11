import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
public class FSIndexer {

	/**
	 * @param args
	 */
	private void index(IndexWriter writer,String filesListLoc) {
		// TODO Auto-generated method stub
		
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(filesListLoc);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] parts=line.split("/");
				Field nameField = new StringField("filename",parts[parts.length-1],Field.Store.YES);
				Field pathField = new StringField("path", line, Field.Store.YES);
				System.out.println(parts[parts.length-1]);
				Document doc=new Document();
				doc.add(pathField);
				doc.add(nameField);
				writer.addDocument(doc);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void indexFiles(String filesListLoc,String indexLoc) {
		// TODO Auto-generated method stub
		 Analyzer mAnalyzer= new StandardAnalyzer();
		 IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_2, mAnalyzer);
		 conf.setOpenMode(OpenMode.CREATE);
		 IndexWriter indexWriter = null;
		 try {
			indexWriter = new IndexWriter(
			            FSDirectory.open(new File(indexLoc)), 
			            conf
			            );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 index(indexWriter,filesListLoc);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filesListLoc=new String(args[0]);
		String indexLoc=new String(args[1]);
		FSIndexer indexer = new FSIndexer();
		System.out.println("indexing begins...");
		indexer.indexFiles(filesListLoc,indexLoc);
		System.out.println("indexing complete...");
	}



}
