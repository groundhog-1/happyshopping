package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecGroup;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author: oyyb
 * @data: 2020-03-19
 * @version: 1.0.0
 * @descript:
 */
public interface CategoryMapper extends Mapper<Category>,IdListMapper<Category,Long> {
}
