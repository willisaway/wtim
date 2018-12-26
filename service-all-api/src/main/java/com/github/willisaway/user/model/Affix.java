package com.github.willisaway.user.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import com.huaxun.core.base.BaseModel;

import lombok.Data;

/**
 * @author SONG
 * @Description:附件实体
 * @time:2017年8月4日 上午11:09:41
 */
@TableName("CM_AFFIX")
@SuppressWarnings("serial")
@Data
public class Affix extends BaseModel {
	private String businessCode;

    private Long businessId;

    private String affixType;

    private String fileName;

    private String saveFileName;

    private String filePath;

    private String fileParameters;

    private BigDecimal fileSize;

    private String fileType;

    private Date uploadTime;

    private Long uploadUserId;

    private String uploadUserName;

    private Integer downloadCount;

    private Long batchNo;

    private String frontFileId;
    
    private Integer chunks;
    
    private Integer chunk;
    
    private String md5String;
}