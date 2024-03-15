package com.invoice.invoicegenerate.config;

import com.invoice.invoicegenerate.utils.NumberChineseFormatterUtils;
import com.invoice.invoicegenerate.utils.PDFUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BeanConfig {
    @Bean
    public PDFUtils pdfUtils() throws IOException {
        return new PDFUtils();
    }

    @Bean
    public NumberChineseFormatterUtils NumToChine(){
        return new NumberChineseFormatterUtils();
    }
}
