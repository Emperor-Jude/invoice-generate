package com.invoice.invoicegenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.invoice.invoicegenerate.domain.PdfFile;
import com.invoice.invoicegenerate.domain.vo.PdfFileVo;


import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author Tellsea
 * @date 2024-03-14
 */
public interface IPdfFileService extends IService<PdfFile> {

    /**
     * 查询全部
     *
     * @param entity
     * @return
     */
    List<PdfFileVo> queryAll(PdfFileVo entity);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    PdfFileVo queryById(Long id);
    PdfFileVo queryById(String id);
}
