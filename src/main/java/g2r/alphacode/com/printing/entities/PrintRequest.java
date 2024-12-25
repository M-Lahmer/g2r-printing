package g2r.alphacode.com.printing.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrintRequest {
    private String type;
    private List<String> contents;
}
