/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment;

import android.app.Fragment;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.cache.UserCache;
import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.repository.UserRepository;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment extends Fragment {
  /**
   * Shows a {@link android.widget.Toast} message.
   *
   * @param message An string representing a message to be shown.
   */
  protected void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

  protected BaseActivity getBaseActivity() {
    return (BaseActivity) getActivity();
  }

  public ThreadExecutor getThreadExecutor() {
    return getBaseActivity().getAndroidApplication().getThreadExecutor();
  }

  public PostExecutionThread getPostExecutionThread() {
    return getBaseActivity().getAndroidApplication().getPostExecutionThread();
  }

  public UserCache getUserCache() {
    return getBaseActivity().getAndroidApplication().getUserCache();
  }

  public UserRepository getUserRepository() {
    return getBaseActivity().getAndroidApplication().getUserRepository();
  }
}
