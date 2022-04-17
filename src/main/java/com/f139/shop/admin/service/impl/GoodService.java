package com.f139.shop.admin.service.impl;

import com.f139.shop.admin.entity.Good;
import com.f139.shop.admin.entity.GoodAttributeValue;
import com.f139.shop.admin.mapper.GoodAttributeValueMapper;
import com.f139.shop.admin.mapper.GoodMapper;
import com.f139.shop.admin.service.IGoodService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoodService implements IGoodService {

    @Resource
    private GoodMapper goodMapper;

    @Resource
    private GoodAttributeValueMapper goodAttributeValueMapper;


    @Override
    public PageInfo<Good> getGoodList(Page<Good> startPage,Good good) {
        Example example = new Example(Good.class);
        example.createCriteria().andLike("name","%"+good.getName()+"%");
        List<Good> list = goodMapper.selectByExample(example);
        PageInfo<Good> pageInfo = new PageInfo<Good>(list);
        return pageInfo;
    }

    @Override
    public Boolean deleteGood(Integer id) {
        return goodMapper.delete(Good.builder().id(id).build()) > 0;
    }

    @Override
    @Transactional
    public Boolean saveGood(Good good) {
        good.setDatetime(LocalDateTime.now());
        goodMapper.insertSelective(good);


        List<GoodAttributeValue> list = new ArrayList<GoodAttributeValue>();
        for(Integer id :good.getAttributeIds()){
            list.add(GoodAttributeValue.builder().goodId(good.getId()).attributeValueId(id).build());
        }
        return goodAttributeValueMapper.insertList(list) > 0;
    }

}
