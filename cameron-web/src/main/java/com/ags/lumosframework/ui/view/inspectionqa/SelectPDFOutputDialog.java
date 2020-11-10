//Changed by Cameron: 勾选并合并工单最终的pdf包

package com.ags.lumosframework.ui.view.inspectionqa;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.service.ICaConfigService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@Scope("prototype")
public class SelectPDFOutputDialog extends BaseDialog {
    private String caption;
    private ProductionOrder productionOrder;
    private List<String> items;
    private List<List<String>> files;
    private File folder;
    private boolean hasFile;
    private CheckBox[] cbs;

    @Autowired
    private ICaConfigService caConfigService;

    private VerticalLayout vlContent = new VerticalLayout();
//    private Button[] btns;

//    private TwinColSelect<String> tcPDF = new TwinColSelect<>();

    public SelectPDFOutputDialog() {
    }

    public void setObject(ProductionOrder productionOrder) {
        this.caption = "选择导出PDF";
        this.productionOrder = productionOrder;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        items = new ArrayList<>();
        files = new ArrayList<>();
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
        if (caConfig == null) {
            throw new PlatformException("请先配置文档报告存放路径");
        }

        //报告首页
        folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.FINAL_REPORT);
        hasFile = false;
        List<String> file1 = new ArrayList<>();
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    hasFile = true;
                    file1.add(f.getAbsolutePath());
                }
            }
        }
        if (hasFile) {
            items.add("首页");
            files.add(file1);
        }

        //COC
        folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.COC);
        hasFile = false;
        List<String> file2 = new ArrayList<>();
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    hasFile = true;
                    file2.add(f.getAbsolutePath());
                }
            }
        }
        if (hasFile) {
            items.add("COC");
            files.add(file2);
        }

        //LEVP
        folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.LEVP);
        hasFile = false;
        List<String> file3 = new ArrayList<>();
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    hasFile = true;
                    file3.add(f.getAbsolutePath());
                }
            }
        }
        if (hasFile) {
            items.add("LEVP");
            files.add(file3);
        }

        //装配记录表
        folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.ASSEMBLY_REPORT);
        hasFile = false;
        List<String> file4 = new ArrayList<>();
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    hasFile = true;
                    file4.add(f.getAbsolutePath());
                }
            }
        }
        if (hasFile) {
            items.add("装配记录表");
            files.add(file4);
        }

        //压力测试
        folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.PRESSURE_REPORT);
        hasFile = false;
        List<String> file5 = new ArrayList<>();
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    hasFile = true;
                    file5.add(f.getAbsolutePath());
                }
            }
        }
        if (hasFile) {
            items.add("压力测试报告");
            files.add(file5);
        }

        //喷漆
        folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.PAINT_REPORT);
        hasFile = false;
        List<String> file6 = new ArrayList<>();
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    hasFile = true;
                    file6.add(f.getAbsolutePath());
                }
            }
        }
        if (hasFile) {
            items.add("内部喷漆报告");
            files.add(file6);
        }

        //Routing
        folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.GX_RECORD);
        hasFile = false;
        List<String> file7 = new ArrayList<>();
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    hasFile = true;
                    file7.add(f.getAbsolutePath());
                }
            }
        }
        if (hasFile) {
            items.add("Routing");
            files.add(file7);
        }

        //Machining Dimension
        folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.DIMENSION_REPORT);
        hasFile = false;
        List<String> file8 = new ArrayList<>();
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    hasFile = true;
                    file8.add(f.getAbsolutePath());
                }
            }
        }
        if (hasFile) {
            items.add("机加工尺寸报告");
            files.add(file8);
        }

//        tcPDF.setItems(items);
//        Iterator iterator=items.iterator();
//        while (iterator.hasNext()){
//            tcPDF.select(iterator.next().toString());
//        }
        cbs = new CheckBox[items.size()];
        vlContent.removeAllComponents();
        for (int i = 0; i < items.size(); i++) {
            cbs[i] = new CheckBox();
            cbs[i].setCaption(items.get(i));
            vlContent.addComponent(cbs[i]);
        }
        showDialog(parentUI, caption, "200px", null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
    }

    @Override
    protected void okButtonClicked() throws Exception {
        boolean hasPDFFile = false;
        for (CheckBox c : cbs) {
            if (c.getValue()) {
                hasPDFFile = true;
            }
        }

        if (files == null || files.size() <= 0 || !hasPDFFile) {
            NotificationUtils.notificationInfo("没有PDF文件导出");
            return;
        }
//        PDFMergerUtility mergePDF = new PDFMergerUtility();
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
        if (caConfig == null) {
            throw new PlatformException("请先配置文档报告存放路径");
        }
        PdfWriter writer = new PdfWriter(caConfig.getConfigValue() + AppConstant.PDF + productionOrder.getProductOrderId() + ".pdf");

        // In smart mode when resources (such as fonts, images,...) are encountered,
        // a reference to these resources is saved in a cache and can be reused.
        // This mode reduces the file size of the resulting PDF document.
        writer.setSmartMode(true);
        PdfDocument pdfDoc = new PdfDocument(writer);

        // This method initializes an outline tree of the document and sets outline mode to true.
        pdfDoc.initializeOutlines();


        for (int i = 0; i < files.size(); i++) {
            if (cbs[i].getValue()) {//如果被选中，则合并至pdf
                for (String f : files.get(i)) {
                    PdfDocument addedDoc = new PdfDocument(new PdfReader(f));
                    addedDoc.copyPagesTo(1, addedDoc.getNumberOfPages(), pdfDoc);
                    addedDoc.close();
                }
            }
        }
        pdfDoc.close();
        NotificationUtils.notificationInfo("成功合并PDF");

//        mergePDF.setDestinationFileName(caConfig.getConfigValue() + AppConstant.PDF + productionOrder.getProductOrderId() + ".pdf");
//        try {
//            mergePDF.mergeDocuments();
//            NotificationUtils.notificationInfo("成功合并PDF");
//        } catch (COSVisitorException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        vlContent.setSizeFull();
//        vlContent.addComponent(tcPDF);
//        tcPDF.setLeftColumnCaption("可选报告");
//        tcPDF.setRightColumnCaption("已选报告");
        return vlContent;
    }
}
