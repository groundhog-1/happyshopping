package com.leyou.search.pojo;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;

import java.util.List;
import java.util.Map;

/**
 * @author: oyyb
 * @data: 2020-04-03
 * @version: 1.0.0
 * @descript:
 */
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;//分类待选项
    private List<Brand> brands;//品牌待选项

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    private List<Map<String,Object>> specs;//规格参数 key及待选项

    public SearchResult() {
    }

    public SearchResult(List<Category> categories, List<Brand> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(List<Goods> items, Long total, List<Category> categories, List<Brand> brands) {
        super(total,items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(List<Goods> items, Long total, Long totalPage, List<Category> categories, List<Brand> brands) {
        super(total, totalPage,items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(List<Goods> items, Long total, Long totalPage, List<Category> categories, List<Brand> brands,List<Map<String,Object>> specs) {
        super(total, totalPage,items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}
