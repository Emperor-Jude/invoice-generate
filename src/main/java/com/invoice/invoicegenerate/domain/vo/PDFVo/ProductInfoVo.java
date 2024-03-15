package com.invoice.invoicegenerate.domain.vo.PDFVo;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class ProductInfoVo {
    private String id;
    private String nameSpecification;
    private String number;
    private String unit;
    private String unitPrice;
    private String totalPrice;
    private String remark;
    private String packingSpecification;

    public JSONObject toJson() {
        return JSONUtil.parseObj(this);
    }
}
