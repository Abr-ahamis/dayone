package com.dayone.ui;

import com.dayone.data.DayOneRepository;
import com.dayone.data.PreferencesStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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
public final class DayOneViewModel_Factory implements Factory<DayOneViewModel> {
  private final Provider<DayOneRepository> repositoryProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  public DayOneViewModel_Factory(Provider<DayOneRepository> repositoryProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    this.repositoryProvider = repositoryProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
  }

  @Override
  public DayOneViewModel get() {
    return newInstance(repositoryProvider.get(), preferencesStoreProvider.get());
  }

  public static DayOneViewModel_Factory create(Provider<DayOneRepository> repositoryProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    return new DayOneViewModel_Factory(repositoryProvider, preferencesStoreProvider);
  }

  public static DayOneViewModel newInstance(DayOneRepository repository,
      PreferencesStore preferencesStore) {
    return new DayOneViewModel(repository, preferencesStore);
  }
}
