package com.invoice.invoicegenerate.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.invoice.invoicegenerate.domain.AjaxResult;
import com.invoice.invoicegenerate.domain.vo.PDFVo.FormVo;
import com.invoice.invoicegenerate.utils.PDFUtils;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invoice/pdf")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PDFGenerateController {

    private final Cache<String, Object> caffeineCache;

    @PostMapping("add")
    public AjaxResult add(@RequestBody FormVo entity) {
        String CACHE_KEY = "pdf:add";
        Object ifPresent = caffeineCache.getIfPresent(CACHE_KEY);
        List<FormVo> formArrayVo = (List<FormVo>) caffeineCache.asMap().get(CACHE_KEY);
        if (formArrayVo == null) {
            formArrayVo = new ArrayList<FormVo>();
        }
        formArrayVo.add(entity);
        caffeineCache.put(CACHE_KEY, formArrayVo);
        return AjaxResult.success();
    }

    private final PDFUtils pdfUtils;

    @PostMapping("generate")
    public AjaxResult generatePDF(HttpServletResponse response) {
        String CACHE_KEY = "pdf:add";
        List<FormVo> formArrayVo = (List<FormVo>) caffeineCache.asMap().get(CACHE_KEY);
        if (formArrayVo == null) {
            return AjaxResult.error();
        }

        //response.setContentType("application/pdf");// 设置输出格式头信息

        List<String> fileNameList = pdfUtils.generatePDFByInvoice(formArrayVo);


        caffeineCache.invalidate(CACHE_KEY);

        return AjaxResult.success();
    }

}
