package bavision.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageUtil {
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private File getPathFileFromInputStream(InputStream inputStream) throws IOException {
        File file = new File("D:\\ba\\picutre_name" + atomicInteger + ".jpg");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
        return file;
    }

}
