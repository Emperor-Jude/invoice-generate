package com.invoice.invoicegenerate.domain.vo;

import com.invoice.invoicegenerate.domain.Client;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 客户 Vo对象 client
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ClientVo extends Client {

}
