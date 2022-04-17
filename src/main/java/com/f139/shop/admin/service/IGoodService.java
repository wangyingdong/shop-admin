package com.f139.shop.admin.service;

import com.f139.shop.admin.entity.Good;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

public interface IGoodService {


    PageInfo<Good> getGoodList(Page<Good> startPage,Good good);

    Boolean deleteGood(Integer id);

    Boolean saveGood(Good good);
}
