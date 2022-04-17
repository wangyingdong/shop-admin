package com.f139.shop.admin.controller;


import com.f139.shop.admin.common.config.ServerConfig;
import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import com.f139.shop.admin.entity.Category;
import com.f139.shop.admin.entity.Good;
import com.f139.shop.admin.service.IGoodService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@ResponseBody
@RequestMapping("/goods")
@Slf4j
public class GoodController {

    @Resource
    private IGoodService goodService;

    @Resource
    private ServerConfig serverConfig;

    @GetMapping
    public PageInfo<Good> getGoodList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                      @RequestParam(value = "query", defaultValue = "") String query) {
        return goodService.getGoodList(PageHelper.startPage(pageNum, pageSize), Good.builder().name(query).build());
    }


    @DeleteMapping(value = "/{id}")
    public Boolean deleteGood(@PathVariable(value = "id") @Valid @NotNull Integer id) {
        return goodService.deleteGood(id);
    }

    @PostMapping
    public Boolean saveGood(@Validated(Good.Save.class) @RequestBody Good good) {
        return goodService.saveGood(good);
    }


    @PostMapping(value = "/upload")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws Exception {
        log.info("path:" + serverConfig.getUrl());
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            List<String> FILE_WHILE_EXT_LIST = Arrays.asList("JPG", "PNG", "JPEG", "GIF");
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if (!FILE_WHILE_EXT_LIST.contains(suffixName.toUpperCase())) {
                throw new BusinessException(Errors.UPLOAD_FILE_NAME_ERROR);
            }
            String date = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
            //本地路径
            String relativePath = "/upload/photo/" + date + "/";
            String filePath = request.getSession().getServletContext().getRealPath(relativePath);
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffixName;
            //相对路径
            File targetFile = new File(filePath, fileName);
            targetFile.mkdirs();
            file.transferTo(targetFile);
            log.info("图片被上传到了：{}", filePath + fileName);
            return (serverConfig.getUrl() + relativePath + fileName);
        }else{
            throw  new BusinessException(Errors.UPLOAD_FILE_EMPTY_ERROR);
        }
    }
}