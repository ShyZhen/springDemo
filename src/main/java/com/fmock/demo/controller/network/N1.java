package com.fmock.demo.controller.network;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpClient.Version;
import java.time.Duration;
import java.util.*;
/**
 * @Author zhenhuaixiu
 * @Date 2022/10/27 11:22
 * @Version 1.0
 */
public class N1 {

    // 2^16 = 65536
    // 2^32 = 4294967296

    // 计算机网络
    // 计算机网络是指两台、或者更多的计算机组成的网络，在同一个网络中，任意两台计算机都可以直接通信，因为所有计算机都要遵循同一种网络协议。

    // 互联网
    // 互联网是计算机网络的网络，即把很多计算机网络连接起来，形成一个全球统一的互联网。
    // 如果计算机网络各自的通讯协议不统一，就没办法互相链接起来形成网。因此进行统一，为了把计算机网络接入互联网，就必须使用TCP/IP协议。

    // TCP/IP协议
    // TCP/IP协议泛指互联网协议，其中最重要的两个协议是TCP协议和IP协议。
    // 只有使用TCP/IP协议的计算机才能够联入互联网，使用其他网络协议（例如NetBIOS、AppleTalk协议等）是无法联入互联网的。

    // IP地址
    // 在互联网中，一个IP地址用来标识一个网络接口（network interface），一台入互联网的计算机肯定有一个(或多个)ip地址。
    // IP地址分为 IPv4 和 IPv6 两种。
    // IPv4: 采用32位地址，总共有2^32个（大约42亿），目前已经耗尽。          例如 101.202.99.12
    // IPv6: 采用128位地址，总共有2^128个（大约340万亿亿亿亿），根本用不完。  例如 2001:0DA8:100A:0000:0000:1020:F2F3:1428

    // 域名
    // 因为直接记忆IP地址非常难，所以我们通常使用域名。域名解析服务器DNS负责把域名翻译成对应的IP,客户端再根据IP地址访问服务器。
    // 用nslookup可以查看域名对应的IP地址

    // 网络模型 OSI网络模型
    //应用层，提供应用程序之间的通信；            HTTP SMTP
    //表示层：处理数据格式，加解密等等；
    //会话层：负责建立和维护会话；
    //传输层：负责提供端到端的可靠传输；          TCP UDP
    //网络层：负责根据目标地址选择路由来传输数据；  网络选择，IP寻址
    //数据链路层和物理层：负责把数据进行分片并且真正通过物理网络传输，例如，无线网、光纤、电缆等。

    // 总结
    // IP协议（网络层）是一种分组交换传输协议，不保证可靠传输
    // TCP协议（传输层）是传输控制协议，支持可靠传输和双向通信。
    // TCP建立在IP协议之上，简单来说，IP协议只负责发数据包，不保证顺序和正确性；而TCP负责控制数据库传输，包括先建立连接，传输数据，以及断开连接。
    // TCP协议之所以能可靠传输，是通过接收确认、超时重传这些机制实现的。
    // TCP协议是最广泛的协议，许多高级协议都是建立在它之上的，比如HTTP、SMTP（应用层）等。
    // UDP协议是一种数据报文协议，他是无连接协议，不保证可靠传输，因为它在通信前不需要建立连接。
    // UDP协议所以，它的传输效率比TCP更高，而且比TCP协议简单的多。
    // 选择UDP时，传输的数据通常是能容忍丢失的，比如一些语音视频通信的应用。
}

class N2 {
    // TCP编程

    // Socket
    // Socket是一个抽象概念，一个应用程序通过Socket来建立一个远程连接，而Socket内部通过TCP/IP协议把数据传输到网络。
    /*
    ┌───────────┐                                   ┌───────────┐
    │Application│                                   │Application│
    ├───────────┤                                   ├───────────┤
    │  Socket   │                                   │  Socket   │
    ├───────────┤                                   ├───────────┤
    │    TCP    │                                   │    TCP    │
    ├───────────┤      ┌──────┐       ┌──────┐      ├───────────┤
    │    IP     │◀────▶│Router│◀─────▶│Router│◀────▶│    IP     │
    └───────────┘      └──────┘       └──────┘      └───────────┘
     */
    // Socket、TCP和部分IP的功能都是由操作系统提供的，不同的编程语言只是提供了对操作系统调用的简单的封装。

    // 为什么需要Socket编程？
    // 因为仅仅通过IP地址进行通信是不够的，同一台计算机同一时间会运行多个网络应用程序，例如浏览器、QQ、邮件客户端等。操作系统接收到一个数据包的时候，如果只有IP地址，它没法判断应该发给哪个应用程序。
    // 一个Socket就是由IP地址和端口号（范围是0～65535）组成，可以把Socket简单理解为IP地址加端口号,
    // 端口号总是由操作系统分配，它是一个0～65535之间的数字，其中，小于1024的端口属于特权端口，需要管理员权限，大于1024的端口可以由任意用户的应用程序打开.
}

class N3 {
    // HTTP编程

    // HTTP就是目前使用最广泛的Web应用程序使用的基础协议,比如PC浏览器，App访问后台服务器等。
    // 它是基于TCP协议之上的一种 [请求-响应] 协议
    // 比如当浏览器希望访问某个网站时，浏览器和网站服务器之间首先建立TCP连接，且服务器总是使用80端口和加密端口443

    // HTTP请求的格式是固定的，它由HTTP Header和HTTP Body两部分构成。

    // 服务器端的HTTP编程：本质上就是编写Web服务器，这是一个非常复杂的体系，也是JavaEE开发的核心内容
    // 客户端的HTTP编程：因为浏览器也是一种HTTP客户端，所以，客户端的HTTP编程，它的行为本质上和浏览器是一样的，即发送一个HTTP请求，接收服务器响应后，获得响应内容。
    //只不过浏览器进一步把响应内容解析后渲染并展示给了用户，而我们使用Java进行HTTP客户端编程仅限于获得响应内容

    // 从Java 11 开始，引入了新的httpClient
    // 我们来看一下如何使用新版的HttpClient。首先需要创建一个全局HttpClient实例，因为HttpClient内部使用线程池优化多个HTTP连接，可以复用:
//
//    // 全局HttpClient:
//    static HttpClient httpClient = HttpClient.newBuilder().build();
//
//    public static void main(String[] args) throws Exception {
//        String url = "https://www.sina.com.cn/";
//        HttpRequest request = HttpRequest.newBuilder(new URI(url))
//                // 设置Header:
//                .header("User-Agent", "Java HttpClient").header("Accept", "*/*")
//                // 设置超时:
//                .timeout(Duration.ofSeconds(5))
//                // 设置版本:
//                .version(Version.HTTP_2).build();
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        // HTTP允许重复的Header，因此一个Header可对应多个Value:
//        Map<String, List<String>> headers = response.headers().map();
//        for (String header : headers.keySet()) {
//            System.out.println(header + ": " + headers.get(header).get(0));
//        }
//        System.out.println(response.body().substring(0, 1024) + "...");
//    }









}


