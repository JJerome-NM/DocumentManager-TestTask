package dev.jjerome.innovatelutesttask;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

public class TestMainClass {
    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();

        DocumentManager.Author author = new DocumentManager.Author(UUID.randomUUID().toString(), "JJerome");

        DocumentManager.Document testDocument1 = DocumentManager.Document.builder()
                .title("Title")
                .content("Lorem ipsum dolor sit amet")
                .created(LocalDateTime.of(2022, Month.APRIL, 1, 0, 0).toInstant(ZoneOffset.UTC))
                .author(author)
                .build();
        DocumentManager.Document testDocument2 = DocumentManager.Document.builder()
                .title("Title")
                .content("Lorem ipsum dolor sit amet")
                .created(LocalDateTime.of(2025, Month.APRIL, 1, 0, 0).toInstant(ZoneOffset.UTC))
                .author(author)
                .build();

        manager.save(testDocument1);
        manager.save(testDocument2);

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of(""))
                .authorIds(List.of(author.getId()))
                .containsContents(List.of("lo"))
                .createdFrom(LocalDateTime.of(2021, Month.OCTOBER, 1, 0, 0).toInstant(ZoneOffset.UTC))
                .createdTo(LocalDateTime.of(2023, Month.APRIL, 1, 0, 0).toInstant(ZoneOffset.UTC))
                .build();

        List<DocumentManager.Document> results = manager.search(searchRequest);

        System.out.println(results.size());
    }
}
