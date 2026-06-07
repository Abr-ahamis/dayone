package com.dayone.data;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DayOneRepository_Factory implements Factory<DayOneRepository> {
  private final Provider<DayOneDao> daoProvider;

  public DayOneRepository_Factory(Provider<DayOneDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public DayOneRepository get() {
    return newInstance(daoProvider.get());
  }

  public static DayOneRepository_Factory create(Provider<DayOneDao> daoProvider) {
    return new DayOneRepository_Factory(daoProvider);
  }

  public static DayOneRepository newInstance(DayOneDao dao) {
    return new DayOneRepository(dao);
  }
}
