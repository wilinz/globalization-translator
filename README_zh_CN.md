# GlobalizationTranslator

[英语](README.md)

Intellij平台i18n插件，一键自动为您的应用翻译131种语言，支持Android strings.xml和java.properties文件

本插件支持翻译 Android.xml 和 java .properties 文件，可以保留占位符如```%1$s```, ```%2$d```, ```{0}```,```{1}``` 并且可以保留转义字符，例如 ```\n```,```\"```,```\'```, 等等, 支持 ```string```, ```string-array```, ```plurals``` 标签.

本插件相对于其他插件的优势：翻译速度快，不到两分钟将文件翻译成131种语言，支持保留占位符，支持增量翻译。

<h3>安装</h3>

~~Intellij平台 -> 设置(Settings) -> 插件(Plugins) -> 市场(Marketplace) -> 搜索 "GlobalizationTranslator" -> 安装(Install)~~（插件正在审核中）

从这里下载 GlobalizationTranslator-1.0.0.zip: [release](https://github.com/wilinz/globalization-translator/releases) -> 设置(Settings) -> 插件(Plugins) -> 单击设置按钮  -> 从磁盘安装插件(Install Plugin form Disk)

依赖：<a href="https://plugins.jetbrains.com/plugin/18439-compose-for-ide-plugin-development-experimental-">Compose for IDE Plugin Development (Experimental)</a>
<h4>下载依赖:</h4>

<ul>
    <li>
        <h4>
            Intellij 版本 >= 213:
        </h4>
        设置(Settings) -> 插件(Plugins) -> 市场(Marketplace) -> 搜索 "Compose for IDE Plugin Development (Experimental)" -> 安装(Install)
    </li>
    <li>
        <h4>
            Intellij 版本 < 213:
        </h4>
        <a href="https://github.com/wilinz/globalization-translator/releases/download/1.0.0/Compose_Intellij_Plugin_Base-0.1.0.zip">点击这里下载依赖库</a>
        -> 设置(Settings) -> 插件(Plugins) -> 单击设置按钮  -> 从磁盘安装插件(Install Plugin form Disk)
    </li>
</ul>

<h2>用法:</h2>
<h3>1. 翻译整个文档</h3>
<ol>
    <li>选择 values/strings.xml 或 .properties 文件</li>
    <li>右键单击并选择“翻译此文件”。</li>
    <li>选择要翻译的语言。</li>
    <li>单击确定。</li>
</ol>
<h3>2. 增量翻译</h3>
<ol>
    <li>在 values/strings.xml 或 .properties 文件中, 选择 n 行要翻译的内容</li>
    <li>右键单击并选择“翻译成其他语言”。</li>
    <li>选择要翻译的语言。</li>
    <li>单击确定。</li>
</ol>

# 支持
https://github.com/wilinz/Sponsor

# 开发：
在终端运行命令运行插件: `./gradlew runIde`

<img src="img/20220417014525.png"   />
<img src="img/20220417014733.png"  />
<img src="img/20220417014758.png"   />
