package com.example.digitalPocket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.digitalPocket.util.IconManager;
import com.example.digitalPocket.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserBalanceAdapter extends ArrayAdapter<Map.Entry<String, BigDecimal>> {
    private final int resource;
    private final List<Map.Entry<String, BigDecimal>> userBalanceList;
    private final boolean displayZero;

    public UserBalanceAdapter(@NonNull Context context, int resource, @NonNull Map<String, BigDecimal> balanceMap, boolean displayZero) {
        super(context, resource, new ArrayList<>(balanceMap.entrySet()));
        this.resource = resource;
        this.userBalanceList = new ArrayList<>(balanceMap.entrySet());
        this.displayZero = displayZero;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Map.Entry<String, BigDecimal> userBalanceEntry = userBalanceList.get(position);
        String cryptoName = userBalanceEntry.getKey();
        BigDecimal balance = userBalanceEntry.getValue();

        if (balance.compareTo(BigDecimal.ZERO) == 0 && !displayZero) {
            return new View(getContext());
        }

        View view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        ImageView iconView = view.findViewById(R.id.cryptoIconView);
        TextView userBalanceView = view.findViewById(R.id.userBalance);
        TextView coinNameView = view.findViewById(R.id.coinNameView);

        String strBalance = balance.stripTrailingZeros().toPlainString();
        userBalanceView.setText(strBalance);
        coinNameView.setText(cryptoName);

        IconManager iconManager = new IconManager();
        iconView.setImageResource(iconManager.getIconResId(cryptoName));

        return view;
    }
}
