package com.g3.feedbackApp.Services;

import com.g3.feedbackApp.DataSources.Interfaces.IDataSourceComment;
import com.g3.feedbackApp.DataSources.Interfaces.IDataSourcePost;
import com.g3.feedbackApp.DataSources.Interfaces.IDataSourceReviewer;
import com.g3.feedbackApp.DataSources.Interfaces.IDataSourceUser;
import com.g3.feedbackApp.Models.PostModel;
import com.g3.feedbackApp.Models.ReviewerModel;
import com.g3.feedbackApp.Models.UserModel;
import com.g3.feedbackApp.Models.VersionModel;
import com.g3.feedbackApp.Services.Interfaces.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PostService implements IPostService {

    @Autowired
    private IDataSourcePost datasource;

    @Autowired
    private IDataSourceReviewer dataSourceReviewers;

    @Autowired
    private IDataSourceComment dataSourceComments;

    @Override
    public Boolean createPost(PostModel postModel, String filePath, List<Long> reviewersIds) {
        //Check if object holds value
        if(!Objects.equals(postModel.getTitle(), "")){
            return datasource.createPost(postModel) && createVersion(postModel.getPostId(), filePath) && datasource.assignReviewers(reviewersIds, postModel.getPostId());
        }
        return false;
    }

    @Override
    public PostModel updatePost(PostModel postmodel, List<Long> reviewersIds) {
        if(!Objects.equals(postmodel.getTitle(), "")){
            updateReviewersIds(reviewersIds, postmodel.getPostId());
            return datasource.updatePost(postmodel);
        }
        return null;
    }

    @Override
    public void updateReviewersIds(List<Long> reviewersIds, Long postId) {
        datasource.removeAllReviewers(postId);
        datasource.assignReviewers(reviewersIds, postId);

    }

    @Override
    public Boolean createVersion(Long postId, String filePath) {
        int versionsListSize = datasource.getVersionsForPost(postId).size();
        Long lastId;
        if(versionsListSize == 0) {
            lastId = 0l;
        }
        else {
            lastId = datasource.getVersionsForPost(postId).get(versionsListSize - 1).getVersionCounter();
        }
        return datasource.createVersion(lastId + 1, postId, filePath);
    }

    @Override
    @Transactional
    public void deleteAllVersionsByPostId(Long postId) {
        datasource.deleteAllVersionsByPostId(postId);
    }


    @Override
    public PostModel getPostWithId(Long id) {
        return datasource.getPostWithId(id);
    }

    @Override
    public List<PostModel> getPostsToReview(Long userId) {
        List<ReviewerModel> reviewers = dataSourceReviewers.getReviewers();

        List<ReviewerModel> reviewersWithUserId = new ArrayList<>();
//        List<ReviewerModel> reviewersWithUserId =  reviewers.
//                stream().
//                filter(reviewerModel -> reviewerModel.getUserId().equals(userId)).
//                collect(Collectors.toList());

        for (ReviewerModel reviewerModel: reviewers) {
            if(reviewerModel.getUserId().equals(userId)){
                reviewersWithUserId.add(reviewerModel);
            }
        }


        List<PostModel> postsToReview = new ArrayList<>();
        for (ReviewerModel reviewer : reviewersWithUserId) {
            for (PostModel post : getAllPosts()) {
                if(post.getPostId() == reviewer.getPostId())
                {
                    postsToReview.add(post);
                }
            }
        }
        return postsToReview;
    }

    @Override
    public List<PostModel> getMyPosts(Long idOP) {
        return getAllPosts().
                stream().
                filter(postModel -> postModel.getIdOP()==idOP).
                collect(Collectors.toList());
    }

    @Override
    public List<PostModel> getAllPosts() {
        return datasource.getAllPosts();
    }

    @Override
    public VersionModel getVersionWithId(Long versionId) {
        return datasource.getVersionWithId(versionId);
    }

    @Override
    public List<VersionModel> getVersionsForPost(Long postId) {
        return datasource.getVersionsForPost(postId);
    }

    @Override
    public List<Long> getReviewersIdsForPost(int postId) {
        return datasource.getReviewersIdsForPost(postId);
    }

    @Override
    public List<UserModel> getAvailableUsersExistingPost(Long postId){
        PostModel toEdit = datasource.getPostWithId(postId);
        //todo
        // all users - op - currently assigned reviewers

        return null;
    }

    @Override
    public boolean deletePostModel(Long postId) {
        return datasource.deletePostModel(postId);
    }

    @Override
    @Transactional
    public void deleteAllCommentsByPostId(Long postId) {
        List<VersionModel> versions = getVersionsForPost(postId);
        for (int i = 0; i < versions.size(); i++) {
            dataSourceComments.deleteAllByVersionId(versions.get(i).getVersionId());
        }
    }

    @Override
    public List<PostModel> getAllPostsByIdOp(Long idOp) {
        return datasource.getAllByIdOp(idOp);
    }

}
