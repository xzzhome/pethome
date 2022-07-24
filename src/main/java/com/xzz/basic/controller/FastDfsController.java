package com.xzz.basic.controller;

import com.xzz.basic.util.JsonResult;
import com.xzz.basic.util.FastDfsUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/fastDfs")
public class FastDfsController {
    //@RequestPart对复杂表单项的处理，默认name="file",与上传文件的name属性值一致,required = true必添项
    @PostMapping
    public JsonResult upload(@RequestPart(required = true,value = "file") MultipartFile file){
        try {
            //获取原始文件名称，为了获取文件后缀（索引+1为了不截取到.）
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            //调用工具栏的上传方法，字节编码数组的file文件和后缀
            String filePath =  FastDfsUtils.upload(file.getBytes(), extName);
            //把上传后的路径通过JsonResult返回前端
            return JsonResult.me().setResultObj(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("上传失败!");
        }
    }

    @DeleteMapping
    public JsonResult del(@RequestParam(required = true,value = "path") String path){
        try {
            String pathTmp = path.substring(1); // /goup1/xxxxx/yyyy
            String groupName =  pathTmp.substring(0, pathTmp.indexOf("/")); // goup1
            String remotePath = pathTmp.substring(pathTmp.indexOf("/")+1);// xxxxx/yyyy
            FastDfsUtils.delete(groupName, remotePath);
            return  JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("删除失败!");
        }
    }
}