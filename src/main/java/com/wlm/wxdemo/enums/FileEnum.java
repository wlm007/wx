package com.wlm.wxdemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 文件类型枚举类
 *
 * @author Ztrain
 * @date 2020/5/20
 */
@Getter
@AllArgsConstructor
public enum FileEnum {

    /**
     * 图片: 0
     */
    GIF("gif", 0, "static/images"),
    JPEG("jpeg", 0, "static/images"),
    PNG("png", 0, "static/images"),
    JPG("jpg", 0, "static/images"),
    BMP("bmp", 0, "static/images"),
    /**
     * 音频: 1
     */
    AMR("amr", 1, "audio"),
    MP3("mp3", 1, "audio"),
    /**
     * 视频: 2
     */
    MP4("mp4", 2, "video"),
    AVI("avi", 2, "video"),
    WMV("wmv", 2, "video"),
    RM("rm", 2, "video"),
    RMVB("rmvb", 2, "video"),
    MPG("mpg", 2, "video"),
    MOV("mov", 2, "video"),
    THREEGP("3gp", 2, "video"),
    /**
     * 文档: 3
     */
    XLSX("xlsx", 3, "document"),
    XLS("xls", 3, "document"),
    DOC("doc", 3, "document"),
    DOCX("docx", 3, "document"),
    PDF("pdf", 3, "document"),
    PPT("ppt", 3, "document"),
    PPTX("pptx", 3, "document"),
    /**
     * 其他: 4
     */
    OTHERS("others", 4, "others");

    private final String fileExt;

    private final Integer fileType;

    private final String filePath;

    public final static Integer IMG_FILE = 0;
    public final static Integer AUDIO_FILE = 1;
    public final static Integer VIDEO_FILE = 2;
    public final static Integer DOCUMENT_FILE = 3;
    public final static Integer OTHERS_FILE = 4;

    public static Boolean isWord(String fileExt) {
        return DOC.fileExt.equals(fileExt) || DOCX.fileExt.equals(fileExt);
    }

    public static Boolean isExcel(String fileExt) {
        return XLSX.fileExt.equals(fileExt) || XLS.fileExt.equals(fileExt);
    }

    public static Integer getFileType(String fileExt) {
        return Stream.of(FileEnum.values()).filter(each -> each.fileExt.equals(fileExt))
            .map(FileEnum::getFileType).findFirst().orElse(4);
    }

    public static String getFilePath(String fileExt) {
        return Stream.of(FileEnum.values()).filter(each -> each.fileExt.equals(fileExt))
            .map(FileEnum::getFilePath).findFirst().orElse("others");
    }
}
