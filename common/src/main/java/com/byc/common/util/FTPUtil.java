package com.byc.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;

import javax.annotation.PreDestroy;
import java.io.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baiyc
 * 2019/6/14/014 20:34
 * Description：
 */
@Slf4j
@Getter
public class FTPUtil {

    public String hostname = "47.105.192.33";       // ftp服务器地址
    public Integer port = 21;                       // ftp服务器端口号默认为21
    public String username = "big";                 // ftp登录账号
    public String password = "big";                 // ftp登录密码

    public FTPClient ftpClient = null;
    private static FTPUtil instance = null;

    /** 五分钟的毫秒数 */
    private static final long TEN_MINUTE = 5 * 60 * 1000L;

    /**
     * 单例
     */
    public synchronized static FTPUtil getInstance() {
        if (instance == null) {
            instance = new FTPUtil();
        }
        return instance;
    }

    /**
     * 初始化ftp服务器
     */
    public void init() {
        if (ftpClient != null && ftpClient.isConnected() && ftpClient.isAvailable()) {
            ftpClient.enterLocalPassiveMode(); // 防止假卡死
            return;
        }
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            ftpClient.setConnectTimeout(10 * 1000); // 连接超时超时
            ftpClient.connect(hostname, port); // 连接ftp服务器
            ftpClient.login(username, password); // 登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.info("ftp服务器登录失败:" + this.hostname + ":" + this.port);
                close();
                return;
            }
            log.info("ftp服务器登录成功:" + this.hostname + ":" + this.port);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            //设置Linux环境:如果ftp服务器部署在linux系统中，此处注释应该打开，若为Windows服务器则不需要
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            ftpClient.configure(conf);

            ftpClient.enterLocalPassiveMode(); // 防止假卡死
            ftpClient.setRemoteVerificationEnabled(false);
            ftpClient.setDataTimeout(1 * 60 * 1000); // 获取数据超时 一分钟
            ftpClient.setReceiveBufferSize(1024 * 1024);
            ftpClient.setBufferSize(1024 * 1024);
        } catch (Exception e) {
            log.error("登录出错：", e);
        }
    }

    /**
     * 关闭客户端
     */
    @PreDestroy
    public void close() {
        if (ftpClient != null) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (Exception e) {
                log.debug("登出失败：", e);
            }
            ftpClient = null;
            log.info("成功退出登录");
        } else {
            log.info("并没有登录，无需退出。请先调用初始化");
        }
    }



    /**
     * 功能描述： 改变目录路径 创建人： wangsen 创建时间：2018年4月12日 上午10:28:28 修改人 修改时间 修改内容
     *
     * @param directory
     *            要改变的路径
     * @return boolean
     */
    private boolean enterDir(String directory) {
        boolean flag;
        try {
            ftpClient.makeDirectory(directory);
            flag = ftpClient.changeWorkingDirectory(directory);
        } catch (IOException e) {
            flag = false;
        }
        log.debug("进入文件夹:{}[{}]",directory,flag?"成功":"失败");
        return flag;
    }

    /**
     * 功能描述：  初始化文件
     */
    private boolean createFile(File localFile) {
        try {
            localFile.mkdirs();
            if (localFile.exists())
                localFile.delete();
            localFile.createNewFile();
            return true;
        } catch (IOException e) {
            log.error("创建文件失败", e);
            return false;
        }
    }

    /**
     * * 下载文件 *
     * @param pathname      FTP服务器文件目录 *
     * @param filename      文件名称 *
     * @param localpath     下载后的文件路径 *
     * @return boolean      是否成功
     */
    public boolean downloadFile(String pathname, String filename, String localpath) {
        boolean flag = false;
        OutputStream os;
        try {
            init();
            enterDir(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equals(file.getName())) {
                    File localFile = new File(localpath + File.separator + file.getName());
                    createFile(localFile);
                    os = new FileOutputStream(localFile);
                    log.info("下载中..." + filename);
                    flag = ftpClient.retrieveFile(file.getName(), os);
                }
            }
            log.info("下载文件成功:{}", filename);
        } catch (Exception e) {
            log.error("下载文件失败:{}", filename, e);
        }
        close();
        return flag;
    }

    /**
     * 功能描述： 下载目录下所有文件
     * @param pathname      FTP服务器文件目录 *
     * @param localpath     下载后的文件路径 * void
     */
    public boolean downloadDir(String pathname, String localpath) {
        boolean flag = false;
        try {
            init();
            enterDir(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (".".equals(file.getName()) || "..".equals(file.getName())) {
                    continue;
                }
                FileOutputStream os;
                long size = file.getSize();
                File localFile = new File(localpath + File.separator + file.getName());
                if (localFile.length() == size) {
                    log.info("文件{}已下载完成", localFile.getName());
                    continue;
                }

                if (localFile != null && localFile.exists() && localFile.isFile() && localFile.length() > 0 && localFile.length() < size) {
                    // 需要断点续传
                    os = new FileOutputStream(localFile, true);
                } else {
                    if (!createFile(localFile)) {
                        return false;
                    }
                    os = new FileOutputStream(localFile);
                }
                log.info("正在下载文件：{}，总大小：{}", file.getName(), size);
                long start = System.currentTimeMillis();
                try {
                    InputStream in = null;
                    try {
                        byte[] bytes = new byte[1024 * 32];
                        long step = size /100;
                        long process = 0L;
                        long localSize = localFile.length();
                        if (localSize > size) {
                            log.error("本地文件大于服务器文件,终止下载");
                            createFile(localFile);
                            return false;
                        }
                        if (localSize > 0) {
                            ftpClient.setRestartOffset(localSize);
                        }
                        int c;
                        in = ftpClient.retrieveFileStream(file.getName());
                        if (in == null) {
                            log.info("连接异常，将退出登录");
                            try {
                                ftpClient.logout();
                            } catch (Exception e) {
                                log.debug("关闭连接错误", e); // 可以忽略的错误
                            }
                            try {
                                ftpClient.disconnect();
                            } catch (Exception e) {
                                log.debug("关闭连接错误", e); // 可以忽略的错误
                            }
                            ftpClient = null;
                            log.info("退出登录成功");
                            return false;
                        }

                        while((c = in.read(bytes))!= -1){
                            os.write(bytes, 0, c);
                            localSize += c;
                            long nowProcess = localSize /step;
                            if(size > 50000000 && nowProcess > process){ // 大于50兆才显示进度
                                process = nowProcess;
                                log.info("{}%", process);
                                if (System.currentTimeMillis() - start > TEN_MINUTE) { // 大于指定时间
                                    log.info("时间已到，未下载部分将在下次任务中下载");
                                    try {
                                        ftpClient.logout();
                                    } catch (Exception e) {
                                        log.debug("关闭连接错误", e); // 可以忽略的错误
                                    }
                                    try {
                                        ftpClient.disconnect();
                                    } catch (Exception e) {
                                        log.debug("关闭连接错误", e); // 可以忽略的错误
                                    }
                                    return false;
                                }
                            }
                        }
                        log.info("文件下载完成到：{}", localpath + "/" + file.getName());
                    } catch (SocketTimeoutException e) {
                        log.info("下载出错：", e);
                        return false;
                    } catch (Exception e) {
                        log.error("下载出错：", e);
                        return false;
                    }
                    finally {
                        try {
                            in.close();
                        } catch (Exception e) {
                            log.debug("关闭流错误", e); // 可以忽略的错误
                        }
                    }
                } catch (Exception e) {
                    log.error("下载文件错误：{}", file.getName(), e);
                    return false;
                } finally {
                    try {
                        os.close();
                    } catch (Exception e) {
                        log.debug("关闭连接异常"); // 可以忽略的错误类型
                    }
                }
            }
            flag = true;
        } catch (Exception e) {
            log.error("下载文件错误：{}", pathname, e);
            return false;
        }
        return flag;
    }
    
    /**
     * Description:上传文件到ftp
     * params:[]
     * return:boolean
     */
    public boolean uploadFile(String pathname, String fileName, InputStream inputStream){
        boolean flag = false;
        try {
            init();
            enterDir(pathname);
            ftpClient.storeFile(fileName, inputStream);
            flag = true;
        } catch (Exception e) {
            log.error("上传文件失败",e);
        } finally{
            try {
                close();
                inputStream.close();
            } catch (IOException e) {
                log.error("关闭文件流失败",e);
            }
        }
        return flag;
    }

    /**
     * Description:删除文件
     * params:[pathname, filename]
     * return:boolean
     */
    public boolean deleteFile(String pathname, String filename){
        boolean flag = false;
        try {
            init();
            enterDir(pathname);
            ftpClient.deleteFile(filename);
        } catch (Exception e) {
            log.error("删除文件失败");
        } finally{
           close();
        }
        return flag;
    }

    /**
     * 功能描述：  获取列表
     * @param path		路径
     * @return FTPFile[]
     */
    public List<FTPData> getDirList(String path) {
        List<FTPData> result = new ArrayList();
        try {
            init();
            enterDir(path);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for(FTPFile file:ftpFiles){
                result.add(new FTPData(file.getName(),file.getSize(),file.getType()==0?"file":"dir"));
            }
        } catch (Exception e) {
            log.error("获取目录【{}】列表错误：", path, e);
        } finally {
            close();
        }
        return result;
    }

    @Data
    @AllArgsConstructor
    public class FTPData {
        private String fileName;
        private long size;
        private String type;
    }

}
