package com.zfg.learn.dao;

import com.zfg.learn.model.bo.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IconMapper {


    public Integer updateIcon(@Param("menuName") String menuName, @Param("iconName") String iconName);

    public List<Menu> selectAll();

    public Menu selectByName(String menu_name);
}
