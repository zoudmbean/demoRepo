<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="js/jquery-1.8.2.js"></script>
</head>
<body>
    <form name="loginForm">
        用户名：<input type="text" name="username" id="username"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        密码：<input type="password" name="password" id="password" /> <br/>
        <input type="button" value="登录" onclick="login()"/>
    </form>
</body>
<script type="text/javascript">
    function login() {
        $.ajax({
            type:'post',
            url:'/chartRoom/login',
            dataType:'json',
            data:{
                username:$("#username").val(),
                password:$("#password").val()
            },
            success:function(data){
                if(data.success){
                    window.location.href="chat.jsp";
                }else{
                    alert(data.message);
                }
            }
        });
    }
</script>
</html>
