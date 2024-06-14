package com.example.digitalPocket.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NimiqHashAvatarGenerator {

    public Bitmap generateAvatar(String input, int size) {
        byte[] hash = generateHash(input);
        return createBitmap(hash, size);
    }

    private byte[] generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private Bitmap createBitmap(byte[] hash, int pixelSize) {
        int size = 5; // 5x5 grid for symmetry
        int width = size * pixelSize;
        int height = size * pixelSize;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        // Generate colors from hash
        int[] colors = new int[3];
        for (int i = 0; i < 3; i++) {
            colors[i] = Color.rgb(hash[i] & 0xFF, hash[i + 1] & 0xFF, hash[i + 2] & 0xFF);
        }

        // Draw symmetrical pattern
        for (int y = 0; y < size; y++) {
            for (int x = 0; x <= size / 2; x++) {
                int index = (y * size + x) % colors.length;
                paint.setColor(colors[index]);
                canvas.drawRect(x * pixelSize, y * pixelSize, (x + 1) * pixelSize, (y + 1) * pixelSize, paint);
                canvas.drawRect((size - x - 1) * pixelSize, y * pixelSize, (size - x) * pixelSize, (y + 1) * pixelSize, paint);
            }
        }

        return bitmap;
    }
}
