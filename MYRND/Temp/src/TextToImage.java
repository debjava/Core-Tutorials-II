import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
 
/**
*
* @author Arthur
*/
public class TextToImage
{
 
   public static void main(String[] args) throws IOException
   {
       String s = "Debadatta Mishra\n Senior Technical Consultant";
       String fileName = "foobar";
       File file = new File("./" + fileName + ".jpeg");
       Font font = new Font("Tahoma", Font.PLAIN, 40);
       FontRenderContext frc = new FontRenderContext(null, true, true);
       Rectangle2D bounds = font.getStringBounds(s, frc);
       int w = (int) bounds.getWidth();
       int h = (int) bounds.getHeight();
       BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
       Graphics2D g = image.createGraphics();
       g.setColor(Color.WHITE);
       g.fillRect(0, 0, w, h);
       g.setColor(Color.BLACK);
       g.setFont(font);
       g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
               RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
       g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
               RenderingHints.VALUE_FRACTIONALMETRICS_ON);
       g.drawString(s, (float) bounds.getX(), (float) -bounds.getY());
       g.dispose();
       ImageIO.write(image, "jpeg", file);
   }
} 