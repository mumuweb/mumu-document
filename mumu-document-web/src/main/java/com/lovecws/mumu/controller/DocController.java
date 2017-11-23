package com.lovecws.mumu.controller;

import com.alibaba.fastjson.JSON;
import com.lovecws.mumu.service.LibreOfficeService;
import com.lovecws.mumu.service.PDFAttributeService;
import com.lovecws.mumu.service.SWFToolsService;
import com.lovecws.mumu.util.FileUtils;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/6/28.
 */
@Controller
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private SWFToolsService swfToolsService;
    @Autowired
    private LibreOfficeService libreOfficeService;
    @Autowired
    private PDFAttributeService pdfAttributeService;

    @Value("#{configProperties['base_path']}")
    private String BASE_PATH;

    @Value("#{configProperties['base_url']}")
    private String BASE_URL;

    @Value("#{configProperties['base_host']}")
    private String BASE_HOST;

    /**
     * 采用同步的方式进行文件的上传
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest request, HttpServletResponse response) {
        return doc(request, response, false);
    }

    /**
     * 采用异步的方式进行文件的上传
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload/async", method = RequestMethod.POST)
    public String uploadAsync(HttpServletRequest request, HttpServletResponse response) {
        return doc(request, response, true);
    }

    private static final ExecutorService executor;

    static {
        executor = Executors.newCachedThreadPool();
    }

    public String doc(HttpServletRequest request, HttpServletResponse response, boolean async) {
        try {
            header(request, response);
            String UPLOAD_FILENAME = base();

            String officeFileName = uploadFile(UPLOAD_FILENAME, request);
            String suffix = "";//文件的后缀
            String name = "";//文件的名称
            String fileName = "";//文件hashcode名称
            int lastIndexOf = officeFileName.lastIndexOf(".");
            if (lastIndexOf != -1) {
                name = officeFileName.substring(0, lastIndexOf).toLowerCase();
                fileName = String.valueOf(Math.abs(name.hashCode()));
                suffix = officeFileName.substring(lastIndexOf + 1);

                officeFileName = fileName + "." + suffix;
            }

            Map<String, String> map = new HashMap<String, String>();

            //pdf文件相对路径
            String officeFilePath = BASE_URL + UPLOAD_FILENAME + officeFileName;
            //pdf文件相对路径

            String pdfFilePath = BASE_URL + UPLOAD_FILENAME + fileName + ".pdf";
            //pdf缩略图相对路径
            String pdfThumbFilePath = BASE_URL + UPLOAD_FILENAME + fileName + ".png";
            //swf相对路径
            String swfFilePath = BASE_URL + UPLOAD_FILENAME;

            //将文档转化为pdf文档(如果文档本来就是pdf格式的 那么就不需要转换了)
            if (!officeFileName.endsWith("pdf")) {
                libreOfficeService.office2PDF(BASE_PATH + officeFilePath, BASE_PATH + pdfFilePath);
            } else {
                pdfFilePath = officeFilePath;
            }
            //解决非utf-8的txt转换出现乱码的问题
            if (officeFileName.endsWith("txt") && !CharEncoding.UTF_8.equals(FileUtils.getCharset(officeFileName))) {
                //创建utf8文件
                FileUtils.writeFile(officeFileName, FileUtils.getFileContent(officeFileName));
            }
            //解决excel因为宽度太大 导致一页无法装满，所以使得一页excel生成多个pdf页数；解决方法:缩放excel
            if (officeFileName.endsWith("xls") || officeFileName.endsWith("xlsx")) {
                //TODO
            }
            //获取pdf文档的数量
            int count = pdfAttributeService.count(BASE_PATH + pdfFilePath);
            //获取封面图片
            pdfAttributeService.thumb(BASE_PATH + pdfFilePath, BASE_PATH + pdfThumbFilePath);
            if (async) {
                final String finalPdfFilePath = pdfFilePath;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //将pdf转化为swf文件，
                        swfToolsService.pdf2swf(BASE_PATH + finalPdfFilePath, BASE_PATH + swfFilePath);
                    }
                });
            } else {
                //将pdf转化为swf文件
                swfToolsService.pdf2swf(BASE_PATH + pdfFilePath, BASE_PATH + swfFilePath);
            }

            map.put("office", BASE_HOST + officeFilePath);
            map.put("pdf", BASE_HOST + pdfFilePath);
            map.put("thumb", BASE_HOST + pdfThumbFilePath);
            map.put("count", String.valueOf(count));
            map.put("swf", BASE_HOST + swfFilePath);
            try {
                map.put("name", URLEncoder.encode(name, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            map.put("suffix", suffix);
            System.out.println(map);
            return JSON.toJSONString(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }

    private void header(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 表明它允许"http://xxx"发起跨域请求
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 表明在xxx秒内，不需要再发送预检验请求，可以缓存该结果
        response.setHeader("Access-Control-Allow-Methods", "*");
        // 表明它允许xxx的外域请求
        response.setHeader("Access-Control-Max-Age", "*");
        // 表明它允许跨域请求包含xxx头
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
    }

    private String base() {
        //上传文件目录
        String UPLOAD_FILENAME = "";
        //上传文件的路径
        if (!BASE_PATH.endsWith("/")) {
            BASE_PATH = BASE_PATH + "/";
        }
        if (!BASE_URL.endsWith("/")) {
            BASE_URL = BASE_URL + "/";
        }
        if (!BASE_HOST.endsWith("/")) {
            BASE_HOST = BASE_HOST + "/";
        }
        UPLOAD_FILENAME = UUID.randomUUID().toString().replace("-", "") + "/";
        return UPLOAD_FILENAME;
    }

    /**
     * 上传文件
     *
     * @param UPLOAD_FILENAME 上传文件的目录
     * @param request
     * @return
     */
    public String uploadFile(String UPLOAD_FILENAME, HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Iterator<String> iterator = multiRequest.getFileNames();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    MultipartFile multipartFile = multiRequest.getFile(key);
                    if (multipartFile != null) {
                        String name = multipartFile.getOriginalFilename();
                        int lastIndexOf = name.lastIndexOf(".");
                        String postFix = "";
                        String fileName = "";
                        if (lastIndexOf != -1) {
                            postFix = name.substring(lastIndexOf).toLowerCase();//获取文件后缀
                            fileName = name.substring(0, lastIndexOf); //获取文件的名称
                        }
                        //如果文件目录不存在 则创建
                        File dirFile = new File(BASE_PATH + BASE_URL + UPLOAD_FILENAME);
                        if (!dirFile.isDirectory()) {
                            dirFile.mkdirs();
                        }
                        String filePath = BASE_PATH + BASE_URL + UPLOAD_FILENAME + Math.abs(fileName.toLowerCase().hashCode()) + postFix;
                        File file = new File(filePath);
                        file.setWritable(true, false);

                        multipartFile.transferTo(file);
                        return fileName + postFix;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

