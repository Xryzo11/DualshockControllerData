import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class IconGenerator {
    public static BufferedImage genIcon(int num) {
        JFrame frame = new JFrame("Icon Generator");

        int iconSize = 32;
        BufferedImage icon = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, iconSize, iconSize);
        g2d.setColor(new Color(0, 150, 0, 100));
        if (num > 99 || num < -99) {
            g2d.setFont(new Font("Arial", Font.BOLD, iconSize - 12));
        } else {
            g2d.setFont(new Font("Arial", Font.BOLD, iconSize));
        }
        FontMetrics fm = g2d.getFontMetrics();
        String number = String.valueOf(num);
        int x = (iconSize - fm.stringWidth(number)) / 2;
        int y = (iconSize - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(number, x, y);
        g2d.dispose();
        return icon;
    }
}
