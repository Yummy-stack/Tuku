package com.tuku.manager;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectResult;

import com.tuku.tukuModel.bo.result.picture.UploadLocalResult;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukucommon.exception.BusinessException;
import com.tuku.tukucommon.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.tuku.tukucommon.constant.file.FileConstant.UPLOAD_COS_PATH;
import static com.tuku.tukucommon.constant.file.FileConstant.UPLOAD_PATH;


@Component
@Slf4j
public class FileManager {
//    @Resource
//    private IPictureService pictureService;

    @Resource
    private CosFileManager cosFileManager;

    @Resource
    private COSClient cosClient;

    @Value("${PIC_BUCKET_NAME}")
    private String PIC_BUCKET_NAME;

    @Value("${COS_URL_BUCKET_ONE}")
    private String COS_URL_BUCKET_ONE;

    /**
     * 上传图片到本地的方法
     *
     * @param multipartFile 文件
     * @param userId        用户id
     */
    public UploadLocalResult uploadPictureToLocal(MultipartFile multipartFile, Long userId) {
        try {
            // 校验参数是否合法
            ThrowUtils.throwIf(multipartFile == null, "上传的文件为空");
            ThrowUtils.throwIf(userId == null, "用户未登录");
            validPicture(multipartFile);
            // 构造文件名称
            String originalFilename = multipartFile.getOriginalFilename();
            ThrowUtils.throwIf(originalFilename == null || originalFilename.isEmpty(), "文件名为空");
            String suffix = FileUtil.getSuffix(originalFilename);
            ThrowUtils.throwIf(suffix.isEmpty(), "文件后缀为空");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH-mm");
            String timeStr = LocalDateTime.now().format(formatter);
            String fileName = IdUtil.fastSimpleUUID() + "-" + timeStr + "." + suffix;
            // 将文件上传到Local
            File dir = FileUtil.file(UPLOAD_PATH + File.separator + userId);
            FileUtil.mkdir(dir);
            File dest = new File(dir, fileName);
            multipartFile.transferTo(dest);

            UploadLocalResult uploadLocalResult = new UploadLocalResult();
            String picUrl = dir.getPath() + File.separator + fileName;
            uploadLocalResult.setPicName(fileName);
            uploadLocalResult.setPicUrl(picUrl);
            uploadLocalResult.setPicFormat(suffix);
            uploadLocalResult.setPicSize(multipartFile.getSize());
            // TODO 上传到Local的文件长、宽属性先写死
            uploadLocalResult.setPicWidth(5);
            uploadLocalResult.setPicHeight(5);
            double scaleValue = NumberUtil.round(5 * 1.0 / 5, 2).doubleValue();
            uploadLocalResult.setPicScale(scaleValue);
            // 返回值
            return uploadLocalResult;
        } catch (IOException e) {
            log.error("文件上传失败 ====> :{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.error("系统出现了异常 ====> :{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 上传图片到COS的方法
     *
     * @param multipartFile 文件
     * @param userId        用户id
     */
    public UploadLocalResult uploadPictureToCOS(MultipartFile multipartFile, Long userId) {
        File dest = null;
        try {
            // 校验参数是否合法
            ThrowUtils.throwIf(multipartFile == null, "上传的文件为空");
            ThrowUtils.throwIf(userId == null, "用户未登录");
            validPicture(multipartFile);
            // 构造文件名称
            String originalFilename = multipartFile.getOriginalFilename();
            ThrowUtils.throwIf(originalFilename == null || originalFilename.isEmpty(), "文件名为空");
            String suffix = FileUtil.getSuffix(originalFilename);
            ThrowUtils.throwIf(suffix.isEmpty(), "文件后缀为空");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH-mm");
            String timeStr = LocalDateTime.now().format(formatter);
            String fileName = IdUtil.fastSimpleUUID() + "-" + timeStr + "." + suffix;
            // 将文件上传到COS
            File dir = FileUtil.file(UPLOAD_PATH + File.separator + userId);
            FileUtil.mkdir(dir);
            dest = new File(dir, fileName);
            multipartFile.transferTo(dest);

            String cosFilePath = UPLOAD_COS_PATH + "/" + userId + "/" + fileName;
            PutObjectResult putObjectResult = cosFileManager.putFile(PIC_BUCKET_NAME, dest, cosFilePath);
            UploadLocalResult uploadLocalResult = new UploadLocalResult();
            uploadLocalResult.setPicName(fileName);
            URL cosUrl = cosClient.getObjectUrl(PIC_BUCKET_NAME, cosFilePath);
            uploadLocalResult.setPicUrl(COS_URL_BUCKET_ONE + cosUrl.getPath());

            uploadLocalResult.setPicFormat(suffix);
            uploadLocalResult.setPicSize(multipartFile.getSize());
            uploadLocalResult.setPicColor(putObjectResult.getContentMd5());

            BufferedImage image = ImageIO.read(dest);
            uploadLocalResult.setPicWidth(image.getWidth());
            uploadLocalResult.setPicHeight(image.getHeight());
            double scaleValue = NumberUtil.round(image.getWidth() * 1.0 / image.getHeight(), 2).doubleValue();
            uploadLocalResult.setPicScale(scaleValue);

            return uploadLocalResult;
        } catch (IOException e) {
            log.error("文件上传失败 ====> :{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.error("系统出现了异常 ====> :{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (dest != null) {
                dest.delete();
            }
        }
    }


    /**
     * 通过上传URL的方式来上传图片
     *
     * @param uploadUrl 图片的URL地址
     * @param userId    上传用户
     * @return 上传结果
     */
    public UploadLocalResult uploadPictureToLocalByUrl(String uploadUrl, Long userId) {
        try {
            // 校验参数是否合法
            ThrowUtils.throwIf(uploadUrl == null, "上传的Url参数为空");
            ThrowUtils.throwIf(userId == null, "用户未登录");
            validUrlOfPic(uploadUrl);
            // 下载图片字节数组
            byte[] imageBytes = HttpUtil.downloadBytes(uploadUrl);
            BufferedImage image = ImgUtil.read(new ByteArrayInputStream(imageBytes));

            String suffix = FileUtil.getSuffix(uploadUrl);
            ThrowUtils.throwIf(suffix.isEmpty(), "文件后缀为空");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH-mm");
            String timeStr = LocalDateTime.now().format(formatter);
            String fileName = IdUtil.fastSimpleUUID() + "-" + timeStr + "." + suffix;
            // 将文件上传到Local中去
            File dir = FileUtil.file(UPLOAD_PATH + File.separator + userId);
            FileUtil.mkdir(dir);
            File dest = new File(dir, fileName);
            HttpUtil.downloadFile(uploadUrl, dest);

            UploadLocalResult uploadLocalResult = new UploadLocalResult();
            String picUrl = dir.getPath() + File.separator + fileName;
            uploadLocalResult.setPicName(fileName);
            uploadLocalResult.setPicUrl(picUrl);
            uploadLocalResult.setPicFormat(suffix);
            uploadLocalResult.setPicSize(Long.valueOf(imageBytes.length));

            int width = image.getWidth();
            int height = image.getHeight();
            uploadLocalResult.setPicWidth(width);
            uploadLocalResult.setPicHeight(height);
            double scaleValue = NumberUtil.round(width * 1.0 / height, 2).doubleValue();
            uploadLocalResult.setPicScale(scaleValue);
            // 返回值
            return uploadLocalResult;
        } catch (Exception e) {
            log.error("系统出现了异常 ====> :{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 通过上传URL的方式来上传图片到COS
     *
     * @param uploadUrl 图片的URL地址
     * @param userId    上传用户
     * @return 上传结果
     */
    public UploadLocalResult uploadPictureToCOSByUrl(String uploadUrl, Long userId) {
        try {
            // 校验参数是否合法
            ThrowUtils.throwIf(uploadUrl == null, "上传的Url参数为空");
            ThrowUtils.throwIf(userId == null, "用户未登录");
            validUrlOfPic(uploadUrl);
            // 下载图片字节数组
            byte[] imageBytes = HttpUtil.downloadBytes(uploadUrl);
            BufferedImage image = ImgUtil.read(new ByteArrayInputStream(imageBytes));

            String suffix = FileUtil.getSuffix(uploadUrl);
            ThrowUtils.throwIf(suffix.isEmpty(), "文件后缀为空");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH-mm");
            String timeStr = LocalDateTime.now().format(formatter);
            String fileName = IdUtil.fastSimpleUUID() + "-" + timeStr + "." + suffix;
            // 将文件上传到Local中去
            File dir = FileUtil.file(UPLOAD_PATH + File.separator + userId);
            FileUtil.mkdir(dir);
            File dest = new File(dir, fileName);
            HttpUtil.downloadFile(uploadUrl, dest);

            String cosFilePath = UPLOAD_COS_PATH + "/" + userId + "/" + fileName;
            PutObjectResult putObjectResult = cosFileManager.putFile(PIC_BUCKET_NAME, dest, cosFilePath);
            UploadLocalResult uploadLocalResult = new UploadLocalResult();
            uploadLocalResult.setPicName(fileName);
            URL cosUrl = cosClient.getObjectUrl(PIC_BUCKET_NAME, cosFilePath);
            uploadLocalResult.setPicUrl(COS_URL_BUCKET_ONE + cosUrl.getPath());

            uploadLocalResult.setPicFormat(suffix);
            uploadLocalResult.setPicSize(Long.valueOf(imageBytes.length));

            uploadLocalResult.setPicWidth(image.getWidth());
            uploadLocalResult.setPicHeight(image.getHeight());
            double scaleValue = NumberUtil.round(image.getWidth() * 1.0 / image.getHeight(), 2).doubleValue();
            uploadLocalResult.setPicScale(scaleValue);
            return uploadLocalResult;
        } catch (Exception e) {
            log.error("系统出现了异常 ====> :{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 从本地磁盘中下载文件 并返回给前端
     *
     * @param filePath 所要下载的文件路径
     * @param response 响应结果
     */
    public void downloadPictureFromLocal(String filePath, HttpServletResponse response) {
        ThrowUtils.throwIf(filePath == null || response == null, "参数为空");

        try (BufferedInputStream inputStream = FileUtil.getInputStream(filePath)) {
            // 校验文件是否存在
            ThrowUtils.throwIf(!FileUtil.exist(filePath), ErrorCode.NOT_FOUND_ERROR, "文件不存在");

            // 设置响应内容类型
            String suffix = FileUtil.getSuffix(filePath);
            response.setContentType("image/" + suffix);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + FileUtil.getName(filePath) + "\"");

            // 将文件流写入响应输出流
            IOUtils.copy(inputStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("文件下载失败: {}", filePath, e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件下载失败");
        } catch (Exception e) {
            log.error("文件下载失败，系统出现异常");
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 从COS中下载文件 并返回给前端
     *
     * @param filePath 所要下载的文件路径
     * @param response 响应结果
     */
    public void downloadPictureFromCOS(String filePath, Picture picture, HttpServletResponse response) {
        ThrowUtils.throwIf(filePath == null || response == null, "参数为空");
        try (
                InputStream fileInputStream = cosFileManager.getFileInputStream(PIC_BUCKET_NAME, filePath)
        ) {
            ThrowUtils.throwIf(fileInputStream == null, ErrorCode.NOT_FOUND_ERROR);
            String suffix = FileUtil.getSuffix(filePath);
            ThrowUtils.throwIf(picture == null, "下载的图片不存在");

            response.setContentType("image/" + suffix);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + picture.getPicName() + "\"");
            IOUtils.copy(fileInputStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("文件下载失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * 校验图片是否合法
     *
     * @param multipartFile 文件
     */
    private void validPicture(MultipartFile multipartFile) {
        // 校验参数
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.NOT_FOUND_ERROR);
        // 逻辑实现
        Long size = multipartFile.getSize();
        Long MaxSize = 1024L * 1024L * 2L;
        ThrowUtils.throwIf(MaxSize.compareTo(size) < 0, ErrorCode.NOT_FOUND_ERROR, "文件不能超过2MB");

        String originalFilename = multipartFile.getOriginalFilename();
        ThrowUtils.throwIf(StrUtil.isBlank(originalFilename), ErrorCode.NOT_FOUND_ERROR, "文件名为空");
        String suffix = FileUtil.getSuffix(originalFilename);
        List<String> pictureSuffixes = Arrays.asList("png", "jpg", "jpeg", "gif", "bmp", "webp");
        ThrowUtils.throwIf(!pictureSuffixes.contains(suffix), "不支持该图片类型");
        // 返回值
    }

    /**
     * 校验图片下载的Url是否合法
     *
     * @param uploadUrl 图片的Url
     */
    private void validUrlOfPic(String uploadUrl) {
        ThrowUtils.throwIf(uploadUrl == null, "校验的Url参数为null");
        ThrowUtils.throwIf(!uploadUrl.startsWith("http://") && !uploadUrl.startsWith("https://"),
                "Url的传输协议不为Http或者Https");

        try (HttpResponse response = HttpRequest.head(uploadUrl)
                .timeout(30000)
                .execute()) {
            URL url = new URL(uploadUrl);
            ThrowUtils.throwIf(!response.isOk(), "Url请求失败，状态码: " + response.getStatus());

            String contentType = response.header("Content-Type");
            ThrowUtils.throwIf(StrUtil.isBlank(contentType), "响应头中未找到Content-Type");
            ThrowUtils.throwIf(!contentType.startsWith("image/"), "响应内容不是图片类型");

            String contentLength = response.header("Content-Length");
            if (StrUtil.isNotBlank(contentLength)) {
                long fileSize = Long.parseLong(contentLength);
                long maxSize = 1024L * 1024L * 2L;
                ThrowUtils.throwIf(fileSize > maxSize, "图片大小不能超过2MB");
            }
        } catch (Exception e) {
            log.error("校验图片Url失败: {}", uploadUrl, e);
            throw new RuntimeException("校验图片Url失败: " + e.getMessage());
        }
    }

}
