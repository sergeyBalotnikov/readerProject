/*
 * Copyright (C) 2004-2019 FBReader.ORG Limited <contact@fbreader.org>
 */

package ru.mail.sergey_balotnikov.literaturetranslator.fbreaderSamplesClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.fbreader.book.Book;
import org.fbreader.extras.info.InfoUtil;
import org.fbreader.extras.selection.SelectionCursorUtil;
import org.fbreader.extras.selection.SelectionPanelUtil;
import org.fbreader.text.extras.navigation.NavigationUtil;
import org.fbreader.text.extras.opener.ExternalHyperlinkOpener;
import org.fbreader.text.extras.opener.InternalHyperlinkOpener;
import org.fbreader.text.extras.search.TextSearchUtil;
import org.fbreader.text.view.SelectionData;
import org.fbreader.text.widget.TextWidget;
import org.fbreader.util.ViewUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import ru.mail.sergey_balotnikov.literaturetranslator.R;
import ru.mail.sergey_balotnikov.literaturetranslator.utils.Interpreter;

public class TextWidgetExt extends TextWidget {
	{
		this.registerOpener(new InternalHyperlinkOpener(this));
		this.registerOpener(new ExternalHyperlinkOpener(this));
		this.registerOpener(new BookmarkOpener(this));
	}

	public TextWidgetExt(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TextWidgetExt(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextWidgetExt(Context context) {
		super(context);
	}

	@Override
	protected GestureListenerExt createGestureListener() {
		return new GestureListenerExt(this);
	}

	@Override
	protected TextBookControllerImpl createController(Book book) {
		return new TextBookControllerImpl(this, book);
	}

	@Override
	public void drawSelectionCursor(Canvas canvas, Point pt, boolean startNotEnd) {
		SelectionCursorUtil.drawCursor(this, canvas, pt, startNotEnd);
	}
	@Override
	protected void showSelectionPanel() {
		final SelectionData data = this.selectionData();
		if (data != null) {
			CompletableFuture.supplyAsync(() ->
					Interpreter.translatedText(data.text()))
					.thenAcceptAsync(s ->{
								Log.d("SVB", s);
								showTranslatePanel(this, data.rects, s, new SelectionPanelListener(this));

							}, ContextCompat.getMainExecutor(this.getContext()));
			/*SelectionPanelUtil.showPanel(
				this, data.rects, new SelectionPanelListener(this)
			);*/
		}
	}
	@Override
	protected void hideSelectionPanel() {
		SelectionPanelUtil.hidePanel(this);
	}

	@Override
	protected void showInfo(String text) {
		InfoUtil.showInfo(this, text, getResources().getColor((Integer)R.id.menu_color_profile));
	}

	@Override
	public void searchInText(String pattern) {
		FullScreenUtil.hideSystemUI(ViewUtil.findContainingActivity(this));
		this.post(() -> NavigationUtil.stopNavigation(this));
		TextSearchUtil.performSearch(this, pattern);
	}
	@Override
	protected void hideTextSearchPanel() {
		TextSearchUtil.hidePanel(this);
	}
	@Override
	public void showPopup(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onContentUpdated() {
		TextSearchUtil.updatePanel(this);
		NavigationUtil.updatePanel(this);
	}
	public void showTranslatePanel(@NonNull View widget, List<Rect> selectionRects, String s,  View.OnClickListener listener){
		final View panel = findOrCreatePanel(widget);
		if (panel == null) {
			return;
		}
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
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
		textView.setText(s);
		ImageButton addBtn = panel.findViewById(R.id.btnAdd);
		/*addBtn.setOnClickListener(view -> {
			//add translate to dictionary

		});*/
		panel.setVisibility(View.VISIBLE);
		panel.requestFocus();
    }

    private View findOrCreatePanel(View widget) {
		final ViewParent parent = widget.getParent();
		if (!(parent instanceof RelativeLayout)) {
			return null;
		}

		final RelativeLayout root = (RelativeLayout)parent;
		return ViewUtil.findView(root, R.id.selection_panel, new ViewUtil.Supplier<View>() {
			@Override
			public View get() {
				root.inflate(root.getContext(), R.layout.layout_selection_translate_pannel, root);
				return ViewUtil.findView(root, R.id.selection_panel);
			}
		});
    }
}
