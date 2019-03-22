package com.socket.lgy.socket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

@Component
@ConfigurationProperties(prefix = "socketpkg")
public class SocketUtil {
    /*private static String host = "172.16.201.148";
    private static String username = "ansroot";
    private static String password = "ansroot";
    private static Integer port = 8090;
    private static Integer timeOut = 10 ;
    private static String charset = "UTF-8";*/

    private static String host;
    private static String username;
    private static String password;
    private static Integer port;
    private static Integer timeOut;
    private static String charset;





    public static String connectDevice() throws IOException {

        System.out.println("sssssssssss"+host);
        Socket socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(host,port);
        socket.connect(socketAddress,timeOut);
        System.out.println("ssssssssss:"+socket.isConnected());

        JSONObject subJson = new JSONObject(true);
        JSONObject params = new JSONObject(true);
        JSONArray filter = new JSONArray();
        params.put("filter",filter);
        params.put("pageno",1);
        params.put("pagesize",10);

        subJson.put("params",params);

        subJson.put("server",new JSONArray());;

        JSONObject json = new JSONObject(true);
        json.put("method","GET");
        json.put("format",subJson);
        json.put("user",username);
        json.put("pass",password);

        OutputStream outputStream = socket.getOutputStream();
        System.out.println((json.toJSONString()+"~#"));
        outputStream.write((json.toJSONString()+"~#").getBytes(charset));

       BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),charset));

       StringBuffer sbf = new StringBuffer();
       String str = "";
       while(true){
           str = str.concat(br.readLine());
           if(str.indexOf("~#") >= 0){
               break;
           }
       }
        outputStream.close();;
        br.close();
        socket.close();
        return str;
    }


    /**
     *
     * @param modelType 查询方法
     * @param filter
     * @param params
     * @param pageno
     * @param pageSize
     * @return
     * @throws IOException
     */
    public static String getList(String modelType,JSONArray filter,JSONObject params,int pageno,int pageSize) throws IOException {
        String method = "GET";
        System.out.println("sssssssssss"+host);
        Socket socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(host,port);
        socket.connect(socketAddress,timeOut);
        System.out.println("ssssssssss:"+socket.isConnected());

        JSONObject subJson = new JSONObject(true);
        //过滤条件为空直接跳过
       if(filter != null && params != null){
           params.put("filter",filter);

       }

       //是否有页数限制
        if(pageno != 0 && params != null){
            params.put("pageno",pageno);
            params.put("pagesize",pageSize);
        }

        //搜索参数
       if(params != null){
           subJson.put("params",params);
       }


        subJson.put(modelType,new JSONArray());;

        JSONObject json = new JSONObject(true);
        json.put("method",method);
        json.put("format",subJson);
        json.put("user",username);
        json.put("pass",password);

        OutputStream outputStream = socket.getOutputStream();

        outputStream.write((json.toJSONString()+"~#").getBytes(charset));

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),charset));

        StringBuffer sbf = new StringBuffer();
        String str = "";
        while(true){
            str = str.concat(br.readLine());
            if(str.indexOf("~#") >= 0){
                break;
            }
        }
        str.replace("~#","");
        outputStream.close();;
        br.close();
        socket.close();
        return str;
    }


    public static String getList(String modelType,JSONArray filter,JSONObject params) throws IOException {
        int pageno = 1;
        int pageSize = 10;
        return getList(modelType,filter,params,pageno,pageSize);
    }

    public static void main(String[] args) throws IOException {
        SocketUtil.connectDevice();
    }

}
