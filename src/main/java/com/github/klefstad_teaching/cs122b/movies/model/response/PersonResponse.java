package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.person;

import java.util.List;

public class PersonResponse extends ResponseModel<PersonResponse> {
    List<person> people;

    public List<person> getPersons() {
        return people;
    }

    public PersonResponse setPersons(List<person> people) {
        this.people = people;
        return this;
    }
}
