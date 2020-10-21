package com.study.loader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by shliangluo on 2020/10/21.
 */
public class MyClassLoader extends ClassLoader{

    public static void main(String[] args) {
        MyClassLoader loader = new MyClassLoader();
        try {
            Class<?> clazz =  loader.findClass("Hello");
            Object obj = clazz.newInstance();
            Method method = obj.getClass().getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "com/study/loader/" + name +".xlass";
        //加载文件路径
        byte[] xlassBytes = null;
        byte[] classBytes = null;
        Path path = null;
        Class<?> clazz = null;
        try {
            filePath = URLDecoder.decode(filePath,"UTF-8");
            System.out.println("filePath:" + filePath);
            path = Paths.get(new URI(filePath));
            xlassBytes = Files.readAllBytes(path);

            //将xlass文件内容转为class，否则无法加载
            classBytes = xlassToClass(xlassBytes);
            clazz = defineClass(name, classBytes, 0, classBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     *
     * @param resource
     * @return
     */
    private byte[] xlassToClass(byte[] resource){
        if(resource == null || resource.length == 0){
            throw new IllegalArgumentException("参数非法");
        }
        byte[] classBytes = new byte[resource.length];
        for(int i = 0;i < resource.length;i++){
            classBytes[i] = (byte)(255 - resource[i]);
        }
        return classBytes;

    }
}
