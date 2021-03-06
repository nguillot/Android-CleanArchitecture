/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.fernandocejas.android10.sample.domain.interactor.GetUserDetails;
import com.fernandocejas.android10.sample.domain.interactor.GetUserList;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.mapper.UserModelDataMapper;
import com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.fernandocejas.android10.sample.presentation.presenter.UserDetailsPresenter;
import com.fernandocejas.android10.sample.presentation.presenter.UserListPresenter;
import com.fernandocejas.android10.sample.presentation.view.UserDetailsView;
import com.fernandocejas.android10.sample.presentation.view.activity.UserDetailsActivity;
import com.fernandocejas.android10.sample.presentation.view.component.AutoLoadImageView;

/**
 * Fragment that shows details of a certain user.
 */
public class UserDetailsFragment extends BaseFragment implements UserDetailsView {

  UserDetailsPresenter userDetailsPresenter;

  @Bind(R.id.iv_cover) AutoLoadImageView iv_cover;
  @Bind(R.id.tv_fullname) TextView tv_fullname;
  @Bind(R.id.tv_email) TextView tv_email;
  @Bind(R.id.tv_followers) TextView tv_followers;
  @Bind(R.id.tv_description) TextView tv_description;
  @Bind(R.id.rl_progress) RelativeLayout rl_progress;
  @Bind(R.id.rl_retry) RelativeLayout rl_retry;
  @Bind(R.id.bt_retry) Button bt_retry;

  public UserDetailsFragment() {
    setRetainInstance(true);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);
    ButterKnife.bind(this, fragmentView);
    setupPresenter();
    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.userDetailsPresenter.setView(this);
    if (savedInstanceState == null) {
      this.loadUserDetails();
    }
  }

  @Override public void onResume() {
    super.onResume();
    this.userDetailsPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    this.userDetailsPresenter.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    this.userDetailsPresenter.destroy();
  }

  @Override public void renderUser(UserModel user) {
    if (user != null) {
      this.iv_cover.setImageUrl(user.getCoverUrl());
      this.tv_fullname.setText(user.getFullName());
      this.tv_email.setText(user.getEmail());
      this.tv_followers.setText(String.valueOf(user.getFollowers()));
      this.tv_description.setText(user.getDescription());
    }
  }

  @Override public void showLoading() {
    this.rl_progress.setVisibility(View.VISIBLE);
    this.getActivity().setProgressBarIndeterminateVisibility(true);
  }

  @Override public void hideLoading() {
    this.rl_progress.setVisibility(View.GONE);
    this.getActivity().setProgressBarIndeterminateVisibility(false);
  }

  @Override public void showRetry() {
    this.rl_retry.setVisibility(View.VISIBLE);
  }

  @Override public void hideRetry() {
    this.rl_retry.setVisibility(View.GONE);
  }

  @Override public void showError(String message) {
    this.showToastMessage(message);
  }

  @Override public Context context() {
    return getActivity().getApplicationContext();
  }

  private void setupPresenter() {
    int userId = ((UserDetailsActivity)getActivity()).getUserId();
    final GetUserDetails getUserListUserCase = new GetUserDetails(userId, getUserRepository(), getThreadExecutor(), getPostExecutionThread());
    UserModelDataMapper userModelDataMapper = new UserModelDataMapper();
    this.userDetailsPresenter = new UserDetailsPresenter(getUserListUserCase, userModelDataMapper);
    this.userDetailsPresenter.setView(this);
  }

  /**
   * Loads all users.
   */
  private void loadUserDetails() {
    if (this.userDetailsPresenter != null) {
      this.userDetailsPresenter.initialize();
    }
  }

  @OnClick(R.id.bt_retry)
  void onButtonRetryClick() {
    UserDetailsFragment.this.loadUserDetails();
  }
}
