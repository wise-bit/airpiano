/**
 * Basic template for every camera aspect of each page, stored for convenience
 */

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Camera_base extends JFrame implements ActionListener {

    Webcam webcam;
    BufferedImage image;
    JLabel imageLbl = new JLabel();
    Border b = BorderFactory.createLineBorder(Color.BLACK, 5, true);
    Border b2 = BorderFactory.createLineBorder(Color.BLACK, 1, true);

    public static final int imageWidth = 720;
    public static final int imageHeight = 480;


    int delay = 30;
    private Timer timer = new Timer(delay, this);

    ImageIcon pianoScan;
    JLabel pianoScanLabel;

    public Camera_base() {

        setSize(800, 500);

        setTitle("Scan your face");
        setLayout(null);

        webcam = Webcam.getDefault();
        webcam.open();

        if (webcam.isOpen()) { //if web cam open
            image = webcam.getImage();
            imageLbl = new JLabel();
            Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            imageLbl.setIcon(new ImageIcon(dimg));
            imageLbl.setBorder(b);
            imageLbl.setBounds(20, 20, imageWidth, imageHeight/2);
            add(imageLbl);

        } else {
            JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam...",
                    "Message", JOptionPane.PLAIN_MESSAGE);
        }

        pianoScanLabel = new JLabel();
        pianoScanLabel.setBorder(b2);
        pianoScanLabel.setBounds(20, 350, 100, 10);
        add(pianoScanLabel);

        timer.start();
        setVisible(true);

    }

    public static BufferedImage cropImage(BufferedImage originalImage, int x, int y, int w, int h) {
        BufferedImage subImgage = originalImage.getSubimage(x, y, w, h);
        return subImgage;
    }

    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        // Image tmp = img.getScaledInstance(width, height, Image.SCALE_FAST);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public static BufferedImage redEnhanced(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        // BufferedImage newImage = image;

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                // fetches rgb of a specific pixel
                int rgb = image.getRGB(x, y);

                // Alternate image generation based on vector values

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb & 0xFF);

                // System.out.println(red);

                int color;

                if (red > 100 && green < 75 && blue < 75) {

                    color = red << 16;

                } else {

                    // Gets the RGB code and gets each of the codes, averages them, therefore calculating the intensity of the pixel
                    int grayLevel = (red + green + blue) / 3;
                    color = (grayLevel << 16) + (grayLevel << 8) + grayLevel;

                }

                newImage.setRGB(x, y, color);
            }
        }
        return newImage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timer) {

            // Gets new image from webcam

            image = webcam.getImage();

            //Sets the image to invisible

            imageLbl.setVisible(false);

            // FUNCTION: FLIP
            for (int i = 0; i < image.getWidth() / 2; i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    int tmp = image.getRGB(i, j);

                    // FLIPS pixels over the y-axis, making the video output more comprehensible to the human brain
                    image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                    image.setRGB(image.getWidth() - i - 1, j, tmp);

                }
            }

            // Crops the image to the right size


            image = resize(image, 480, 720);
            image = cropImage(image, 0, 0, imageWidth, imageHeight/2);

            image = redEnhanced(image);

            // Creates new image icon for the image

            ImageIcon imageIcon = new ImageIcon(image);
            imageIcon.getImage().flush();
            imageLbl.setIcon(imageIcon);
            imageLbl.setVisible(true);


            ////

            image = cropImage(image, 0, 0, imageWidth, imageHeight/2);
            BufferedImage processableImage = resize(image, 2, 100);

            pianoScan = new ImageIcon(processableImage);
            pianoScan.getImage().flush();
            pianoScanLabel.setIcon(pianoScan);
            pianoScanLabel.setVisible(true);



        }

    }
}


