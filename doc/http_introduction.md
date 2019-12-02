# 接口说明

 - [1.获取用户勋章列表](#1.获取用户勋章列表)
 - [2.获取用户在线状态](#2.获取用户在线状态)
 - [3.获取好友邀请列表](#3.获取好友邀请列表)
 - [4.获取用户信息](#4.获取用户信息)
 - [5.设置用户信息](#5.设置用户信息)
 - [6.获取用户信息](6.获取用户信息)
 - [7.获取新增用户列表](#7.获取新增用户列表)
 - [8.获取部门列表](#8.获取部门列表)
 - [9.设置群信息](#9.设置群信息)
 - [10.获取群信息](#10.获取群信息)
 - [11.获取新增群信息](#11.获取新增群信息)
 - [12.获取系统时间](#12.获取系统时间)
 - [13.增量获取群名片](#13.增量获取群名片)
 - [14.增量获取群名片](#14.快捷回复消息接口)
 
## 需要验证q_ckey接口
以newapi开头并没有跟nck的接口，在or一层会验证q_ckey。如下接口是验证q_ckey的
### 1.获取用户勋章列表
```
接口：/newapi/user/get_user_decoration.qunar
请求方式：POST
参数：
{
   "userId":"xxxx",    #必须参数
   "host":"domain1"          #必须参数
}

返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": [
        {
            "id": 1,
            "userId": "xxxx",
            "host": "domain1",
            "type": "interview",
            "url": "http://www.aaa.com/aaa.jpg"
        }
    ]
}
```

### 2.获取用户在线状态
```
接口：/newapi/domain/get_user_status.qunar
请求方式：POST
参数：
{ 
    "users":["dongzd.zhang@ejabhost1", "bin.wang@ejabhost1"] 
}
返回值：
  {
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": {
        "ul": [
            {
                "u": "xx@domain1",
                "o": "offline"
            },
            {
                "u": "xx@domain1",
                "o": "offline"
            }
        ]
    }
}

```
### 3.获取好友邀请列表
```
接口：/newapi/base/get_invite_info.qunar
请求方式：POST
参数：
{
  "user":"xxxx",  #必须参数
  "d":"domain1",        #必须参数
  "time":"1"              #必须参数
}
 
 
返回值：
{
     "ret": true,
     "errcode": 0,
     "errmsg":"",
     "data": [
         {
             "inviter":"xxxx",
             "ihost":"domain1",
             "body":"加我",
             "timestamp":"10"
         }
     ]
 }
```

### 4.获取用户信息
```
接口：/newapi/profile/get_profile.qunar
请求方式：POST
参数：
{
 "user":"xxxx",
 "domain":"domain1",
 "version":"1"
}
返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": [
        {
            "mood": "心情1",
            "host": "domain1",
            "version": 3,
            "username": "xxxx"
        }
    ]
}
```

### 5.设置用户信息
```
接口：/newapi/profile/set_profile.qunar
请求方式：POST
参数：
[{
 "user":"xxxx",
 "domain":"domain1",
 "mood":"心情1",
 "url":"urld11"
}]
返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": {
        "user": "xxxx",
        "domain": "domain1",
        "version": "22",
        "mood": "心情1243",
        "url": "urld11234"
    }
}
```

### 6.获取用户信息
```
接口：/newapi/domain/get_vcard_info.qunar
请求方式：POST
参数：
[{
 "domain": "domain1",
 "users": [{
  "user": "xxx",
  "version": 0
 }]
}]
返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": [
        {
            "domain": "domain1",
            "users": [
                {
                    "type": "qunar_emp",
                    "loginName": "xxx",
                    "email": "",
                    "gender": "0",
                    "nickname": "xxx",
                    "imageurl": "http://xxxx/xxx.jpg?name=2259dda784fe8c4a7a7d26b208a06476.jpg&file=file/2259dda784fe8c4a7a7d26b208a06476.jpg&fileName=file/2259dda784fe8c4a7a7d26b208a06476.jpg",
                    "uid": "0",
                    "username": "malin.ma",
                    "domain": "domain1",
                    "commenturl": "https://xxx/xxx",
                    "v": "4"
                }
            ]
        }
    ]
}
```
### 7.获取新增用户列表
```
接口：/newapi/user/get_increment_users.qunar
请求方式：POST
参数：
{
 "version":1957,    #必须参数
 "domain":"domain1"   #必须参数
}
返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": [
        {
            "S": "xxx",
            "D": "xxx",
            "T": "U",
            "U": "xx",
            "V": "xxx",
            "F": "xx",
            "H": "0",
            "Domain": "domain1",
            "N": "xxx"
        }
 ]
}
```

### 8.获取部门列表
```
接口：/newapi/base/qtalk/get_deps.qunar?v=10121103&p=qim_windows
请求方式：GET
请求参数：无
返回值：
{
 "ret": true,
 "errcode": 0,
 "errmsg": "",
 "data": [...]   #格式与原接口相同
}
```

### 9.设置群信息
```
接口：/newapi/muc/set_muc_vcard.qunar
请求方式：POST
参数：
[{
    "muc_name": "xxx",   #必须参数
    "nick": "cccc",           #非必须参数，不传则不更新
    "title": "newtitle",      #非必须参数，不传则不更新
    "desc": "newdesc"         #非必须参数，不传则不更新
}]
返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": [
        {
            "muc_name": "xxxx",
            "version": "12",
            "show_name": "cccc",
            "muc_title": "newtitle",
            "muc_desc": "newdesc"
        }
    ]
}
```
### 10.获取群信息
```
接口：/newapi/muc/get_muc_vcard.qunar
请求方式：POST
参数：
[
 {
  "domain": "conference.domain2",
  "mucs": [{
   "muc_name": "xxx", 
   "version": "0"
  }]
 }
]

返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": [
        {
            "domain": "conference.domain2",
            "mucs": [
                {
                    "sn": "xxxx,xs",
                    "md": "",
                    "mt": "",
                    "mp": "https://xxx/xxx.png",
                    "vs": "1",
                    "mn": "xxx"
                }
            ]
        }
    ]
}
```

### 11.获取新增群信息
```
接口：/newapi/muc/get_increment_mucs.qunar
请求方式：POST
参数：
{
 "d":"domain1",     #必须参数，若不传，则从q_ckey中获取
 "u":"xxx",  #必须参数，若不传，则从q_ckey中虎丘
 "t":"1233"           #必须参数

返回值：
{
  "ret": true,
  "errcode": 0,
  "errmsg": "",
  "data": [{
    "M":"",
    "D":"",
    "T":"",
    "F":""
  }]
}
```



### 12.获取系统时间
```
接口：/newapi/base/getservertime.qunar
请求方式：GET
参数：无
返回值：
{
  "ret": true,
  "errcode": 0,
  "errmsg": "",
  "data": 1540959380
}
```

 
### 13.增量获取群名片
```
接口：/newapi/muc/get_user_increment_muc_vcard.qunar
请求方式：POST
参数：
{
    "userid":"testuser",
    "lastupdtime":"1559108079000"
}  

返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": [
        {
            "VS": "17",
            "UT": "",
            "MT": "3",
            "MP": "https://localhost:8080/file/v2/download/temp/new/9c74475153c716728fc486255f9546f1.png",
            "SN": "test_test1",
            "MN": "muc_id@host1",
            "MD": ""
        }
    ]
}
```
## 无需验证q_ckey的接口
以new/nck开头请求
 ### 14.快捷回复消息接口
```
接口：/newapi/nck/send_wlan_msg.qunar
请求方式：POST
参数：
type=chat/groupchat
[{
    "from": "xxx",
    "type": "groupchat",
    "count": "1542802555",
    "msg_type": "666",
    "key": "xxxx",
    "body": "微软 Bing 搜索 - 国内版[obj type=\"url\" value=\"https:\/\/www.bing.com\/\"]",
    "extend_info": "{\n  \"title\" : \"微软 Bing 搜索 - 国内版\",\n  \"linkurl\" : \"https:\\\/\\\/www.bing.com\\\/\"\n}",
    "to": [{
        "user": "xxxx@domain1"
    }]
}]

type=consult
[{
    "from": "xxxx@domain1",
    "type": "consult",
    "count": "1542802555",
    "msg_type": "1",
    "key": "xxxx=",
    "body": "xxxxa",
    "extend_info": "{\n  \"title\" : \"微软 Bing 搜索 - 国内版\",\n  \"linkurl\" : \"https:\\\/\\\/www.bing.com\\\/\"\n}",
    "to": [{
        "user": "xx@domain1",
        "realto":"xxxli@domain1",
        "channelid":"{\"cn\":\"consult\",\"d\":\"send\",\"usrType\":\"usr\"}",
        "qchatid":"4"
    }]
}]

返回值：
{
    "ret": true,
    "errcode": 0,
    "errmsg": "",
    "data": "发送成功"
}
```
## Tips 自己如何定义哪个接口验证q_ckey
```
修改ng转发配置文件 /startalk/openresty/nginx/conf/conf.d/subconf/or.server.location.package.qtapi.conf 

location /***/ {
    rewrite_by_lua_file /startalk/openresty/nginx/lua_app/checks/qim/checkchains.lua; //加上这行可对该请求验证q_ckey，否则不验证
    proxy_pass http://***/;
    proxy_set_header   Host             $host;
    proxy_set_header   X-Real-Scheme    $scheme;
    proxy_set_header   X-Real-IP        $remote_addr;
    proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
}
```
