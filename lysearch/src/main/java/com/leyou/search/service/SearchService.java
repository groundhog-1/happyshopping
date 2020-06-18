package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandCliecnt;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: oyyb
 * @data: 2020-04-02
 * @version: 1.0.0
 * @descript:
 */
@Service
@Slf4j
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandCliecnt brandCliecnt;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    public Goods buildGoods(Spu spu){
        /*Long spuId = spu.getId();
        //查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(),
                spu.getCid2(),
                spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandCliecnt.queryBrandById(spu.getBrandId());
        if (brand == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //搜索字段

        String all = spu.getTitle()+ StringUtils.join(names," ")+brand.getName();
        //查询sku
        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        if (CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //对sku进行处理
        List<Map<String,Object>>  skuList = new ArrayList<>();
        //价格集合
        List<Long> priceList = new ArrayList<>();
        for (Sku sku : skus) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("image",StringUtils.substringBefore(sku.getImages(),""));
            skuList.add(map);
            //处理价格
            priceList.add(sku.getPrice());
        }
        //查询规格参数
        List<SpecParam> params = specificationClient.queryParamList(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(params)){
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);

        }
        //查询商品详情
        SpuDetail detail = goodsClient.queryDetailById(spuId);
        //获取通用规格参数
        String json = detail.getGenericSpec();
        Map<Long,String> genericSpec = JsonUtils.parseMap(json,Long.class,String.class);
        //获取特有规格参数
        Map<Long,List<String>> specialSpec = JsonUtils.nativeRead(detail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>() {
                });

        //规格参数，key是规格参数的名字，值是规格参数的值
        Map<String,Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            //规格名称
            String key = param.getName();
            Object value = "";
            //判断是否通用规格
            if (param.getGeneric()){
                value = genericSpec.get(param.getId());
                //判断是否是数值类型
                if(param.getNumeric()){
                    //处理成字段
                    value = chooseSegment(value.toString(),param);
                }

            }else{
                value = specialSpec.get(param.getId());
            }
            //存入map
            specs.put(key,value);
        }


        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setAll(all);//搜索字段 包含标题 父类 品牌 规格
        goods.setPrice(priceList);//TODO 所有sku的价格集合
        goods.setSkus(null);
        goods.setSpecs(null);
        goods.setSubTitle(spu.getSubTitle());

        return goods;*/
        //
        Long id = spu.getId();
        //准备数据
        //sku集合
        List<Sku> skus = this.goodsClient.querySkuBySpuId(id);

        //spuDetail
        SpuDetail spuDetail = this.goodsClient.queryDetailById(id);

        //商品分类名称
        List<Category> categories = this.categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        //查询通用规格参数
        List<SpecParam> genericParas = this.specificationClient.queryParamList(null, spu.getCid3(), true);

        //处理sku
        //把商品价格取出单独存放，便于展示
        List<Long> prices = new ArrayList<>();

        List<Map<String,Object>> skuList = new ArrayList<>();

        for (Sku sku : skus) {
            prices.add(sku.getPrice());
            Map<String,Object> skuMap = new HashMap<>();
            skuMap.put("id",sku.getId());
            skuMap.put("title",sku.getTitle());
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "":sku.getImages().split(",")[0]);
            skuMap.put("price",sku.getPrice());
            skuList.add(skuMap);
        }

        //处理规格参数
        Map<Long, String> genericMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);


        Map<Long,List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        //处理规格参数显示问题，默认显示id+值，处理后显示id对应的名称+值
        Map<String, Object> specs = new HashMap<>();

        for (SpecParam param : genericParas) {

            //规格名称
            String key = param.getName();
            Object value = "";
            //判断是否通用规格
            if (param.getGeneric()){
                value = genericMap.get(param.getId());
                //判断是否是数值类型
                if(param.getNumeric()){
                    //处理成字段
                    value = chooseSegment(value.toString(),param);
                }

            }else{
                value = specialMap.get(param.getId());
            }
            //存入map
            specs.put(key,value);
        }

        Goods goods = new Goods();
        goods.setId(spu.getId());
        //搜索条件拼接：这里如果要加品牌，可以再写个BrandClient，根据id查品牌
        goods.setAll(spu.getTitle()+" "+StringUtils.join(names," "));
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);

        return goods;

    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult search(SearchRequest request) {
        Integer page = request.getPage()-1;
        Integer size = request.getSize();
        //创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //0.结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //1.分页
        queryBuilder.withPageable(PageRequest.of(page,size));
        //2.过滤
        String key = request.getKey();
        //QueryBuilder basicQuery = QueryBuilders.matchQuery("all", key);
        BoolQueryBuilder basicQuery = buildBasicQuery(request);
        queryBuilder.withQuery(basicQuery);
        //3.聚合分类和品牌
        //3.1 聚合分类
        String categoryAggName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //3.2 聚合品牌
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //4.查询
//        Page<Goods> result = goodsRepository.search(queryBuilder.build());
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        //5.解析结果
        //5.1解析分页结果
        long total = result.getTotalElements();
        long totalPage = result.getTotalPages();
        List<Goods> content = result.getContent();
        //5.2解析聚合结果
        Aggregations aggs = result.getAggregations();
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));
        //6.完成规格参数聚合
        List<Map<String,Object>> specs = null;
        if (categories != null && categories.size() == 1){
            //商品分类存在且数量为1，可以聚合规格参数
            specs = buildSpecificationAgg(categories.get(0).getId(),basicQuery);
        }

        return new SearchResult(content,total,totalPage,categories,brands,specs);
    }

    private BoolQueryBuilder buildBasicQuery(SearchRequest request) {
        /*//创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all",request.getKey()));
        //过滤条件
        Map<String,String> map = request.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            //处理key
            if (!"cid3".equals(key) && !"brandId".equals(key)){
                key = "specs"+key+".keyword";
            }
            String value = entry.getValue();
            queryBuilder.filter(QueryBuilders.termsQuery(key,value));
        }

        return queryBuilder;*/
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 添加基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));

        // 添加过滤条件
        if (CollectionUtils.isEmpty(request.getFilter())){
            return boolQueryBuilder;
        }
        Map<String,String> map = request.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {

            String key = entry.getKey();
            // 如果过滤条件是“品牌”, 过滤的字段名：brandId
           /* if (StringUtils.equals("brandId", key)) {

            } else if (StringUtils.equals("cid3", key)) {
                // 如果是“分类”，过滤字段名：cid3
                key = "cid3";
            } else {
                // 如果是规格参数名，过滤字段名：specs.key.keyword
                key = "specs." + key + ".keyword";
            }*/
           String[] s= new String[2];

           if(!"brandId".equals(key) && !"cid3".equals(key)){
               key = "specs." + key + ".keyword";
           }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }

        return boolQueryBuilder;
    }

    private List<Map<String,Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String,Object>> specs = new ArrayList<>();
        //1.查询需要聚合的规格参数
        List<SpecParam> specParams = specificationClient.queryParamList(null, cid, true);
        //2.聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //2.1带上查询条件
        queryBuilder.withQuery(basicQuery);
        //2.2聚合
        for (SpecParam specParam : specParams) {
            String name = specParam.getName();

            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs."+name+".keyword"));
            
        }
        //3.获取结果
        AggregatedPage<Goods> results = template.queryForPage(queryBuilder.build(), Goods.class);
        //4.解析结果
        Aggregations aggregations = results.getAggregations();


        for (SpecParam specParam : specParams) {
            String name = specParam.getName();

            StringTerms terms = aggregations.get(name);
            //准备map
            Map<String,Object> map = new HashMap<>();
            map.put("k",name);
            map.put("options",terms.getBuckets().
                    stream().map(b -> b.getKeyAsString()).collect(Collectors.toList()));
            specs.add(map);
        }
        return specs;
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
        try{
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Brand> brands = brandCliecnt.queryBrandByIds(ids);
            return brands;
        }catch (Exception e){
            log.error("搜索服务，查询品牌异常",e);
            return null;
        }

    }

    private List<Category> parseCategoryAgg(LongTerms terms) {
        try{
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryByIds(ids);
            return categories;
        }catch (Exception e){
            log.error("搜索服务，查询品牌异常",e);
            return null;
        }

    }

    public void createOrUpdateIndex(Long spuId) {
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //构建goods
        Goods goods = buildGoods(spu);
        //存入索引库
        goodsRepository.save(goods);
    }

    public void deleteIndex(Long spuId) {
        goodsRepository.deleteById(spuId);
    }
}
