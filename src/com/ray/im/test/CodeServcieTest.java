package com.ray.im.test;

import java.io.IOException;

import org.junit.Test;

import com.ray.im.service.CodeServcie;

/**@desc  : 
 * 
 * @author: shirayner
 * @date  : 2017年9月27日 下午12:56:31
 */
public class CodeServcieTest {

	@Test
	public void testSendCode() {
		String mobile="17621619597";
		
		CodeServcie cs=new CodeServcie();
		boolean result=false;
		try {
			
			 result=cs.sendCode(mobile);	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result) {
			System.out.println("发送成功");
		}else {
			System.out.println("发送失败");
		}
		
		
		
	}
	
	
	
}
