package parser;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyHandler extends DefaultHandler {

	private Map<Integer,NYDocument> docMap = null;
	private NYDocument doc = null;
	static int docIdCount=0;
	public Map<Integer,NYDocument> getDocMap() {
		return docMap;
	}
	String snippetString = "";
	String descriptionString = "";
	boolean dTitle = false;
	boolean dSnippet = false;
	boolean dDescription = false;
	boolean dSnippetPTag = false;
	boolean dDescriptionPTag = false;
	boolean afterFlag = false;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {

		if (qName.equalsIgnoreCase("nitf")) {
			if (docMap == null)
				docMap = new HashMap<Integer,NYDocument>();
			doc = new NYDocument();
		}	//create a new Employee and put it in Map
		if(qName.equalsIgnoreCase("meta"))
		{
			if(attributes.getValue("name").equalsIgnoreCase("online_sections"))
			{
				doc.setCategory(attributes.getValue("content").trim());
			}
		}
		if(qName.equalsIgnoreCase("doc-id"))
		{
			doc.setDocId(attributes.getValue("id-string").trim());
		}
		if(qName.equalsIgnoreCase("doc.copyright"))
		{
			doc.setSource(attributes.getValue("holder").trim());
		}
		if(qName.equalsIgnoreCase("pubdata"))
		{
			doc.setUrl(attributes.getValue("ex-ref").trim());
			doc.setDate(attributes.getValue("date.publication").trim());
		}
		if(qName.equalsIgnoreCase("title"))
		{
			dTitle=true;
		}
		if(qName.equalsIgnoreCase("block"))
		{
			if(attributes.getValue("class").equalsIgnoreCase("lead_paragraph"))
			{
				dSnippet=true;
			}
		}
		if(dSnippet && qName.equalsIgnoreCase("p"))
		{
			dSnippetPTag=true;
		}
		if(qName.equalsIgnoreCase("block"))
		{
			if(attributes.getValue("class").equalsIgnoreCase("full_text"))
				dDescription=true;
		}
		if(dDescription && qName.equalsIgnoreCase("p"))
		{
			dDescriptionPTag=true;
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("nitf")) {
			docMap.put(docIdCount, doc);
		}
		if (qName.equalsIgnoreCase("block")) {
			dSnippet=false;
			doc.setSnippet(snippetString);
			afterFlag=true;
		}
		if (qName.equalsIgnoreCase("block") && afterFlag) {
			dDescription=false;
			doc.setDescription(descriptionString);
		}

	}
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		if (dTitle) {
			doc.setTitle(new String(ch, start, length).trim());
			dTitle = false;
		} 
		if (dSnippetPTag) {
			snippetString=snippetString+new String(ch, start, length).trim();
			dSnippetPTag=false;
		}
		if (dDescriptionPTag) {
			descriptionString=descriptionString+new String(ch, start, length).trim();
			dDescriptionPTag=false;
		}
	}
}