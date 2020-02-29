package ru.mail.sergey_balotnikov.literaturetranslator.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import org.fbreader.util.ViewUtil;
import java.util.List;
import ru.mail.sergey_balotnikov.literaturetranslator.R;
import ru.mail.sergey_balotnikov.literaturetranslator.fbreaderSamplesClass.TextWidgetExt;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordEntity;

public class TranslatePanel {
    private TranslatePanel() {
    }
    public static void showTranslatePanel(@NonNull View widget, List<Rect> selectionRects,
              String translate, String original, TextWidgetExt.OnAddWordClickListener listener){
        final View panel = findOrCreatePanel(widget);
        if (panel == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        final int verticalPosition;
        if (selectionRects.isEmpty()) {
            verticalPosition = RelativeLayout.CENTER_VERTICAL;
        } else {
            int top = Integer.MAX_VALUE;
            int bottom = Integer.MIN_VALUE;
            for (Rect r : selectionRects) {
                top = Math.min(top, r.top);
                bottom = Math.max(bottom, r.bottom);
            }
            final int spaceTop = top;
            final int spaceBottom = widget.getHeight() - bottom;
            if (spaceTop > spaceBottom) {
                verticalPosition = spaceTop > panel.getHeight() + 20
                        ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.CENTER_VERTICAL;
            } else {
                verticalPosition = spaceBottom > panel.getHeight() + 20
                        ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.CENTER_VERTICAL;
            }
        }
        layoutParams.addRule(verticalPosition);
        panel.setLayoutParams(layoutParams);

        TextView textView = panel.findViewById(R.id.tvTranslate);
        textView.setText(translate);
        ImageButton addBtn = panel.findViewById(R.id.btnAdd);
		addBtn.setOnClickListener(view -> {
            WordEntity wordEntity = new WordEntity();
            wordEntity.setTranslate(translate);
            wordEntity.setOriginal(original);
            listener.onAddWordClick(wordEntity);

		});
        panel.setVisibility(View.VISIBLE);
        panel.requestFocus();
    }

    private static View findOrCreatePanel(View widget) {
        final ViewParent parent = widget.getParent();
        if (!(parent instanceof RelativeLayout)) {
            return null;
        }

        final RelativeLayout root = (RelativeLayout)parent;
        return ViewUtil.findView(root, R.id.selection_panel, () -> {
            root.inflate(root.getContext(), R.layout.layout_selection_translate_pannel, root);
            return ViewUtil.findView(root, R.id.selection_panel);
        });
    }
}
