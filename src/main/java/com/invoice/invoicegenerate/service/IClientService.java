package com.invoice.invoicegenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.invoice.invoicegenerate.domain.Client;
import com.invoice.invoicegenerate.domain.vo.ClientVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户 Service接口
 *
 * @author Tellsea
 * @date 2024-03-01
 */

public interface IClientService extends IService<Client> {

    /**
     * 查询全部
     *
     * @param entity
     * @return
     */
    List<ClientVo> queryAll(ClientVo entity);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    ClientVo queryById(String id);
}
