package com.lovecws.mumu.service.impl;

import com.lovecws.mumu.service.PDFAttributeService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/6/28.
 */
public class PDFAttributeServiceImpl implements PDFAttributeService{

    @Override
    public void thumb(String pdfFilePath, String imagePath) {
        FileOutputStream out = null;
        ImageOutputStream outImage=null;
        try {
            float zoom = 1;
            float rotation = 0f;
            Document document = new Document();
            document.setFile(pdfFilePath);

            BufferedImage img = (BufferedImage) document.getPageImage(0, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);

            Iterator<?> iter = ImageIO.getImageWritersBySuffix("png");
            ImageWriter writer = (ImageWriter) iter.next();
            File outFile = new File(imagePath);

            if (!new File(FilenameUtils.getFullPath(imagePath)).exists()) {
                new File(FilenameUtils.getFullPath(imagePath)).mkdirs();
            }
            out = new FileOutputStream(outFile);

            outImage = ImageIO.createImageOutputStream(out);
            writer.setOutput(outImage);
            writer.write(new IIOImage(img, null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(outImage);
        }
    }

    @Override
    public int images(String pdfFilePath, String baseImagePath, String suffix) {
        if (StringUtils.isEmpty(suffix)) suffix = "png";
        FileOutputStream out = null;
        ImageOutputStream outImage = null;
        if (!baseImagePath.endsWith("/")) baseImagePath = baseImagePath + "/";
        if (!new File(baseImagePath).exists()) {
            new File(baseImagePath).mkdirs();
        }
        try {
            float zoom = 3;
            float rotation = 0f;
            Document document = new Document();
            document.setFile(pdfFilePath);
            int numberOfPages = document.getPageTree().getNumberOfPages();
            for (int i = 0; i < numberOfPages; i++) {
                BufferedImage img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);

                String imagePath = baseImagePath + (i + 1) + "." + suffix;
                out = new FileOutputStream(new File(imagePath));
                outImage = ImageIO.createImageOutputStream(out);

                Iterator<?> iter = ImageIO.getImageWritersBySuffix(suffix);
                ImageWriter writer = (ImageWriter) iter.next();
                writer.setOutput(outImage);
                writer.write(new IIOImage(img, null, null));
            }
            return numberOfPages;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(outImage);
        }
        return 0;
    }

    @Override
    public int count(String pdfFilePath) {
        try {
            Document document = new Document();
            document.setFile(pdfFilePath);
            return document.getPageTree().getNumberOfPages();
        } catch (PDFException e) {
            e.printStackTrace();
        } catch (PDFSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
