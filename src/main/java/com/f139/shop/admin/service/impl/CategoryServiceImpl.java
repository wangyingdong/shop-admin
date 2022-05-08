package com.f139.shop.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import com.f139.shop.admin.entity.Attribute;
import com.f139.shop.admin.entity.AttributeValue;
import com.f139.shop.admin.entity.Category;
import com.f139.shop.admin.mapper.AttributeMapper;
import com.f139.shop.admin.mapper.AttributeValueMapper;
import com.f139.shop.admin.mapper.CategoryMapper;
import com.f139.shop.admin.service.ICategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {


    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private AttributeMapper attributeMapper;

    @Resource
    private AttributeValueMapper attributeValueMapper;


    @Override
    @Cacheable(value = "getCategoryList",key="#page.pageNum-#page.pageSize")
    public  PageInfo getCategoryList(Page page) {
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("parentId",0);
        List<Category> categoryList = categoryMapper.selectByExample(example);
        categoryList = getCategoryChildrenList(categoryList,1);
        PageInfo<Category> pageInfo = new PageInfo<Category>(categoryList);
        return pageInfo;
    }

    @Override
    public Boolean saveCategory(Category category) {
        return categoryMapper.insert(category) > 0 ;
    }

    @Override
    @Cacheable(value = "getAttributeList")
    public List<Attribute> getAttributeList(Attribute attribute){
        List<Attribute> attributeList = attributeMapper.select(attribute);
        List attributeId = attributeList.stream().map(a -> a.getId()).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(attributeId)){
            Example example = new Example(AttributeValue.class);
            Example.Criteria ca = example.createCriteria();
            ca.andIn("attributeId", attributeId);
            List<AttributeValue> valueList = attributeValueMapper.selectByExample(example);

            Map<Integer, List<AttributeValue>> map = valueList.stream().collect(Collectors.groupingBy(attributeValue -> attributeValue.getAttributeId()));

            attributeList.stream().map( a -> {
                a.setAttributeValue(map.get(a.getId()));
                return  a;
            }).collect(Collectors.toList());
        }
        return attributeList;
    }

    @Override
    public Boolean saveAttribute(Attribute attribute) {
        return attributeMapper.insert(attribute) > 0;
    }

    @Override
    public Boolean updateAttribute(Attribute attribute) {
        return attributeMapper.updateByPrimaryKeySelective(attribute) > 0;
    }

    @Override
    @Transactional
    public Boolean deleteAttribute(Attribute attribute) {
        if( attributeMapper.delete(attribute) > 0){
            Example example = new Example(AttributeValue.class);
            Example.Criteria ca = example.createCriteria();
            ca.andEqualTo("attributeId", attribute.getId());
            attributeValueMapper.deleteByExample(example);
        }
        return true;
    }

    @Override
    public Boolean deleteAttributeValue(AttributeValue attributeValue) {
        return attributeValueMapper.delete(attributeValue) > 0;
    }

    @Override
    public AttributeValue saveAttributeValue(AttributeValue attributeValue) {
        if (attributeValueMapper.insertSelective(attributeValue) <= 0) {
            new BusinessException(Errors.NO_OBJECT_FOUND_ERROR);
        }
        return attributeValue;
    }


    private List<Category> getCategoryChildrenList(List<Category> categoryChildrenList,Integer level){
        if(!CollectionUtils.isEmpty(categoryChildrenList)) {
            List<Integer> categoryChildrenId = categoryChildrenList.stream().map( category -> category.getId()).collect(Collectors.toList());
            Map<Integer,List<Category>> categoryChildren = this.getCategoryChildrenGroupingByParentId(categoryChildrenId,level+1);
            categoryChildrenList.stream().map(
                    category -> {
                        category.setChildren(categoryChildren.get( category.getId() ));
                        return category;
                    }
            ).collect(Collectors.toList());

        }
        return categoryChildrenList;
    }

    private Map<Integer,List<Category>> getCategoryChildrenGroupingByParentId(List<Integer> parentId, Integer level) {
        Example example = new Example(Category.class);
        example.createCriteria().andIn("parentId",parentId).andEqualTo("level",level);
        List<Category> categoryChildrenList = categoryMapper.selectByExample(example);
        categoryChildrenList = getCategoryChildrenList(categoryChildrenList,level);
        return categoryChildrenList.stream().collect(Collectors.groupingBy(Category::getParentId ));
    }
}
