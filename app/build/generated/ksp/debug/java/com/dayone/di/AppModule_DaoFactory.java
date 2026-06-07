package com.dayone.di;

import com.dayone.data.DayOneDao;
import com.dayone.data.DayOneDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_DaoFactory implements Factory<DayOneDao> {
  private final Provider<DayOneDatabase> databaseProvider;

  public AppModule_DaoFactory(Provider<DayOneDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DayOneDao get() {
    return dao(databaseProvider.get());
  }

  public static AppModule_DaoFactory create(Provider<DayOneDatabase> databaseProvider) {
    return new AppModule_DaoFactory(databaseProvider);
  }

  public static DayOneDao dao(DayOneDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.dao(database));
  }
}
