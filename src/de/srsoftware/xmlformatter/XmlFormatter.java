package de.srsoftware.xmlformatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import de.srsoftware.tools.translations.Translations;

public class XmlFormatter {

	public static Vector<String> formatDocument(Vector<String> documentLines, int tabWidth) {
		int l = documentLines.size();
		int einr = 0;
		for (int i = 0; i < l; i++) {
			String s = documentLines.get(i);
			if (s.startsWith("</")) einr -= tabWidth;
			documentLines.set(i, createSpaces(einr) + s);
			if (s.startsWith("<") && !s.startsWith("</") && !s.endsWith("/>")) einr += tabWidth;
		}
		return documentLines;
	}

	public static String loadDocument(URL url) throws IOException {		
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuffer resultBuffer = new StringBuffer();
		String line;
		while ((line = br.readLine())!=null){
		  resultBuffer.append(line+"\n");
		}		
		return resultBuffer.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String doc = loadDocument(new URL("file:///home/srichter/eclipse/SBWTools/webTools/data/example network.xml"));
			Vector<String> docLines = parseDocument(doc);
			docLines = formatDocument(docLines, 2);
			writeDocument(docLines,"/home/srichter/eclipse/SBWTools/webTools/data/example network.xml");
			System.out.println(_("done."));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Vector<String> parseDocument(String documentAsString) {
		boolean inText1 = false;
		boolean inText2 = false;
		int i = 0;
		int l = documentAsString.length();
		StringBuffer line = new StringBuffer();
		Vector<String> result = new Vector<String>();
		while (i < l) {
			char c = documentAsString.charAt(i);
			if (c == '"' && !inText2) inText1 = !inText1;
			if (c == '\'' && !inText1) inText2 = !inText2;
			line.append(c);
			if (c == '>' && !inText1 && !inText2) {
				String ln = line.toString().trim();
				if (!ln.startsWith("<")) {
					int k = ln.indexOf("<");
					result.add(ln.substring(0, k).trim());
					ln = ln.substring(k);
				} 
				result.add(ln.trim());
				line = new StringBuffer();
			}
			i++;
		}
		return result;
	}

	public static void writeDocument(Vector<String> document, String filename) {
    BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter (filename));
			for (int i = 0; i < document.size(); i++)	bw.write(document.get(i)+"\n");
	    bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static String _(String text) { 
		return Translations.get(text);
	}

	private static String createSpaces(int num) {
		if (num == 0) return "";
		String result = createSpaces(num / 2);
		result += result + ((num % 2 == 1) ? " " : "");
		return result;
	}

}
