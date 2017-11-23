package com.lovecws.mumu.service.impl;

import com.lovecws.mumu.service.SWFToolsService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
public class SWFToolsServiceImpl implements SWFToolsService{

    private String swftoolsPath;
    private String languagedirPath;

    public String getSwftoolsPath() {
        return swftoolsPath;
    }

    public void setSwftoolsPath(String swftoolsPath) {
        this.swftoolsPath = swftoolsPath;
    }

    public String getLanguagedirPath() {
        return languagedirPath;
    }

    public void setLanguagedirPath(String languagedirPath) {
        this.languagedirPath = languagedirPath;
    }

    public SWFToolsServiceImpl(String swftoolsPath,String languagedirPath){
        this.swftoolsPath=swftoolsPath;
        this.languagedirPath=languagedirPath;
    }


    /**
     * 利用SWFTools工具将pdf转换成swf，转换完后的swf文件名按页码命名
     * @param pdfFilePath  PDF文件存放路径（包括文件名）
     * @param outDirectory 输出目录
     * @throws IOException
     */
    @Override
    public void pdf2swf(String pdfFilePath, String outDirectory) {

        //文件路径
        String filePath = (StringUtils.isBlank(outDirectory)) ? FilenameUtils.getFullPath(pdfFilePath) + "swf" : outDirectory;

        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
        if(!filePath.endsWith("/")){
            filePath=filePath+"/";
        }

        Process pro = null;
        try {
            if (isWindowsSystem()) {
                //如果是windows系统
                //命令行命令
                String cmd = swftoolsPath + "pdf2swf.exe" + " \"" + pdfFilePath + "\" -s -t -T 9 -o \"" + filePath + "%.swf\"";
                //Runtime执行后返回创建的进程对象
                pro = Runtime.getRuntime().exec(cmd);
            } else {
                //如果是linux系统,路径不能有空格，而且一定不能用双引号，否则无法创建进程
                String[] cmd = new String[6];
                cmd[0] = swftoolsPath + "pdf2swf";
                cmd[1] = pdfFilePath;
                cmd[2] = "-s" + languagedirPath;
                cmd[3] = "-t";
                cmd[4] = "-T 9";
                cmd[5] = "-o" + filePath + "%" + ".swf";
                //Runtime执行后返回创建的进程对象
                pro = Runtime.getRuntime().exec(cmd);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
        new DoOutput(pro.getInputStream()).start();
        new DoOutput(pro.getErrorStream()).start();
        try {

            //调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
            pro.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用SWFTools工具将pdf转换成swf
     * @param pdfFilePath  PDF文件存放路径（包括文件名）
     * @param outDirectory 输出目录
     * @param swfFileName  swf文件名(无需后缀)，为空时取pdf文件名
     * @throws IOException
     */
    @Override
    public void pdf2swf(String pdfFilePath, String outDirectory, String swfFileName) {

        //文件路径
        String filePath = (StringUtils.isBlank(outDirectory)) ? FilenameUtils.getFullPath(pdfFilePath) + "swf" : outDirectory;

        if (StringUtils.isBlank(swfFileName)) {
            swfFileName = FilenameUtils.getBaseName(pdfFilePath);
        }


        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }

        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }

        Process pro = null;

        try {
            if (isWindowsSystem()) {
                //如果是windows系统
                //命令行命令
                String cmd = swftoolsPath + "pdf2swf.exe" + " \"" + pdfFilePath + "\" -s -t -T 9 -o \"" + filePath + swfFileName + ".swf\"";
                //Runtime执行后返回创建的进程对象
                pro = Runtime.getRuntime().exec(cmd);
            } else {
                //如果是linux系统,路径不能有空格，而且一定不能用双引号，否则无法创建进程
                String[] cmd = new String[6];
                cmd[0] = swftoolsPath + "pdf2swf";
                cmd[1] = pdfFilePath;
                cmd[2] = "-s" + languagedirPath;
                cmd[3] = "-t";
                cmd[4] = "-T 9";
                cmd[5] = "-o" + filePath + swfFileName + ".swf";
                //Runtime执行后返回创建的进程对象
                pro = Runtime.getRuntime().exec(cmd);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
        new DoOutput(pro.getInputStream()).start();
        new DoOutput(pro.getErrorStream()).start();
        try {
            //调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
            pro.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes a font file (.ttf, .afm, .pfa, .pfb and all other types supported by FreeType) and converts it into a SWF file.
     * @param fontFilePath font file
     * @param swfFilePath output file
     */
    @Override
    public void font2swf(String fontFilePath, String swfFilePath){
        if(StringUtils.isBlank(fontFilePath)||!new File(fontFilePath).exists()){
            throw new IllegalArgumentException("输入文件不能为空");
        }
        if(StringUtils.isBlank(swfFilePath)){
            swfFilePath=fontFilePath;
        }
        int lastIndexOf = fontFilePath.lastIndexOf(".");
        if(lastIndexOf!=-1){
            swfFilePath=fontFilePath.substring(0,lastIndexOf)+".swf";
        }else{
            swfFilePath=swfFilePath+".swf";
        }

        Process pro = null;
        try {
            if (isWindowsSystem()) {
                //如果是windows系统
                //命令行命令
                String cmd = swftoolsPath + "font2swf.exe" + " \"" + fontFilePath + "\" -o \"" + swfFilePath;
                //Runtime执行后返回创建的进程对象
                pro = Runtime.getRuntime().exec(cmd);
            } else {
                //如果是linux系统,路径不能有空格，而且一定不能用双引号，否则无法创建进程
                String[] cmd = new String[3];
                cmd[0] = swftoolsPath + "font2swf";
                cmd[1] = fontFilePath;
                cmd[2] = "-o" + swfFilePath;
                //Runtime执行后返回创建的进程对象
                pro = Runtime.getRuntime().exec(cmd);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
        new DoOutput(pro.getInputStream()).start();
        new DoOutput(pro.getErrorStream()).start();
        try {
            //调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
            pro.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将多张图片生成swf文件
     * @param gifFiles 图片
     * @param swfFilePath 生成swf文件
     * @param width 宽度
     * @param height 高度
     * @param rate 旋转
     * @param loop 循环次数
     */
    @Override
    public void gif2swf(String[] gifFiles, String swfFilePath,String width,String height,String rate,String loop){
        if(gifFiles==null||gifFiles.length==0||StringUtils.isBlank(swfFilePath)){
            throw new IllegalArgumentException("输入图片、输出swf文件不能为空");
        }

        Process pro = null;
        try {
            //如果是windows系统
            if (isWindowsSystem()) {
                StringBuilder builder=new StringBuilder(swftoolsPath + "gif2swf.exe ");
                if(!StringUtils.isBlank(width)){
                    builder.append(" -X \""+width+"\"");
                }
                if(!StringUtils.isBlank(height)){
                    builder.append(" -Y \""+height+"\"");
                }
                builder.append(" -o \""+swfFilePath+"\"");
                if(!StringUtils.isBlank(rate)){
                    builder.append(" -r \""+rate+"\"");
                }
                for (String gifFile:gifFiles){
                    builder.append("  \""+gifFile+"\"");
                }
                //命令行命令
                String cmd = builder.toString();
                System.out.println(cmd);
                //Runtime执行后返回创建的进程对象
                pro = Runtime.getRuntime().exec(cmd);
            }
            //如果是linux系统,路径不能有空格，而且一定不能用双引号，否则无法创建进程
            else {
                List<String> list=new ArrayList<String>();
                list.add(swftoolsPath + "gif2swf");
                if(!StringUtils.isBlank(width)){
                    list.add(" -X "+width);
                }
                if(!StringUtils.isBlank(height)){
                    list.add(" -Y "+height);
                }
                list.add(" -o "+swfFilePath);
                if(!StringUtils.isBlank(rate)){
                    list.add(" -r "+rate);
                }
                for (String gifFile:gifFiles){
                    list.add(" "+gifFile);
                }
                //Runtime执行后返回创建的进程对象
                pro = Runtime.getRuntime().exec((String[])list.toArray());
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
        new DoOutput(pro.getInputStream()).start();
        new DoOutput(pro.getErrorStream()).start();
        try {
            //调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
            pro.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断是否是windows操作系统
     * @return
     */
    private static boolean isWindowsSystem() {
        String p = System.getProperty("os.name");
        return p.toLowerCase().indexOf("windows") >= 0 ? true : false;
    }



    /**
     * 类名称：DoOutput<br>
     * 类描述：多线程内部类 读取转换时cmd进程的标准输出流和错误输出流，这样做是因为如果不读取流，进程将死锁<br>
     * 创建人：lovecws<br>
     * 创建时间：2016年11月10日 下午1:32:51<br>
     *
     * @version v1.0
     */
    private static class DoOutput extends Thread {
        public InputStream is;

        //构造方法
        public DoOutput(InputStream is) {
            this.is = is;
        }

        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
            String str = null;
            try {
                //这里并没有对流的内容进行处理，只是读了一遍
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
