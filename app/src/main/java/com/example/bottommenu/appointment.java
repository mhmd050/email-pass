package com.example.bottommenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class appointment extends Fragment {

    private RadioGroup radioGroup;
    private RadioButton selectedButton = null;

    public appointment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // استبدل R.layout.fragment_appointment باسم ملف XML الخاص بك
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // ربط RadioGroup باستخدام المعرف الموجود في XML
        radioGroup = view.findViewById(R.id.radioGroup);

        // المرور على جميع الأطفال داخل RadioGroup وتعيين OnClickListener للـ RadioButton فقط
        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = radioGroup.getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeButtonColor((RadioButton) v);
                    }
                });
            }
        }

        return view;
    }

    // دالة لتغيير لون الزر عند الضغط
    private void changeButtonColor(RadioButton clickedButton) {
        // اللون الافتراضي: #A1E3F9
        int defaultColor = android.graphics.Color.parseColor("#A1E3F9");
        // اللون المحدد: holo_green_light (يمكنك تغييره حسب رغبتك)
        int selectedColor = ContextCompat.getColor(getContext(), android.R.color.holo_green_light);

        // إعادة لون الزر السابق إلى اللون الافتراضي إذا كان موجودًا
        if (selectedButton != null) {
            selectedButton.setBackgroundColor(defaultColor);
        }
        // تغيير لون الزر الذي تم الضغط عليه إلى اللون المحدد (الأخضر)
        clickedButton.setBackgroundColor(selectedColor);
        // تحديث المتغير لتخزين الزر المحدد الحالي
        selectedButton = clickedButton;
    }
}
