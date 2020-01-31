package com.bjc.servlet;

import com.alibaba.fastjson.JSON;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name="loginServlet",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private static final String PASSWORD = "123456";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应字符集
        resp.setCharacterEncoding("UTF-8");

        // 1. 接收页面传递的参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Map<String,Object> restltMap = new HashMap<>();
        // 2. 判定用户名密码是否正确
        if(PASSWORD.equals(password)){ // 3. 如果正确，给客户端响应登录成功的信息
            restltMap.put("success",true);
            restltMap.put("message","登录成功！");

            // 保存用户信息到session
            req.getSession().setAttribute("username",username);

        } else { // 4. 如果不正确，响应登录失败的信息
            restltMap.put("success",false);
            restltMap.put("message","登录失败，用户名或密码错误！");
        }
        // 将数据以json格式响应给前端
        resp.getWriter().write(JSON.toJSONString(restltMap));
    }
}
