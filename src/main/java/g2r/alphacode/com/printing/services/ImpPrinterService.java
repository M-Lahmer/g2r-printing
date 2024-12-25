package g2r.alphacode.com.printing.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.List;
import java.util.ArrayList;
import g2r.alphacode.com.printing.entities.PrinterConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.print.DocFlavor;
import javax.print.Doc;
import javax.print.SimpleDoc;
import javax.print.DocPrintJob;
import javax.print.attribute.HashPrintRequestAttributeSet;

@Service
public class ImpPrinterService {

    private final String configFilePath;
    private final ObjectMapper objectMapper;

    public ImpPrinterService() {

        this.configFilePath = System.getProperty("user.home").toString()
                + File.separator + "g2r_printer.config";
        this.objectMapper = new ObjectMapper();
    }

    public void configurePrinter(PrinterConfig printerConfig) {

        File config = new File(configFilePath);
        config.getParentFile().mkdirs();
        try {
            objectMapper.writeValue(config, printerConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public PrinterConfig getPrinterConfig() throws IOException {

        File config = new File(configFilePath);
        if (config.exists()) {

            return objectMapper.readValue(config, PrinterConfig.class);
        }
        return null;
    }

    public void printDocument(List<String> contents, String type) throws Exception {
        PrinterConfig printerConfig = getPrinterConfig();
        PrintService printService;
        DocFlavor flavor = DocFlavor.STRING.TEXT_HTML;
        if (type.equals("A4")) {
            printService = findPrinterByName(printerConfig.getDocument());

        } else {
            printService = findPrinterByName(printerConfig.getTicket());

        }
        if (printService == null) {
            throw new Exception("Printer not found: " + type);
        }
        for (String documentToPrint : contents) {
            DocPrintJob printJob = printService.createPrintJob();

            Doc doc = new SimpleDoc(documentToPrint, flavor, null);
            printJob.print(doc, new HashPrintRequestAttributeSet());
        }
    }

    /*
     * public void printPDF(String pdfFilePath) throws Exception {
     * FileInputStream fis = new FileInputStream(pdfFilePath);
     * Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.PDF, null);
     * PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
     * DocPrintJob printJob = printService.createPrintJob();
     * printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
     * fis.close();
     * }
     */
    public List<String> getAllPrinters() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        List<String> printerNames = new ArrayList<>();

        for (PrintService printer : printServices) {
            printerNames.add(printer.getName());
        }

        return printerNames;
    }

    public PrintService findPrinterByName(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().equalsIgnoreCase(printerName)) {
                return printService;
            }
        }
        return null; // Return null if the printer is not found
    }

}
