package com.f139.shop.admin.service;

import com.f139.shop.admin.entity.Attribute;
import com.f139.shop.admin.entity.AttributeValue;
import com.f139.shop.admin.entity.Category;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ICategoryService {

    PageInfo<Category> getCategoryList(Page page);

    Boolean saveCategory(Category category);

    List<Attribute> getAttributeList(Attribute attribute);

    Boolean saveAttribute(Attribute attribute);

    Boolean updateAttribute(Attribute attribute);

    Boolean deleteAttribute(Attribute attribute);

    Boolean deleteAttributeValue(AttributeValue attributeValue);

    AttributeValue saveAttributeValue(AttributeValue attributeValue);
}
