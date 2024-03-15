package com.invoice.invoicegenerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.invoice.invoicegenerate.domain.Product;
import com.invoice.invoicegenerate.domain.vo.ProductVo;
import com.invoice.invoicegenerate.mapper.ProductMapper;
import com.invoice.invoicegenerate.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品 Service业务层处理
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Override
    public List<ProductVo> queryAll(ProductVo entity) {
        return this.baseMapper.queryList(entity);
    }

    @Override
    public ProductVo queryById(String id) {
        return this.baseMapper.queryById(String.valueOf(id));
    }
}
