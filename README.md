#Socns

> Socns 是一个简单的内容分享社区, 希望为用户提供一个纯粹、高质的交流平台.

##使用的框架：
* Bootstrap 3
* Spring mvc
* Velocity
* Hibernate

## 数据库配置(MySQL)
> src/main/resources/init.properties
> 数据库默认使用blog的schema，连接数据库，使用db_init.sql初始化数据库
> 默认使用root/hu1983作为mysql的账号密码

```
jdbc.url=jdbc:mysql://localhost:3306/blog?autoReconnect=true&useUnicode=true&characterEncoding=utf-8
jdbc.username=your username
jdbc.password=your password

# 初始数据库文件位置(sql/db_init.sql), 默认用户: admin / 12345
```

## 索引文件存放目录
> src/main/resources/init.properties
> 默认data/indexs在apache tomcat的安装目录下，也可以设置其他（考虑到linux和windows不一致，所以没有写绝对路径）

```
# indexs path
hibernate.search.indexs=data/indexs
```

## 图片工具安装路径配置（已废弃，减少部署步骤）
> src/main/resources/mtons.properties
> 新的patch使用java-image-scaling来处理缩放，裁剪使用jdk的ImageIO来处理

```
# graphicsmagick for windows
gmagick.home=C:/Program Files/GraphicsMagick-1.3.20-Q8
```

[Graphicsmagick 下载](http://www.graphicsmagick.org/download.html)

## 实际应用站点
> 只需通过web的项目mvn package -DskipTests生成*.war，然后直接上传到tomcat上；不支持jetty

