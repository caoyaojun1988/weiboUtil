package com.cao.xinyao.guzhang;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSON;

public class WeiboClient {
	private final ArrayList<Cookie> cookieList = new ArrayList<Cookie>();
	private static final String name = "286982920@qq.com";
	private static final String pwd = "1234554321";

	public static void main(String[] args) throws Exception {

		WeiboClient weiboClient = new WeiboClient();
		weiboClient.login(WeiboClient.name, WeiboClient.pwd);
		HttpClient client = new HttpClient();
		for (int page = 1; page < 1000; page++) {
			System.out.println("page "+page);
            // 浣跨敤 GET 鏂规硶
            HttpMethod method = new GetMethod(
					"http://s.weibo.com/user/&type=school&school=%25E6%25B5%2599%25E6%25B1%259F%25E4%25BC%25A0%25E5%25AA%2592%25E5%25AD%25A6%25E9%2599%25A2&page="
							+ page);

//			method.setRequestHeader("Accept",					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			method.setRequestHeader("Accept-Charset",				 	"GB2312,utf-8;q=0.7,*;q=0.7");
//			method.setRequestHeader("Accept-Encoding", "gzip, deflate");
//		 	method.setRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			method.setRequestHeader("Connection", "keep-alive");
			method.setRequestHeader("Content-Type",					"application/x-www-form-urlencoded; charset=UTF-8");
			method.setRequestHeader("Cookie",					CookieUtil.getCookie(weiboClient.cookieList));
			method.setRequestHeader("Host", "	s.weibo.com");
			method.setRequestHeader("User-Agent",					"	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0) Gecko/20100101 Firefox/8.0");

			client.executeMethod(method);
            InputStream input = method.getResponseBodyAsStream();

			StringBuffer out = new StringBuffer();
			byte[] b = new byte[1024];
			for (int n; (n = input.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			System.out.println("the " + page
					+ " time request , the response is " + out.toString());
			ArrayList<String> uids = HtmlParser.parser(out.toString());

			Cookie[] cookies = client.getState().getCookies();
			weiboClient.cookieList.addAll(Arrays.asList(cookies));

			for (String uid : uids) {
				Thread.sleep(1 * 60 * 1000);
				System.out.println("begin uid=" + uid);
				PostMethod postMethod = new PostMethod(
						"http://s.weibo.com/ajax/follow.php?type=followed");
//				postMethod
//						.setRequestHeader("Accept",
//								"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//				postMethod.setRequestHeader("Accept-Charset",
//						"GB2312,utf-8;q=0.7,*;q=0.7");
//				postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
//				postMethod
//						.setRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
				postMethod.setRequestHeader("Cache-Control", "no-cache");
				postMethod.setRequestHeader("Connection", "keep-alive");
				postMethod.setRequestHeader("Content-Length", "19");
				postMethod.setRequestHeader("Content-Type",
						"application/x-www-form-urlencoded; charset=UTF-8");
				postMethod.setRequestHeader("Cookie",
						CookieUtil.getCookie(weiboClient.cookieList));
				postMethod.setRequestHeader("Host", "	s.weibo.com");
				postMethod.setRequestHeader("Pragma", "no-cache");
				postMethod
						.setRequestHeader("Referer",
								"	http://s.weibo.com/user/%25E6%25B5%2599%25E6%25B1%259F&Refer=weibo_user");
				postMethod
						.setRequestHeader("User-Agent",
								"	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0) Gecko/20100101 Firefox/8.0");
				postMethod.setRequestHeader("X-Requested-With",
						"XMLHttpRequest");

				postMethod.addParameter("_t", "0");
				postMethod.addParameter("uid", uid);
				client.executeMethod(postMethod);
				String result  = postMethod.getResponseBodyAsString();
				System.out.println("this uid "+uid+" result="+result);
				postMethod.releaseConnection();

			}
            method.releaseConnection(); // 閲婃斁杩炴帴
		}
	}

	private Cookie[] login(String email, String passwd) throws IOException,
			NoSuchAlgorithmException {
		HttpClient client = new HttpClient();
        // 绗竴姝et
        // url=http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su="+浣犻�杩嘊ASE64鍔犲瘑鐨勭敤鎴峰悕+"&client=ssologin.js(v1.3.19)
		String usernameBase64 = new BASE64Encoder().encode(email
				.getBytes("UTF-8"));
		PostMethod prePostMethod = new PostMethod(
				"http://login.sina.com.cn/sso/prelogin.php?entry=miniblog&callback=sinaSSOController.preloginCallBack&user="
						+ usernameBase64 + "&client=ssologin.js(v1.3.19)");
		client.executeMethod(prePostMethod);
		String response = prePostMethod.getResponseBodyAsString();
		String reString = StringUtils.substring(response,
				"sinaSSOController.preloginCallBack({".length() - 1,
				response.length() - 1);
		PrePostResponse prePostResponse = JSON.parseObject(reString,
				PrePostResponse.class);
		prePostMethod.releaseConnection();
		System.out.println("login step  one " + response);
        // 绗簩姝ha1鍔犲瘑涓ら亶瀵嗙爜鍚庡姞涓婁笂涓�杩斿洖鐨剆ervertime鍜宯once锛屽啀鐢╯ha1鍔犲瘑涓�亶銆俻ost
        // 杩欎釜鍦板潃http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.19)锛屽彇cookie鍜岃繑鍥炲�
		PostMethod post = new PostMethod(
				"http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.19)");
		post.setRequestHeader("User-Agent",
				"Mozilla/5.0 (X11; Linux i686; rv:5.0) Gecko/20100101 Firefox/5.0");
		post.setRequestHeader("Referer", "http://weibo.com/");
		post.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		post.addParameter("entry", "weiblog");
		post.addParameter("gateway", "1");
		post.addParameter("from", "");
		post.addParameter("savestate", "7");
		post.addParameter("useticket", "1");
		post.addParameter("ssosimplelogin", "1");
		post.addParameter("su", usernameBase64);
		post.addParameter("service", "miniblog");
		post.addParameter("servertime", prePostResponse.getServertime());
		post.addParameter("nonce", prePostResponse.getNonce());
		post.addParameter("pwencode", "wsse");

		String sp = SHA1Utils.hex_sha1(SHA1Utils.hex_sha1(SHA1Utils
				.hex_sha1(passwd))
				+ prePostResponse.getServertime()
				+ prePostResponse.getNonce());
		post.addParameter("sp", sp);
		post.addParameter("client", "ssologin.js(v1.3.19)");
		post.addParameter("encoding", "utf-8");
		post.addParameter(
				"url",
				"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
		post.addParameter("returntype", "META");
		post.addParameter("vsnf", "1");
		post.addParameter("vsnval", "");

		client.executeMethod(post);
		String secondReponse = post.getResponseBodyAsString();
		post.releaseConnection();
		System.out.println("login step  two " + secondReponse);
        // 绗笁姝�get
		// http://kandian.com/logon/do_crossdomain.php?action=login&savestate="+G_cookies.get("savestate")+"&callback=sinaSSOController.doCrossDomainCallBack&scriptId=ssoscript0&client=ssologin.js(v1.3.16)
        // 杩欎竴姝ヤ綘鍙互涓嶅彇鍊硷紝浣嗘渶濂紾ET涓�笅
        SecondResponse secondResponse = new SecondResponse();
		secondResponse.setInput(secondReponse);
		secondResponse.build();
		GetMethod firstGetMethod = new GetMethod(secondResponse.getFirstUrl());
		client.executeMethod(firstGetMethod);
		String firstGetReponse = firstGetMethod.getResponseBodyAsString();
		firstGetMethod.releaseConnection();
		System.out.println("login step  three " + firstGetReponse);
        // 绗洓姝�get http://login.t.cn/sinaurl/sso.json?action=login&uid=
		// "+G_cookies.get("
		// uid")+"&callback=sinaSSOController.doCrossDomainCallBack
        // &scriptId=ssoscript1&client=ssologin.js(v1.3.19) 杩欎竴姝ヤ篃鍙互涓嶅彇鍊硷紝浣嗚繕鏄疓ET涓�笅

		GetMethod secondGetMethod = new GetMethod(secondResponse.getSecondUrl());
		client.executeMethod(secondGetMethod);
		String secondGetReponse = secondGetMethod.getResponseBodyAsString();
		secondGetMethod.releaseConnection();
		System.out.println("login step  four " + secondGetReponse);
        /**
         * 绗簲姝�get http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent. sinaSSOController
         * .feedBackUrlCallBack&ssosavestate="+G_cookies.get("ssosavestate ")+"&ticket="+G_cookies.get("ticket")
         * 鍙朇OOKIE鍜岃繑鍥炲� 鎵�湁SOOKIE鍜岃繑鍥炲�鍩烘湰鍙栧畬
         */
		GetMethod thirdGetMethod = new GetMethod(secondResponse.getThirdUrl());
		client.executeMethod(thirdGetMethod);
		String thirdGetReponse = thirdGetMethod.getResponseBodyAsString();
		thirdGetMethod.releaseConnection();
		System.out.println("login step  five " + thirdGetReponse);
        /**
         * 鍙互璁块棶http://weibo.com/"+G_cookies.get("userdomain")浜嗐� 鐧诲綍鏈�富瑕佺殑鍑犱釜鍊�ALF NSC_wjq_xfjcp.dpn_ipnfqbhf
         * SSOLoginState //杩欎釜闈炲父閲嶈 SUE SUP //杩欎笁涓�鍩烘湰鍙烦杞叾浠栭〉闈�un //浣犵殑鐢ㄦ埛ID锛屽彇涓嶅彇锛屼綘閮界煡閬�wvr //鐗堟湰鍙�get 鍚庡啀鍙朣OOKIE
         * U_TRS1 U_TRS2 NSC_wjq_xfjcp.dpn_w3.6_w4 杩欎笁涓�涓嶇煡閬撴槸骞蹭粈涔堢殑锛屼笉杩囧叾浠栭〉闈㈡湁鏃朵細鍒锋柊COOKIE 杩樻湁鍑犱釜鍊间及璁℃槸JS绠楀嚭鏉ョ殑
         * Apache NSC_wjq_wpuf.u.tjob.dpn.do SINAGLOBAL ULV 鏃㈢劧鏀惧嚭鏉ヨ嚜鐒舵湁鐢紝鍦ㄦ煡JS HttpClient鏃㈢劧鑳界櫥褰�JSOUP涔熻兘锛屾柟渚垮浜�
         */
		Cookie[] cookies = client.getState().getCookies();
		cookieList.addAll(Arrays.asList(cookies));
		System.out.println("login  success..cookie =" + cookieList);
		return cookies;
	}
}
