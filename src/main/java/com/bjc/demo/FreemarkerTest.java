package com.bjc.demo;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {
	public static void main(String[] args) throws Exception {
		
		// 1. 创建一个page对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		
		// 2. 设置输出文件编码
		configuration.setDefaultEncoding("utf-8");
		
		// 3. 设置模板文件所在的目录
		configuration.setDirectoryForTemplateLoading(new File("D:\\pinyougou\\freemarkerDemo\\src\\main\\resources"));
		
		// 4. 获取模板对象
		Template template = configuration.getTemplate("test.ftl");
		
		// 5. 创建数据模型
		Map map = new HashMap();
		map.put("name", "张三");
		map.put("message", "欢迎登录，本次登录时间是：" + new Date());
		map.put("success", false);
		
		List goodsList=new ArrayList();
		Map goods1=new HashMap();
		goods1.put("name", "苹果");
		goods1.put("price", 5.8);
		Map goods2=new HashMap();
		goods2.put("name", "香蕉");
		goods2.put("price", 2.5);
		Map goods3=new HashMap();
		goods3.put("name", "橘子");
		goods3.put("price", 3.2);
		goodsList.add(goods1);
		goodsList.add(goods2);
		goodsList.add(goods3);
		map.put("goodsList", goodsList);

		map.put("today", new Date());
		map.put("point", 1314926);
		
		// 6. 创建一个输出流对象，参数为目标文件地址
		Writer out = new FileWriter("d:\\test.html");
		
		// 7. 执行模板对象方法，生成文件到磁盘
		template.process(map, out );
		
		// 8. 关闭流对象
		out.close();
	}
}
