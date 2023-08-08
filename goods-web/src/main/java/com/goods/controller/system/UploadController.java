package com.goods.controller.system;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.common.error.SystemException;
import com.goods.common.model.system.ImageAttachment;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.system.ImageAttachmentVO;
import com.goods.system.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 文件上传
 **/
@Slf4j
@Api(tags = "系统模块-文件上传相关接口")
@RestController
@RequestMapping("/system/upload")
public class UploadController {

	@Value("${minio.endpointUrl}")
    private String endpointUrl; // endpointUrl=http://192.168.200.129:9000

    @Value("${minio.accessKey}")
    public String accessKey;

    @Value("${minio.secreKey}")
    public String secreKey;

    @Value("${minio.bucketName}")
    public String bucketName;


    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片文件
     * @param file
     * @return
     */
    @ApiOperation(value = "上传文件")
    @RequiresPermissions({"upload:image"})
    @PostMapping("/image")
    public ResponseBean<String> uploadImage(MultipartFile file) throws IOException, SystemException {
        String realPath= null;
        try {
            realPath = uploadService.uploadImage(file,endpointUrl,accessKey,secreKey,bucketName);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseBean.error("上传出问题了");
        }
        return ResponseBean.success(realPath);
    }


    /**
     * 附件列表(图片)
     *
     * @return
     */
    @ApiOperation(value = "附件列表", notes = "模糊查询附件列表")
    @GetMapping("/findImageList")
    public ResponseBean<PageInfo<ImageAttachment>> findImageList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                 @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize,
                                                                 ImageAttachmentVO imageAttachmentVO) {
        PageHelper.startPage(pageNum,pageSize);
        List<ImageAttachment> imageAttachmentVOList=uploadService.findImageList(imageAttachmentVO);
        PageInfo<ImageAttachment> pageInfo = new PageInfo<>(imageAttachmentVOList);
        return ResponseBean.success(pageInfo);
    }

    /**
     * 删除图片
     * @param id
     * @return
     */
    @ApiOperation(value = "删除图片", notes = "删除数据库记录,删除图片服务器上的图片")
    @RequiresPermissions("attachment:delete")
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) throws SystemException {
        uploadService.delete(id);
        return ResponseBean.success();
    }

}
