import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonManager {
    
    public void save(CaptureMetadata metadata, File jsonFile) {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, metadata);
            System.out.println("JSON 저장 완료");
        } catch (IOException e) {
            System.out.println("JSON 저장 실패");
            e.printStackTrace();
        }
    }
}
