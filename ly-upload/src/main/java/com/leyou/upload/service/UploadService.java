package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/23 0023 - 上午 11:02
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties prop;

    //private static final List<String> ALLOW_TYPES = Arrays.asList("image/jpeg","image/png","image/bmp");

    public String uploadImage(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if(!prop.getAllowTypes().contains(contentType)){
                throw new LyException(ExceptionEnums.FILE_TYPE_NOT_ERROR);
            }

            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                throw new LyException(ExceptionEnums.FILE_TYPE_NOT_ERROR);
            }


            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);


            /*File dest = new File("E:\\编程代码空间\\java2\\leyou\\upload",file.getOriginalFilename());
            file.transferTo(dest);*/
            return prop.getBaseUrl() + storePath.getFullPath();
        } catch (IOException e) {
            log.error("上传文件失败!",e);
            throw new LyException(ExceptionEnums.UPLOAD_FILE_ERROR);
        }
    }
}
