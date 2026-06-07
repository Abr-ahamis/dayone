package com.dayone.receiver;

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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<DayOneAlarmScheduler> schedulerProvider;

  public BootReceiver_MembersInjector(Provider<DayOneAlarmScheduler> schedulerProvider) {
    this.schedulerProvider = schedulerProvider;
  }

  public static MembersInjector<BootReceiver> create(
      Provider<DayOneAlarmScheduler> schedulerProvider) {
    return new BootReceiver_MembersInjector(schedulerProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectScheduler(instance, schedulerProvider.get());
  }

  @InjectedFieldSignature("com.dayone.receiver.BootReceiver.scheduler")
  public static void injectScheduler(BootReceiver instance, DayOneAlarmScheduler scheduler) {
    instance.scheduler = scheduler;
  }
}
