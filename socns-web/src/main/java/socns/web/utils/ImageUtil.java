package socns.web.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;
import org.im4java.core.IM4JavaException;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

/**
 * 图片处理工具类
 *
 */
public class ImageUtil {

    private static Logger log = Logger.getLogger(ImageUtil.class);

    /**
     * 根据传入的图片坐标进行图片截取
     * 
     * @param x1 X起点坐标
     * @param y1 Y起点坐标
     * @param x2 X终点坐标 
     * @param y2 Y终点坐标
     * @param originPath 原始图片的存放路径
     * @param savePath 截取后图片的存储路径
     * @throws IOException
     */
    public static void truncate(String originPath, String savePath, int x1, int y1, int width,
                                int height) throws IOException {

        FileInputStream is = null;
        ImageInputStream iis = null;

        try {

            // 读取图片文件
            is = new FileInputStream(originPath);

            /*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，
             * 这些 ImageReader 声称能够解码指定格式。
             * 参数：formatName - 包含非正式格式名称 .（例如 "jpeg" 或 "tiff"）等 。
             */
            Iterator<ImageReader> it = ImageIO
                .getImageReadersByFormatName(getExtention(originPath).toLowerCase());
            ImageReader reader = it.next();
            // 获取图片流
            iis = ImageIO.createImageInputStream(is);

            /*
             * iis:读取源.true:只向前搜索，将它标记为 ‘只向前搜索’。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 
             * reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);

            /*
             * 描述如何对流进行解码的类，用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 
             * 将从其 ImageReader 实现的
             * getDefaultReadParam方法中返回 ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

            /*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = new Rectangle(x1, y1, width, height);

            // 提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);

            /*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
             * BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);

            // 保存新图片
            ImageIO.write(bi, getExtention(originPath).toLowerCase(), new File(savePath));
        } finally {
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
    }

    /**
     * 
     * 缩放图片
     * 
     * @param width 宽
     * @param height 高
     * @param originPath 原始路径
     * @param savePath 保存路径
     * @throws IOException
     */
    public static void scale(String originPath, String savePath, int width,
                             int height) throws IOException {

        BufferedImage sourceImage = readImage(originPath);
        ResampleOp resampleOp = new ResampleOp(width, height);
        BufferedImage rescaledTomato = resampleOp.filter(sourceImage, null);
        ImageIO.write(rescaledTomato, getExtention(originPath).toLowerCase(), new File(savePath));
    }

    private static BufferedImage readImage(String imagePath) throws IOException {
        return readImage(new File(imagePath));
    }

    private static BufferedImage readImage(File image) throws IOException {
        return ImageIO.read(image);
    }

    public static void validate(File ori, String dest) throws IOException {
        File destFile = new File(dest);

        if (ori == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (dest == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!ori.exists()) {
            throw new FileNotFoundException("Source '" + ori + "' does not exist");
        }
        if (ori.isDirectory()) {
            throw new IOException("Source '" + ori + "' exists but is a directory");
        }
        if (ori.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException(
                "Source '" + ori + "' and destination '" + dest + "' are the same");
        }
        if ((destFile.getParentFile() != null) && (!destFile.getParentFile().exists())
            && (!destFile.getParentFile().mkdirs())) {
            throw new IOException("Destination '" + dest + "' directory cannot be created");
        }

        if ((destFile.exists()) && (!destFile.canWrite())) {
            throw new IOException("Destination '" + dest + "' exists but is read-only");
        }
    }

    public static boolean scaleImageByWidth(String ori, String dest,
                                            int maxWidth) throws IOException, InterruptedException,
                                                          IM4JavaException {
        File oriFile = new File(ori);
        validate(oriFile, dest);

        BufferedImage src = ImageIO.read(oriFile);
        int w = src.getWidth();
        int h = src.getHeight();

        log.debug("origin with/height " + w + "/" + h);

        int tow = w;
        int toh = h;

        if (w > maxWidth) {
            tow = maxWidth;
            toh = h * maxWidth / w;
        }

        log.debug("scaled with/height : " + tow + "/" + toh);

        scale(ori, dest, tow, toh);

        return true;
    }

    /**
     * 功能：提取文件名的后缀
     * 
     * @param fileName
     * @return
     */
    private static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }

    /**
     * 图片水印
     * 
     * @param pressImg
     *            水印图片
     * @param targetImg
     *            目标图片
     * @param x
     *            修正值 默认在中间
     * @param y
     *            修正值 默认在中间
     * @param alpha
     *            透明度
     */
    public final static void pressImage(String pressImg, String targetImg, int x, int y,
                                        float alpha) {
        try {
            File img = new File(targetImg);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2,
                wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write((BufferedImage) image, "jpg", img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文字水印
     * 
     * @param pressText
     *            水印文字
     * @param targetImg
     *            目标图片
     * @param fontName
     *            字体名称
     * @param fontStyle
     *            字体样式
     * @param color
     *            字体颜色
     * @param fontSize
     *            字体大小
     * @param x
     *            修正值
     * @param y
     *            修正值
     * @param alpha
     *            透明度
     */
    public static void pressText(String pressText, String targetImg, String fontName, int fontStyle,
                                 Color color, int fontSize, int x, int y, float alpha) {
        try {
            File img = new File(targetImg);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x,
                (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "jpg", img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

    // -----------------压缩图片--------------------

    /**
     * 压缩图片（默认为jpg格式）
     * 
     * @param fromPath
     *            源路径
     * @param toPath
     *            输出路径
     * @param width
     *            文件转换后的宽度
     * @param height
     *            文件转换后的高度
     * @return
     */
    public static boolean resize(String fromPath, String toPath, Integer width, Integer height) {
        return resize(fromPath, toPath, width, height, false, false, "jpg");
    }

    /**
     * 压缩图片（默认为jpg格式）
     * 
     * @param fromPath
     *            源路径
     * @param toPath
     *            输出路径
     * @param width
     *            文件转换后的宽度
     * @param height
     *            文件转换后的高度
     * @return
     */
    public static boolean resize(String fromPath, String toPath, Integer width, Integer height,
                                 boolean isOtherThan) {
        return resize(fromPath, toPath, width, height, isOtherThan, true, "jpg");
    }

    /**
     * 压缩图片
     * 
     * @param fromPath
     *            源路径
     * @param toPath
     *            输出路径
     * @param width
     *            文件转换后的宽度
     * @param height
     *            文件转换后的高度
     * @param isOtherThan
     *            是否等比缩小 true:等比 false:不等比
     * @param isFiller
     *            是否补白，前提条件是图片是等比缩小
     * @param format
     *            文件压缩后的格式(jpg格式文件小，gif质量差，文件稍微大一点，bmp文件大，清晰度还可以)
     * @return
     */
    public static boolean resize(String fromPath, String toPath, Integer width, Integer height,
                                 boolean isOtherThan, boolean isFiller, String format) {
        try {
            // 将图片文件读入到缓存中
            BufferedImage inImage = ImageIO.read(new FileInputStream(fromPath));

            // System.out.println("转前图片高度和宽度：" + inImage.getHeight() + ":"+
            // inImage.getWidth());
            int scaledW = width;
            int scaledH = height;
            if (isOtherThan) {
                int imh = inImage.getWidth();
                int imw = inImage.getHeight();

                double scale;
                if (imh <= height && imw <= width)
                    scale = 1;
                else if (imh > imw)
                    scale = (double) height / (double) imh;
                else
                    scale = (double) width / (double) imw;

                scaledW = (int) (scale * imw);
                scaledH = (int) (scale * imh);
            }

            // 将图片分辨率转成指定分辨率（高+宽）
            AdvancedResizeOp resampleOp = new ResampleOp(scaledW, scaledH);
            // 重新生成图片
            BufferedImage rescaledTomato = resampleOp.filter(inImage, null);

            // 是否补白(必须是等比缩放才会补白)
            if (isFiller && isOtherThan) {
                //
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == rescaledTomato.getWidth(null))
                    g.drawImage(rescaledTomato, 0, (height - rescaledTomato.getHeight(null)) / 2,
                        rescaledTomato.getWidth(null), rescaledTomato.getHeight(null), Color.white,
                        null);
                else
                    g.drawImage(rescaledTomato, (width - rescaledTomato.getWidth(null)) / 2, 0,
                        rescaledTomato.getWidth(null), rescaledTomato.getHeight(null), Color.white,
                        null);
                g.dispose();
                rescaledTomato = image;
            }

            // 将文件写入输入文件
            ImageIO.write(rescaledTomato, format, new File(toPath));

            // System.out.println("转后图片高度和宽度：" + rescaledTomato.getHeight() +
            // ":"+ rescaledTomato.getWidth());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void main(String[] args) {
        resize("F://s31.jpg", "F://s121.jpg", 120, 120);
    }

}