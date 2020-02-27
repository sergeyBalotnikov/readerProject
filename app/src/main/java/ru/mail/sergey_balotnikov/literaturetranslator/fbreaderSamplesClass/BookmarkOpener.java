/*
 * Copyright (C) 2004-2019 FBReader.ORG Limited <contact@fbreader.org>
 */

package ru.mail.sergey_balotnikov.literaturetranslator.fbreaderSamplesClass;

import android.widget.Toast;

import org.fbreader.text.entity.Entity;
import org.fbreader.text.entity.BookmarkEntity;
import org.fbreader.text.widget.EntityOpener;
import org.fbreader.text.widget.TextWidget;

public class BookmarkOpener extends EntityOpener {
	BookmarkOpener(TextWidget widget) {
		super(widget, BookmarkEntity.class);
	}

	@Override
	protected void open(Entity entity, Entity.Location location) {
		Toast.makeText(this.widget.getContext(), ((BookmarkEntity)entity).bookmark.getText(), Toast.LENGTH_LONG).show();
	}
}
