package com.g3.feedbackApp.Services.Interfaces;

import com.g3.feedbackApp.Models.UserModel;

import java.util.List;

public interface IUserService {
    public UserModel getUserByStudentNr(Long studentNr);
    public List<UserModel> getUserModels();
    public boolean deleteUserModel(Long studentNr);
    public boolean addUserModel(UserModel userModel);
    public boolean updateUserModel(UserModel userModel);
    public List<UserModel> getAvailableUsersNewPost(Long userId);
    List<UserModel> getAvailableUsersEditPost(Long postId, Long userId);

}
