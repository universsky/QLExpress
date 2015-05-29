package com.ql.util.express.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;

public class DongtaiFieldTest {
	@Test
	public void testField() throws Exception{
		String express ="String �û� = \"����\";" +
				"����.�û�  = 100;" +
				"�û� = \"����\";" +
				"����.�û�  = 200;";
		
		ExpressRunner runner = new ExpressRunner(false,true);
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		Map<String,Object> fee = new HashMap<String,Object>();
		context.put("����",fee);
		InstructionSet set = runner.parseInstructionSet(express);
		InstructionSet.executeOuter(new InstructionSet[]{set},null, context, null, true, false,null, true);
		runner.execute(express, context, null, false, true);
		System.out.println(context.get("����"));
		Assert.assertTrue("��̬���Դ���",fee.get("����").toString().equals("100"));
		Assert.assertTrue("��̬���Դ���",fee.get("����").toString().equals("200"));
	}
	
}