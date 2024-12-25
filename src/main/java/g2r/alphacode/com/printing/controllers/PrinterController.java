package g2r.alphacode.com.printing.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import g2r.alphacode.com.printing.entities.PrintRequest;
import g2r.alphacode.com.printing.entities.PrinterConfig;
import g2r.alphacode.com.printing.services.ImpPrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping("/printer")
public class PrinterController {
    @Autowired
    ImpPrinterService printerService;

    @PostMapping("/configure")
    public ResponseEntity<String> configurePrinter(@RequestBody PrinterConfig config) {
        try {
            printerService.configurePrinter(config);
            return ResponseEntity.ok("well configured");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("error in configuration");
        }
    }

    @GetMapping("/configured")
    public ResponseEntity<PrinterConfig> getPrinterConfig() {
        try {

            return ResponseEntity.ok(printerService.getPrinterConfig());
        } catch (Exception e) {

            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/print")
    public ResponseEntity<String> printDocument(@RequestBody PrintRequest printRequest) {
        try {
            printerService.printDocument(printRequest.getContents(), printRequest.getType());
            return ResponseEntity.ok("printing ok ");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/printers")
    public List<String> getConfiguredPrinters() {
        return printerService.getAllPrinters();

    }

}
