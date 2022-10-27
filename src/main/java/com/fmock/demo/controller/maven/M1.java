package com.fmock.demo.controller.maven;

/**
 * @Author zhenhuaixiu
 * @Date 2022/10/19 10:31
 * @Version 1.0
 */
public class M1 {
    // # 基础介绍
    // # 结构
    //a-maven-project             一个maven项目
    //├── pom.xml                 项目描述文件，依赖，描述等
    //├── src
    //│   ├── main
    //│   │   ├── java            java源码
    //│   │   └── resources       资源文件
    //│   └── test
    //│       ├── java            测试源码
    //│       └── resources       测试资源
    //└── target                  所有编译、打包生成的文件都放在target


    // # pom.xml
    // 使用<dependency>声明一个依赖后，Maven就会自动下载这个依赖包并把它放到classpath中
    //	<dependencies>
    //        <dependency>
    //            <groupId>commons-logging</groupId>                              groupId类似于Java的包名，通常是公司或组织名称
    //            <artifactId>commons-logging</artifactId>                        artifactId类似于Java的类名，通常是项目名称
    //            <version>1.2</version>
    //        </dependency>
    //	</dependencies>


    // # 安装maven
    // 官网下载地址  https://maven.apache.org/
    // 然后在本地解压，设置几个环境变量：
    // M2_HOME=/path/to/maven-3.8.x
    // PATH=$PATH:$M2_HOME/bin


    // # 总结
    // Maven使用pom.xml定义项目内容，并使用预设的目录结构；
    // 在Maven中声明一个依赖项可以自动下载并导入classpath；
    // Maven使用groupId，artifactId和version唯一定位一个依赖。
}


class M2 {
    // # 依赖管理
    // # Maven的第一个作用就是解决依赖管理。我们声明了自己的项目需要abc，Maven会自动导入abc的jar包，再判断出abc需要xyz，又会自动导入xyz的jar包
    //
    // Maven定义了几种依赖关系，分别是compile、test、runtime和provided
    // 其中，默认的compile是最常用的，Maven会把这种类型的依赖直接放入classpath
    //
    // scope	               说明                                          示例
    //compile          编译时需要用到该jar包（默认）                       commons-logging
    //test             编译Test时需要用到该jar包                          junit
    //runtime          编译时不需要，但运行时需要用到                       mysql
    //provided         编译时需要用到，但运行时由JDK或某个服务器提供          servlet-api


    // # Maven如何知道从何处下载所需的依赖？也就是相关的jar包？
    // Maven维护了一个中央仓库（https://repo1.maven.org），所有第三方库将自身的jar以及相关信息上传至中央仓库，Maven就可以从中央仓库把所需依赖下载到本地。
    // 但并不会每次都从中央仓库下载jar包。一个jar包一旦被下载过，就会被Maven自动缓存在本地目录（用户主目录的.m2目录），
    // 所以，除了第一次编译时因为下载需要时间会比较慢，后续过程因为有本地缓存，并不会重复下载相同的jar包。


    // # 镜像下载地址
    // 如果访问Maven的中央仓库非常慢，我们可以选择一个速度较快的Maven的镜像仓库,中国区用户可以使用阿里云提供的Maven镜像仓
    // <!-- 国内推荐阿里云的Maven镜像 -->
    // <url>https://maven.aliyun.com/repository/central</url>


    // # 如何搜索第三方组件包？
    // 如果想引用一个包，如何确切地获得它的groupId、artifactId和version？
    // 方法是通过(https://search.maven.org)搜索关键字，找到对应的组件后，直接复制。


    // # 命令行编译
    // 在命令中，进入到pom.xml所在目录，输入一下命令，即可在target目录下获得编译后自动打包的jar。
    // mvn clean
    // mvn package


    // # 执行jar包 通过上一步mvn package生成的jar包，即可通过java -jar来运行
    // java -jar demo-0.0.1-SNAPSHOT.jar
}


class M3 {
    // # 构建流程
    // # Maven不但有标准化的项目结构，还有一套标准化的构建流程，自动化实现编译，打包，发布等
    // Maven的生命周期由一些列解析器（phase）构成，以内置的生命周期`default`为例子：
    //
    //validate                   校验
    //initialize                 初始化
    //generate-sources           生成源码
    //process-sources            处理源码
    //generate-resources         生成资源
    //process-resources          处理资源
    //compile                    编译
    //process-classes            处理classes
    //generate-test-sources      生成测试源码
    //process-test-sources       处理测试源码
    //generate-test-resources    生成测试资源
    //process-test-resources     处理测试资源
    //test-compile               测试编译
    //process-test-classes       处理测试classes
    //test                       测试
    //prepare-package            预打包
    //package                    打包
    //pre-integration-test       预综合测试
    //integration-test           综合测试
    //post-integration-test      综合测试之后
    //verify                     核实
    //install                    安装
    //deploy                     部署
    //
    //
    // 如果我们运行`mvn package`，Maven就会执行default生命周期，它会从开始一直运行到package这个phase为止



    // # 在实际开发过程中，经常使用的命令有：
    //
    //mvn clean              清理所有生成的class和jar；
    //mvn clean compile      先清理，再执行到compile；
    //mvn clean test         先清理，再执行到test，因为执行test前必须执行compile，所以这里不必指定compile；
    //mvn clean package      先清理，再执行到package。
    //
    //大多数phase在执行过程中，因为我们通常没有在pom.xml中配置相关的设置，所以这些phase什么事情都不做。
    //
    //经常用到的phase其实只有几个：
    //
    //clean：清理
    //compile：编译
    //test：运行测试
    //package：打包
}


class M4 {
    // # 使用插件
    // 其实，Maven执行每个phase，都是通过某个插件plugin来执行的，所以他就是配置好需要使用的插件，然后通过phase调用他们

    // 如果标准插件无法满足需求，我们还可以使用自定义插件。使用自定义插件的时候，需要声明。
    // 例如，使用maven-shade-plugin可以创建一个可执行的jar，要使用这个插件，需要在pom.xml中声明它
    //<project>
    //    ...
    //	<build>
    //		<plugins>
    //			<plugin>
    //				<groupId>org.apache.maven.plugins</groupId>
    //				<artifactId>maven-shade-plugin</artifactId>
    //                <version>3.2.1</version>
    //				<executions>
    //					<execution>
    //						<phase>package</phase>
    //						<goals>
    //							<goal>shade</goal>
    //						</goals>
    //						<configuration>
    //                            ...
    //						</configuration>
    //					</execution>
    //				</executions>
    //			</plugin>
    //		</plugins>
    //	</build>
    //</project>
}


class M5 {
    // # 模块管理
    // 在软件开发中，把一个大项目拆分成多个模块是降低软件复杂度的有效方法
    // 对于Maven来说，原来是一个大项目
    //
    //single-project
    //├── pom.xml
    //└── src
    //
    // 现在可以分拆成三个模块
    //
    //mutiple-project
    //├── module-a
    //│   ├── pom.xml
    //│   └── src
    //├── module-b
    //│   ├── pom.xml
    //│   └── src
    //└── module-c
    //    ├── pom.xml
    //    └── src


    // Maven可以有效的管理多个模块，我们只需要把每个模块当作一个独立的Maven项目，他们有各自独立的pom.xml
    // - 首先创建一个parent项目，把abc项目中重复的提取出来，他继承springframework，并定好各个包的版本号以及引入共同依赖包（<packaging>pom</packaging>）
    // - 之后，其他项目比如a,b,c就可以继承parent项目(<packaging>jar</packaging>)
    // - 最后，在编译的时候，还要在根目录创建一个pom.xml（也就是build pom）统一编译，其中规定了 <modules>，比如
    //    <modules>
    //        <module>parent</module>
    //        <module>module-a</module>
    //        <module>module-b</module>
    //        <module>module-c</module>
    //    </modules>
    //
    // 这样，在根目录执行 mvn clean package时，maven根据根目录的pom.xml找到包括parent在内的4个<module>,一次性编译
}


class M6 {
    // 发布自己的Artifact
}









