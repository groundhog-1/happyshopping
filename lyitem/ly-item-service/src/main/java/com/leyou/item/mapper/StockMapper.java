package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 * @author: oyyb
 * @data: 2020-03-29
 * @version: 1.0.0
 * @descript:
 */
public interface StockMapper extends BaseMapper<Stock>{

    @Update("update tb_stock set stock = stock - #{num} where sku_id = #{id} and stock >= #{stock}")
    int decreaseStock(@Param("id")Long id,@Param("num")Integer num);
}
