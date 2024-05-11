package com.example.demo.auth.mapper;

import com.example.demo.auth.dto.LoginDto;
import com.example.demo.auth.dto.MeDto;
import com.example.demo.auth.dto.SignUpDto;
import com.example.demo.auth.entity.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

     Tenant toEntity(SignUpDto signUpDto);
}
