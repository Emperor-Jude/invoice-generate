package com.invoice.invoicegenerate.controller;


import com.invoice.invoicegenerate.domain.AjaxResult;
import com.invoice.invoicegenerate.domain.vo.ProductVo;
import com.invoice.invoicegenerate.service.IProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 产品 Controller
 *
 * @author Tellsea
 * @date 2024-03-01
 */
@RestController
@RequestMapping("/invoice/product")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductController extends BaseController {

    private final IProductService productService;

    @GetMapping("/listAll")
    public AjaxResult listAll(ProductVo entity) {
        return AjaxResult.success("查询成功", productService.queryAll(entity));
    }

    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success("查询成功", productService.queryById(id));
    }

    @PostMapping("add")
    public AjaxResult add(@RequestBody ProductVo entity) {
        entity.setDelFlag(String.valueOf(1));
        return toAjax(productService.save(entity));
    }

    @PostMapping("edit")
    public AjaxResult edit(@RequestBody ProductVo entity) {
        return toAjax(productService.updateById(entity));
    }

	@GetMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(productService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
