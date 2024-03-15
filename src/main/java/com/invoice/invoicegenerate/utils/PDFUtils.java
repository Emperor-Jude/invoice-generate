package com.invoice.invoicegenerate.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.json.JSONObject;
import com.invoice.invoicegenerate.domain.PdfFile;
import com.invoice.invoicegenerate.domain.vo.PDFVo.FormVo;
import com.invoice.invoicegenerate.domain.vo.PDFVo.ProductInfoVo;
import com.invoice.invoicegenerate.service.IPdfFileService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.invoice.invoicegenerate.constant.Paths.ROOT_PATH;

public class PDFUtils {
    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    //正常细体字体
    Font fontFine = new Font(bfChinese, 10, Font.NORMAL);
    //正常细体字体
    Font fontFine2 = new Font(bfChinese, 8, Font.NORMAL);
    //正常字体
    Font font = new Font(bfChinese, 11, Font.BOLD);
    //正常加粗字体
    Font fontc = new Font(bfChinese, 8, Font.NORMAL);
    //正常加粗字体
    Font fontBold = new Font(bfChinese, 10, Font.BOLD);
    //加粗大字体
    Font fontBigBold = new Font(bfChinese, 16, Font.NORMAL);

    public PDFUtils() throws DocumentException, IOException {
    }

    public List<String> generatePDFByInvoice(List<FormVo> formVoList) {

        List<String> fileNameList = new ArrayList<>();

        for (FormVo vo : formVoList) {
            // 构建PDF文件名
            String savePath = ROOT_PATH + getCurrentDate() + ".pdf";
            fileNameList.add(savePath);
            generatePDF(savePath, vo);
        }

        return fileNameList;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public long getCurrentDate() {

        Instant instant = Instant.now();

        // 将 Instant 转换为纳秒级时间戳
        return instant.toEpochMilli() * 1_000_000 + instant.getNano();
    }


    @Value("${ip:127.0.0.1}")
    private String ip;
    @Value("${server.port}")
    private String port;

    @Autowired
    private IPdfFileService iPdfFileService;
    /**
     * 生成pdf文件
     *
     * @param savePath
     * @param vo
     * HttpServletResponse response,
     */
    private void generatePDF(String savePath, FormVo vo) {

        File file = new File(savePath);

        // 这里是删除以前上传的附件 毕竟为了节省空间
        FileUtil.del(file);
        // 如果文件夹不存在 创建文件夹
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
        }

        String mainName = FileNameUtil.getName(file);
        String url = "http://" + ip + ":" + port + "/invoice/pdfFile/download/"+mainName+"/pre";
        PdfFile pdfFile = new PdfFile();
        pdfFile.setDelFlag("1");
        pdfFile.setFilename(mainName);
        pdfFile.setUrl(url);
        iPdfFileService.save(pdfFile);

        try (OutputStream out = new FileOutputStream(file)){
            Document document = new Document(PageSize.A4, 50, 40, 5, 50);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            /**============================文字类的内容处理============================*/
            //创建一个段落
//            Paragraph title = new Paragraph("力元纵购五金建材出仓单", fontBigBold);
//            //设置该段落为水平居中
//            title.setAlignment(Element.ALIGN_CENTER);
//            //将该段落放入该文件中
//            document.add(title);
            PdfPTable table1 = generateTable1(vo);
            PdfPTable table2 = generateTable2(vo.getProductInfo());
            PdfPTable table3 = generateTable3(vo);

            //在创建一个段落，文字内容为当前日期
            Paragraph hint = new Paragraph("温馨提示：货物数量请当面点清，过后数量有误，本公司概不负责；" +
                    "商品质量问题需退换请出示本单据，如无法出示单据，恕不给予退换；非质量问题退换货，" +
                    "按本单价格打 8 折退换；特殊订货产品非质量问题不予退换。多谢理解。", fontFine2);
            //设置该段落文字水平靠右
            hint.setAlignment(Element.ALIGN_LEFT);
            //设置间距
            hint.setSpacingAfter(10f);
            hint.setSpacingBefore(10f);
            //将该段落添加到文件中

            PdfPTable table4 = generateTable4(vo);

            PdfPTable table = generateTableDemo();
            document.add(table);

            //document.add(table1);
            document.add(table2);
            document.add(table3);
            document.add(hint);
            document.add(table4);
            document.close();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 客户名称，客户代码
     *
     * @param vo
     * @return
     * @throws DocumentException
     */
    private PdfPTable generateTable1(FormVo vo) throws DocumentException {
        //创建一个pdf的表格
        PdfPTable table = new PdfPTable(3);
        //设置该表格的基本属性
        table.setWidthPercentage(100); // 宽度100%填充
        table.setSpacingBefore(20f); // 前间距
        table.setSpacingAfter(1f); // 后间距
        //创建表格的的行对象集合，并指向表格行对象
        ArrayList<PdfPRow> listRow = table.getRows();
        //将表格设置为八列，并指定列宽
        float[] columnWidths = {
                4f, 3f, 3f
        };
        table.setWidths(columnWidths);

        //创建8个单元格，并指定给第一行
        PdfPCell[] cells0 = new PdfPCell[3];
        PdfPRow row0 = new PdfPRow(cells0);
        //配置第一个单元格--单位名称
        cells0[0] = new PdfPCell(new Paragraph("客户名称：" + vo.getClientName(), fontFine));//单元格内容
        cells0[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells0[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells0[0].setFixedHeight(5f);//固定高度
        // 设置单元格的边框宽度为0，即无边框
        cells0[0].setBorderWidth(0);
        //配置第二个单元格--开始年月
        cells0[1] = new PdfPCell(new Paragraph("", fontFine));
        cells0[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells0[1].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells0[1].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells0[2] = new PdfPCell(new Paragraph("关联-" + vo.getWarehouse(), fontFine));
        cells0[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells0[2].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells0[2].setBorderWidth(0);
        //将第一行加入到表格的行集合中
        listRow.add(row0);

        //创建7个单元格，并指定给第一行
        PdfPCell[] cells1 = new PdfPCell[3];
        PdfPRow row1 = new PdfPRow(cells1);
        //配置第一个单元格--单位名称
        cells1[0] = new PdfPCell(new Paragraph("客户代码：" + vo.getClientCode(), fontFine));//单元格内容
        cells1[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells1[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells1[0].setFixedHeight(5f);//固定高度
        // 设置单元格的边框宽度为0，即无边框
        cells1[0].setBorderWidth(0);
        //配置第二个单元格--开始年月
        cells1[1] = new PdfPCell(new Paragraph("联系人：" + vo.getContactWay(), fontFine));
        cells1[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells1[1].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells1[1].setBorderWidth(0);
        //配置第三个单元格--统筹区
        cells1[2] = new PdfPCell(new Paragraph("单号：" + vo.getOddNum(), fontFine));
        cells1[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells1[2].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells1[2].setBorderWidth(0);
        //将第一行加入到表格的行集合中
        listRow.add(row1);
        //创建7个单元格，并指定给第一行
        PdfPCell cells2[] = new PdfPCell[3];
        PdfPRow row2 = new PdfPRow(cells2);
        //配置第一个单元格--单位名称
        cells2[0] = new PdfPCell(new Paragraph("日期：" + vo.getCurrentDate(), fontFine));//单元格内容
        cells2[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells2[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells2[0].setFixedHeight(5f);//固定高度
        // 设置单元格的边框宽度为0，即无边框
        cells2[0].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells2[2] = new PdfPCell(new Paragraph("电话：" + vo.getContacts(), fontFine));
        cells2[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells2[2].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells2[2].setBorderWidth(0);
        //将第一行加入到表格的行集合中
        listRow.add(row2);
        //把表格添加到文档中
        table.setSpacingAfter(-10f);

        return table;
    }

    private PdfPTable generateTableDemo() throws DocumentException {
        //创建一个pdf的表格
        PdfPTable table = new PdfPTable(8);
        //设置该表格的基本属性
        table.setWidthPercentage(100); // 宽度100%填充
        table.setSpacingBefore(20f); // 前间距
        table.setSpacingAfter(1f); // 后间距
        //创建表格的的行对象集合，并指向表格行对象
        ArrayList<PdfPRow> listRow = table.getRows();
        //将表格设置为八列，并指定列宽
        float[] columnWidths = {
                2.7f, 3f, 2.3f, 3f, 3f, 3f, 3f, 3f
        };
        table.setWidths(columnWidths);

        /**=======================标题=========================*/
        //创建8个单元格，并指定给第一行
        PdfPCell[] cells0 = new PdfPCell[8];
        PdfPRow row0 = new PdfPRow(cells0);
        //配置第一个单元格--单位名称
        cells0[0] = new PdfPCell(new Paragraph("", fontFine));//单元格内容
        cells0[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells0[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells0[0].setFixedHeight(5f);//固定高度
        cells0[0].setBorderWidth(0);

        //配置第二个单元格--开始年月
        cells0[1] = new PdfPCell(new Paragraph("", fontFine));
        cells0[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells0[1].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells0[1].setBorderWidth(0);

        //配置第8个单元格--统筹区
        cells0[2] = new PdfPCell(new Paragraph("力元纵购五金建材出仓单", fontBigBold));
        cells0[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells0[2].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        cells0[2].setColspan(4);
        cells0[2].setBorderWidth(0);

        //配置第8个单元格--统筹区
        cells0[3] = new PdfPCell(new Paragraph("", fontFine));
        cells0[3].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells0[3].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells0[3].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells0[4] = new PdfPCell(new Paragraph("", fontFine));
        cells0[4].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells0[4].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells0[4].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells0[5] = new PdfPCell(new Paragraph("", fontFine));
        cells0[5].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells0[5].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells0[5].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells0[6] = new PdfPCell(new Paragraph("关联-五金仓", fontFine));
        cells0[6].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells0[6].setVerticalAlignment(Element.ALIGN_BOTTOM);//垂直居中
        cells0[6].setColspan(2);
        cells0[6].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells0[7] = new PdfPCell(new Paragraph("", fontFine));
        cells0[7].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells0[7].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        cells0[7].setBorderWidth(0);
        //将第一行加入到表格的行集合中
        listRow.add(row0);


        /**=======================客户名称，单号=========================*/

        //创建8个单元格，并指定给第一行
        PdfPCell[] cells1 = new PdfPCell[8];
        PdfPRow row1 = new PdfPRow(cells1);
        //配置第一个单元格--单位名称
        cells1[0] = new PdfPCell(new Paragraph("客户名称：", font));//单元格内容
        cells1[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells1[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        //cells1[0].setFixedHeight(5f);//固定高度
//        cells1[0].setRowspan(2);
//        cells1[0].setColspan(2);
        cells1[0].setBorderWidth(0);
        //配置第二个单元格--开始年月
        cells1[1] = new PdfPCell(new Paragraph("横山镇鸿辉五金店", fontFine));
        cells1[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells1[1].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells1[1].setColspan(3);
        cells1[1].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells1[2] = new PdfPCell(new Paragraph("", fontFine));
        cells1[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells1[2].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        //cells1[2].setColspan(4);
        cells1[2].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells1[3] = new PdfPCell(new Paragraph("", fontFine));
        cells1[3].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells1[3].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells1[3].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells1[4] = new PdfPCell(new Paragraph("", fontFine));
        cells1[4].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells1[4].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells1[4].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells1[5] = new PdfPCell(new Paragraph("", fontFine));
        cells1[5].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells1[5].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells1[5].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells1[6] = new PdfPCell(new Paragraph("单号：", fontFine));
        cells1[6].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells1[6].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        cells1[6].setColspan(2);
        cells1[6].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells1[7] = new PdfPCell(new Paragraph("", fontFine));
        cells1[7].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells1[7].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        cells1[7].setBorderWidth(0);
        //将第一行加入到表格的行集合中
        listRow.add(row1);

        /**=======================日期=========================*/

        //创建8个单元格，并指定给第一行
        PdfPCell[] cells2 = new PdfPCell[8];
        PdfPRow row2 = new PdfPRow(cells2);
        //配置第一个单元格--单位名称
        cells2[0] = new PdfPCell(new Paragraph("客户代码：", font));//单元格内容
        cells2[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells2[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        //cells2[0].setFixedHeight(5f);//固定高度
//        cells2[0].setColspan(2);
//        cells2[0].setRowspan(2);
        cells2[0].setBorderWidth(0);
        //配置第二个单元格--开始年月
        cells2[1] = new PdfPCell(new Paragraph("21101", fontFine));
        cells2[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells2[1].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells2[1].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells2[2] = new PdfPCell(new Paragraph("联系人：", font));
        cells2[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells2[2].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        //cells1[2].setColspan(4);
        cells2[2].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells2[3] = new PdfPCell(new Paragraph("莫理英", fontFine));
        cells2[3].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells2[3].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells2[3].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells2[4] = new PdfPCell(new Paragraph("", fontFine));
        cells2[4].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells2[4].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells2[4].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells2[5] = new PdfPCell(new Paragraph("", fontFine));
        cells2[5].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells2[5].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells2[5].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells2[6] = new PdfPCell(new Paragraph("日期：", fontFine));
        cells2[6].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells2[6].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        cells2[6].setColspan(2);
        cells2[6].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells2[7] = new PdfPCell(new Paragraph("", fontFine));
        cells2[7].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells2[7].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        cells2[7].setBorderWidth(0);
        //将第一行加入到表格的行集合中
        listRow.add(row2);

        //创建8个单元格，并指定给第一行
        PdfPCell[] cells3 = new PdfPCell[8];
        PdfPRow row3 = new PdfPRow(cells3);
        //配置第一个单元格--单位名称
        cells3[0] = new PdfPCell(new Paragraph("", font));//单元格内容
        cells3[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells3[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells3[0].setFixedHeight(5f);//固定高度
        cells3[0].setBorderWidth(0);
        //配置第二个单元格--开始年月
        cells3[1] = new PdfPCell(new Paragraph("", fontFine));
        cells3[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells3[1].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells3[1].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells3[2] = new PdfPCell(new Paragraph("", font));
        cells3[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells3[2].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        //cells1[2].setColspan(4);
        cells3[2].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells3[3] = new PdfPCell(new Paragraph("", fontFine));
        cells3[3].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells3[3].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells3[3].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells3[4] = new PdfPCell(new Paragraph("", fontFine));
        cells3[4].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells3[4].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells3[4].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells3[5] = new PdfPCell(new Paragraph("", fontFine));
        cells3[5].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells3[5].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells3[5].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells3[6] = new PdfPCell(new Paragraph("电话：", fontFine));
        cells3[6].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells3[6].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        cells3[6].setColspan(2);
        cells3[6].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells3[7] = new PdfPCell(new Paragraph("", fontFine));
        cells3[7].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells3[7].setVerticalAlignment(Element.ALIGN_CENTER);//垂直居中
        cells3[7].setBorderWidth(0);
        //将第一行加入到表格的行集合中
        listRow.add(row3);

        //创建7个单元格，并指定给第一行
        /*PdfPCell[] cells1 = new PdfPCell[3];
        PdfPRow row1 = new PdfPRow(cells1);
        //配置第一个单元格--单位名称
        cells1[0] = new PdfPCell(new Paragraph("客户代码：" + vo.getClientCode(), fontFine));//单元格内容
        cells1[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells1[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells1[0].setFixedHeight(5f);//固定高度
        //配置第二个单元格--开始年月
        cells1[1] = new PdfPCell(new Paragraph("联系人：" + vo.getContactWay(), fontFine));
        cells1[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells1[1].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        //配置第三个单元格--统筹区
        cells1[2] = new PdfPCell(new Paragraph("单号：" + vo.getOddNum(), fontFine));
        cells1[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells1[2].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        //将第一行加入到表格的行集合中
        listRow.add(row1);
        //创建7个单元格，并指定给第一行
        PdfPCell cells2[] = new PdfPCell[3];
        PdfPRow row2 = new PdfPRow(cells2);
        //配置第一个单元格--单位名称
        cells2[0] = new PdfPCell(new Paragraph("日期：" + vo.getCurrentDate(), fontFine));//单元格内容
        cells2[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells2[0].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        cells2[0].setFixedHeight(5f);//固定高度
        // 设置单元格的边框宽度为0，即无边框
        cells2[0].setBorderWidth(0);
        //配置第8个单元格--统筹区
        cells2[2] = new PdfPCell(new Paragraph("电话：" + vo.getContacts(), fontFine));
        cells2[2].setHorizontalAlignment(Element.ALIGN_MIDDLE);//水平居中
        cells2[2].setVerticalAlignment(Element.ALIGN_TOP);//垂直居中
        //将第一行加入到表格的行集合中
        listRow.add(row2);*/

        table.setSpacingAfter(-10f);

        return table;
    }

    /**
     * 自动生成表格
     *
     * @param productInfoVos
     * @return
     * @throws DocumentException
     */
    private PdfPTable generateTable2(List<ProductInfoVo> productInfoVos) throws DocumentException {
        //创建一个pdf的表格
        PdfPTable table2 = new PdfPTable(8);
        //设置该表格的基本属性
        table2.setWidthPercentage(100); // 宽度100%填充
        table2.setSpacingBefore(20f); // 前间距
        table2.setSpacingAfter(1f); // 后间距
        //创建表格的的行对象集合，并指向表格行对象
        ArrayList<PdfPRow> listRow2 = table2.getRows();
        //将表格设置为八列，并指定列宽
        float[] columnWidths2 = {
                1f, 4f, 1f, 1f, 2f, 2f, 2f, 3f
        };
        table2.setWidths(columnWidths2);

        //创建7个单元格，并指定给第一行
        PdfPCell[] cells00 = new PdfPCell[8];
        PdfPRow row00 = new PdfPRow(cells00);
        cells00[0] = new PdfPCell(new Paragraph("序", font));//单元格内容
        cells00[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells00[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells00[0].setFixedHeight(16f);//固定高度

        cells00[1] = new PdfPCell(new Paragraph("名 称 规 格", font));
        cells00[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells00[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells00[2] = new PdfPCell(new Paragraph("单位", font));
        cells00[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells00[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells00[3] = new PdfPCell(new Paragraph("数量", font));
        cells00[3].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells00[3].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells00[4] = new PdfPCell(new Paragraph("单价", font));
        cells00[4].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells00[4].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells00[5] = new PdfPCell(new Paragraph("金额", font));
        cells00[5].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells00[5].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells00[6] = new PdfPCell(new Paragraph("备注", font));
        cells00[6].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells00[6].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells00[7] = new PdfPCell(new Paragraph("包装规格", font));
        cells00[7].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells00[7].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        //将第一行加入到表格的行集合中
        listRow2.add(row00);

        double countMoney = 0D;
        int count = 1;
        for (ProductInfoVo vo : productInfoVos) {
            //创建7个单元格，并指定给第一行
            PdfPCell[] cells = new PdfPCell[8];
            PdfPRow row = new PdfPRow(cells);

            cells[0] = new PdfPCell(new Phrase(String.valueOf(count), fontFine));
            cells[0].setHorizontalAlignment(Element.ALIGN_CENTER);
            cells[0].setVerticalAlignment(Element.ALIGN_MIDDLE);

            cells[1] = new PdfPCell(new Phrase(vo.getNameSpecification(), fontFine));
            cells[1].setHorizontalAlignment(Element.ALIGN_LEFT);
            cells[1].setVerticalAlignment(Element.ALIGN_MIDDLE);

            cells[2] = new PdfPCell(new Phrase(vo.getUnit(), fontFine));
            cells[2].setHorizontalAlignment(Element.ALIGN_CENTER);
            cells[2].setVerticalAlignment(Element.ALIGN_MIDDLE);

            cells[3] = new PdfPCell(new Phrase(vo.getNumber(), fontFine));
            cells[3].setHorizontalAlignment(Element.ALIGN_CENTER);
            cells[3].setVerticalAlignment(Element.ALIGN_MIDDLE);

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedValue = decimalFormat.format(Double.valueOf(vo.getUnitPrice()));
            cells[4] = new PdfPCell(new Phrase(formattedValue, fontFine));
            cells[4].setHorizontalAlignment(Element.ALIGN_CENTER);
            cells[4].setVerticalAlignment(Element.ALIGN_MIDDLE);

            cells[5] = new PdfPCell(new Phrase(vo.getTotalPrice(), fontFine));
            cells[5].setHorizontalAlignment(Element.ALIGN_CENTER);
            cells[5].setVerticalAlignment(Element.ALIGN_MIDDLE);

            cells[6] = new PdfPCell(new Phrase(vo.getRemark(), fontFine));
            cells[6].setHorizontalAlignment(Element.ALIGN_CENTER);
            cells[6].setVerticalAlignment(Element.ALIGN_MIDDLE);

            cells[7] = new PdfPCell(new Phrase(vo.getPackingSpecification(), fontFine));
            cells[7].setHorizontalAlignment(Element.ALIGN_CENTER);
            cells[7].setVerticalAlignment(Element.ALIGN_MIDDLE);

            listRow2.add(row);
            double v = Double.parseDouble(vo.getTotalPrice());
            countMoney += v;
            count += 1;
        }

        // 转大写
        String format = NumberChineseFormatterUtils.format(countMoney, true);

        //创建7个单元格，并指定给第一行
        PdfPCell[] cells01 = new PdfPCell[8];
        PdfPRow row01 = new PdfPRow(cells01);
        cells01[0] = new PdfPCell(new Paragraph("金额合计 (大写)：" + format, fontFine));//单元格内容
        cells01[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells01[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells01[0].setFixedHeight(14f);//固定高度
        // 跨列
        cells01[0].setColspan(6);

        cells01[1] = new PdfPCell(new Paragraph("", fontFine));
        cells01[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells01[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells01[2] = new PdfPCell(new Paragraph("", fontFine));
        cells01[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells01[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells01[3] = new PdfPCell(new Paragraph("", fontFine));
        cells01[3].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells01[3].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells01[4] = new PdfPCell(new Paragraph("", fontFine));
        cells01[4].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells01[4].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells01[5] = new PdfPCell(new Paragraph("", fontFine));
        cells01[5].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells01[5].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedValue = decimalFormat.format(countMoney);

        cells01[6] = new PdfPCell(new Paragraph("小写：￥" + formattedValue, fontFine));
        cells01[6].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells01[6].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        // 跨列
        cells01[6].setColspan(2);
        // 设置单元格的边框宽度为0，即无边框
//            cells01[6].setBorderWidthLeft(0);

        cells01[7] = new PdfPCell(new Paragraph("", fontFine));
        cells01[7].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells01[7].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        //将第一行加入到表格的行集合中
        listRow2.add(row01);

        //把表格添加到文档中
        table2.setSpacingAfter(-14f);

        return table2;
    }

    /**
     * 制单人 拣货人 复核人
     *
     * @param vo
     * @return
     */
    private PdfPTable generateTable3(FormVo vo) {
        //创建一个pdf的表格
        PdfPTable table3 = new PdfPTable(4);
        //设置该表格的基本属性
        table3.setWidthPercentage(100); // 宽度100%填充
        table3.setSpacingBefore(20f); // 前间距
        table3.setSpacingAfter(1f); // 后间距
        //创建表格的的行对象集合，并指向表格行对象
        ArrayList<PdfPRow> listRow3 = table3.getRows();
        //将表格设置为八列，并指定列宽
        float[] columnWidths3 = {
                4f, 4f, 4f, 2f
        };
        table3.setWidths(columnWidths3);

        //创建7个单元格，并指定给第一行
        PdfPCell[] cells03 = new PdfPCell[4];
        PdfPRow row03 = new PdfPRow(cells03);

        cells03[0] = new PdfPCell(new Paragraph("制单人：" + vo.getPreparedBy(), fontFine));//单元格内容
        cells03[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells03[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells03[0].setFixedHeight(14f);//固定高度
        // 设置单元格的边框宽度为0，即无边框
        cells03[0].setBorderWidth(0);

        cells03[1] = new PdfPCell(new Paragraph("拣货人：", fontFine));//单元格内容
        cells03[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells03[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells03[1].setBorderWidth(0);

        cells03[2] = new PdfPCell(new Paragraph("复核人：", fontFine));//单元格内容
        cells03[2].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells03[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells03[2].setBorderWidth(0);

        cells03[3] = new PdfPCell(new Paragraph("打印次数：", fontFine));//单元格内容
        cells03[3].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells03[3].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells03[3].setBorderWidth(0);

        listRow3.add(row03);


        //创建7个单元格，并指定给第一行
        PdfPCell[] cells04 = new PdfPCell[4];
        PdfPRow row04 = new PdfPRow(cells04);
        String remark = vo.getRemark() == null ? "" : vo.getRemark();
        cells04[0] = new PdfPCell(new Paragraph("备注：" + remark, fontFine));//单元格内容
        cells04[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells04[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells04[0].setFixedHeight(14f);//固定高度
        cells04[0].setColspan(4);
        cells04[0].setBorderWidth(0);

        cells04[1] = new PdfPCell(new Paragraph("", fontFine));//单元格内容
        cells04[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells04[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells04[1].setBorderWidth(0);

        cells04[2] = new PdfPCell(new Paragraph("", fontFine));//单元格内容
        cells04[2].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells04[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells04[2].setBorderWidth(0);

        cells04[3] = new PdfPCell(new Paragraph("", fontFine));//单元格内容
        cells04[3].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells04[3].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells04[3].setBorderWidth(0);

        listRow3.add(row04);

        table3.setSpacingAfter(-10f);

        return table3;
    }

    private PdfPTable generateTable4(FormVo vo) {
        //创建一个pdf的表格
        PdfPTable table4 = new PdfPTable(3);
        //设置该表格的基本属性
        table4.setWidthPercentage(100); // 宽度100%填充
        table4.setSpacingBefore(20f); // 前间距
        table4.setSpacingAfter(1f); // 后间距
        //创建表格的的行对象集合，并指向表格行对象
        ArrayList<PdfPRow> listRow4 = table4.getRows();
        //将表格设置为八列，并指定列宽
        float[] columnWidths4 = {
                4f, 4f, 4f
        };
        table4.setWidths(columnWidths4);

        //创建7个单元格，并指定给第一行
        PdfPCell[] cells11 = new PdfPCell[3];
        PdfPRow row11 = new PdfPRow(cells11);

        cells11[0] = new PdfPCell(new Paragraph("地址：" + vo.getLocation(), fontFine));//单元格内容
        cells11[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells11[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells11[0].setFixedHeight(14f);//固定高度
        // 设置单元格的边框宽度为0，即无边框
        cells11[0].setBorderWidth(0);

        cells11[1] = new PdfPCell(new Paragraph("客服热线：" + vo.getCustomerServiceHotline(), fontFine));//单元格内容
        cells11[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells11[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells11[1].setBorderWidth(0);

        cells11[2] = new PdfPCell(new Paragraph("投诉电话：" + vo.getComplaintsHotline(), fontFine));//单元格内容
        cells11[2].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells11[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        // 设置单元格的边框宽度为0，即无边框
        cells11[2].setBorderWidth(0);

        listRow4.add(row11);


        //创建7个单元格，并指定给第一行
        PdfPCell[] cells12 = new PdfPCell[3];
        PdfPRow row12 = new PdfPRow(cells12);

        cells12[0] = new PdfPCell(new Paragraph("扫码：", fontFine));//单元格内容
        cells12[0].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells12[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells12[0].setFixedHeight(10f);//固定高度
        cells12[0].setBorderWidth(0);

        cells12[1] = new PdfPCell(new Paragraph("其他：", fontFine));//单元格内容
        cells12[1].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells12[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells12[1].setBorderWidth(0);

        cells12[2] = new PdfPCell(new Paragraph("收货人(签字)：", fontFine));//单元格内容
        cells12[2].setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cells12[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells12[2].setBorderWidth(0);

        listRow4.add(row12);

        table4.setSpacingBefore(0.5f);

        //把表格添加到文档中
        return table4;
    }
}
