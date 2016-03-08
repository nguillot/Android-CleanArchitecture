/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.presentation;

import android.app.Application;

import com.fernandocejas.android10.sample.data.cache.FileManager;
import com.fernandocejas.android10.sample.data.cache.UserCache;
import com.fernandocejas.android10.sample.data.cache.UserCacheImpl;
import com.fernandocejas.android10.sample.data.cache.serializer.JsonSerializer;
import com.fernandocejas.android10.sample.data.entity.mapper.UserEntityDataMapper;
import com.fernandocejas.android10.sample.data.executor.JobExecutor;
import com.fernandocejas.android10.sample.data.repository.UserDataRepository;
import com.fernandocejas.android10.sample.data.repository.datasource.UserDataStoreFactory;
import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.repository.UserRepository;
import com.squareup.leakcanary.LeakCanary;

/**
 * Android Main Application
 */
public class AndroidApplication extends Application {

  private ThreadExecutor threadExecutor;
  private PostExecutionThread postExecutionThread;
  private UserCache userCache;
  private UserRepository userRepository;

  @Override public void onCreate() {
    super.onCreate();
    this.initializeInjector();
    this.initializeLeakDetection();
  }

  private void initializeInjector() {
    JsonSerializer userCacheSerializer = new JsonSerializer();
    FileManager fileManager = new FileManager();
    threadExecutor = new JobExecutor();
    userCache = new UserCacheImpl(getApplicationContext(), userCacheSerializer, fileManager, threadExecutor);

    UserDataStoreFactory dataStoreFactory = new UserDataStoreFactory(getApplicationContext(), userCache);
    UserEntityDataMapper userEntityDataMapper = new UserEntityDataMapper();

    postExecutionThread = new UIThread();
    userRepository = new UserDataRepository(dataStoreFactory, userEntityDataMapper);
  }

  public void setThreadExecutor(ThreadExecutor threadExecutor) {
    this.threadExecutor = threadExecutor;
  }

  public void setPostExecutionThread(PostExecutionThread postExecutionThread) {
    this.postExecutionThread = postExecutionThread;
  }

  public void setUserCache(UserCache userCache) {
    this.userCache = userCache;
  }

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public ThreadExecutor getThreadExecutor() {
    return threadExecutor;
  }

  public PostExecutionThread getPostExecutionThread() {
    return postExecutionThread;
  }

  public UserCache getUserCache() {
    return userCache;
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }

  private void initializeLeakDetection() {
    if (BuildConfig.DEBUG) {
      LeakCanary.install(this);
    }
  }
}
