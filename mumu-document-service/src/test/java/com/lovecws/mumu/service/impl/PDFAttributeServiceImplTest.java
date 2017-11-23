package com.lovecws.mumu.service.impl;

import com.lovecws.mumu.service.PDFAttributeService;
import org.junit.Test;

/**
 * Created by Administrator on 2017/6/28.
 */
public class PDFAttributeServiceImplTest {

    @Test
    public void thumb(){
        PDFAttributeService pdfAttributeService=new PDFAttributeServiceImpl();
        pdfAttributeService.thumb("D:\\ppt.pdf","D:\\ppt.png");
    }


    @Test
    public void images(){
        PDFAttributeService pdfAttributeService=new PDFAttributeServiceImpl();
        pdfAttributeService.images("D:\\BaiduNetdiskDownload\\从PAXOS到ZOOKEEPER分布式一致性原理与实践.pdf","D:\\BaiduNetdiskDownload\\paxos",null);
    }

    @Test
    public void count() {
        PDFAttributeService pdfAttributeService = new PDFAttributeServiceImpl();
        int totalCount = pdfAttributeService.count("D:\\PPT黄金.pdf");
        System.out.println(totalCount);
    }
}
