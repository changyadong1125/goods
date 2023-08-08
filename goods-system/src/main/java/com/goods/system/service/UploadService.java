package com.goods.system.service;

import com.goods.common.error.SystemException;
import com.goods.common.model.system.ImageAttachment;
import com.goods.common.vo.system.ImageAttachmentVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface UploadService {
     String uploadImage(MultipartFile file,String endpointUrl,String accessKey,String secreKey,String bucketName) throws Exception; //图片上传
    List<ImageAttachment> findImageList(ImageAttachmentVO imageAttachmentVO); //图片列表
    void delete(Long id) throws SystemException; //删除图片
}
