package com.travelog.comment;

import com.travelog.comment.dto.CommentReqDto;
import com.travelog.comment.dto.CommentResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<Comment> getComments(Long boardId){
        return commentRepository.findAllByBoardId(boardId);
    }

    @Transactional(readOnly = true)
    public CommentResDto createComment(CommentReqDto commentReqDto, Long boardId){
        Comment comment = Comment.builder()
                .boardId(boardId)
                .nickname(commentReqDto.getNickname())
                .content(commentReqDto.getContent())
                .status(commentReqDto.isStatus())
                .build();

        commentRepository.save(comment);
        CommentResDto commentResDto = new CommentResDto(comment);
        return commentResDto;
    }
    @Transactional
    public Comment updateComment(CommentReqDto commentReqDto, Long boardId, Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new NoSuchElementException("해당 댓글이 존재하지 않습니다."));
        comment.updateComment(commentReqDto);
        return comment;
    }

    @Transactional(readOnly = true)
    public void deleteComment(Long boardId, Long commentId){
        commentRepository.deleteByBoardIdAndCommentId(boardId, commentId);
    }

}
