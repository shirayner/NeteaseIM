package com.ray.im.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ray.im.util.CheckSumBuilder;
import com.ray.im.util.YunXinParameterUtil;

/**@desc  : 
 * 
 * @author: shirayner
 * @date  : 2017年9月26日 下午5:22:17
 */
public class CodeServcie {
	//发送验证码的请求路径URL
	private static final String SERVER_URL="https://api.netease.im/sms/sendcode.action";

	public Boolean sendCode(String mobile) throws ClientProtocolException, IOException {

		//1.开启http连接
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(SERVER_URL);

		//2.设置请求的参数，Header
		//当前时间戳
		String curTime = String.valueOf((new Date()).getTime() / 1000L);
		//类似微信的签名：参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
		String checkSum = CheckSumBuilder.getCheckSum(YunXinParameterUtil.APP_SECRET, YunXinParameterUtil.NONCE, curTime);

		// 2.2 设置请求的header
		httpPost.addHeader("AppKey", YunXinParameterUtil.APP_KEY);
		httpPost.addHeader("Nonce", YunXinParameterUtil.NONCE);
		httpPost.addHeader("CurTime", curTime);
		httpPost.addHeader("CheckSum", checkSum);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// 3. 设置请求的的参数，requestBody参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		/*
		 * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
		 * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
		 * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
		 */
		nvps.add(new BasicNameValuePair("templateid", YunXinParameterUtil.TEMPLATEID));
		nvps.add(new BasicNameValuePair("mobile", mobile));
		nvps.add(new BasicNameValuePair("codeLen", YunXinParameterUtil.CODELEN));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		// 4.执行请求
		CloseableHttpResponse response = httpClient.execute(httpPost);


		/*
		 * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
		 * 2.具体的code有问题的可以参考官网的Code状态表
		 */

		String responseEntity = EntityUtils.toString(response.getEntity(), "utf-8");

		JsonObject responseEntityJsonObject = new JsonParser().parse(responseEntity).getAsJsonObject();
		String code=responseEntityJsonObject.get("code").getAsString();
		System.out.println(responseEntityJsonObject.toString());
		
		if (code.equals("200")){
			return true;
		}
		
		return false;


	}


}
