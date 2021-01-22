package com.zfg.learn.service;

import com.zfg.learn.model.bo.KeywordCountBo;

import java.util.List;

public interface KeywordCountService {

    public List<KeywordCountBo> listMid();

    public List<KeywordCountBo> listKeyword();
}
