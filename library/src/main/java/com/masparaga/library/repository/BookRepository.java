package com.masparaga.library.repository;

import com.masparaga.library.model.Book;
import com.masparaga.library.model.dto.SearchBookQuery;
import com.masparaga.library.model.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, ObjectId>, BookRepoCustom {
    default Book getById(ObjectId id){
        return findById(id).orElseThrow(()->new NotFoundException(Book.class, id));
    }

    List<Book> findAllById(Iterable<ObjectId> ids);
}

interface BookRepoCustom{
    List<Book> searchBooks(SearchBookQuery query);
}

@RequiredArgsConstructor
class BookRepoCustomImpl implements BookRepoCustom{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Book> searchBooks(SearchBookQuery query) {
        List<AggregationOperation> operations = new ArrayList<>();

        List<Criteria> criteriaList = new ArrayList<>();

        if(!query.getId().isEmpty()){
            criteriaList.add(Criteria.where("id").is(new ObjectId(query.getId())));
        }

        if(!query.getName().isEmpty()){
            criteriaList.add(Criteria.where("name").regex(query.getName(), "i"));
        }

        if(!query.getIsTaken().isEmpty()){
            criteriaList.add(Criteria.where("isTaken").is(query.getIsTaken()));
        }

        if(!query.getAuthor().isEmpty()){
            criteriaList.add(Criteria.where("author").regex(query.getAuthor(), "i"));
        }

        if(!criteriaList.isEmpty()){
            Criteria bookCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
            operations.add(Aggregation.match(bookCriteria));
        }

        operations.add(Aggregation.sort(Sort.Direction.DESC, "id"));
        TypedAggregation<Book> aggregation = Aggregation.newAggregation(Book.class, operations);
        AggregationResults<Book> results = mongoTemplate.aggregate(aggregation, Book.class);
        return results.getMappedResults();
    }
}