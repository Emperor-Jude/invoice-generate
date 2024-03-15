package com.invoice.invoicegenerate.domain.vo;

import com.invoice.invoicegenerate.domain.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 产品 Vo对象 product
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductVo extends Product {

}
