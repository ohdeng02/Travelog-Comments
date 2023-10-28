package com.travelog.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);

    @Query(value = "delete from comment where board_id=:boardId and comment_id=:commentId", nativeQuery = true)
    void deleteByBoardIdAndCommentId(@Param("boardId") Long boardId, @Param("commentId") Long commentId);
}
