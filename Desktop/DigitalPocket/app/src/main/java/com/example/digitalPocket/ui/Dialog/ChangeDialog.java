package com.example.digitalPocket.ui.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.digitalPocket.dao.UserDao;

public class ChangeDialog {
    public static void showChangeDialog(Context context, String email, String flg, OnUsernameChangeListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (flg.equals("passwd")) {
            dialog.setTitle("修改密码");
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10);

            final EditText newPasswordInput = new EditText(context);
            newPasswordInput.setHint("新密码");
            newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(newPasswordInput);

            final EditText confirmPasswordInput = new EditText(context);
            confirmPasswordInput.setHint("确认密码");
            confirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(confirmPasswordInput);

            dialog.setView(layout);

            dialog.setPositiveButton("确认", (dialogInterface, i) -> {
                String newPassword = newPasswordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                if (newPassword.equals(confirmPassword)) {
                    changePassword(context, email, newPassword, newPassword);
                } else {
                    Toast.makeText(context, "密码不匹配", Toast.LENGTH_SHORT).show();
                }
                dialogInterface.dismiss();
            });
            dialog.setNegativeButton("返回", (dialogInterface, i) -> dialogInterface.dismiss());
        } else {
            dialog.setTitle("修改用户名");
            LinearLayout layout = new LinearLayout(context);
            layout.setPadding(50, 40, 50, 10);
            final EditText newUsernameView = new EditText(context);
            newUsernameView.setHint("新用户名");
            newUsernameView.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(newUsernameView);
            dialog.setView(layout);
            dialog.setPositiveButton("确认", (dialogInterface, i) -> {
                String newUsername = newUsernameView.getText().toString();
                changeUsername(context, email, newUsername, listener);
                dialogInterface.dismiss();
            });
        }
        dialog.setNegativeButton("返回", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.show();
    }

    private static void changePassword(Context context, String e_mail, String currentPassword, String newPassword) {
        UserDao.getInstance().updateUser(e_mail, null, newPassword, new UserDao.UserCallback<String>() {
            @Override
            public void onSuccess(String data) {
                ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "密码修改成功", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(String message) {
                ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "密码修改失败: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private static void changeUsername(Context context, String e_mail, String username, OnUsernameChangeListener listener) {
        UserDao.getInstance().updateUser(e_mail, username, null, new UserDao.UserCallback<String>() {
            @Override
            public void onSuccess(String data) {
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "用户名修改成功", Toast.LENGTH_SHORT).show();
                    listener.onUsernameChanged(username);
                });
            }

            @Override
            public void onError(String message) {
                ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "用户名修改失败: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }
    public interface OnUsernameChangeListener {
        void onUsernameChanged(String newUsername);
    }
}
