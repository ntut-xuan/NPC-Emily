package emily.dcb.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PhotoSynthesis {
    public static BufferedImage photoSynthesis(String discordTag, String School, String studentClass, String studentName) throws IOException {
        File file = new File("base.png");
        if(!file.exists()){
            return null;
        }
        BufferedImage image = ImageIO.read(file);
        System.out.println(image.getHeight());
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        int length = (int) Math.min(-2.7*(discordTag.length()-5)-(-2.7*12)+96, 96);
        graphics.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, length));
        graphics.drawString(discordTag, 89, 437);
        graphics.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 60));
        graphics.drawString(School, 89, 666);
        graphics.drawString(studentClass, 89, 751);
        graphics.drawString(studentName, 89, 876);
        return image;
    }
}
