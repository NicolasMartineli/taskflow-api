package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.comment.CommentCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.comment.CommentResponse;
import com.nicolasmartineli.taskflow_api.models.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment toEntity(CommentCreateRequest commentCreateRequest);

    @Mapping(target = "createdAt", source = "registrationDate")
    CommentResponse toResponse(Comment comment);
}
