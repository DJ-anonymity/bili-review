package com.zfg.learn.service;

import com.zfg.learn.bo.KeywordCountBo;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface KeywordCountService {

    public List<KeywordCountBo> listMid();

    public List<KeywordCountBo> listKeyword();
}
