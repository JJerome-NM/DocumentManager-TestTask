package dev.jjerome.innovatelutesttask;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {
    private final Map<String, Document> documents = new HashMap<>();

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (Objects.isNull(document.getId())) {
            document.setId(UUID.randomUUID().toString());
        }
        documents.put(document.id, document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        Stream<Document> stream = documents.values().stream();

        if (Objects.nonNull(request.titlePrefixes)) {
            stream = stream.filter(document -> request.titlePrefixes.stream().anyMatch(p -> document.title.startsWith(p)));
        }
        if (Objects.nonNull(request.containsContents)) {
            stream = stream.filter(document -> request.containsContents.stream().anyMatch(content -> document.content.contains(content)));
        }
        if (Objects.nonNull(request.authorIds)){
            stream = stream.filter(document -> request.authorIds.contains(document.author.id));
        }
        if (Objects.nonNull(request.createdFrom)){
            stream = stream.filter(document -> document.created.isAfter(request.createdFrom));
        }
        if (Objects.nonNull(request.createdTo)){
            stream = stream.filter(document -> document.created.isBefore(request.createdTo));
        }

        return stream.collect(toList());
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return Optional.ofNullable(documents.get(id));
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}