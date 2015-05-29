package com.ql.util.express.test;

import org.junit.Assert;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

public class VarAreaTest {
	@org.junit.Test
	public void testVarArea1() throws Exception{
		String express =
				" qh = 1; " +
				"��� ( ��� true �� false ���� true)  �� {" +
				"  3 + {3} + {4 + 1}" +
				" }����{" +
				" qh = 3;" +
				" qh = qh + 100;" +
				"}; " +
				"qh = qh + 1;";
		ExpressRunner runner = new ExpressRunner();
		DefaultContext<String, Object>  context = new DefaultContext<String, Object>();
		runner.addOperatorWithAlias("���", "if",null);
		runner.addOperatorWithAlias("��", "then",null);
		runner.addOperatorWithAlias("����", "else",null);
		Object r = runner.execute(express,context, null, false,false);
		System.out.println(r);
		System.out.println(context);
		Assert.assertTrue("�����������������", context.get("qh").toString().equals("104"));
		
	}	
	@org.junit.Test
	public void testVarArea2() throws Exception{
		String express =
				" qh = 1; " +
				"��� ( ��� true �� false ���� true)  �� {" +
				"  3 + {3} + {4 + 1}" +
				" }����{" +
				" def int qh = 3;" +
				" qh = qh + 100;" +
				"}; " +
				"qh = qh + 1;";
		ExpressRunner runner = new ExpressRunner();
		DefaultContext<String, Object>  context = new DefaultContext<String, Object>();
		runner.addOperatorWithAlias("���", "if",null);
		runner.addOperatorWithAlias("��", "then",null);
		runner.addOperatorWithAlias("����", "else",null);
		Object r = runner.execute(express,context, null, false,false);
		System.out.println(r);
		System.out.println(context);
		Assert.assertTrue("�����������������", context.get("qh").toString().equals("2"));
		
	}		
}