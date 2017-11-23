package com.lovecws.mumu.service;

import java.io.IOException;

/**
 * Created by Administrator on 2017/6/28.
 * 使用swftools将pdf转化为swf文件
 */
public interface SWFToolsService {

    /**
     * 利用SWFTools工具将pdf转换成swf，转换完后的swf文件名按页码命名
     *
     * @param pdfFilePath  PDF文件存放路径（包括文件名）
     * @param outDirectory 输出目录
     * @throws IOException
     */
    public void pdf2swf(String pdfFilePath, String outDirectory);

    /**
     * 利用SWFTools工具将pdf转换成swf
     * @param pdfFilePath  PDF文件存放路径（包括文件名）
     * @param outDirectory 输出目录
     * @param swfFileName  swf文件名(无需后缀)，为空时取pdf文件名
     * @throws IOException
     */
    public void pdf2swf(String pdfFilePath, String outDirectory, String swfFileName);

    /**
     *Takes a font file (.ttf, .afm, .pfa, .pfb and all other types supported by FreeType) and converts it into a SWF file.
     * @param fontFilePath
     * @param outFilePath
     */
    public void font2swf(String fontFilePath, String outFilePath);

    /**
     * 将多张图片生成swf文件
     * @param gifFiles 图片
     * @param swfFilePath 生成swf文件
     * @param width 宽度
     * @param height 高度
     * @param rate 旋转
     * @param loop 循环次数
     */
    public void gif2swf(String[] gifFiles, String swfFilePath,String width,String height,String rate,String loop);
}
