package com.invoice.invoicegenerate.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 产品 对象 product
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

    /**
     * 主键
     */
    private String id;

    /**
     * 删除标识（0-正常,1-删除）
     */
    private String delFlag;

    /**
     * 名称规格
     */
    private String nameSpecification;

}
