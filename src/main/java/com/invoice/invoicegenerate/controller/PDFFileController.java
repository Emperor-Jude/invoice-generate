package com.invoice.invoicegenerate.controller;

import cn.hutool.core.io.FileUtil;
import com.invoice.invoicegenerate.domain.AjaxResult;
import com.invoice.invoicegenerate.domain.vo.ClientVo;
import com.invoice.invoicegenerate.domain.vo.PdfFileVo;
import com.invoice.invoicegenerate.service.IPdfFileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

import static com.invoice.invoicegenerate.constant.Paths.ROOT_PATH;

@RestController
@RequestMapping("/invoice/pdfFile")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PDFFileController extends BaseController{

    private final IPdfFileService iPdfFileService;

    @GetMapping("/listAll")
    public AjaxResult listAll(PdfFileVo entity) {
        return AjaxResult.success("查询成功", iPdfFileService.queryAll(entity));
    }

    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success("查询成功", iPdfFileService.queryById(id));
    }

    @PostMapping("add")
    public AjaxResult add(@RequestBody PdfFileVo entity) {
        entity.setDelFlag(String.valueOf(1));
        return toAjax(iPdfFileService.save(entity));
    }

    @PostMapping("edit")
    public AjaxResult edit(@RequestBody PdfFileVo entity) {
        return toAjax(iPdfFileService.updateById(entity));
    }

    @GetMapping("/remove/{id}/{filename}")
    public AjaxResult remove(@PathVariable String id,@PathVariable String filename) {
        String filePath = ROOT_PATH + filename;
        boolean del = FileUtil.del(filePath);
        boolean b = iPdfFileService.removeById(id);
        if(del || b){
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        String filePath = ROOT_PATH + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            // 设置响应头
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            try {
                // 读取文件并将内容写入 HttpServletResponse 输出流
                FileInputStream fileInputStream = new FileInputStream(file);
                OutputStream outputStream = response.getOutputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // 关闭输入输出流
                fileInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } else {
            // 如果文件不存在，返回 404 Not Found
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    @GetMapping("/download/{fileName}/{pre}")
    public AjaxResult download(@PathVariable String fileName,@PathVariable String pre, HttpServletResponse response) throws IOException {
        String filePath = ROOT_PATH + fileName;

        if (!FileUtil.exist(filePath)) {
            return AjaxResult.error("操作失败，文件路径不存在");
        }

        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        // 数组是一个字节数组，也就是文件的字节流数组
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        return AjaxResult.success("操作成功");
    }

}
