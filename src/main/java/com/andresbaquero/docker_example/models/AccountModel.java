package com.andresbaquero.docker_example.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document(indexName = "accounts", createIndex = true, writeTypeHint = WriteTypeHint.FALSE)
public class AccountModel {

    public static final String INDEX = "accounts";

    public static final String ACTIVE = "A";
    public static final String INACTIVE = "I";

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Keyword)
    private String email;

    @Field(type = FieldType.Keyword)
    private String password;

    @Field(type = FieldType.Long)
    private Long logins;

    @Field(type = FieldType.Keyword)
    private String state;
    
    @Nullable
    @Field(type = FieldType.Date, storeNullValue = true)
    private Date lastLogin;

    @JsonIgnore
    @Field(type = FieldType.Date, name = "@timestamp")
    private Date timestamp;

}
