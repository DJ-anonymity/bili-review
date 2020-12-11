package com.zfg.learn.dao;

import com.zfg.learn.bo.Menu;
import com.zfg.learn.pojo.Stat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface IconMapper {


    public Integer updateIcon(@Param("menuName") String menuName, @Param("iconName") String iconName);

    public List<Menu> selectAll();

    public Menu selectByName(String menu_name);
}
