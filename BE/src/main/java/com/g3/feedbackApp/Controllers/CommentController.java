package com.g3.feedbackApp.Controllers;

import com.g3.feedbackApp.Models.CommentModel;
import com.g3.feedbackApp.Models.Converters.CommentConverter;
import com.g3.feedbackApp.Models.DTOS.CommentDTO;
import com.g3.feedbackApp.Services.Interfaces.ICommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping("/comments")
public class CommentController {

    private final ICommentService commentService;
    private final CommentConverter commentConverter;

    public CommentController(ICommentService commentService) {

        this.commentService = commentService;
        commentConverter = new CommentConverter();
    }

    @PostMapping("/create")
    //POST at http://localhost:XXXX/comments/
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        CommentModel commentModel = commentConverter.convertCommentDTOToCommentModel(commentDTO);
        if (commentService.createComment(commentModel) != null) {
            CommentDTO commentDTO1 = commentConverter.convertCommentModelToCommentDTO(commentModel);
            return ResponseEntity.ok().body(commentDTO1);
        }
        String message = "Something went wrong, comment not created";
        return new ResponseEntity(message, HttpStatus.CONFLICT);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentDTO> getCommentWithId(@PathVariable(value = "id") int id) {
        CommentModel modelToGet = commentService.getCommentWithId((long)id);
        CommentDTO dtoToReturn = commentConverter.convertCommentModelToCommentDTO(modelToGet);

        if (modelToGet != null) {
            return ResponseEntity.ok().body(dtoToReturn);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/version/{id}")
    public ResponseEntity<List<CommentModel>> getCommentWithVersionId(@PathVariable(value = "id") int id) {
        List<CommentModel> comments = commentService.getCommentsWithVersionId((long) id);


        if (comments != null) {
            return ResponseEntity.ok().body(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //at /comments
    @GetMapping()
    public ResponseEntity<List<CommentModel>> getComments() {
        List<CommentModel> comments = commentService.getComments();

        if(comments != null){
        return ResponseEntity.ok().body(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}