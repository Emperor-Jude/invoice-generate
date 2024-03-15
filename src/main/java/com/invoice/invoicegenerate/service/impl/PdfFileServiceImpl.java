package com.invoice.invoicegenerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.invoice.invoicegenerate.domain.PdfFile;
import com.invoice.invoicegenerate.domain.vo.PdfFileVo;
import com.invoice.invoicegenerate.mapper.PdfFileMapper;
import com.invoice.invoicegenerate.service.IPdfFileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author Tellsea
 * @date 2024-03-14
 */
@Service
public class PdfFileServiceImpl extends ServiceImpl<PdfFileMapper, PdfFile> implements IPdfFileService {


    @Override
    public List<PdfFileVo> queryAll(PdfFileVo entity) {
        return this.baseMapper.queryList(entity);
    }

    @Override
    public PdfFileVo queryById(Long id) {
        return this.baseMapper.queryById(String.valueOf(id));
    }

    @Override
    public PdfFileVo queryById(String id) {
        return this.baseMapper.queryById(id);
    }

}
