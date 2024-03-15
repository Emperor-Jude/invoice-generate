package com.invoice.invoicegenerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.invoice.invoicegenerate.domain.Client;
import com.invoice.invoicegenerate.domain.vo.ClientVo;
import com.invoice.invoicegenerate.mapper.ClientMapper;
import com.invoice.invoicegenerate.service.IClientService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户 Service业务层处理
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {


    @Override
    public List<ClientVo> queryAll(ClientVo entity) {
        return this.baseMapper.queryList(entity);
    }

    @Override
    public ClientVo queryById(String id) {
        return this.baseMapper.queryById(id);
    }
}
