package com.rose.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rose.model.VO.BulletVO;
import com.rose.model.dto.BulletQueryRequest;
import com.rose.model.entity.Bullet;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface BulletService extends IService<Bullet> {

    String getBulletPriceGson(String imagePath);

    Map<String,String> getBulletPriceMap(String imagePath);

    Map<String,String> getBulletPriceMapByFolder(String folderPath);

    Page<BulletVO> getBulletVOPage(Page<Bullet> bulletPage, HttpServletRequest request);

    QueryWrapper<Bullet> getQueryWrapper(BulletQueryRequest bulletQueryRequest);
}
