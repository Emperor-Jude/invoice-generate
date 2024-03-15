package com.invoice.invoicegenerate.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 【请填写功能名称】对象 pdf_file
 *
 * @author Tellsea
 * @date 2024-03-14
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PdfFile extends BaseEntity {

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 删除标识（0-正常,1-删除）
     */
    private String delFlag;

    /**
     * 文件名
     */
    private String filename;

    /**
     * $column.columnComment
     */
    private String url;

}
