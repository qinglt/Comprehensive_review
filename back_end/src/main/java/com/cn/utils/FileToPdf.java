package org.jit.sose.util;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author qinghua
 * @date 2022/5/20 12:02
 */
@Slf4j
@Component
public class FileToPdf {

    /**
     * word转pdf
     * @param wordFilePath
     * @param pdfFilePath
     * @return {@link boolean }
     * @author qinghua
     * @date 2022/5/21 21:06
     */
    public boolean wordToPdf(String wordFilePath, String pdfFilePath){
        boolean result = false;
        try {
            File file = new File(pdfFilePath);
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(wordFilePath);
            doc.save(os, SaveFormat.PDF);
            os.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(wordFilePath + "+word文件转换成pdf失败");
        }
        return result;
    }

    /**
     * excel转pdf
     * @param excelFilePath  excel文件地址
     * @param pdfFilePath 转出的pdf存放地址
     */
    public boolean excelToPdf(String excelFilePath, String pdfFilePath){
        boolean result = false;
        try {
            //加载Excel文档
            Workbook wb = new Workbook();
            wb.loadFromFile(excelFilePath);
            //调用方法保存为PDF格式
            wb.saveToFile(pdfFilePath, FileFormat.PDF);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(excelFilePath + "+word文件转换成pdf失败");
        }
        return result;
    }

}
