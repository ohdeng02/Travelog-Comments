package com.travelog.comment;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.travelog.comment.dto.CommentDocumentResDto;
import com.travelog.comment.dto.CommentReqDto;
import com.travelog.comment.dto.CommentResDto;
import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    @Value("${elastic.serverUrl}")
    private String serverUrl;
    @Value("${elastic.apiKey}")
    private String apiKey;

    RestClient restClient = RestClient
            .builder(HttpHost.create(serverUrl))
            .setDefaultHeaders(new Header[]{
                    new BasicHeader("Authorization", "ApiKey " + apiKey)
            })
            .build();

    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());
    ElasticsearchClient client = new ElasticsearchClient(transport);

    // 댓글 개수
    @Transactional(readOnly = true)
    public int countCommentSize(Long boardId){
        return commentRepository.findIdByBoardId(boardId).size();
    }

    @Transactional(readOnly = true)
    public List<CommentDocumentResDto> getComments(Long boardId) throws IOException {

        SearchResponse<CommentDocument> search = client.search(s -> s
                        .index("sourcedb.comment.comment").size(5000)
                        .query(q -> q
                                .match(t -> t
                                        .field("board_id").query(boardId)
                                )), CommentDocument.class);

        List<CommentDocumentResDto> comments = new ArrayList<>();
        for (Hit<CommentDocument> hit: search.hits().hits()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date timeInDate = new Date(hit.source().getCreated_at());
            String timeInFormat = sdf.format(timeInDate);

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date timeInDate2 = new Date(hit.source().getUpdated_at());
            String timeInFormat2 = sdf2.format(timeInDate2);

            comments.add(new CommentDocumentResDto(hit.source().getComment_id(),
                    hit.source().getBoard_id(),
                    hit.source().getMember_id(),
                    hit.source().getNickname(),
                    hit.source().getContent(),
                    timeInFormat,
                    timeInFormat2,
                    hit.source().getReport(),
                    hit.source().isStatus()));
        }
        return comments;
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
