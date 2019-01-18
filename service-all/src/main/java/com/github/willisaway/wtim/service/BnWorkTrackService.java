package com.github.willisaway.wtim.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.willisaway.wtim.dao.BnWorkTrackMapper;
import com.github.willisaway.wtim.model.BnWorkTrack;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.willisaway.core.base.BaseService;
import com.github.willisaway.core.util.RedissonUtil;

@Service
@CacheConfig(cacheNames = "BnWorkTrack")
public class BnWorkTrackService extends BaseService<BnWorkTrackMapper,BnWorkTrack>{
    @Autowired
    BnWorkTrackService self;
}
