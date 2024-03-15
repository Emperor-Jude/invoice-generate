package com.invoice.invoicegenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.invoice.invoicegenerate.domain.Product;
import com.invoice.invoicegenerate.domain.vo.ProductVo;

import java.util.List;

/**
 * 产品 Service接口
 *
 * @author Tellsea
 * @date 2024-03-01
 */
public interface IProductService extends IService<Product> {


    /**
     * 查询全部
     *
     * @param entity
     * @return
     */
    List<ProductVo> queryAll(ProductVo entity);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    ProductVo queryById(String id);
}
