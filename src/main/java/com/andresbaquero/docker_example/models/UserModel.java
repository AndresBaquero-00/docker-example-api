package com.andresbaquero.docker_example.models;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document(indexName = "users", createIndex = true, writeTypeHint = WriteTypeHint.FALSE)
public class UserModel {

    public static final String INDEX = "users";

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Keyword)
    private String email;

    @Transient
    private Long age;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private Date birthDate;

    @JsonIgnore
    @Field(type = FieldType.Date, name = "@timestamp")
    private Date timestamp;

    public Long getAge() {
        Calendar bd = Calendar.getInstance();
        bd.setTime(birthDate);

        LocalDate now = LocalDate.now();
        long age = now.minusYears(bd.get(Calendar.YEAR)).getYear();
        return age;
    }

}
