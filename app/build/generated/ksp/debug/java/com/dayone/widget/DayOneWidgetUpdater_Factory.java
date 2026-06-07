package com.dayone.widget;

import android.content.Context;
import com.dayone.data.DayOneRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DayOneWidgetUpdater_Factory implements Factory<DayOneWidgetUpdater> {
  private final Provider<Context> contextProvider;

  private final Provider<DayOneRepository> repositoryProvider;

  public DayOneWidgetUpdater_Factory(Provider<Context> contextProvider,
      Provider<DayOneRepository> repositoryProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DayOneWidgetUpdater get() {
    return newInstance(contextProvider.get(), repositoryProvider.get());
  }

  public static DayOneWidgetUpdater_Factory create(Provider<Context> contextProvider,
      Provider<DayOneRepository> repositoryProvider) {
    return new DayOneWidgetUpdater_Factory(contextProvider, repositoryProvider);
  }

  public static DayOneWidgetUpdater newInstance(Context context, DayOneRepository repository) {
    return new DayOneWidgetUpdater(context, repository);
  }
}
