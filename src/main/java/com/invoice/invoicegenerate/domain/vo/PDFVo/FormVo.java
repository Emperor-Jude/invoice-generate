package com.invoice.invoicegenerate.domain.vo.PDFVo;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class FormVo {
   private String id;
   private String clientName;
   private String clientCode;
   private String contacts;
   private String contactWay;
   private String warehouse;
   private String currentDate;
   private String oddNum;
   /**
    * 制单人
    */
   private String preparedBy;
   private String location;
   /**
    * 客服热线
    */
   private String customerServiceHotline;
   /**
    * 投诉热线
    */
   private String complaintsHotline;
   private String remark;
   private List<ProductInfoVo> productInfo;
   private String savePath;

   public JSONObject toJson() {
      return JSONUtil.parseObj(this);
   }
}
