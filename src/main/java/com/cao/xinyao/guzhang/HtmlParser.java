package com.cao.xinyao.guzhang;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

public class HtmlParser {
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		File input = new File("/home/caoyaojun/workspace/weiboUtil/test.html");
		// ArrayList<String> list = parser();
		// System.out.println(list);
	}

	public static ArrayList<String> parser(String input)
			throws ParserConfigurationException, SAXException, IOException {
		ArrayList<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(input);
		Element content = doc.getElementById("pl_userList");
		Elements links = content.select("a[action-type=follow]");
		for (Element link : links) {
			String uid = link.attr("uid");
			list.add(uid);
			System.out.println(uid + " be add");
		}
		return list;
	}
}
