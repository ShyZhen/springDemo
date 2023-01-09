package com.fmock.demo.controller.httpserver;

import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author zhenhuaixiu
 * @Date 2023/1/9 15:28
 * @Version 1.0
 */
public class H1 {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8080);
            System.out.println("HTTP Server is running");

            for (;;) {
                Socket socket = ss.accept();
                System.out.println("连接来自：" + socket.getRemoteSocketAddress());

                Thread t = new HttpHandle(socket);
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class HttpHandle extends Thread {
    Socket socket;

    public HttpHandle(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream input = this.socket.getInputStream();
            OutputStream output = this.socket.getOutputStream();
            this.handleSome(input, output);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 实际处理方法
    // 只需要在handleSome()方法中，用Reader读取HTTP请求，用Writer发送HTTP响应，即可实现一个最简单的HTTP服务器
    @SneakyThrows
    private void handleSome(InputStream input, OutputStream output) {
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

        //System.out.println(reader);
        //System.out.println(writer);

        // 读取HTTP请求
        boolean requestOk = false;
        String firstLine = reader.readLine();
        if (firstLine.startsWith("GET / HTTP/1.")) {
            requestOk = true;
        }

        for (;;) {
            String header = reader.readLine();
            if (header.isEmpty()) { // 当读取到空行时，HTTP Header读取完毕
                break;
            }
            System.out.println(header);
        }

        System.out.println(requestOk ? "Response OK" : "Response Error");

        // 相当于自己编写了一个HTTP应用层的协议处理方案
        if (!requestOk) {
            // 发送错误响应
            writer.write("HTTP/1.0 400 Bad Request\r\n");
            writer.write("111111\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
        } else {
            // 发送成功响应:
            String data = "<html><head><meta charset=\"utf-8\"></head><body><h1>Hello, world! 是的成功了！</h1></body></html>";
            int length = data.getBytes(StandardCharsets.UTF_8).length;
            writer.write("HTTP/1.0 200 OK\r\n");
            writer.write("Connection: close\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            writer.write("\r\n"); // 空行标识Header和Body的分隔 上面是header，下面就是body
            writer.write("222222\r\n");
            writer.write("333333\r\n");
            writer.write(data);
        }
        writer.flush();
    }

    // 上面核心代码是：先读取HTTP请求，这里我们只处理GET /的请求。
    // 当读取到空行时，表示已读到连续两个\r\n，说明请求结束，可以发送响应。
    // 发送响应的时候，首先发送响应代码HTTP/1.0 200 OK表示一个成功的200响应，使用HTTP/1.0协议，
    // 然后，依次发送Header，发送完Header后，再发送一个空行标识Header结束，紧接着发送HTTP Body，在浏览器输入http://127.0.0.1:8080/就可以看到响应页面
}


// HTTP版本升级历史
//[HTTP 1.0] 是最早期版本，浏览器每次建立TCP连接后，只发送一个HTTP请求并接收一个HTTP响应，然后就关闭TCP连接，因此比较消耗资源浪费时间。
//[HTTP 1.1] 允许浏览器和服务器在同一个TCP连接上反复发送、接收多个HTTP请求和响应，大大提高了传输效率。
//[HTTP 2.0] 可以支持浏览器同时发出多个请求，但是每个请求需要唯一标识，服务器不需要按照顺序返回多个响应，由浏览器自己把收到的响应和请求对应起来，进一步提高了传输效率，因为浏览器发出一个请求后不必等待响应，就可以继续发下一个请求
//[HTTP 3.0] 为了更进一步提高速度，将抛弃TCP,改用无需创建连接的UDP协议，目前仍然处于试验阶段。