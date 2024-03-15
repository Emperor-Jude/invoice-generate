package com.invoice.invoicegenerate.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 客户 对象 client
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Client extends BaseEntity {

    /**
     * 主键
     */
    private String id;

    /**
     * 删除标识（0-正常,1-删除）
     */
    private String delFlag;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 客户代码
     */
    private String clientCode;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String contactWay;

}
