<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="css/main.css"/>
    <script src="js/jquery-1.8.2.js"></script>
</head>
<body onload="startWebSocket(self);" style="TEXT-ALIGN: center;">

    <h2 style="background-color: greenyellow">用户 ${username}</h2>
    <div id="contentBody" class="contentBody">
        <div class="leftDiv">
            <div class="msgShow" id="msgShow">
            </div>
            <div clas="bottomDiv">
                <div class="textArea">
                    <textarea name="content" id="content" ></textarea>
                    <input type="button" value="发送" id="btnSend" />
                </div>
            </div>
        </div>
        <div class="rightDiv">
            <div class="up">
                <div style="border-bottom: 1px solid #000;">好友列表</div>
                <div id="userList"></div>
            </div>
            <div class="down">
                <div style="border-bottom: 1px solid #000;">系统广播</div>
                <div id="broadcastList"></div>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
    <%
        String name = session.getAttribute("username") + "";
    %>
    var self = "<%=name%>";

    var ws;
    function startWebSocket(self) {
        var url = "ws://localhost:8080/chartRoom/chatWeSocket"
        if ("WebSocket" in window) {
            ws = new WebSocket(url);
        } else if ("MozWebSocket" in window) {
            ws = new MozWebSocket(url);
        } else {
            alert("浏览器版本过低，请升级您的浏览器");
        }

        // 监听消息，有消息传递，会触发此方法
        ws.onmessage = function(evt){
            var _data = evt.data;
            console.log("data >> " + _data);

            var o = JSON.parse(_data);
            if(o.type == 'message'){ // 如果后端响应的是消息，在页面展示
                setMessageInnerHTML(o,self);
            } else if(o.type == 'user'){ // 如果客户端相应的是用户列表，在界面显示用户列表
                var userArray = o.data.split(",");
                $("#userList").empty();
                $("#userList").append('<li><input type="radio" name="toUser" value="All">广播</li>');
                $.each(userArray,function(n,value){
                    if(value != self && value != 'admin'){
                        $("#userList").append('<li><input type="radio" name="toUser" value="'+value+'" />' + value + '</li>');

                        $("#broadcastList").append('<li>您的好友 '+value+' 上线啦！</li>');
                    }

                });
            }

            // 关闭连接的时候触发
            ws.onclose = function(evt){
                $("#username").html("用户" + self + "离线了");
            }

            ws.onopen = function(evt){
                $("#username").html("用户" + self + "上线了");
            }

        }
    }

    function setMessageInnerHTML(o,self){
        var data = JSON.parse(o.data);
        if(self == data.fromName){
            $("#msgShow").append("<div style='margin-left: 10px;text-align: left'> "+ data.fromName +" 说： "+data.content+"</div>");
        } else {
            $("#msgShow").append("<div style='margin-right: 10px;text-align: right'> "+ data.fromName +" 说： "+data.content+"</div>");
        }
    }

    $("#btnSend").click(function () {
        var content = $("#content").val();
        if(!content){
            alert("请输入内容！");
            return;
        }
        var message = {};
        message.fromName = self;
        message.toName = $("input:radio:checked").val() ? $("input:radio:checked").val() : "All";
        message.content = content;
        var msg = JSON.stringify(message);
        console.log(msg);
        ws.send(msg);

    });
</script>
</html>
