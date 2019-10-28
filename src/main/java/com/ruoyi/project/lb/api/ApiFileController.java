package com.ruoyi.project.lb.api;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.content.FileTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

@Api("文件接口")
@Controller
@RequestMapping("/api/file")
public class ApiFileController {
    private static final Logger log = LoggerFactory.getLogger(ApiFileController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 根据文件类型上传不同目录
     */
    @ApiOperation("上传文件 type(0 照片，1 二维码 ，2 身份证 ，3收款码)")
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult uploadByType(Integer type, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            request.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 上传文件路径
            String filePath = RuoYiConfig.getProfile()+ FileTypeEnum.findByValue(type).getPath();

            /** 页面控件的文件流* */
            MultipartFile file = null;
            Map map =multipartRequest.getFileMap();
            for (Iterator i = map.keySet().iterator(); i.hasNext();) {
                Object obj = i.next();
                file=(MultipartFile) map.get(obj);
            }

            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            log.error("文件上传错误",e);
            return null;
        }
    }
}
