package com.rose.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rose.common.BaseResponse;
import com.rose.common.ErrorCode;
import com.rose.common.ResultUtils;
import com.rose.exception.BusinessException;
import com.rose.exception.ThrowUtils;
import com.rose.model.VO.BulletVO;
import com.rose.model.dto.BulletQueryRequest;
import com.rose.model.entity.Bullet;
import com.rose.model.entity.User;
import com.rose.service.BulletService;
import com.rose.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/dataProgress")
@Slf4j
public class DataProgressController {

    @Resource
    private BulletService bulletService;

    @Resource
    private UserService userService;

    @GetMapping("/get/bullet")
    public String getBulletPrice(String imagePath) {
        return bulletService.getBulletPriceGson(imagePath);
    }

    @PostMapping("/add/bullet")
    public BaseResponse<Long> addBulletPrice(String imagePath) {
        try {
            // 从服务层获取子弹价格的 Map
            Map<String, String> map = bulletService.getBulletPriceMap(imagePath);
            // 遍历 Map 中的每个键值对
            for (Map.Entry<String, String> entry : map.entrySet()) {
                // 每次循环创建一个新的 Bullet 对象
                Bullet bullet = new Bullet();
                bullet.setBulletName(entry.getKey());
                bullet.setBulletPrice(entry.getValue());
                // 调用服务层的保存方法
                boolean result = bulletService.save(bullet);
                if (!result) {
                    // 如果保存失败，记录日志并返回失败响应
                    log.error("Failed to save bullet: name={}, price={}", entry.getKey(), entry.getValue());
                    return ResultUtils.error(ErrorCode.SAVE_FAILED, "保存子弹信息失败");
                }
            }
            // 所有子弹信息保存成功，返回成功响应
            return ResultUtils.success(1L);
        } catch (Exception e) {
            // 捕获并处理异常，记录日志并返回错误响应
            log.error("Error occurred while adding bullet prices", e);
            return ResultUtils.error(ErrorCode.INTERNAL_SERVER_ERROR, "服务器内部错误");
        }

    }

    @PostMapping("/add/bulletBatch")
    public BaseResponse<Long> addBulletPriceByFolder(String folderPath) {
        try {
            // 从服务层获取子弹价格的 Map
            Map<String, String> map = bulletService.getBulletPriceMapByFolder(folderPath);
            // 遍历 Map 中的每个键值对
            for (Map.Entry<String, String> entry : map.entrySet()) {
                // 每次循环创建一个新的 Bullet 对象
                Bullet bullet = new Bullet();
                bullet.setBulletName(entry.getKey());
                bullet.setBulletPrice(entry.getValue());
                // 调用服务层的保存方法
                boolean result = bulletService.save(bullet);
                if (!result) {
                    // 如果保存失败，记录日志并返回失败响应
                    log.error("Failed to save bullet: name={}, price={}", entry.getKey(), entry.getValue());
                    return ResultUtils.error(ErrorCode.SAVE_FAILED, "保存子弹信息失败");
                }
            }
            // 所有子弹信息保存成功，返回成功响应
            return ResultUtils.success(1L);
        } catch (Exception e) {
            // 捕获并处理异常，记录日志并返回错误响应
            log.error("Error occurred while adding bullet prices", e);
            return ResultUtils.error(ErrorCode.INTERNAL_SERVER_ERROR, "服务器内部错误");
        }

    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<BulletVO>> getBulletVOPage(@RequestBody BulletQueryRequest  bulletQueryRequest,
                                                        HttpServletRequest request) {
        if (bulletQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if(!loginUser.getUserRole().equals("admin")){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        long current = bulletQueryRequest.getCurrent();
        long size = bulletQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<Bullet> bulletPage = bulletService.page(new Page<>(current, size),
                bulletService.getQueryWrapper(bulletQueryRequest));
        return ResultUtils.success(bulletService.getBulletVOPage(bulletPage, request));
    }

}
