package com.byc.finance.controller;

import com.byc.common.mvc.R;
import com.byc.common.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baiyc
 * 2019/6/14/014 16:34
 * Description：
 */
@Slf4j
@Controller
public class FTPController {

    @Value("${video.local.path}")
    private String localPath;
    private String ftpPath = "big";
    private int bufferSize = 1024 * 1024 * 10;
    private static Map<String,String> progress = new HashMap();

    @RequestMapping("playLocalVideo")
    public void playVideo(HttpServletResponse response,String fileName){
        OutputStream os = null;
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(localPath+File.separator+fileName);
            int size=fis.available(),c,s=0;
            response.setContentType("video/*");
            response.setBufferSize(bufferSize);
            os = new BufferedOutputStream(response.getOutputStream());
            DecimalFormat df = new DecimalFormat("0.00%");
            byte[] data=new byte[1024 * 1024];
            while ((c = fis.read(data)) != -1){
                os.write(data, 0, c);
                log.info("已读取：{}，总共：{},进度：{}",formatSize(s += c),formatSize(size),df.format((double)s/size));
            }
        }catch(Exception e){
            log.error("文件不存在");
        }finally {
            try {
                if(os != null)os.close();
                if(fis != null)fis.close();
            } catch (IOException e) {
                log.error("关闭流异常");
            }
        }
    }
    @RequestMapping("playFtpVideo")
    public void downloadFile(HttpServletResponse response,String fileName){
        FTPClient ftpClient = null;
        BufferedInputStream in = null;
        OutputStream os = null;
        FTPUtil ftpUtil = null;
        try {
            ftpUtil = FTPUtil.getInstance();
            ftpUtil.init();
            ftpClient = ftpUtil.getFtpClient();
            ftpClient.changeWorkingDirectory(ftpPath);

            response.setContentType("video/*"); //设置返回的文件类型
            response.setBufferSize(bufferSize);
            os = new BufferedOutputStream(response.getOutputStream()); //得到向客户端输出二进制数据的对象
            FTPFile[] ftpFiles = ftpClient.listFiles();
            double size = 0L;
            for (FTPFile file:ftpFiles){
                if(file.getName().equals(fileName))
                    size = file.getSize();
            }
            if(size==0){
                response.getWriter().print("视频文件不存在");
                return;
            }
            in = new BufferedInputStream(ftpClient.retrieveFileStream(fileName));
            int c,s=0;
            DecimalFormat df = new DecimalFormat("0.00%");
            byte[] bytes = new byte[1024 * 1024];
            while((c = in.read(bytes))!= -1) {
                os.write(bytes, 0, c);
                if(s >= bufferSize)
                    os.flush();
                log.info("已读取：{}，总共：{},进度：{}",formatSize(s += c),formatSize(size),df.format(s/size));
            }
        } catch (Exception e) {
            log.error("读取异常");
        } finally {
            try {
                if(os != null)os.close();
                if(in != null)in.close();
                ftpUtil.close();
            } catch (IOException e) {
                log.error("关闭资源异常");
            }
        }
    }

    private static String formatSize(double size){
        DecimalFormat df = new DecimalFormat("0.00M");
        return df.format(size/1024/1024);
    }

    @ResponseBody
    @RequestMapping("uploadFile")
    public R doUpload(@RequestParam("file") MultipartFile file,String fileName,String folder){
        if(file==null || StringUtils.isEmpty(fileName) || StringUtils.isEmpty(folder))
            return R.fail();
        try {
            InputStream inputStream = file.getInputStream();
            if(FTPUtil.getInstance().uploadFile(folder,fileName,inputStream))
                return R.succ();
            else
                return R.fail();
        }catch (Exception e){
            log.error("上传出错");
            return R.fail();
        }
    }
}
