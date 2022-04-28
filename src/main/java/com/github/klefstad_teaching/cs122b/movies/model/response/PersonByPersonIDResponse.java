package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.person;

public class PersonByPersonIDResponse extends ResponseModel<PersonByPersonIDResponse> {
    private person Person;

    public person getPerson() {
        return Person;
    }

    public PersonByPersonIDResponse setPerson(person p) {
        this.Person = p;
        return this;
    }
}
