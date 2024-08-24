package com.andresbaquero.docker_example.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import lombok.Data;

@Data
@Document(indexName = "todos", createIndex = true, writeTypeHint = WriteTypeHint.FALSE)
public class TodoModel {

    public static final String INDEX = "todo";

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Boolean)
    private Boolean completed;

    @Field(type = FieldType.Keyword)
    private String user;

    @Field(type = FieldType.Date, name = "@timestamp")
    private Date timestamp;

}
