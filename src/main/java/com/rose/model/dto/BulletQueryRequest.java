package com.rose.model.dto;

import com.rose.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class BulletQueryRequest extends PageRequest implements Serializable {

    private String bulletName;

    private String bulletType;

    private String bulletPrice;

    private static final long serialVersionUID = 1L;
}
