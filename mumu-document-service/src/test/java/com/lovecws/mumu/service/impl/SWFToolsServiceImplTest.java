package com.lovecws.mumu.service.impl;

import com.lovecws.mumu.service.SWFToolsService;
import org.junit.Test;

/**
 * Created by Administrator on 2017/6/28.
 */
public class SWFToolsServiceImplTest {

    @Test
    public void pdf2swf() {
        SWFToolsService swfToolsService=new SWFToolsServiceImpl("D:/Program Files (x86)/SWFTools/",null);
        swfToolsService.pdf2swf("D:/FreeMarker_Manual_zh_CN-1.pdf", "D:/swf");
    }

    @Test
    public void font2swf() {
        SWFToolsService swfToolsService=new SWFToolsServiceImpl("D:/Program Files (x86)/SWFTools/",null);
        swfToolsService.font2swf("D:/arial.ttf", null);
    }

    @Test
    public void gif2swf() {
        SWFToolsService swfToolsService=new SWFToolsServiceImpl("D:/Program Files (x86)/SWFTools/",null);
        swfToolsService.gif2swf(new String[]{"D:/1.gif"},"d:/gif.swf",null,null,null,null);
    }
}
