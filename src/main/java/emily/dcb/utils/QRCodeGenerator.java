package emily.dcb.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    public static BufferedImage getNPCQRCode(String str) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> encodeHintTypeHashMap = new HashMap<>();
        encodeHintTypeHashMap.put(EncodeHintType.QR_VERSION, 10);
        encodeHintTypeHashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix bitMatrix = qrCodeWriter.encode(str, BarcodeFormat.QR_CODE, 600, 600, encodeHintTypeHashMap);
        File file = new File("Demo.png");
        if(!file.exists()){
            file.createNewFile();
        }
        Image npc_image = ImageIO.read(new URL("https://tinyurl.com/r5bh38v5"));
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(npc_image, 200, 200, 200, 200, null);
        return bufferedImage;
    }
}
