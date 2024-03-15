package com.invoice.invoicegenerate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.invoice.invoicegenerate.domain.PdfFile;
import com.invoice.invoicegenerate.domain.vo.PdfFileVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author Tellsea
 * @date 2024-03-14
 */
@Mapper
public interface PdfFileMapper extends BaseMapper<PdfFile> {

    Page<PdfFileVo> queryList(Page<?> page, @Param("entity") PdfFileVo entity);

    List<PdfFileVo> queryList(@Param("entity") PdfFileVo entity);

    PdfFileVo queryById(@Param("id") String id);

}
