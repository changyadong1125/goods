package com.goods.system.service.impl;

import com.goods.common.error.SystemCodeEnum;
import com.goods.common.error.SystemException;
import com.goods.common.model.system.ImageAttachment;
import com.goods.common.utils.FdfsUtil;
import com.goods.common.vo.system.ImageAttachmentVO;
import com.goods.system.mapper.ImageAttachmentMapper;
import com.goods.system.service.UploadService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ImageAttachmentMapper attachmentMapper;

	@Override
    public String uploadImage(MultipartFile file,String endpointUrl,String accessKey,String secreKey,String bucketName) throws Exception {
        String url = "";
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endpointUrl)
                            .credentials(accessKey, secreKey)
                            .build();
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            //  获取文件的后缀名
            String originalFilename = file.getOriginalFilename(); // atguigu.jpg;
            //  获取文件名称
            String fileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(originalFilename);
            System.out.println("file:"+file);

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                            file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            //  url: http://192.168.200.129:9000/gmall/67204606-8538-4413-b62d-138d813fabc4.jpg
            url=endpointUrl+"/"+bucketName+"/"+fileName;
            System.out.println("url:\t"+url);
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public List<ImageAttachment> findImageList(ImageAttachmentVO imageAttachmentVO) {
        Example o = new Example(ImageAttachment.class);
        Example.Criteria criteria = o.createCriteria();
        o.setOrderByClause("create_time desc");
        if (imageAttachmentVO.getMediaType() != null && !"".equals(imageAttachmentVO.getMediaType())) {
            criteria.andEqualTo("mediaType", imageAttachmentVO.getMediaType());
        }
        if (imageAttachmentVO.getPath() != null && !"".equals(imageAttachmentVO.getPath())) {
            criteria.andLike("path", "%" + imageAttachmentVO.getPath() + "%");
        }
        return attachmentMapper.selectByExample(o);
    }

    @Override
    @Transactional
    public void delete(Long id) throws SystemException {
        ImageAttachment image = attachmentMapper.selectByPrimaryKey(id);
        if(image==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"图片不存在");
        }else {
            attachmentMapper.deleteByPrimaryKey(id);

        }
    }
}
