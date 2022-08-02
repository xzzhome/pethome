package com.xzz.basic.service.impl;

import com.xzz.basic.annotation.PreAuthorize;
import com.xzz.basic.service.IPermissionScanService;
import com.xzz.basic.util.ClassUtils;
import com.xzz.system.domain.Permission;
import com.xzz.system.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 业务实现类：扫描权限注解
 */
@Service
public class PermissionScanServiceImpl implements IPermissionScanService {
    private static final String  PKG_PREFIX   = "com.xzz.";
    private static final String  PKG_SUFFIX   = ".controller";

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public void scanPermission() {
        //获取com.xzz下面所有的模块目录
        String path = this.getClass().getResource("/").getPath() + "/com/xzz/";
        File file = new File(path);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });

        //获取com.xzz.*.controller里面所有的类
        Set<Class> clazzes = new HashSet<>();
        for (File fileTmp : files) {
            System.out.println("===============权限注解解析：获取所有的包==============");
            System.out.println(fileTmp.getName());
            clazzes.addAll(ClassUtils.getClasses(PKG_PREFIX+fileTmp.getName()+PKG_SUFFIX));
        }

        for (Class clazz : clazzes) {
            Method[] methods = clazz.getMethods();
            if (methods==null || methods.length<1)
                return;
            for (Method method : methods) {
                String uri = getUri(clazz, method);
                try {
                    PreAuthorize preAuthorizeAnno = method.getAnnotation(PreAuthorize.class);
                    if (preAuthorizeAnno==null)
                        continue;
                    String name = preAuthorizeAnno.name();
                    String permissionSn = preAuthorizeAnno.value();
                    Permission permissionTmp = permissionMapper.loadBySn(permissionSn);
                    //如果不存在就添加
                    if (permissionTmp == null) {
                        Permission permission = new Permission();
                        permission.setName(name);       //t_permission表中的权限名
                        permission.setSn(permissionSn); //t_permission表中的权限编号
                        permission.setUrl(uri);           //t_permission表中的权限路径
                        permissionMapper.save(permission);
                    }else{
                        //如果存在就修改
                        permissionTmp.setName(name);
                        permissionTmp.setSn(permissionSn);
                        permissionTmp.setUrl(uri);
                        permissionMapper.update(permissionTmp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //获取t_permission表中的url  //@RequestMapping("/department") //@GetMapping("/{id}")
    private String getUri(Class clazz, Method method) {
        //获取类上的请求路径：/department
        String classPath = "";
        Annotation annotation = clazz.getAnnotation(RequestMapping.class);
        if (annotation!=null){
            RequestMapping requestMapping = (RequestMapping) annotation;
            String[] values = requestMapping.value();
            if(values!=null&&values.length>0){
                classPath = values[0];
                if (!"".equals(classPath)&&!classPath.startsWith("/"))
                    classPath="/"+classPath;
            }
        }
        //以下是获取方法上的请求路径：/{id}
        GetMapping getMapping =  method.getAnnotation(GetMapping.class);
        String methodPath = "";
        if (getMapping!=null){
            String[] values = getMapping.value();
            if(values!=null&&values.length>0){
                methodPath = values[0];
                if (!"".equals(methodPath)&&!methodPath.startsWith("/"))
                    methodPath="/"+methodPath;
            }
        }
        PostMapping postMapping =  method.getAnnotation(PostMapping.class);
        if (postMapping!=null){
            String[] values = postMapping.value();
            if(values!=null&&values.length>0){
                methodPath = values[0];
                if (!"".equals(methodPath)&&!methodPath.startsWith("/"))
                    methodPath="/"+methodPath;
            }
        }
        DeleteMapping deleteMapping =  method.getAnnotation(DeleteMapping.class);
        if (deleteMapping!=null){
            String[] values = deleteMapping.value();
            if(values!=null&&values.length>0){
                methodPath = values[0];
                if (!"".equals(methodPath)&&!methodPath.startsWith("/"))
                    methodPath="/"+methodPath;
            }
        }
        PutMapping putMapping =  method.getAnnotation(PutMapping.class);
        if (putMapping!=null){
            String[] values = putMapping.value();
            if(values!=null&&values.length>0){
                methodPath = values[0];
                if (!"".equals(methodPath)&&!methodPath.startsWith("/"))
                    methodPath="/"+methodPath;
            }

        }
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        if (patchMapping!=null){
            String[] values = patchMapping.value();
            if(values!=null&&values.length>0){
                methodPath = values[0];
                if (!"".equals(methodPath)&&!methodPath.startsWith("/"))
                    methodPath="/"+methodPath;
            }
        }
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping!=null){
            String[] values = requestMapping.value();
            if(values!=null&&values.length>0){
                methodPath = values[0];
                if (!"".equals(methodPath)&&!methodPath.startsWith("/"))
                    methodPath="/"+methodPath;
            }
        }
        return classPath+methodPath;  // /department/{id}
    }

    private String getPermissionSn(String value) {
        String regex = "\\[(.*?)]";
        Pattern p = Pattern.compile("(?<=\\()[^\\)]+");
        Matcher m = p.matcher(value);
        String permissionSn =null;
        if (m.find()){
            permissionSn= m.group(0).substring(1, m.group().length()-1);
        }
        return permissionSn;
    }

    public static void main(String[] args) {
        String regex = "\\[(.*?)]";
        String text = "hasAuthority('permission:add')";
        Pattern p = Pattern.compile("(?<=\\()[^\\)]+");
        Matcher m = p.matcher(text);
        if (m.find()) {
            System.out.println(m.group(0).substring(1, m.group().length()-1));
        }
    }
}