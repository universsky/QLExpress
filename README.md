# QLExpress
QLExpress

QLExpress的特性支持：

1、编译执行：

编译生成基础指令后执行，性能能得到基本保障。执行过程：单词分解-->单词类型分析-->语法分析-->生成运行期指令集合-->执行生成的指令集合

runner.execute("10 * 10 + 1 + 2 * 3 + 5 * 2", null, true,null); 

最后生成的指令：

	1:LoadData 10
	2:LoadData 10
	3:OP : * OPNUMBER[2] 
	4:LoadData 1
	5:OP : + OPNUMBER[2] 
	6:LoadData 2
	7:LoadData 3
	8:OP : * OPNUMBER[2] 
	9:OP : + OPNUMBER[2] 
	10:LoadData 5
	11:LoadData 2
	12:OP : * OPNUMBER[2] 
	13:OP : + OPNUMBER[2]
	
	
2、支持标准的java语法、JAVA运算符号和关键字
import：引入一个包或者类，例如：import java.util.*;需要放在脚本的最前面
new:创建一个对象，例如：new ArrayList();
for:操作符号
if:操作符号
then:操作符号
else:操作符号
break: 终止循环
continue: 绩效循环
return: 返回

     A、四则运算 : 10 * 10 + 1 + 2 * 3 + 5 * 2
     B、boolean运算: 3 > 2 and 2 > 3
     C、创建对象，对象方法调用，静态方法调用:new com.ql.util.express.test.BeanExample("张三").unionName("李四")
     D、变量赋值：a = 3 + 5
     E、支持 in,max,min:  (a in (1,2,4)) and (b in("abc","bcd","efg"))
     
     
3、自定义的关键字

include:在一个表达式中引入其它表达式。

例如： include com.taobao.upp.b; 

资源的转载可以自定义接口IExpressResourceLoader来实现，缺省是从文件中装载

[]:匿名创建数组.int[][] abc = [[11,12,13],[21,22,23]];
NewMap:创建HashMap. Map abc = NewMap(1:1,2:2);Map abc = NewMap("a":1,"b":2)
NewList:串接ArrayList.List abc = NewList(1,2,3);
exportDef: 将局部变量转换为全局变量，例如：exportDef long userId
alias:创建别名，例如： alias 用户ID user.userId
exportAlias: 创建别名，并转换为全局别名
macro: 定义宏，例如： macro 降级  {level = level - 1}
function: 定义函数，例如： function add(int a,int b){  return a+b; };
in: 操作符号，例如： 3 in (3,4,5)
mod:操作符号，例如：  7 mod 3 
like:操作符号，例如： "abc" like 'ab%'


4、自定义的系统函数,后续还会不断的添加
   max:取最大值max(3,4,5)
   min:最最小值min(2,9,1)
   round:四舍五入round(19.08,1)
   print:输出信息不换行print("abc")
   println:输出信息并换行 println("abc")
   
5、提供表达式上下文，属性的值不需要在初始的时候全部加入，而是在表达式计算的时候，需要什么信息才通过上下文接口获取。
避免因为不知道计算的需求，而在上下文中把可能需要的数据都加入。 
runner.execute("三星卖家 and 消保用户",errorList,true,expressContext) "三星卖家"和"消保用户"的属性是在需要的时候通过接口去获取。

6、可以将计算结果直接存储到上下文中供后续业务使用。例如：
  runner.execute("c = 1000 + 2000",errorList,true,expressContext); 
  则在expressContext中会增加一个属性c=3000，也可以在expressContext实现直接的数据库操作等。
  
7、支持高精度浮点运算，只需要在创建执行引擎的时候指定参数即可：new ExpressRunner(true,false);

8、可以将Class和Spring对象的方法映射为表达式计算中的别名，方便其他业务人员的立即和配置。例如
     将 Math.abs() 映射为 "取绝对值"。
      runner.addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs",new String[] { "double" }, null); 
      runner.execute("取绝对值(-5.0)",null,true,null); 
      
9、可以为已经存在的boolean运算操作符号设置别名，增加错误信息同步输出，在计算结果为false的时候，同时返回错误信息,减少业务系统相关的处理代码
   runner.addOperatorWithAlias("属于", "in", "用户$1不在允许的范围")。
   用户自定义的函数同样也可以设置错误信息：例如： 
   runner.addFunctionOfClassMethod("isOk", BeanExample?.class.getName(),"isOk", new String[] { "String" }, "$1 不是VIP用户"); 
  则在调用:
  
     List errorList = new ArrayList?(); 
     Object result =runner.execute("( (1+1) 属于 (4,3,5)) and isOk("玄难")",errorList,true,null); 
     执行结果 result = false.同时在errorList中还会返回2个错误原因： 
         1、"用户 2 不在允许的范围"
         2、玄难 不是VIP用户 
         
10、可以自定义函数,自定一个操作函数 group

class GroupOperator extends Operator {
	public GroupOperator(String aName) {
		this.name= aName;
	}
	public Object executeInner(Object[] list)throws Exception {
		Object result = new Integer(0);
		for (int i = 0; i < list.length; i++) {
			result = OperatorOfNumber.Add.execute(result, list[i]);
		}
		return result;
	}
}

则执行：
     runner.addFunction("累加", new GroupOperator("累加"));
     runner.addFunction("group", new GroupOperator("group"));
    //则执行：group(2,3,4)  = 9 ,累加(1,2,3)+累加(4,5,6)=21
    
11、可以自定操作符号。自定义的操作符号优先级设置为最高。例如自定一个操作函数 love：
class LoveOperator extends Operator {	
	public LoveOperator(String aName) {
		this.name= aName;
	}
	public Object executeInner(Object[] list)
			throws Exception {
		String op1 = list[0].toString();
		String op2 = list[1].toString();
		String result = op2 +"{" + op1 + "}" + op2;		
		return result;
	}
}

然后增加到运算引擎：
 runner.addOperator("love", new LoveOperator("love"));
    //则执行：'a' love 'b' love 'c' love 'd' = "d{c{b{a}b}c}d"
    
12、可以重载已有的操作符号。例如替换“＋”的执行逻辑。参见：com.ql.util.express.test.ReplaceOperatorTest

13、可以延迟运算需要的数据

14、一个脚本可以调用其它脚本定义的宏和函数.参见com.ql.util.express.test.DefineTest

15、可以类似VB的语法来使用操作符号和函数。print abc; 等价于 print(abc).参见 com.ql.util.express.test.OpCallTest

16、支持类定义

17、对 in 操作支持后面的是一个数组或者List变量义[完成2012-06-01]



QLExpress 实现了一个功能丰富、扩展性极高的编译执行器，实现了一个完整的词法分析，语法分析，语义分析，编译指令，指令执行的过程。

在动态脚本满天飞的年代，有很多相对成熟的脚本解析工具，groovy，ruby，python... ...，淘宝开源工具 qlexpress作为一个淘宝开源的项目，也有它很多独特之处。

QLExpress 是一个开放的脚本处理工具，它开放了很多api扩展定义接口，使很多开发团队进行简单的二次开发后，就可以满足很多复杂的业务需求。

在淘宝的开源项目中，这个项目也开源了。

这个是源代码地址 ：http://code.taobao.org/p/QLExpress/src/ （导入的时候安装maven插件http://maven.apache.org/）

 wiki百科以及特性的描述：  http://code.taobao.org/p/QLExpress/wiki/qlexpress-feature/
 
 
  QLExpress脚本语言技术讲解（2） -----QL的基本执行过程
  
分类： QL语言 2012-02-20 22:33 1102人阅读 评论(0) 收藏 举报
脚本语言integerexpressexceptionfunction

[java] view plaincopy
@org.junit.Test  
public void testDemo() throws Exception{  
 String express = "10 * 10 + 1 + 2 * 3 + 5 * 2";  
 ExpressRunner runner = new ExpressRunner();  
 Object r = runner.execute(express,null, null, false,false);  
 Assert.assertTrue("表达式计算", r.toString().equalsIgnoreCase("117"));  
 System.out.println("表达式计算：" + express + " = " + r);  
}  

安装好maven 执行eclipse命令，导入eclipse 之后，运行 com.ql.util.express.test.ExpressTest 单元测试


可以运行处结果：

表达式计算：10 * 10 + 1 + 2 * 3 + 5 * 2 = 117



[java] view plaincopy
/** 
 * 执行一段文本 
 * @param expressString 程序文本 
 * @param context 执行上下文 
 * @param errorList 输出的错误信息List 
 * @param isCache 是否使用Cache中的指令集 
 * @param isTrace 是否输出详细的执行指令信息 
 * @return 
 * @throws Exception 
 */  
  
public Object execute(String expressString, IExpressContext<String,Object> context,  
List<String> errorList, boolean isCache, boolean isTrace) throws Exception ;  

我们为了要看到运行的过程，修改把ExpressRunner的构造函数，以及execute的参数中： isTrace =true，重新执行下这个单元测试。



[java] view plaincopy
@org.junit.Test  
public void testDemo() throws Exception{  
String express = "10 * 10 + 1 + 2 * 3 + 5 * 2";  
ExpressRunner runner = new ExpressRunner(false,true); // 显示执行编译过程  
Object r = runner.execute(express,null, null, false,true); // 显示指令执行过程  
Assert.assertTrue("表达式计算", r.toString().equalsIgnoreCase("117"));  
System.out.println("表达式计算：" + express + " = " + r);  
}  



日志信息：


[plain] view plaincopy
[2012-02-20 22:07:07,844] [main] (ExpressParse.java:486) DEBUG com.ql.util.express.parse.ExpressParse -   
执行的表达式:10 * 10 + 1 + 2 * 3 + 5 * 2  
[2012-02-20 22:07:07,844] [main] (ExpressParse.java:487) DEBUG com.ql.util.express.parse.ExpressParse -   
单词分解结果:{10},{*},{10},{+},{1},{+},{2},{*},{3},{+},{5},{*},{2}  
[2012-02-20 22:07:07,844] [main] (ExpressParse.java:491) DEBUG com.ql.util.express.parse.ExpressParse -   
预处理后结果:{10},{*},{10},{+},{1},{+},{2},{*},{3},{+},{5},{*},{2}  
[2012-02-20 22:07:07,860] [main] (ExpressParse.java:502) DEBUG com.ql.util.express.parse.ExpressParse -   
单词分析结果:  
10:CONST_INTEGER,*:*,10:CONST_INTEGER,+:+,1:CONST_INTEGER,+:+,  
2:CONST_INTEGER,*:*,3:CONST_INTEGER,+:+,5:CONST_INTEGER,*:*,  
2:CONST_INTEGER  
[2012-02-20 22:07:07,860] [main] (ExpressParse.java:506) DEBUG com.ql.util.express.parse.ExpressParse -   
Block拆分后的结果:  
1:   main:FUNCTION_DEFINE                                                         FUNCTION_DEFINE  
2:      10:CONST_INTEGER CONST  
2:      *:* *  
2:      10:CONST_INTEGER CONST  
2:      +:+ +  
2:      1:CONST_INTEGER CONST  
2:      +:+ +  
2:      2:CONST_INTEGER CONST  
2:      *:* *  
2:      3:CONST_INTEGER CONST  
2:      +:+ +  
2:      5:CONST_INTEGER CONST  
2:      *:* *  
2:      2:CONST_INTEGER CONST  
[2012-02-20 22:07:07,860] [main] (ExpressParse.java:511) DEBUG com.ql.util.express.parse.ExpressParse -   
语句拆分后的结果:  
1:   main:FUNCTION_DEFINE                                                         FUNCTION_DEFINE  
2:      ;:; STAT_SEMICOLON_EOF  
3:         10:CONST_INTEGER CONST  
3:         *:* *  
3:         10:CONST_INTEGER CONST  
3:         +:+ +  
3:         1:CONST_INTEGER CONST  
3:         +:+ +  
3:         2:CONST_INTEGER CONST  
3:         *:* *  
3:         3:CONST_INTEGER CONST  
3:         +:+ +  
3:         5:CONST_INTEGER CONST  
3:         *:* *  
3:         2:CONST_INTEGER CONST  
[2012-02-20 22:07:07,860] [main] (ExpressParse.java:520) DEBUG com.ql.util.express.parse.ExpressParse -   
最后的语法树:  
1:   main:FUNCTION_DEFINE                                     FUNCTION_DEFINE  
2:      ;:; STAT_SEMICOLON_EOF  
3:         +:+ EXPRESS_LEVEL5  
4:            +:+ EXPRESS_LEVEL5  
5:               +:+ EXPRESS_LEVEL5  
6:                  *:* EXPRESS_LEVEL4  
7:                     10:CONST_INTEGER CONST  
7:                     10:CONST_INTEGER CONST  
6:                  1:CONST_INTEGER CONST  
5:               *:* EXPRESS_LEVEL4  
6:                  2:CONST_INTEGER CONST  
6:                  3:CONST_INTEGER CONST  
4:            *:* EXPRESS_LEVEL4  
5:               5:CONST_INTEGER CONST  
5:               2:CONST_INTEGER CONST  
[2012-02-20 22:07:07,860] [main] (ExpressRunner.java:491) DEBUG com.ql.util.express.ExpressRunner -   
1:LoadData 10  
2:LoadData 10  
3:OP : * OPNUMBER[2]  
4:LoadData 1  
5:OP : + OPNUMBER[2]  
6:LoadData 2  
7:LoadData 3  
8:OP : * OPNUMBER[2]  
9:OP : + OPNUMBER[2]  
10:LoadData 5  
11:LoadData 2  
12:OP : * OPNUMBER[2]  
13:OP : + OPNUMBER[2]  
[2012-02-20 22:15:36,090] [main] (InstructionConstData.java:25) DEBUG com.ql.util.express.instruction.detail.Instruction - LoadData 10  
[2012-02-20 22:15:36,090] [main] (InstructionConstData.java:25) DEBUG com.ql.util.express.instruction.detail.Instruction - LoadData 10  
[2012-02-20 22:15:36,090] [main] (InstructionOperator.java:40) DEBUG com.ql.util.express.instruction.detail.Instruction - *(10,10)  
[2012-02-20 22:15:36,105] [main] (InstructionConstData.java:25) DEBUG com.ql.util.express.instruction.detail.Instruction - LoadData 1  
[2012-02-20 22:15:36,105] [main] (InstructionOperator.java:40) DEBUG com.ql.util.express.instruction.detail.Instruction - +(100,1)  
[2012-02-20 22:15:36,105] [main] (InstructionConstData.java:25) DEBUG com.ql.util.express.instruction.detail.Instruction - LoadData 2  
[2012-02-20 22:15:36,105] [main] (InstructionConstData.java:25) DEBUG com.ql.util.express.instruction.detail.Instruction - LoadData 3  
[2012-02-20 22:15:36,105] [main] (InstructionOperator.java:40) DEBUG com.ql.util.express.instruction.detail.Instruction - *(2,3)  
[2012-02-20 22:15:36,105] [main] (InstructionOperator.java:40) DEBUG com.ql.util.express.instruction.detail.Instruction - +(101,6)  
[2012-02-20 22:15:36,105] [main] (InstructionConstData.java:25) DEBUG com.ql.util.express.instruction.detail.Instruction - LoadData 5  
[2012-02-20 22:15:36,105] [main] (InstructionConstData.java:25) DEBUG com.ql.util.express.instruction.detail.Instruction - LoadData 2  
[2012-02-20 22:15:36,105] [main] (InstructionOperator.java:40) DEBUG com.ql.util.express.instruction.detail.Instruction - *(5,2)  
[2012-02-20 22:15:36,105] [main] (InstructionOperator.java:40) DEBUG com.ql.util.express.instruction.detail.Instruction - +(107,10)  
表达式计算：10 * 10 + 1 + 2 * 3 + 5 * 2 = 117  


由这个简单的例子，我们看到了整个QL的执行过程：
单词分解-->单词类型分析-->语法分析-->生成运行期指令集合-->执行生成的指令集合。


其中前4个过程涉及语法的匹配运算等非常耗时，所以我们看到了 execute方法的  isCache 是否使用Cache中的指令集参数，它可以缓存前四个过程。
即把 expressString 本地缓存乘一段指令，第二次重复执行的时候直接执行指令，极大的提高了性能。




