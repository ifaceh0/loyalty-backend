package com.sts.QrcodeGenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QrCodeGenerator {
//	public static byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
//		QRCodeWriter qrCodeWriter = new QRCodeWriter();
//		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//
//		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//		return pngOutputStream.toByteArray();
//	}
	public static String generateQRCodeBase64(String content, int width, int height) throws Exception {
		QRCodeWriter writer = new QRCodeWriter();
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.MARGIN, 1);

		BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
}
