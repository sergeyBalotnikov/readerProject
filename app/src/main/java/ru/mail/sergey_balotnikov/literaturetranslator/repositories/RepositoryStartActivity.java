package ru.mail.sergey_balotnikov.literaturetranslator.repositories;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import ru.mail.sergey_balotnikov.literaturetranslator.books.Book;

public interface RepositoryStartActivity {
    CompletableFuture<List<Book>> getStorageBooks();
}
