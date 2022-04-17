package com.f139.shop.admin.controller;


import com.f139.shop.admin.entity.Attribute;
import com.f139.shop.admin.entity.AttributeValue;
import com.f139.shop.admin.entity.Category;
import com.f139.shop.admin.service.ICategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.istack.internal.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/category")
public class CategoryController {



    @Resource
    private ICategoryService categoryService;


    @GetMapping
    public PageInfo<Category> getCategoryList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return categoryService.getCategoryList(PageHelper.startPage(pageNum, pageSize));
    }

    @PostMapping
    public Boolean saveCategory(@Validated(Category.Save.class) @RequestBody Category category){
        return categoryService.saveCategory(category);
    }


    @GetMapping(value = "/attribute/{id}")
    public List<Attribute> getAttributeList(@PathVariable(value = "id") Integer id, @RequestParam(value = "type") String type){
        Attribute attribute = Attribute.builder().categoryId(id).type(type).build();
        return categoryService.getAttributeList(attribute);
    }

    @PostMapping(value = "/attribute")
    public Boolean saveAttribute(@RequestBody Attribute attribute){
        return categoryService.saveAttribute(attribute);
    }

    @PutMapping(value = "/attribute/{id}/name/{name}")
    public Boolean AttributeList(@PathVariable(value = "id") @Valid @NotNull Integer id, @PathVariable(value = "name") @Valid @NotNull String name){
        Attribute attribute = Attribute.builder().id(id).name(name).build();
        return categoryService.updateAttribute(attribute);
    }


    @DeleteMapping(value = "/attribute/{id}")
    public Boolean deleteAttribute(@PathVariable(value = "id") @Valid @NotNull Integer id){
        Attribute attribute = Attribute.builder().id(id).build();
        return categoryService.deleteAttribute(attribute);
    }

    @DeleteMapping(value = "/attribute/value/{id}")
    public Boolean deleteAttributeValue(@PathVariable(value = "id") @Valid @NotNull Integer id){
        AttributeValue attributeValue = AttributeValue.builder().id(id).build();
        return categoryService.deleteAttributeValue(attributeValue);
    }

    @PostMapping(value = "/attribute/value")
    public AttributeValue saveAttributeValue(@RequestBody AttributeValue attributeValue){
        return categoryService.saveAttributeValue(attributeValue);
    }


}
