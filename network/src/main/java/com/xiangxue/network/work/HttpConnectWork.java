package com.xiangxue.network.work;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.xiangxue.network.utils.SLog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import rxtool.SPUtils;


public class HttpConnectWork {
    public final static int SocketCode = -10000;
    public final static int ServiceErrorCode = -30000;
    private final static int TimeOut = 10 * 1000;
    private InputStream mInputStream = null;
    private HttpURLConnection mCon;
    private HttpsURLConnection mCons;
    private SSLSocketFactory mSSLSocket;
    private int mCode;

    public String getRequestManager(String url, Map<String, Object> headMap) {
        return connectNetwork(url, headMap, null, HttpType.GET);
    }

    public String postRequestManager(String url, Map<String, Object> headMap, byte[] postByte) {
        return connectNetwork(url, headMap, postByte, HttpType.POST);
    }

    public String putRequestManager(String url, Map<String, Object> headMap, byte[] postByte) {
        return connectNetwork(url, headMap, postByte, HttpType.PUT);
    }

    public String deleteRequestManager(String url, Map<String, Object> headMap, byte[] postByte) {
        return connectNetwork(url, headMap, postByte, HttpType.DELETE);
    }

    private String connectNetwork(String url, Map<String, Object> headMap, byte[] postByte, HttpType method) {
        try {
            if (initConnection(url, headMap, method)) {
                if (postByte != null) {
                    if (mCon != null) {
                        mCon.getOutputStream().write(postByte);
                        mCon.getOutputStream().flush();
                        mCon.getOutputStream().close();
                    } else {
                        mCons.getOutputStream().write(postByte);
                        mCons.getOutputStream().flush();
                        mCons.getOutputStream().close();
                    }
                }
                if (mCon != null) {
                    mCode = mCon.getResponseCode();
                } else {
                    mCode = mCons.getResponseCode();
                }
                if (SLog.debug) SLog.w("Connect Code:" + mCode);
                if (mCode >= 200 && mCode < 300) {
                    if (mCon != null) {
                        mInputStream = mCon.getInputStream();
                    } else {
                        mInputStream = mCons.getInputStream();
                    }
                    String encode = getHeaderVal("Set-Cookie");
                    if (!TextUtils.isEmpty(encode)) {
//                        if (SharedUtils.getString("mSessionId") == null) {
//                            if (SLog.debug)
//                        }
                        if (encode.startsWith("DSESSIONID")) {
                            SPUtils.getInstance().put("mSessionId", encode);
                            SPUtils.getInstance().put("currentTime", System.currentTimeMillis());
                        }
//                        if (SLog.debug)
//                            SLog.e(encode);
//                        if (SharedUtils.getString("mSessionId") == null || (TimeUtils.milliseconds2Unit(System.currentTimeMillis() - SharedUtils.getLong("currentTime"), UNIT_DAY) > 1)) {
//                        }
                    }
                    return getRespContent(getContentEncoding());
                }
                if (mInputStream == null) {
                    try {
                        if (mCon != null) {
                            mInputStream = mCon.getErrorStream();
                        } else {
                            mInputStream = mCons.getErrorStream();
                        }
                        return getRespContent(getContentEncoding());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mCode = ServiceErrorCode;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mCode = SocketCode;
        }
        return null;
    }

    private boolean initConnection(String url, Map<String, Object> headMap, HttpType method) {
        try {
            URL u = new URL(url);
            if (url.indexOf("https") == 0) {
                mCons = (HttpsURLConnection) u.openConnection();
                mCons.setConnectTimeout(TimeOut);
                mCons.setReadTimeout(TimeOut);
                if (mSSLSocket != null) {
                    mCons.setSSLSocketFactory(mSSLSocket);
                }
//				mCons.setInstanceFollowRedirects(false);
            } else {
                mCon = (HttpURLConnection) u.openConnection();
                mCon.setConnectTimeout(TimeOut);
                mCon.setReadTimeout(TimeOut);
//				mCon.setInstanceFollowRedirects(false);
            }
            if (headMap != null) {
                for (String key : headMap.keySet()) {
                    Object val = headMap.get(key);
                    if (SLog.debug) SLog.d(key + ":" + val);
                    if (mCon != null) {
                        mCon.setRequestProperty(key, String.valueOf(val));
                    } else {
                        mCons.setRequestProperty(key, String.valueOf(val));
                    }
                }
            }
            if (mCon != null) {
                mCon.setRequestMethod(method.toString());
            } else {
                mCons.setRequestMethod(method.toString());
            }
            if (HttpType.POST == method || HttpType.PUT == method || HttpType.DELETE == method) {
                if (mCon != null) {
                    mCon.setDoInput(true);
                    mCon.setDoOutput(true);
                } else {
                    mCons.setDoInput(true);
                    mCons.setDoOutput(true);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized String getRespContent(String encoding)
            throws IOException {
        if (encoding.toLowerCase().equals("gzip")) {
            return getRespContentGzip();
        }
        String content = "";
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = mInputStream.read()) != -1) {
                bytestream.write(ch);
            }
            mInputStream.close();
            byte[] byteContent = bytestream.toByteArray();
            content = new String(byteContent, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    private String getRespContentGzip() throws IOException {
        GZIPInputStream gzipis = new GZIPInputStream(mInputStream);
        InputStreamReader inreader = new InputStreamReader(gzipis, StandardCharsets.UTF_8);
        char[] bufArr = new char[128];

        StringBuilder strBuilder = new StringBuilder();
        int nBufLen = inreader.read(bufArr);

        String line;
        while (nBufLen != -1) {
            line = new String(bufArr, 0, nBufLen);
            strBuilder.append(line);
            nBufLen = inreader.read(bufArr);
        }
        inreader.close();
        gzipis.close();
        return strBuilder.toString();
    }

    public String getContentEncoding() {
        String encode = getHeaderVal("content-encoding");
        if (!TextUtils.isEmpty(encode)) {
            return encode;
        }
        return "UTF-8";
    }

    public String getHeaderVal(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        Map<String, List<String>> headMap;
        if (mCon != null) {
            headMap = mCon.getHeaderFields();
        } else {
            headMap = mCons.getHeaderFields();
        }
        if (headMap != null) {
            List<String> vals = headMap.get(key);
            if (vals != null) {
                String v = "";
                for (String val : vals) {
                    v += val + ";";
                }
                return v;
            }
        }
        return null;
    }

    public int getCode() {
        return mCode;
    }

    public void disconnect() {
        try {
            if (mCon != null) {
                mCon.disconnect();
            }
            if (mCons != null) {
                mCons.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String downFile(String url, Map<String, Object> headMap, String savePath) {
        try {
            if (downStream(url, headMap)) {
                FileOutputStream out = new FileOutputStream(savePath);
                byte[] buff = new byte[1024 * 8];
                int temp;
                int fileSize;
                if (mCon != null) {
                    fileSize = mCon.getContentLength();
                } else {
                    fileSize = mCons.getContentLength();
                }
                if (SLog.debug) SLog.d("fileSize:" + fileSize);
                int length = 0;
                while ((temp = mInputStream.read(buff)) != -1) {
                    out.write(buff, 0, temp);
                    length += temp;
                }
                out.flush();
                out.close();
                mInputStream.close();
                if (length != fileSize) {
                    return null;
                }
                return savePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap downFile(String url, Map<String, Object> headMap) {
        if (downStream(url, headMap)) {
            return BitmapFactory.decodeStream(mInputStream);
        }
        return null;
    }

    /**
     * 获取下载的信息
     */
    private boolean downStream(String url, Map<String, Object> headMap) {
        try {
            if (initConnection(url, headMap, HttpType.GET)) {
                if (mCon != null) {
                    mCode = mCon.getResponseCode();
                } else {
                    mCode = mCons.getResponseCode();
                }
                if (SLog.debug) SLog.d("down code:" + mCode);
                if (mCode == 200) {
                    if (mCon != null) {
                        mInputStream = mCon.getInputStream();
                    } else {
                        mInputStream = mCons.getInputStream();
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param url 地址
     * @return
     */

    public String uploadFileManager(String url, Map<String, File> fileParams, Map<String, String> textParams, Map<String, Object> headMap) {
        String hyphens = "--";
        String boundary = UUID.randomUUID().toString();
        String end = "\r\n";
        try {
            if (initConnection(url, headMap, HttpType.POST)) {
                DataOutputStream dos;
                if (mCon != null) {
                    mCon.setChunkedStreamingMode(128 * 1024);
                    mCon.setRequestProperty("Connection", "Keep-Alive");
                    mCon.setRequestProperty("Charset", "UTF-8");
                    mCon.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
                    dos = new DataOutputStream(mCon.getOutputStream());
                } else {
                    mCons.setChunkedStreamingMode(128 * 1024);
                    mCons.setRequestProperty("Connection", "Keep-Alive");
                    mCons.setRequestProperty("Charset", "UTF-8");
                    mCons.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
                    dos = new DataOutputStream(mCons.getOutputStream());
                }
                dos.writeBytes(hyphens + boundary + end);
                if (fileParams != null) {
                    for (String key : fileParams.keySet()) {
                        File file = fileParams.get(key);
                        dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
                                + encode(file.getName())
                                + "\""
                                + end);
                        dos.writeBytes(end);
                        FileInputStream fis = new FileInputStream(file);
                        byte[] buffer = new byte[1024 * 8]; // 8k
                        int count;
                        // 读取文件
                        while ((count = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, count);
                        }
                        fis.close();
                        dos.writeBytes(end);
                        dos.writeBytes(hyphens + boundary + end);
                    }
                }
                if (textParams != null) {
                    //					dos.writeBytes(end);
                    for (String key : textParams.keySet()) {
                        String str = textParams.get(key);
                        dos.writeBytes(hyphens + boundary + end);
                        dos.writeBytes("Content-Disposition: form-data; name=\"" + key
                                + "\"\r\n");
                        dos.writeBytes(end);
                        dos.writeBytes(encode(str) + end);
                    }
                }
                dos.flush();
                dos.writeBytes(hyphens + boundary + hyphens + end);
                dos.writeBytes(end);
                if (mCon != null) {
                    mCode = mCon.getResponseCode();
                } else {
                    mCode = mCons.getResponseCode();
                }
                if (SLog.debug) SLog.d("Upload mCode:" + mCode);
                if (mCode == 200) {
                    if (mCon != null) {
                        mInputStream = mCon.getInputStream();
                    } else {
                        mInputStream = mCons.getInputStream();
                    }
                    return getRespContent(getContentEncoding());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mCode = SocketCode;
        }
        return "";
    }

    /**
     * 中文转码
     *
     * @param value
     * @return
     * @throws Exception
     */
    private String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    public void setSSLSocket(SSLSocketFactory mSSLSocket) {
        this.mSSLSocket = mSSLSocket;
    }
}
