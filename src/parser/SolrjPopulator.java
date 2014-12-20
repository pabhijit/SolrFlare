package parser;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;

public class SolrjPopulator {
	static SolrServer solr=null;
	public static void main(String[] args) throws IOException, SolrServerException {

		try {
			NYDocument doc=null;
			int filesindexed=0;
			Map<Integer,NYDocument> docMap=null;
			String urlString = "http://localhost:8983/solr";
			solr = new HttpSolrServer(urlString);
			String ipDir = "C:\\Users\\Harsh\\Desktop\\NyTimes\\extracteddata\\06\\06";
			File ipDirectory = new File(ipDir);
			String[] catDirectories = ipDirectory.list();
			String[] files;
			File dir;
			try {
				System.out.println("Processing..");
				for (String cat : catDirectories) {
					dir = new File(ipDir + File.separator + cat);
					files = dir.list();
					if (files == null) {
						continue;
					}
					for (String f : files) {
						docMap=parseXMLDocument(dir.getAbsolutePath() + File.separator + f);
						doc=docMap.get(MyHandler.docIdCount);
						//System.out.println("\nDocId-->"+doc.getDocId()+"\nCategory-->"+doc.getCategory()+"\nDate-->"+doc.getDate()+"\nSnippet-->"+doc.getSnippet()+"\nDescription-->"+doc.getDescription()+"\nTitle-->>"+doc.getTitle()+"\nUrl-->"+doc.getUrl());
						MyHandler.docIdCount++;
						SolrInputDocument document = new SolrInputDocument();
						document.addField("id", doc.getDocId());
						document.addField("title",doc.getTitle());
						document.addField("url", doc.getUrl());	
						document.addField("snippet", doc.getSnippet());
						document.addField("description", doc.getDescription());
						document.addField("date", toUtcDate(doc.getDate()));
						document.addField("category",doc.getCategory());
						document.addField("src",doc.getSource());
						filesindexed++;
						System.out.println("\nFiles Indexed-->"+filesindexed);
						try {
							UpdateResponse response = solr.add(document);
						} catch (SolrServerException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
						try {
							solr.commit();
						} catch (SolrServerException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String toUtcDate(String dateStr) {
		Date date;
		try {
			date = new SimpleDateFormat("yyyyMMdd'T'HHmmss").parse(dateStr);
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Map<Integer,NYDocument> parseXMLDocument(String fileName)
	{  
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setValidating(false);
		Map<Integer,NYDocument> docMap=null;
		try {
			File file=new File(fileName);
			SAXParser saxParser = saxParserFactory.newSAXParser();
			final XMLReader xmlParser = saxParser.getXMLReader();
			xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			xmlParser.setFeature("http://xml.org/sax/features/validation", false);
			MyHandler handler = new MyHandler();
			saxParser.parse(file, handler);
			docMap = handler.getDocMap();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return docMap;
	}
}


