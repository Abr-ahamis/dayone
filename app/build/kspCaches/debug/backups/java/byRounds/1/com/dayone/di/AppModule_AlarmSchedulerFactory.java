package com.dayone.di;

import android.content.Context;
import com.dayone.alarm.DayOneAlarmScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_AlarmSchedulerFactory implements Factory<DayOneAlarmScheduler> {
  private final Provider<Context> contextProvider;

  public AppModule_AlarmSchedulerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DayOneAlarmScheduler get() {
    return alarmScheduler(contextProvider.get());
  }

  public static AppModule_AlarmSchedulerFactory create(Provider<Context> contextProvider) {
    return new AppModule_AlarmSchedulerFactory(contextProvider);
  }

  public static DayOneAlarmScheduler alarmScheduler(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.alarmScheduler(context));
  }
}
