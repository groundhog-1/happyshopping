package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandCliecnt;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: oyyb
 * @data: 2020-04-05
 * @version: 1.0.0
 * @descript:
 */
@Service
@Slf4j
public class PageService {

    @Autowired
    private BrandCliecnt brandCliecnt;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private TemplateEngine templateEngine;

    public Map<String,Object> loadMode(Long spuId) {
        Map<String,Object> model = new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询skus
        List<Sku> skus = spu.getSkus();
        //查询商品详情
        SpuDetail spuDetail = spu.getSpuDetail();
        //查询brand
        Brand brand = brandCliecnt.queryBrandById(spu.getBrandId());
        //查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specGroups =specClient.queryGroupByCid(spu.getCid3());


        //model.put("spu",spu);
        model.put("title",spu.getTitle());
        model.put("subtitle",spu.getSubTitle());
        model.put("skus",skus);
        model.put("detail",spuDetail);
        model.put("brand",brand);
        model.put("category",categories);
        model.put("specs",specGroups);

        return model;

    }

    public void createHtml(Long spuId){
        //上下文
        Context context = new Context();
        context.setVariables(loadMode(spuId));
        //输出流
        File file = new File("E:\\study_java\\upload", spuId + ".html");

        if (file.exists()){
            file.delete();
        }
        try(PrintWriter writer = new PrintWriter(file, "utf-8")){
            //生成html
            templateEngine.process("item",context,writer);
        }catch (Exception e){
            log.error("[静态页服务] 生成静态页异常！",e);
        }

    }

    public void deleteHtml(Long spuId){
        File file = new File("E:\\study_java\\upload", spuId + ".html");
        if (file.exists()){
            file.delete();
        }

    }
}
