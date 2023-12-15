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

    // 댓글 개수
    @Transactional(readOnly = true)
    public int countCommentSize(Long boardId){
        return commentRepository.findIdByBoardId(boardId).size();
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(Long boardId){
        return commentRepository.findAllByBoardId(boardId);
    }

    @Transactional(readOnly = true)
    public CommentResDto createComment(CommentReqDto commentReqDto, Long boardId){
        Comment comment = Comment.builder()
                .boardId(boardId)
                .memberId(commentReqDto.getMemberId())
                .nickname(commentReqDto.getNickname())
                .content(commentReqDto.getContent())
                .status(commentReqDto.isStatus())
                .build();

        commentRepository.save(comment);
        return new CommentResDto(comment);
    }
    @Transactional
    public CommentResDto updateComment(CommentReqDto commentReqDto, Long boardId, Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new NoSuchElementException("해당 댓글이 존재하지 않습니다."));
        comment.updateComment(commentReqDto);
        return new CommentResDto(comment);
    }

    @Transactional(readOnly = true)
    public void deleteComment(Long boardId, Long commentId){
        commentRepository.deleteByBoardIdAndCommentId(boardId, commentId);
    }

}
