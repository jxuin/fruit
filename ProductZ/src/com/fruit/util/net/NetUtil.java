package com.fruit.util.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

@SuppressWarnings("rawtypes")
public class NetUtil {
	
	public static String AcceptEncoding = "gzip,deflate,sdch";
	
	public static String Accept = "image/gif, image/jpeg, image/pjpeg, image/pjpeg, video/mpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*";

	public static HttpClient getHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		HttpConnectionParams.setSoTimeout(httpParameters, 12000);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		return httpClient;
	}

	public static String getHtmlHttpGet(String urlStr, HashMap params) {
		
		HttpClient httpClient = getHttpClient();

		StringBuilder urlSb = new StringBuilder(urlStr);
		
		if (params != null) {
			Iterator iterator = params.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = (String) params.get(key);
				urlSb.append(key).append("=").append(value).append("&");
			}
			urlStr = urlSb.toString();
			if (urlStr.lastIndexOf("&") == (urlStr.length() - 1)) {
				urlStr = urlStr.substring(0, urlStr.length() - 1);
			}
		}
//		SysLog.v("urlStr:" + urlStr);
		HttpGet httpGet = new HttpGet(urlStr);
		httpGet.setHeader("Accept-Encoding", AcceptEncoding);
		String requestStr = "";
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				requestStr = retrieveEntity(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return requestStr;
	}

	public static String getHtmlHttpPost(String urlStr, HashMap params) {
		HttpClient httpClient = getHttpClient();
		
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		if (params != null) {

			Iterator iterator = params.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = (String) params.get(key);
				postParams.add(new BasicNameValuePair(key, value));
			}
		}
		HttpPost httpPost = new HttpPost(urlStr);
		httpPost.setHeader("Accept-Encoding", AcceptEncoding);
		String requestStr = "";
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				requestStr = retrieveEntity(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return requestStr;
	}

	public static String retrieveEntity(HttpEntity httpEntity)
			throws IOException {
		Header header = httpEntity.getContentEncoding();
		InputStream in = null;
		if (header != null && header.getValue().equalsIgnoreCase("gzip")) {
			GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(
					httpEntity);
			in = gzipEntity.getContent();
		} else {
			in = httpEntity.getContent();
		}
		String requestStr = getHTMLContent(in);
		if (in != null) {
			in.close();
		}
		return requestStr;
	}

	public static String getHTMLContent(InputStream in) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
