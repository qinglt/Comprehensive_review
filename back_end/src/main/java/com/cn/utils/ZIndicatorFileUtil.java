package org.jit.sose.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jit.sose.config.security.util.DateFormatUtil;
import org.jit.sose.entity.zExpert.ZData;
import org.jit.sose.service.config.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * @author qinghua
 * @date 2022/5/14 16:12
 */
@Slf4j
@Component
public class ZIndicatorFileUtil {

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private FileToPdf fileToPdf;

    /**
     * 虚拟目录
     */
    @Value("${file.staticAccessPath}")
    private String staticAccessPath;

    /**
     * 文件上传路径
     */
    @Value("${file.uploadFolder}")
    private String uploadPath;

    /**
     * 上传指标文件
     *
     * @param file
     * @return
     */
    public ZData uploadIndicatorFile(MultipartFile file) {
        //获取原始文件名,获取上传的文件的原始名称
        String originalFileNameString = file.getOriginalFilename();
        //获取自定义文件名称（不包含基础上传路径）,文件类型+时间戳+_文件名
        String fileName = getIndicatorFileName(originalFileNameString);
        //获取页面文件访问路径
        String accessUrl = getAccessUrl(fileName);
        // 获取文件后缀 ppt,doc,docx,pdf
        String suffix = originalFileNameString.substring(originalFileNameString.lastIndexOf(".") + 1);

        //数据库对应实体类对象
        ZData zData = new ZData();
        zData.setFileName(originalFileNameString);
        zData.setAccessUrl(accessUrl);
        zData.setType(suffix);

        File newFile = getNewFile(fileName);
        try {
            file.transferTo(newFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            log.error("文件写入失败");
        }

        return zData;
    }

    /**
     * 上传指标文件转成pdf, 添加异步注解，因为转换时间可能会比较长(问题：使用异步注解不生效）
     */
    public Future<String> uploadFileToPdf(ZData zData) {

        String accessUrl = zData.getAccessUrl();
        String suffix = zData.getType();

        // 将word转成pdf
        String inPath = getFileLocalPath(accessUrl);
        String outPath = inPath.substring(0, inPath.length() - suffix.length()) + "pdf";

        if ("docx".equals(suffix)) {
            fileToPdf.wordToPdf(inPath, outPath);
        }
        if ("doc".equals(suffix)) {
            fileToPdf.wordToPdf(inPath, outPath);
        }
        if ("xls".equals(suffix) || "xlsx".equals(suffix)) {
            fileToPdf.excelToPdf(inPath, outPath);
        }

        log.info("=========================转换完成==========================");

        return new AsyncResult<String>("=========================转换完成==========================");

    }


    /**
     * 获取指标文件自定义文件名(不包含基础上传路径)
     *
     * @param originalFileNameString 文件原始名称
     * @return 文件名
     */
    public String getIndicatorFileName(String originalFileNameString) {
        String fileName = null;
        // 获取当前时间字符串
        String fileDateCode = DateFormatUtil.formatCode(new Date());
        // 获取文件后缀//ppt,doc,docx,xls,xlsx
        String suffix = originalFileNameString.substring(originalFileNameString.lastIndexOf(".") + 1);
        // 自定义允许上传的文件String数组,允许上传ppt，word,pdf文件
        String[] allowFileSuffix = {"ppt", "doc", "docx", "xls", "xlsx"};
        //File.separator代表系统中默认的路径分隔符，在windows中位"\",在Linux中为"/",fileParentDir的类型有video\,pic\,text\
        String fileParentDir = "text" + File.separator;

        // 判断当前文件类型(后缀)是否允许上传
        boolean b = Arrays.asList(allowFileSuffix).contains(suffix);
        if (b) {
            // 文件名称：时间字符串+文件名全名
            fileName = fileParentDir + fileDateCode + "_" + originalFileNameString;
        } else {
            log.error("当前文件类型禁止上传");
        }
        return fileName;
    }

    /**
     * 根据文件名(不包含基础上传路径) 获取页面访问路径
     *
     * @param originalFileNameString 文件名称
     * @return
     */
    public String getAccessUrl(String originalFileNameString) {
        return staticAccessPath + "/" + originalFileNameString.replace(File.separator, "/");
    }


    /**
     * 根据文件名获取文件对象
     *
     * @param fileName
     * @return
     */
    public File getNewFile(String fileName) {
        // 根据上传路径和文件名创建 文件对象
        //File 类提供了如下三种形式构造方法。
        //File(String path)：如果 path 是实际存在的路径，则该 File 对象表示的是目录；如果 path 是文件名，则该 File 对象表示的是文件。
        //File(String path, String name)：path 是路径名，name 是文件名。
        //File(File dir, String name)：dir 是路径对象，name 是文件名。
        File newFile = new File(uploadPath, fileName);
        // 获取新对象的父目录对象
        //getParentFile().mkdirs()的作用，
        //其作用就是为了避免文件创建失败（该文件所在的文件夹不存在所以创建它所在的文件目录）
        //注意mkdirs() ,方法 ，如果文件夹已经存在，是不会再次创建的
        //getParentFile将会创建出该文件所在位置的绝对路径
        File parentFile = newFile.getParentFile();
        // 判断父目录对象是否存在
        boolean isParentFileExist = parentFile.exists();
        // 父目录不存在，创建父目录对象
        if (!isParentFileExist) {
            parentFile.mkdirs();
        }
        // 判断新对象是否存在
        //当且仅当由抽象路径名确定文件是否存在，则该方法返回布尔值true；否则为false。
        boolean isNewFileExist = newFile.exists();
        // 新对象不存在，创建新对象
        if (!isNewFileExist) {
            try {
                boolean isCreateNewFile = newFile.createNewFile();
                if (!isCreateNewFile) {
                    System.out.println("文件创建失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newFile;
    }

    /**
     * 获取本地真实访问路径
     *
     * @param accessUrl
     * @return
     */
    public String getFileLocalPath(String accessUrl) {
        return accessUrl.replace(staticAccessPath, uploadPath);
    }


}
