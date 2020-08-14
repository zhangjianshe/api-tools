# API-TOOLS

## 前言

本项目利用 Java 注解 自动生成 API 文档，并提供界面展示，并可以直接在界面中调用接口并返回结果。

本项目代码管理方式为 MAVEN 管理，源代码在 GITHUB 上开源。

基本原理是 提供一个 Servlet 根据请求 解析源代码中的注解，并生成一个接口的描述对象 ApiDoc 通过 JSON 格式传回客户端

## 项目结构

项目分为 3 个子项目

**api-tools-view** 项目 利用 GWT 技术 将服务端返回的 Json 格式转换成 UI 界面，通过 GWT 编译器编译后生成 可运行在浏览器中的 Javascript 脚本，编译后直接将编译结果拷贝至 api-tools-doc 子项目中的 resource 目录。

**api-tools-doc** 项目 利用 SpringBoot 的自动配置技术，将 UI 和接口数据返回到浏览器执行

**api-tools-test** 项目 一个使用 api-tools-doc 包的测试项目

## 开发和编译

### 将项目安装到本地 MAven 仓库

install.bat 3.0.6 release 或者 install.sh 3.0.6 release

### 将项目发布到中央仓库

build.bat 3.0.6 release 或者 build.sh 3.0.6 release

## 使用例子展示

 <img src="https://github.com/zhangjianshe/api-tools/blob/master/example.png?raw=true" width="500px" />
 
## API文档使用方法
 
 使用方式有两种一种是 SpringBoot集成，一种直接使用Servlet
### SpringBoot集成（支持SpringBoot2.0以上版本） 
 API文档采用了Spring的 AutoConfigure技术，引入Jar包后直接可以通过application.properties或者application.yaml配置即可使用
 不需要自己建立servlet BEAN.
 
```xml
<dependency>
    <groupId>cn.mapway</groupId>
    <artifactId>api-tools-doc</artifactId>
    <version>3.3.0</version>
</dependency>
```
- 配置属性
```yaml
mapway:
    apidoc:
       enabled: true #开启或者关闭文档
       url: /doc/* #文档访问路径URL
       author: zjs #文档管理员
       copyright: mapway.cn  #版权人
       title: CIM模型标准库 #文档标题
       subTitle: CIM LAB  #文档副标题
       scans: cn.enn.cim.api  #扫描的包
       #文档的LOGO 可以用64编码的图片或者直接是网络上的图片如 https://www.baidu.com/img/baidu_jgylogo3.gif
       logo: data:image/png;base64,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
       style: .queryStyle{border:solid 1px green;} #自定义样式
```

### 直接使用Servlet方式

可以在 Web.xml 文件中直接配置 Servlet cn.mapway.document.servlet.MapwayDocServlet,Servlet 参数可以参见上面的内容

- 编写文档
  在 Controller 类中添加 @Doc 注解
  在 Controller 的方法中添加 @Doc 注解
  在输入输出参数中的字段上添加 @ApiField 注解

- 如何支持 markdown 文件和 html 文件
  可以通过编写 markdown 文件直接关联到@Doc 注解的 refs 属性上如

```java
@RequestMapping(value = "/api/v2")
@Doc(value = "容器设备管理", order = 2,group = "/V2",refs ={"ref/test.md"})
public class EquipmentController extends BaseController {
 ...
}
```

group 属性将会在文档左侧按照树形结构展示，多个 group 会合并为一颗 API 树，group 的级别用/号分割

ref/test.md 是在当前类目录下的子目录 ref 中的 test.md markdown 文件,也可以是\*.html 文件

- 图片的支持
  在 html 或者 markdown 文件中可以使用同一目录中的图片，图片的 URL 格式使用方法为 **ref/${PACKAGE}/**文件名
  粗体部分为固定格式不能变
  也可直接使用网络上的图片如 http://www.abidu.com/logo.gif

- 接口标签支持
  在方法中添加注解@Doc 后可以将该方法输出文档

```java
@Doc(value = "根据容器ID查询容器", group = "/容器", order = 2, tags = {"查询"}, state = DevelopmentState.FINISH, author = Authors.AUTHOR_SUN_JIYANG)
    @RequestMapping(value = "/container/query/by/id", method = RequestMethod.POST)
    public ContainerQueryByIdResp containerQueryById(@RequestBody ContainerQueryByIdReq req) {
        return getEquipmentService().findContainerById(req);
    }
```

tags 属性为 API 接口的标签是一个字符串数组,每一个标签的格式为 标签名-自定义样式 比如

查询-queryStyle 查询是标签名 queryStyle 是自定义样式，自定义样式通过属性配置传入文档系统参加属性配置章节

### 文档强制刷新

在请求的URL后加上 force=true,系统会重新生成文档
