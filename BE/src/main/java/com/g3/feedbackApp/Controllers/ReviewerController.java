package com.g3.feedbackApp.Controllers;

import com.g3.feedbackApp.Models.ReviewerModel;
import com.g3.feedbackApp.Models.UserModel;
import com.g3.feedbackApp.Services.Interfaces.IReviewerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/reviewers")
public class ReviewerController {
    private IReviewerService reviewerService;

    public ReviewerController(IReviewerService reviewerService) {
        this.reviewerService = reviewerService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ReviewerModel> getReviewer(@PathVariable(value = "id") int id) {
        ReviewerModel reviewerModel = reviewerService.getReviewerById((long) id);

        if(reviewerModel != null) {
            return ResponseEntity.ok().body(reviewerModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReviewerModel>> getAllReviewers() {
        List<ReviewerModel> reviewerModels = null;

        reviewerModels = reviewerService.getReviewers();

        if(reviewerModels != null) {
            return ResponseEntity.ok().body(reviewerModels);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/asUsers")
    public ResponseEntity<List<UserModel>> getReviewersOnPostAsUsers(@RequestParam(value = "postId") Optional<Long> postId) {
        List<UserModel> reviewerModels = null;
        if(postId.isPresent()) {
            reviewerModels = reviewerService.getReviewersOnPost(postId.get());
            return ResponseEntity.ok().body(reviewerModels);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("{id}")
    public ResponseEntity deleteReviewer(@PathVariable int id) {
        reviewerService.deleteReviewerById((long)id);
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<ReviewerModel> createReviewer(@RequestBody ReviewerModel reviewerModel) {
        if (!reviewerService.addReviewer(reviewerModel)){
            String entity =  "The reviewer with the id: " + reviewerModel.getUserId() + " already exists.";
            return new ResponseEntity(entity, HttpStatus.CONFLICT);
        } else {
            String url = "users" + "/" + reviewerModel.getUserId();
            URI uri = URI.create(url);
            return new ResponseEntity(uri,HttpStatus.CREATED);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<UserModel> updateStudent(@PathVariable("id") int id,
                                                   @RequestParam("reviewerId") int reviewerId) {

        ReviewerModel reviewerModel = reviewerService.getReviewerById((long)id);

        if (reviewerModel == null){
            return new ResponseEntity("Please provide a valid reviewer id.",HttpStatus.NOT_FOUND);
        }

        reviewerModel.setUserId((long)reviewerId);
        return ResponseEntity.noContent().build();
    }
}
