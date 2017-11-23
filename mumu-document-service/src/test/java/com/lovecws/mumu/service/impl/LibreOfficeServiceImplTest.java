package com.lovecws.mumu.service.impl;

import com.lovecws.mumu.service.LibreOfficeService;

/**
 * Created by Administrator on 2017/6/28.
 */
public class LibreOfficeServiceImplTest {

    public static void main(String[] args){

        //LibreOfficeService libreOfficeService=new LibreOfficeServiceImpl("2002","D:/Program Files/LibreOffice 5");
        LibreOfficeService libreOfficeService=new LibreOfficeServiceImpl("2002","D:/Program Files (x86)/OpenOffice 4");

        libreOfficeService.office2PDF("d:/SVN使用指南.ppt","D:/SVN使用指南2.pdf");
    }
}
