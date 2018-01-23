package com.example.footer.utils;

import android.util.Config;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 上传工具类(韩冰)
 */

public class UploadUtil {
    private YourListener mYourListener=null;
    private static UploadUtil uploadUtil;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型  

    private UploadUtil() {
    }

    /**
     * 单例模式获取上传工具类
     */
    public static UploadUtil getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new UploadUtil();
        }
        return uploadUtil;
    }

    private static final String TAG = "UploadUtil";
    private int readTimeOut = 10 * 1000; // 读取超时  
    private int connectTimeout = 10 * 1000; // 超时时间  
    /***
     * 请求使用多长时间
     */
    private static int requestTime = 0;

    private static final String CHARSET = "utf-8"; // 设置编码  

    /***
     * 上传成功
     */
    public static final int UPLOAD_SUCCESS_CODE = 1;
    /**
     * 文件不存在
     */
    public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
    /**
     * 服务器出错
     */
    public static final int UPLOAD_SERVER_ERROR_CODE = 3;
    protected static final int WHAT_TO_UPLOAD = 1;
    protected static final int WHAT_UPLOAD_DONE = 2;

    /**
     * android上传文件到服务器
     *  @param filePath   需要上传的文件的路径
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    public String uploadFile(String filePath, String fileKey, String RequestURL,
                             Map<String, String> param) {
        if (filePath == null) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return filePath;
        }
        try {
            File file = new File(filePath);
            uploadFile(file, fileKey, RequestURL, param);
        } catch (Exception e) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            if(Config.DEBUG){
                e.printStackTrace();
            }
            return filePath;
        }
        return filePath;
    }

    //一张成功再传下一张
    public void uploadFileListNext(final List<String> listurl, String fileKey, String RequestURL,
                               Map<String, String> param) {

        for( int i = 0;i < listurl.size(); i ++){
            if (listurl.get(0) == null) {
                sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            }
            try {
                File file = new File(listurl.get(0));
                uploadFile(file, fileKey, RequestURL, param);
            } catch (Exception e) {
                sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
                if(Config.DEBUG){
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    public void uploadFile(final File file, final String fileKey,
                           final String RequestURL, final Map<String, String> param) {
        if (file == null || (!file.exists())) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }

        Log.i(TAG, "请求的URL=" + RequestURL);
        Log.i(TAG, "请求的fileName=" + file.getName());
        Log.i(TAG, "请求的fileKey=" + fileKey);
        new Thread(new Runnable() {  //开启线程上传文件  
            @Override
            public void run() {
                toUploadFile(file, fileKey, RequestURL, param);
            }
        }).start();

    }

    public void UploadFiles(final List<String> files, final String fileKey, final String RequestURL,
                            final Map<String,String> param){
        if(files.size()==0){
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
        }

        new Thread(new Runnable() {  //开启线程上传文件
            @Override
            public void run() {
                //toUploadFiles(files,fileKey,RequestURL,param);
                try {
                    doFilePost(RequestURL,param,files);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private String toUploadFile(File file, String fileKey, String RequestURL,
                                Map<String, String> param) {
        String result = null;
        requestTime = 0;

        long requestTime = System.currentTimeMillis();
        long responseTime = 0;

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流  
            conn.setDoOutput(true); // 允许输出流  
            conn.setUseCaches(false); // 不允许使用缓存  
            conn.setRequestMethod("POST"); // 请求方式  
            conn.setRequestProperty("Charset", CHARSET); // 设置编码  
            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//          conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  

            /**
             * 当文件不为空，把文件包装并且上传 
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";

            /*** 
             * 以下是用于上传参数 
             */
            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    Log.i(TAG, key + "=" + params + "##");
                    dos.write(params.getBytes());
//                  dos.flush();  
                }
            }

            sb = null;
            params = null;
            sb = new StringBuffer();
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 
             * filename是文件的名字，包含后缀名的 比如:abc.png 
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\"" + fileKey
                    + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type:image/jpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();
            sb = null;

            Log.i(TAG, file.getName() + "=" + params + "##");
            dos.write(params.getBytes());
            /**上传文件*/
            InputStream is = new FileInputStream(file);
            onUploadProcessListener.initUpload((int) file.length());
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                curLen += len;
                dos.write(bytes, 0, len);
                //onUploadProcessListener.onUploadProcess(curLen);
            }
            is.close();

            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
//            
//          dos.write(tempOutputStream.toByteArray());  
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流 
             */
            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            this.requestTime = (int) ((responseTime - requestTime) / 1000);
            Log.e(TAG, "response code:" + res);
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                Log.e(TAG, "result : " + result);
                sendMessage(UPLOAD_SUCCESS_CODE, "上传结果："
                        + result);
                //把上传结果回调
                if (mYourListener != null) {
                    mYourListener.onSomeChange(result, UPLOAD_SUCCESS_CODE);
                }
                return result;
            } else {
                Log.e(TAG, "request error");
                sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：code=" + res);
                return result;
            }
        } catch (MalformedURLException e) {
            sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：error=" + e.getMessage());
            if(Config.DEBUG){
                e.printStackTrace();
            }
            return result;
        } catch (IOException e) {
            sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：error=" + e.getMessage());
            if(Config.DEBUG){
                e.printStackTrace();
            }
            return result;
        }
    }

    /**
     * 发送上传结果
     *
     * @param responseCode
     * @param responseMessage
     */
    private void sendMessage(int responseCode, String responseMessage) {
        if (mYourListener != null) {
            mYourListener.onSomeChange(responseMessage, responseCode);
        }

        onUploadProcessListener.onUploadDone(responseCode, responseMessage);
    }



    /**
     * 下面是一个自定义的回调函数，用到回调上传文件是否完成
     *
     * @author shimingzheng
     */
    public static interface OnUploadProcessListener {
        /**
         * 上传响应
         *
         * @param responseCode
         * @param message
         */
        void onUploadDone(int responseCode, String message);

        /**
         * 上传中
         *
         * @param uploadSize
         */
        void onUploadProcess(int uploadSize);

        /**
         * 准备上传
         *
         * @param fileSize
         */
        void initUpload(int fileSize);
    }

    private OnUploadProcessListener onUploadProcessListener;


    public void setOnUploadProcessListener(
            OnUploadProcessListener onUploadProcessListener) {
        this.onUploadProcessListener = onUploadProcessListener;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 获取上传使用的时间
     *
     * @return
     */
    public static int getRequestTime() {
        return requestTime;
    }

    public static interface uploadProcessListener {

    }
    //回调接口(监听器)
    public interface YourListener {
        public void onSomeChange(String re, int i);
    }

    //把上传成功的结果回调
    public void setYourListener(YourListener yourListener) {
        mYourListener = yourListener;
    }

    //多图片上传后台服务器
    public String doFilePost(String urlstr, Map<String, String> map,
                             List<String> files) throws IOException {
        String  result=null;
        String BOUNDARY = "----WebKitFormBoundaryDwvXSRMl0TBsL6kW"; // 定义数据分隔线

        URL url = new URL(urlstr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 发送POST请求必须设置如下两行
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Android WYJ");
        connection.setRequestProperty("Charsert", "UTF-8");
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);

        OutputStream out = new DataOutputStream(connection.getOutputStream());
        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线

        // 文件
        if (files != null && !files.isEmpty()) {
            for (String wenjian:files) {
                File file = new File(wenjian);
                String fileName = file.getName();

                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"" + "file"
                        + "\";filename=\"" + file.getName() + "\"\r\n");
                sb.append("Content-Type: image/jpg\r\n\r\n");
                byte[] data = sb.toString().getBytes();
                out.write(data);

                DataInputStream in = new DataInputStream(new FileInputStream(
                        file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
                in.close();
                LogUtil.e("图片情况："+sb.toString());
            }
        }
        // 数据参数
        if (map != null && !map.isEmpty()) {

            for (Map.Entry<String, String> entry : map.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"");
                sb.append("\r\n");
                sb.append("\r\n");
                sb.append(entry.getValue());
                sb.append("\r\n");
                byte[] data = sb.toString().getBytes();
                LogUtil.e(sb.toString());
                out.write(data);
            }
        }
        out.write(end_data);
        out.flush();
        out.close();

        // 定义BufferedReader输入流来读取URL的响应
        //      BufferedReader reader = new BufferedReader(new InputStreamReader(
        //              connection.getInputStream()));
        //      String line = null;
        //      while ((line = reader.readLine()) != null) {
        //          System.out.println(line);
        //      }
        int res=connection.getResponseCode();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inStream = connection.getInputStream();
           /* byte[] number = read(inStream);
            String json = new String(number);*/

            BufferedReader reader=new BufferedReader(new InputStreamReader(inStream));
            String line=null;

            while ((line = reader.readLine()) != null){
                result+=line;

            }
            LogUtil.e("-----"+result);
            Log.e(TAG, "result : " + result);
            sendMessage(UPLOAD_SUCCESS_CODE, "上传结果："
                    + result);
            //把上传结果回调
            if (mYourListener != null) {
                mYourListener.onSomeChange(result, UPLOAD_SUCCESS_CODE);
            }
            return result;
        }else{
            Log.e(TAG, "request error");
            sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：code=" + res);
            return result;
        }

    }
}