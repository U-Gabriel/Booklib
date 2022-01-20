package com.esgi.booklib.repositories;

import com.esgi.booklib.entities.Reader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReaderRepository extends CrudRepository<Reader, Integer> {
}
