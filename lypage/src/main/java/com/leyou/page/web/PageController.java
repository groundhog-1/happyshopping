package com.leyou.page.web;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author: oyyb
 * @data: 2020-04-05
 * @version: 1.0.0
 * @descript:
 */
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id")Long spuId, Model model){
        //准备模型数据
        Map<String,Object> attributes = pageService.loadMode(spuId);
        model.addAllAttributes(attributes);
        //返回数据
        return "item";
    }
}
