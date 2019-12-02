# im_http_service_open

## 概述
提供包括用户信息、群信息、组织架构、消息发送以及订阅公众号相关的QTalk接口服务。

## 项目结构
im_http_service_open
- src/main
    - env_resources（资源文件） 
        - beta
        - prod
    - java（代码）
        - com.qunar.qchat
    - resources（全局资源文件）
    - webapp（web目录）
        - WEB-INF
        - healthcheck.html
- pom.xml

## 运行方式
1. >git clone git@github.com:qunarcorp/im_http_service_open.git
2. >mvn package
3. run on tomcat


## 接口说明

[参考文档](doc/http_introduction.md)