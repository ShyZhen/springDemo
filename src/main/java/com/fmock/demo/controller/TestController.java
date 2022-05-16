package com.fmock.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;

/**
 * @Author zhenhuaixiu
 * @Date 2022/5/16 10:24
 * @Version 1.0
 */
public class TestController {

    /**
     * 期和时间分隔符是T
     * 日期：yyyy-MM-dd
     * 时间：HH:mm:ss
     * 带毫秒的时间：HH:mm:ss.SSS
     * 日期和时间：yyyy-MM-dd'T'HH:mm:ss
     * 带毫秒的日期和时间：yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    @Test
    @GetMapping(value = "/time", produces = "application/json")
    public void time() {
        LocalDate date = LocalDate.now();              // 当前日期 2022-05-13
        LocalTime time = LocalTime.now();              // 当前时间 16:19:41.535584
        LocalDateTime dateTime = LocalDateTime.now();  // 当前日期和时间 2022-05-13T16:19:41.535584

        // 可以避免上面代码执行的消耗导致时间对不上
        LocalTime time1 = dateTime.toLocalTime();      // 转换到当前日期
        LocalDate date1 = dateTime.toLocalDate();      // 转换到当前时间

        // 自定义格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM/dd HH:mm:ss.SSS");
        System.out.println(formatter.format(dateTime));  // 2022-05/13 16:30:30.321

        // 时间戳
        Instant now = Instant.now();
        System.out.println(now.getEpochSecond());  // 秒    1652433256
        System.out.println(now.toEpochMilli());    // 毫秒   1652433256827
    }

    /**
     * 用.可以匹配任意字符
     * 修饰符*可以匹配任意个字符，包括0个字符
     * 进行多行匹配时，我们用^表示开头，$表示结尾
     */
    @Test
    public void matchTest() {
        String s = "123456";
        String reg = "\\d{6}";
        System.out.println(s.matches(reg));  // true

        String zh = "和A";
        String reg2 = "\u548c\\w";
        System.out.println(zh.matches(reg2)); // true
    }

    /**
     * 编码
     * 因为ASCII编码最多只能有127个字符，要想对更多的文字进行编码，就需要用Unicode
     * 而中文的`中`字使用Unicode编码就是0x4e2d，UTF-8则需要三个字节编码就是0xe4b8ad
     *
     * URL编码，为了兼容，因为很多服务器只识别ASCII字符。
     * 对[AZaz09-_.*]保持不变,其他字符则先转为UTF-8，然后对每个【字节】以%XX表示。
     * 比如：`中`的UTF-8为0xe4b8ad,则url编码为 %E4%B8%AD
     */
    @Test
    public void urlEncodeTest() {
        String zh = "中";
        String encodeZh = URLEncoder.encode(zh, StandardCharsets.UTF_8);
        System.out.println(encodeZh);    // %E4%B8%AD

        System.out.println(URLDecoder.decode(encodeZh, StandardCharsets.UTF_8));  // 中
    }

    /**
     * Base64编码  Base64编码的思想是是采用64个基本的ASCII码字符对数据进行重新编码
     * URL编码是对字符进行编码，表示成%XX的形式；而Base64则是对二进制数据进行编码，表示成文本格式
     *
     * 他是把任意长度的二进制数据遍布纯文本，且只包含【AZaz09+/=】这些字符。原理是把三字节的二进制数据按照6bit一组，
     */
    @Test
    public void base64Test() {
        String s = "待编码的字符串";

        // 字符串转成byte[]   在Java中，二进制数据就是byte[]数组
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(b));                        // [-27, -66, -123, -25, -68, -106, -25, -96, -127, -25, -102, -124, -27, -83, -105, -25, -84, -90, -28, -72, -78]


        // 对byte[]进行Base64编码
        String base64str = Base64.getEncoder().encodeToString(b);
        System.out.println(base64str);                                 // 5b6F57yW56CB55qE5a2X56ym5Liy


        // 解码
        byte[] output = Base64.getDecoder().decode(base64str);
        System.out.println(Arrays.toString(output));                   // [-27, -66, -123, -25, -68, -106, -25, -96, -127, -25, -102, -124, -27, -83, -105, -25, -84, -90, -28, -72, -78]
        // byte转string
        String outputStr = new String(output, StandardCharsets.UTF_8);
        System.out.println(outputStr);                                 // 待编码的字符串
    }

    /**
     * Hash算法  对任意一组输入数据进行计算，得到一个固定长度的输出摘要
     * 相同的输入一定得到相同的输出
     * 不同的输入大概率得到不同的输出
     * 验证原始数据是否被篡改
     */
    @Test
    public void hashTest() {

    }

}
