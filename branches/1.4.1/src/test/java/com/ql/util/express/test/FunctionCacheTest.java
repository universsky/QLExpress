package com.ql.util.express.test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.FuncitonCacheManager;

public class FunctionCacheTest {
	@org.junit.Test
	public void testFunctionCallCache() throws Exception {
		String express = " cache isVIP(\"qh\") ;  cache isVIP(\"xuannan\"); cache isVIP(\"qh\") ;"
				+ " cache example.unionName(\"李四\") ;"
				+ " return cache example.unionName(\"李四\") ;";
		ExpressRunner runner = new ExpressRunner();
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		context.put("example", new BeanExample("张三"));
		runner.addFunctionOfClassMethod("isVIP", BeanExample.class.getName(),
				"isVIP", new String[] { "String" }, "$1不是VIP用户");
		FuncitonCacheManager mananger = new FuncitonCacheManager();
		Object r = runner.execute(express,context, null, false,false);
		System.out.println(r);
		System.out.println(context);
		System.out.println(((BeanExample) context.get("example")).child.a);
		System.out.println(mananger.functionCallCache);
	}
}
