package com.rose.model.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class BulletVO implements Serializable {

    private String bulletName;

    private String bulletPrice;

    private static final long serialVersionUID = 1L;
}
