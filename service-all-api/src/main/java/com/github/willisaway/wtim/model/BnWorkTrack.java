package com.github.willisaway.wtim.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableName;
import com.github.willisaway.core.base.BaseModel;

import lombok.Data;

@TableName("BN_WORK_TRACK")
@SuppressWarnings("serial")
@Data
public class BnWorkTrack extends BaseModel{
	private String originFlag;
	
	private String workName;
	
	private String workCode;
	
	private Date receiveTime;
	
	private String knowDesc;
	
	private String recordDesc;
	
	private String sendUserName;
	
	private String receiveUserName;
	
	private String impUserName;
	
	private Date limitTime;
	
	private String respUserName;
	
	private String workNote;
	
	private String impNote;
	
	private String impWay;
	
	private String shouldKnow;
	
	private String impFlag;
	
	private String comFlag;
}
