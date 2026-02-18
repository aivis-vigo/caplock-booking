package com.caplock.booking.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class QrService {

    private static final String DEFAULT_IMAGE_FORMAT = "png";

    @Value("${qr.storage.path}")
    private String storagePath;

    public byte[] generateQRCode(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        var qrcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrcodeImage, DEFAULT_IMAGE_FORMAT, pngOutputStream);

        log.info("QR code generated for text: {}", text);
        return pngOutputStream.toByteArray();
    }

    public String generateAndSave(String text, String filename) throws WriterException, IOException {
        log.info("Generating and saving QR code for filename: {}", filename);
        byte[] qrBytes = generateQRCode(text);

        Path directory = Paths.get(storagePath);
        Files.createDirectories(directory);

        Path filePath = directory.resolve(filename + ".png");
        Files.write(filePath, qrBytes);

        log.info("QR code saved to: {}", filePath);
        return filePath.toString();
    }

    public String decodeQRCode(MultipartFile file) throws IOException, NotFoundException {
        log.info("Decoding QR code from file: {}", file.getOriginalFilename());
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        log.info("QR code decoded successfully");
        return result.getText();
    }

}
