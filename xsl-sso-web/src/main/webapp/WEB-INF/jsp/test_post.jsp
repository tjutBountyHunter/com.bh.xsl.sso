<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>xsl后台系统——登录</title>
	<base href="<%=basePath%>">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <script type="text/javascript" src="js/jquery.min.js"></script>
<script>
$(function(){
    $("#url").hide();
    $("#tokenKey").hide();
    var c = 1;
    var url = $("#url").val();
	   $("#login-button").click(function(){
	      if($("#username").val()==""){
	         alert("请输入用户名!");
	         return;
	      }
	      if ($("#passwd").val() == "") {
              alert("请输入密码!");
              return;
		  }
		  if ($("#passwd").val() != "" && c == 1 ) {
              var pas = $("#passwd").val();
              $("#passwd").val("123ds6816sd6a88f66515156as1d8f168as141d"+ pas + "d7as8884986d4a8s6f46as48f12215138gd5fs19415649292")
			  c = 0;
		  }
           $.post("manager/login",$("#info").serialize(),function(data){
               var cva=eval('('+data+')');
               if (cva.statu != 200){
                   alert("用户名或密码错误");
                   $("#username").val("");
                   $("#passwd").val("");
                   c = 1;
               }
               if (cva.statu == 200){
                   window.location.replace(cva.returnUrl);
               }
           });

      });
});
</script>
</head>

<body>
	<div class="htmleaf-container">
		<div class="wrapper">
			<div class="container">
				<h1>悬赏令管理员登录</h1>
				<form class="form" id="info">
					<input type="text" placeholder="Username" id="username" name="username">
					<input type="password" placeholder="Password" id="passwd" name="passwd">
					<%--<input type="text" id="url" name="returnUrl" value="http://www.baidu.com">--%>
					<input type="text" id="url" name="returnUrl" value=${returnUrl}>
					<input type="text" id="tokenKey" name="tokenKey" value="">
					<button type="button" id="login-button">登录</button>
				</form>
			</div>

			<ul class="bg-bubbles">
				<li></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
			</ul>
		</div>
	</div>

</body>
</html>
