package com.example.teamcity.api.models;

import com.example.teamcity.api.anatation.Random;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseModel {

    @Random
    private String username;
    private String id;
    @Random
    private String password;
    private Roles roles;

}
