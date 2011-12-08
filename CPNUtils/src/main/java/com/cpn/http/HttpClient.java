package com.cpn.http;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class HttpClient {
	final static DefaultHttpClient client = new DefaultHttpClient();
	final static ObjectMapper mapper = new ObjectMapper();

	public static String postForm(URI anAction, TreeMap<String, String> aMessage) throws IOException {
		final StringBuffer sb = new StringBuffer();
		for (final String s : aMessage.keySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(s + "=" + aMessage.get(s));
		}
		StringEntity entity;

		entity = new StringEntity(sb.toString());

		entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
		final HttpPost post = new HttpPost(anAction);
		final ResponseHandler<String> responseHandler = new BasicResponseHandler();
		post.setEntity(entity);
		final String responseBody = client.execute(post, responseHandler);
		return responseBody;
	}

	public static Map<String, Object> postJson(URI anAction, Map<String, Object> aMessage) throws JsonProcessingException, IOException {
		final HttpPost post = new HttpPost(anAction);
		final StringEntity entity = new StringEntity(mapper.writeValueAsString(aMessage));
		entity.setContentType("application/json; charset=UTF-8");
		final ResponseHandler<String> responseHandler = new BasicResponseHandler();
		post.setEntity(entity);
		final String responseBody = client.execute(post, responseHandler);
		return mapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {
		});
	}
}
