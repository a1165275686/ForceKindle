package com.rose.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.rose.common.ORCUtils;
import com.rose.mapper.BulletMapper;
import com.rose.model.VO.BulletVO;
import com.rose.model.dto.BulletQueryRequest;
import com.rose.model.entity.Bullet;
import com.rose.service.BulletService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BulletServiceImpl extends ServiceImpl<BulletMapper, Bullet> implements BulletService {

    @Override
    public String getBulletPriceGson(String imagePath) {
        ORCUtils orcUtils = new ORCUtils();
        Map<String, String> map = orcUtils.getORCBatch(imagePath);
        Gson gson = new Gson();
        String bulletPrice = gson.toJson(map);
        return bulletPrice;
    }

    @Override
    public Map<String,String> getBulletPriceMap(String imagePath) {
        ORCUtils orcUtils = new ORCUtils();
        Map<String, String> map = orcUtils.getORCBatchByRapidOCR(imagePath);
        return map;
    }

    @Override
    public Map<String,String> getBulletPriceMapByFolder(String folderPath) {
        ORCUtils orcUtils = new ORCUtils();
        Map<String, String> map = orcUtils.getORCBatchByRapidOCRToFolder(folderPath);
        return map;
    }

    @Override
    public QueryWrapper<Bullet> getQueryWrapper(BulletQueryRequest bulletQueryRequest) {
        QueryWrapper<Bullet> queryWrapper = new QueryWrapper<>();
        if (bulletQueryRequest == null) {
            return queryWrapper;
        }
        String bulletName = bulletQueryRequest.getBulletName();
        String bulletPrice = bulletQueryRequest.getBulletPrice();
        if(StringUtils.isAnyBlank(bulletName,bulletPrice)){
            return queryWrapper;
        }
        queryWrapper.eq("bulletName", bulletName)
                .eq("bulletPrice", bulletPrice);
        return queryWrapper;
    }

    @Override
    public Page<BulletVO> getBulletVOPage(Page<Bullet> bulletPage, HttpServletRequest request) {
        List<Bullet> bulletList = bulletPage.getRecords();
        Page<BulletVO> bulletVOPage = new Page<>(bulletPage.getCurrent(), bulletPage.getSize(),bulletPage.getTotal());
        if (CollectionUtils.isEmpty(bulletList)) {
            return bulletVOPage;
        }
        List<BulletVO> bulletVOList = bulletList.stream()
                .map(Bullet::objToVo)
                .collect(Collectors.toList());
        // 设置分页记录
        bulletVOPage.setRecords(bulletVOList);
        return bulletVOPage;

    }
}
