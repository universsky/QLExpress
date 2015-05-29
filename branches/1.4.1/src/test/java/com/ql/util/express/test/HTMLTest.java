package com.ql.util.express.test;

import org.junit.Assert;
import org.junit.Test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

public class HTMLTest {
	@Test
	public void testABC() throws Exception {
		//String express ="\"<div style=\\\"font-family:����;font-size:12px;line-height:25px;\\\">�������루\"";
		ExpressRunner runner = new ExpressRunner(true);
		String express ="\"��\\\"����\\\"��\\\"aaa-\" + 100";
		Object r = runner.execute(express, null, null, false, true);
		System.out.println(r);
		Assert.assertTrue("�ַ�����������",r.equals("��\\\"����\\\"��\\\"aaa-100"));		
	}
}