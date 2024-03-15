package com.invoice.invoicegenerate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.invoice.invoicegenerate.domain.Product;
import com.invoice.invoicegenerate.domain.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品 Mapper接口
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {


    List<ProductVo> queryList(@Param("entity") ProductVo entity);

    ProductVo queryById(@Param("id") String id);

}
