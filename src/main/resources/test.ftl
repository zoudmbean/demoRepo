<html>
<head>
	<meta charset="utf-8">
	<title>Freemarker入门小DEMO </title>
</head>
<body>

<!--引入头文件-->
<#include "head.ftl" >

<#--我只是一个注释，我不会输出到生成的文件中  -->
<!-- 我一个html注释，我会输出到生成的文件中  -->
${name},你好。${message}
<br/>
<#assign linkman="邹先生" />
${linkman}

<#if success>
	登录成功！
<#else>
	登录失败！
</#if>
<br/>
<hr/>
<br/>
<#list goodsList as goods>
	${goods_index+1}  商品名称：${goods.name}  价格：${goods.price} <br/>
</#list>
	共${goodsList?size}条记录
<br/>
<hr/>
<br/>

<#assign text="{'name':'张三','age':12}" />
<#assign data = text ? eval />
${(text ? eval).name} <br/> ${data.age}
<br/>
<hr/>
<br/>
当前日期：${today?date} <br/>
当前时间：${today?time}<br/>
当前日期+时间：${today?datetime}
日期格式化：  ${today?string("yyyy年MM月")}
<br/>
<hr/>
<br/>
${point} <br/>
${point?c}
</body>
</html>
