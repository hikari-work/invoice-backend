package org.yann.integerasiorderkuota.orderkuota.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class QrGenerator {

    private String generateQrString(String baseQrString, Long amount) {
        String qrCode = baseQrString.trim();
        String amountString = String.valueOf(amount);
        qrCode = qrCode.substring(0, qrCode.length() -4);
        String step1 = qrCode.replace("010211", "010212");
        String[] step2 = step1.split("5802ID");
        String amountPart = "54" + String.format("%02d", amountString.length()) + amountString;
        amountPart += "5802ID";

        String fix = step2[0].trim() + amountPart + step2[1];
        fix += crcCalculator(fix);
        return fix;
    }

    private byte[] generateImage(String qr, int width,int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(qr, BarcodeFormat.QR_CODE, width, height);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            return baos.toByteArray();
        }
    }
    private String crcCalculator(String qrString) {
        int crc = 0xFFFF;
        int stringLength = qrString.length();

        for (int c = 0; c < stringLength; c++) {
            crc ^= qrString.charAt(c) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc = crc << 1;
                }
            }
        }
        int hex = crc & 0xFFFF;
        String hexString = Integer.toHexString(hex).toUpperCase();
        if (hexString.length() == 3) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    public String generateQr(String baseQr, Long amount) {
        return generateQrString(baseQr, amount);
    }
    public byte[] generateQrImage(String baseAmount, Long amount, int width, int height) throws WriterException, IOException {
        String qrString = generateQrString(baseAmount, amount);
        return generateImage(qrString, width, height);
    }
}
