package com.byc.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;

import javax.annotation.PreDestroy;
import java.io.*;
import java.net.SocketTimeoutException;

/**
 * Created by baiyc
 * 2019/6/14/014 20:34
 * Description：
 */
@Slf4j
public class FTPUtils {
    // ftp服务器地址
    public String hostname = "47.105.192.33"; //TODO 换上你的地址
    // ftp服务器端口号默认为21
    public Integer port = 21;
    // ftp登录账号
    public String username = "big"; //TODO 换上你的用户名
    // ftp登录密码
    public String password = "big"; //TODO 换上你的密码

    public FTPClient ftpClient = null;

    /** 单例 */
    private static FTPUtils instance = null;

    /** 五分钟的毫秒数 */
    private static final long TEN_MINUTE = 5 * 60 * 1000L;

    /**
     * 功能描述：  获取连接实例
     * 创建人： wangsen
     * 创建时间：2018年5月16日 上午9:51:59
     * 修改人                        修改时间                          修改内容
     * @return
     * FtpUtils
     */
    public synchronized static FTPUtils getInstance() {
        if (instance == null) {
            instance = new FTPUtils();
        }
        return instance;
    }

    /** 私有化构造器 */
    private FTPUtils() {
        super();
    }

    /**
     * 功能描述：  注销登录
     * 创建人： wangsen
     * 创建时间：2018年5月16日 上午10:59:02
     * 修改人                        修改时间                          修改内容
     * void
     */
    public static void logout() {
        if (instance != null && instance.ftpClient != null) {
            try {
                instance.ftpClient.logout();
            } catch (Exception e) {
                log.debug("登出失败：", e); // 可以忽略的错误
            }
            try {
                instance.ftpClient.disconnect();
            } catch (Exception e) {
                log.debug("关闭链接失败：", e); // 可以忽略的错误
            }
            instance.ftpClient = null;
            log.info("成功退出登录");
        } else {
            log.info("并没有登录，无需退出");
        }
    }

    /**
     * 初始化ftp服务器
     */
    public void initFtpClient() {
        if (ftpClient != null && ftpClient.isConnected() && ftpClient.isAvailable()) {
            ftpClient.enterLocalPassiveMode(); // 防止假卡死
            return;
        }
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            ftpClient.connect(hostname, port); // 连接ftp服务器
            ftpClient.login(username, password); // 登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.info("ftp服务器登录失败:" + this.hostname + ":"
                        + this.port);
            }
            log.info("ftp服务器登录成功:" + this.hostname + ":"
                    + this.port);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            //设置Linux环境:如果ftp服务器部署在linux系统中，此处注释应该打开，若为Windows服务器则不需要
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            ftpClient.configure(conf);

            ftpClient.enterLocalPassiveMode(); // 防止假卡死
            ftpClient.setRemoteVerificationEnabled(false);
            ftpClient.setConnectTimeout(10 * 1000); // 登录十秒超时
            ftpClient.setDataTimeout(1 * 60 * 1000); // 获取数据超时 一分钟
            ftpClient.setReceiveBufferSize(1024 * 1024);
            ftpClient.setBufferSize(1024 * 1024);
        } catch (Exception e) {
            log.error("登录出错：", e);
        }
    }


    @PreDestroy
    public void closeConn() {
        if (ftpClient != null) {
            try {
                ftpClient.logout();
            } catch (IOException e1) {
                log.debug("关闭连接错误", e1); // 可以忽略的错误
            }
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                log.debug("关闭连接错误", e); // 可以忽略的错误
            }
        }
    }

    /**
     * 功能描述： 改变目录路径 创建人： wangsen 创建时间：2018年4月12日 上午10:28:28 修改人 修改时间 修改内容
     *
     * @param directory
     *            要改变的路径
     * @return boolean
     */
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                log.debug("进入文件夹" + directory + " 成功！");
            } else {
                log.info("进入文件夹" + directory + " 失败！开始创建文件夹");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    /**
     * * 下载文件 *
     * @WARNNING 该方法可能不太好用，我用的是下面下载文件夹的方法，若想下载单独的文件请参考FtpUtils.downloadDir(String, String, String)
     * @param pathname
     *            FTP服务器文件目录 *
     * @param filename
     *            文件名称 *
     * @param localpath
     *            下载后的文件路径 *
     * @return
     */
    public boolean downloadFile(String pathname, String filename,
                                String localpath) {
        boolean flag = false;
        OutputStream os = null;
        try {
            initFtpClient();
            // 切换FTP目录
            changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localpath + "/" + file.getName());
                    if (localFile.exists()) { // 已经存在则删除
                        localFile.delete();
                    }
                    // 然后再创建
                    localFile.createNewFile();
                    os = new FileOutputStream(localFile);
                    log.info("下载中：" + filename);
                    ftpClient.retrieveFile(file.getName(), os);
                }
            }
            flag = true;
            log.info("下载文件成功:{}", filename);
        } catch (Exception e) {
            log.error("下载文件失败:{}", filename, e);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (Exception e) {
                    log.debug("关闭连接异常"); // 可以忽略的错误类型
                }
            }
        }
        return flag;
    }

    /**
     * 功能描述： 下载目录下所有文件 创建人： wangsen 创建时间：2018年4月11日 下午6:47:10 修改人 修改时间 修改内容
     *
     * @param pathname
     *            FTP服务器文件目录 *
     * @param localpath
     *            下载后的文件路径 * void
     */
    public boolean downloadDir(String pathname, String localpath) {
        boolean flag = false;
        try {
            initFtpClient();
            // 切换FTP目录
            changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (".".equals(file.getName()) || "..".equals(file.getName())) {
                    continue;
                }
                FileOutputStream os;
                long size = file.getSize();
                File localFile = new File(localpath + "/" + file.getName());
                if (localFile.length() == size) {
                    log.info("文件{}已下载完成", localFile.getName());
                    continue;
                }

                if (localFile != null && localFile.exists() && localFile.isFile() && localFile.length() > 0 && localFile.length() < size) {
                    // 需要断点续传
                    os = new FileOutputStream(localFile, true);
                } else {
                    if (!checkFile(localFile)) {
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
                            checkFile(localFile);
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
     * 功能描述：  初始化文件
     * 创建人： wangsen
     * 创建时间：2018年4月12日 下午2:26:58
     * 修改人  修改时间  修改内容
     * @param localFile
     * void
     */
    private boolean checkFile(File localFile) {
        if (localFile.exists()) { // 已经存在则删除
            localFile.delete();
        }
        // 然后再创建
        localFile.mkdirs();
        if (localFile.exists()) {
            localFile.delete();
        }
        try {
            localFile.createNewFile();
            return true;
        } catch (IOException e) {
            log.error("创建文件失败", e);
            return false;
        }
    }

    /**
     * 功能描述：  获取列表
     * 创建人： wangsen
     * 创建时间：2018年4月12日 上午11:02:57
     * 修改人  修改时间  修改内容
     * @param path		路径
     * @return
     * FTPFile[]
     */
    public FTPFile[] getDirList(String path) {
        try {
            initFtpClient();
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(path);
            return ftpClient.listFiles();
        } catch (Exception e) {
            log.error("获取目录【{}】列表错误：", path, e);
        }
        return null;
    }

    /**
     * 功能描述：  获取目录下的第一个文件夹
     * 创建人： wangsen
     * 创建时间：2018年4月12日 上午11:07:51
     * 修改人  修改时间   修改内容
     * @param pathName		目录
     * @return
     * FTPFile
     */
    public FTPFile getFirst(String pathName) {
        try {
            initFtpClient();
            // 切换FTP目录
            changeWorkingDirectory(pathName);
            FTPFile[] listFiles = ftpClient.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                return null;
            }
            for (FTPFile ftpFile : listFiles) {
                if (".".equals(ftpFile.getName()) || "..".equals(ftpFile.getName())) {
                    continue;
                } else {
                    return ftpFile;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("获取目录【{}】列表错误：", pathName, e);
        }
        return null;
    }
}
