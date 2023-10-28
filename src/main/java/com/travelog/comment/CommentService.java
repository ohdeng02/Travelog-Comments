package com.travelog.comment;

import com.travelog.comment.dto.CommentReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public List<Comment> getComments(Long boardId){
        return commentRepository.findAllByBoardId(boardId);
    }

    public Comment createComment(CommentReqDto commentReqDto, Long boardId){
        Comment comment = Comment.builder()
                .boardId(boardId)
                .nickname(commentReqDto.getNickname())
                .content(commentReqDto.getContent())
                .status(commentReqDto.isStatus())
                .build();

        commentRepository.save(comment);
        return comment;
    }

    public void deleteComment(Long boardId, Long commentId){
        commentRepository.deleteByBoardIdAndCommentId(boardId, commentId);
    }

}