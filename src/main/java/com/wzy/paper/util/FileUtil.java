package com.wzy.paper.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static ArrayList<String> FileNameListDC;

    public static void move(String frompath, String topath) {
        FileNameListDC = new ArrayList<String>();
        makeDir(topath);

        int num = scanningFile(frompath, FileNameListDC);
        if (num > 0) {
            for (String fullname : FileNameListDC) {
                try {
                    transFile(fullname, frompath, topath);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static  void writeList(File file,List list,boolean isAppend) throws IOException {
        if(!file.exists()){
            file.createNewFile();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String ref = StringUtil.deleteChSpace(StringUtil.subForeSen((String) list.get(i)));

            // 如果是中文
            if (StringUtil.isChineseString(ref)) {
                sb.append(list.get(i) + "\n");
            }
        }

        FileUtils.write(file,sb.toString(),"GBK",isAppend);
    }

    public static String getPath() {
        return System.getProperty("user.dir") + "/src/main/resources/";//程序
//		String path = FileUtil.class.getProtectionDomain().getCodeSource()  
//	                .getLocation().getFile();  //jar包
//		path=path.substring(0, path.lastIndexOf("/"));  
//		return path+"/resources/";
    }

    public static InputStream getInputStream(String configFile) throws FileNotFoundException {
//		return FileUtil.class.getClassLoader().getResourceAsStream(configFile);//运行环境中
        return new FileInputStream(new File(FileUtil.getPath() + configFile));//jar包环境中

    }

    public static void transFile(String fullpath, String completepath) throws Exception {
        File newFile = new File(completepath);
        if (!newFile.exists()) {
            int byteread = 0;
            FileInputStream fileInputStream = new FileInputStream(fullpath);
            FileOutputStream fileOutputStream = new FileOutputStream(completepath);
            byte[] buffer = new byte[1444];
            while ((byteread = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteread);
            }
            fileInputStream.close();
            fileOutputStream.close();
        }
    }

    public static void transFile(String fullpath, String rootpath, String completepath) throws Exception {
        File file = new File(completepath);
        if (!file.isDirectory() || !file.exists()) {
            file.mkdirs();
        }
        File oldFile = new File(fullpath);
        String name = oldFile.getName();
        String middlepath = fullpath.substring(rootpath.length(), fullpath.indexOf(name));
        String newpath = completepath + middlepath;
        File newFile = new File(newpath);
        if (!newFile.isDirectory() || !newFile.exists()) {
            newFile.mkdirs();
        }
        newpath = newpath + name;
        newFile = new File(newpath);
        if (!newFile.exists()) {
            oldFile.renameTo(newFile);

//            int byteread = 0;
//            FileInputStream fileInputStream = new FileInputStream(fullpath);
//            FileOutputStream fileOutputStream = new FileOutputStream(newpath);
//            byte[] buffer = new byte[1444];
//            while ((byteread = fileInputStream.read(buffer)) != -1) {
//                fileOutputStream.write(buffer, 0, byteread);
//            }
//            fileInputStream.close();
//            fileOutputStream.close();
        }

    }

    public static int scanningFile(String path, List<String> fileNameList) {
        File file = new File(path);
        File[] filelist = file.listFiles();
        if (filelist == null) {
            file.delete();
            return 0;
        }

        for (File filesub : filelist) {
            if (filesub.isDirectory()) {
                int num = scanningFile(path + "\\" + filesub.getName(), fileNameList);
                if (num == 0) break;
            } else {
                fileNameList.add(filesub.getPath());
            }
        }
        return fileNameList.size();
    }

    public static String getFileName(File file){
        String fileName = file.getName();
        return fileName;
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static void makeDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
