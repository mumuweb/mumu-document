package com.lovecws.mumu.service;

/**
 * Created by Administrator on 2017/6/28.
 * 获取pdf的封面图片、pdf总页数
 */
public interface PDFAttributeService {

    /**
     * 获取pdf封面图片
     * @param pdfFilePath pdf路径
     * @param imgPath 封面图片路径
     */
    public void thumb(String pdfFilePath,String imgPath);

    /**
     * 将pdf文件转化为图片集合
     *
     * @param pdfFilePath   pdf路径
     * @param baseImagePath 图片目录
     * @return
     */
    public int images(String pdfFilePath, String baseImagePath, String suffix);

    /**
     * 获取pdf总页数
     * @param pdfFilePath pdf路径
     * @return
     */
    public int count(String pdfFilePath);
}
