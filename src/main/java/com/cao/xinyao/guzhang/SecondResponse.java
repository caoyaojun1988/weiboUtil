package com.cao.xinyao.guzhang;

import org.apache.commons.lang.StringUtils;

public class SecondResponse {
	private String input;
	private String firstUrl;
	private String secondUrl;
	private String thirdUrl;

	public static void main(String[] args) {
		SecondResponse secondResponse = new SecondResponse();
		secondResponse
				.setInput("<html><head><title>���˻�Ա</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\" />"
						+ "<script charset=\"utf-8\" src=\"http://i.sso.sina.com.cn/js/ssologin.js\"></script>"
						+ "</head>"
						+ "<body><script>try{sinaSSOController.setCrossDomainUrlList("
						+ "{'retcode':0,'arrURL':"
						+ "['http://kandian.com"
						+ "\\/logon\\/do_crossdomain.php?action=login&savestate=1332579774\","
						+ "\"http://login.t.cn\\/sinaurl/sso.json?action=login&uid=1659330205']});}catch(e){}try{sinaSSOController.crossDomainAction('login',function(){location.replace('http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack&ssosavestate=1332579774&ticket=ST-MTY1OTMzMDIwNQ==-1331974974-gz-D9F2B3CB94B308111F4826F8488CED8D&retcode=0');});}catch(e){}"
						+ "</script></body></html>");
		secondResponse.build();
	}

	public void build() {
		int arrURLIndex1 = StringUtils.indexOf(input, "kandian");
		String arrURLs1 = StringUtils.substring(input, arrURLIndex1);
		int arrURIndexEnd1 = StringUtils.indexOf(arrURLs1, "\",\"");
		String arrURL1 = StringUtils.substring(arrURLs1, 0, arrURIndexEnd1);
		arrURL1 =StringUtils.replaceChars(arrURL1, "\\/", "/");
		setFirstUrl("http://" + arrURL1);

		int arrURLIndex2 = StringUtils.indexOf(arrURLs1, "login.t.cn");
		String arrURLs2 = StringUtils.substring(arrURLs1, arrURLIndex2);
		int arrURIndexEnd2 = StringUtils.indexOf(arrURLs2, "\"]}");
		String arrURL2 = StringUtils.substring(arrURLs2, 0, arrURIndexEnd2);
		arrURL2 = StringUtils.replaceChars(arrURL2, "\\/", "/");
		setSecondUrl("http://" + arrURL2);

		int arrURLIndex3 = StringUtils.indexOf(arrURLs2, "weibo.com");
		String arrURLs3 = StringUtils.substring(arrURLs2, arrURLIndex3);
		int arrURIndexEnd3 = StringUtils.indexOf(arrURLs3, "');});}");
		String arrURL3 = StringUtils.substring(arrURLs3, 0, arrURIndexEnd3);
		setThirdUrl("http://" + arrURL3);
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getFirstUrl() {
		return firstUrl;
	}

	public void setFirstUrl(String firstUrl) {
		this.firstUrl = firstUrl;
	}

	public String getSecondUrl() {
		return secondUrl;
	}

	public void setSecondUrl(String secondUrl) {
		this.secondUrl = secondUrl;
	}

	public String getThirdUrl() {
		return thirdUrl;
	}

	public void setThirdUrl(String thirdUrl) {
		this.thirdUrl = thirdUrl;
	}

}
