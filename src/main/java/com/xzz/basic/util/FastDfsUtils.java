package com.xzz.basic.util;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

/**
 * fastDfs工具类
 */
public class FastDfsUtils {

    //从classpath：resources中的内容最终会编译到classpath下
    public static String CONF_FILENAME  = FastDfsUtils.class.getClassLoader()
            .getResource("fdfs_client.conf").getFile();


    /**
     * 上传文件
     * @param file
     * @param extName
     * @return
     */
    public static  String upload(byte[] file,String extName) {

        try {
            ClientGlobal.init(CONF_FILENAME);

            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;

            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            
            String fileIds[] = storageClient.upload_file(file,extName,null);

            //  /group1/M00/00/09/rBEACmKXF8-AUc6KAANsldwx3H4713.jpg
            return  "/"+fileIds[0]+"/"+fileIds[1];

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
    /**
     * 上传文件
     * @param extName
     * @return
     */
    public static  String upload(String path,String extName) {
 
        try { 
            ClientGlobal.init(CONF_FILENAME);
 
            TrackerClient tracker = new TrackerClient(); 
            TrackerServer trackerServer = tracker.getConnection(); 
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            String fileIds[] = storageClient.upload_file(path, extName,null);
             
            return  "/"+fileIds[0]+"/"+fileIds[1];
 
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 下载文件
     * @param groupName
     * @param fileName
     * @return
     */
    public static byte[] download(String groupName,String fileName) {
        try {
 
            ClientGlobal.init(CONF_FILENAME);
 
            TrackerClient tracker = new TrackerClient(); 
            TrackerServer trackerServer = tracker.getConnection(); 
            StorageServer storageServer = null;
 
            StorageClient storageClient = new StorageClient(trackerServer, storageServer); 
            byte[] b = storageClient.download_file(groupName, fileName);
            return  b;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        } 
    }


    /**
     * 删除文件
     * @param groupName
     * @param fileName
     */
    public static void delete(String groupName,String fileName){
        try { 
            ClientGlobal.init(CONF_FILENAME);
 
            TrackerClient tracker = new TrackerClient(); 
            TrackerServer trackerServer = tracker.getConnection(); 
            StorageServer storageServer = null;
 
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            int i = storageClient.delete_file(groupName,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("删除异常,"+e.getMessage());
        } 
    }

//    public static void main(String[] args) {
//        FastDfsUtils.delete("group1","M00/00/0F/oYYBAGJ6IGaAWQeOAAbMJw3URKE510.gif");
//    }
}