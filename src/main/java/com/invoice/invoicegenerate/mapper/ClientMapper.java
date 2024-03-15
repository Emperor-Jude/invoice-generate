package com.invoice.invoicegenerate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.invoice.invoicegenerate.domain.Client;
import com.invoice.invoicegenerate.domain.vo.ClientVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户 Mapper接口
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@Mapper
public interface ClientMapper extends BaseMapper<Client> {


    List<ClientVo> queryList(@Param("entity") ClientVo entity);

    ClientVo queryById(@Param("id") String id);

}
