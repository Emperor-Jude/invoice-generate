package com.invoice.invoicegenerate.domain.vo.PDFVo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class InvoiceArrayVo {
    @JsonProperty("FormVo")
    private List<FormVo> formVoList;
}
