package com.invoice.invoicegenerate.controller;


import com.invoice.invoicegenerate.domain.AjaxResult;
import com.invoice.invoicegenerate.domain.vo.ClientVo;
import com.invoice.invoicegenerate.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 客户 Controller
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@RestController
@RequestMapping("/invoice/client")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ClientController extends BaseController{

    private final IClientService clientService;


    @GetMapping("/listAll")
    public AjaxResult listAll(ClientVo entity) {
        return AjaxResult.success("查询成功", clientService.queryAll(entity));
    }

    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success("查询成功", clientService.queryById(id));
    }

    @PostMapping("add")
    public AjaxResult add(@RequestBody ClientVo entity) {
        entity.setDelFlag(String.valueOf(1));
        return toAjax(clientService.save(entity));
    }

    @PostMapping("edit")
    public AjaxResult edit(@RequestBody ClientVo entity) {
        return toAjax(clientService.updateById(entity));
    }

	@GetMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(clientService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
