package com.andresbaquero.docker_example.models;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document(indexName = "accounts", createIndex = true, writeTypeHint = WriteTypeHint.FALSE)
public class AccountModel implements UserDetails {

    public static final String INDEX = "accounts";

    public static final String STATE_ACTIVE = "A";
    public static final String STATE_INACTIVE = "I";

    public static final String ROLE_CLIENT = "C";
    public static final String ROLE_ADMIN = "A";

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Keyword)
    private String email;

    @Field(type = FieldType.Keyword)
    private String password;

    @Field(type = FieldType.Keyword)
    private List<String> roles;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
