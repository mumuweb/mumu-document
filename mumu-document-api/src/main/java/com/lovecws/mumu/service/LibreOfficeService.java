package com.lovecws.mumu.service;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/28.
 * 使用libreoffice将文档转化为pdf文档
 */
public interface LibreOfficeService {

    /**
     * 将word文档转化为pdf文档
     * @param wordPath word文档路径
     * @param pdfPath  pdf保存路径
     * @return
     */
    Map<String,String> office2PDF(String wordPath, String pdfPath);


    /**
     * 将word文档转化为pdf文档
     * @param wordPath word文档路径
     * @param pdfPath  pdf保存路径
     * @return
     */
    Map<String, String> office2PDF(File wordPath, File pdfPath);
}
