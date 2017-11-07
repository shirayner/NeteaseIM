package com.ray.im.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.ray.im.util.CheckSumBuilder;


/**@desc  : 发送验证码
 * 
 * @author: shirayner
 * @date  : 2017年9月26日 下午4:17:13
 */
public class SendCaptcha2 {
	//发送验证码的请求路径URL
	private static final String SERVER_URL="https://api.netease.im/sms/sendcode.action";
	//网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
	private static final String APP_KEY="09d5244d6a148b41b55062a4f1e77d17";
	//网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
	private static final String APP_SECRET="936c831b1704";
	//随机数
	private static final String NONCE="123456";
	//短信模板ID
	private static final String TEMPLATEID="3119062";
	//手机号
	private static final String MOBILE="17612106091";
	//验证码长度，范围4～10，默认为4
	private static final String CODELEN="6";

	
	public static void main(String[] args) throws Exception {
		//1.开启http连接
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(SERVER_URL);

		//2.获取请求参数
		//当前时间戳
		String curTime = String.valueOf((new Date()).getTime() / 1000L);
		/*
		 * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
		 */
		String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);

		// 3.设置请求参数
		// 3.1 设置请求的header
		httpPost.addHeader("AppKey", APP_KEY);
		httpPost.addHeader("Nonce", NONCE);
		httpPost.addHeader("CurTime", curTime);
		httpPost.addHeader("CheckSum", checkSum);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// 3.2 设置请求的的参数，requestBody参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		/*
		 * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
		 * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
		 * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
		 */
		nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
		nvps.add(new BasicNameValuePair("mobile", MOBILE));
		nvps.add(new BasicNameValuePair("codeLen", CODELEN));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		// 4.执行请求
		CloseableHttpResponse response = httpClient.execute(httpPost);
		
		
		/*
		 * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
		 * 2.具体的code有问题的可以参考官网的Code状态表
		 */
		//System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
		
		 String responseEntity = EntityUtils.toString(response.getEntity(), "utf-8");


		 System.out.println(responseEntity);
		 
		 
		response.close();
		httpClient.close();

	}
}