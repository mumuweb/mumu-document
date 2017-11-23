package com.lovecws.mumu.service.impl;

import com.lovecws.mumu.service.LibreOfficeService;
import org.apache.commons.lang3.StringUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/28.
 */
public class LibreOfficeServiceImpl implements LibreOfficeService{

    private String officePort;
    private String officeHome;

    private final OfficeManager officeManager;

    private final OfficeDocumentConverter documentConverter;

    public LibreOfficeServiceImpl(String officePort,String officeHome) {
        this.officePort=officePort;
        this.officeHome=officeHome;

        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();

        //设置转换端口，默认为2002
        if (StringUtils.isNotBlank(this.officePort)) {
            configuration.setPortNumber(Integer.parseInt(this.officePort));
        }

        //设置office安装目录
        if (StringUtils.isNotBlank(officeHome)) {
            configuration.setOfficeHome(new File(this.officeHome));
        }

        officeManager = configuration.buildOfficeManager();
        documentConverter = new OfficeDocumentConverter(this.officeManager);

        //初始化
        init();
    }

    public synchronized void init() {
        if(!officeManager.isRunning()){
            officeManager.start();
        }
    }

    public synchronized void destroy() {
        if (officeManager.isRunning()) {
            officeManager.stop();
        }
    }

    /**
     * 将word文档转化为pdf文档
     * @param wordPath word文档路径
     * @param pdfPath pdf保存路径
     * @return
     */
    @Override
    public Map<String,String> office2PDF(String wordPath,String pdfPath){
        documentConverter.convert(new File(wordPath),new File(pdfPath));
        return null;
    }

    /**
     * 将word文档转化为pdf文档
     * @param wordPath word文档路径
     * @param pdfPath  pdf保存路径
     * @return
     */
    @Override
    public Map<String, String> office2PDF(File wordPath, File pdfPath) {
        documentConverter.convert(wordPath, pdfPath);
        return null;
    }

    public String getOfficePort() {
        return officePort;
    }

    public void setOfficePort(String officePort) {
        this.officePort = officePort;
    }

    public String getOfficeHome() {
        return officeHome;
    }

    public void setOfficeHome(String officeHome) {
        this.officeHome = officeHome;
    }

    public OfficeManager getOfficeManager() {
        return officeManager;
    }

    public OfficeDocumentConverter getDocumentConverter() {
        return documentConverter;
    }
}
