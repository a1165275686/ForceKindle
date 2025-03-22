package com.rose.model.entity;

import com.rose.model.VO.BulletVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class Bullet implements Serializable {

    private Long id;

    private String bulletName;

    private String bulletPrice;

    private String bulletImage;

    private String bulletType;

    private String bulletDescription;

    private String createTime;

    private String updateTime;

    private static final long serialVersionUID = 1L;

    public static BulletVO objToVo(Bullet bullet) {
        BulletVO bulletVO = new BulletVO();
        BeanUtils.copyProperties(bullet, bulletVO);
        return bulletVO;
    }

}
