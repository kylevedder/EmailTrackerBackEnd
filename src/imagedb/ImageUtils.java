/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagedb;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Kyle
 */
public class ImageUtils
{

    /**
     * Generates the image at the given file location and generates the folders
     * if need be.
     *
     * @param f File path to where the image is to be generated. Generates all needed folders containing the file.
     * @return success of generating image.
     */
    public static boolean genImage(File f)
    {
        if (!f.getParentFile().exists())
        {
            if(!f.getParentFile().mkdirs())return false;
        }
        try
        {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, new Color(255, 255, 255).getRGB());
            ImageIO.write(image, "png", f);
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
