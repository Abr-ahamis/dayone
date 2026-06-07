package com.dayone;

import com.dayone.alarm.DayOneAlarmScheduler;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DayOneApplication_MembersInjector implements MembersInjector<DayOneApplication> {
  private final Provider<DayOneAlarmScheduler> alarmSchedulerProvider;

  public DayOneApplication_MembersInjector(Provider<DayOneAlarmScheduler> alarmSchedulerProvider) {
    this.alarmSchedulerProvider = alarmSchedulerProvider;
  }

  public static MembersInjector<DayOneApplication> create(
      Provider<DayOneAlarmScheduler> alarmSchedulerProvider) {
    return new DayOneApplication_MembersInjector(alarmSchedulerProvider);
  }

  @Override
  public void injectMembers(DayOneApplication instance) {
    injectAlarmScheduler(instance, alarmSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.dayone.DayOneApplication.alarmScheduler")
  public static void injectAlarmScheduler(DayOneApplication instance,
      DayOneAlarmScheduler alarmScheduler) {
    instance.alarmScheduler = alarmScheduler;
  }
}
