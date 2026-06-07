package com.dayone.receiver;

import com.dayone.alarm.DayOneAlarmScheduler;
import com.dayone.data.DayOneRepository;
import com.dayone.widget.DayOneWidgetUpdater;
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
public final class DayOneAlarmReceiver_MembersInjector implements MembersInjector<DayOneAlarmReceiver> {
  private final Provider<DayOneRepository> repositoryProvider;

  private final Provider<DayOneAlarmScheduler> schedulerProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private final Provider<DayOneWidgetUpdater> widgetUpdaterProvider;

  public DayOneAlarmReceiver_MembersInjector(Provider<DayOneRepository> repositoryProvider,
      Provider<DayOneAlarmScheduler> schedulerProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<DayOneWidgetUpdater> widgetUpdaterProvider) {
    this.repositoryProvider = repositoryProvider;
    this.schedulerProvider = schedulerProvider;
    this.notificationHelperProvider = notificationHelperProvider;
    this.widgetUpdaterProvider = widgetUpdaterProvider;
  }

  public static MembersInjector<DayOneAlarmReceiver> create(
      Provider<DayOneRepository> repositoryProvider,
      Provider<DayOneAlarmScheduler> schedulerProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<DayOneWidgetUpdater> widgetUpdaterProvider) {
    return new DayOneAlarmReceiver_MembersInjector(repositoryProvider, schedulerProvider, notificationHelperProvider, widgetUpdaterProvider);
  }

  @Override
  public void injectMembers(DayOneAlarmReceiver instance) {
    injectRepository(instance, repositoryProvider.get());
    injectScheduler(instance, schedulerProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
    injectWidgetUpdater(instance, widgetUpdaterProvider.get());
  }

  @InjectedFieldSignature("com.dayone.receiver.DayOneAlarmReceiver.repository")
  public static void injectRepository(DayOneAlarmReceiver instance, DayOneRepository repository) {
    instance.repository = repository;
  }

  @InjectedFieldSignature("com.dayone.receiver.DayOneAlarmReceiver.scheduler")
  public static void injectScheduler(DayOneAlarmReceiver instance, DayOneAlarmScheduler scheduler) {
    instance.scheduler = scheduler;
  }

  @InjectedFieldSignature("com.dayone.receiver.DayOneAlarmReceiver.notificationHelper")
  public static void injectNotificationHelper(DayOneAlarmReceiver instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }

  @InjectedFieldSignature("com.dayone.receiver.DayOneAlarmReceiver.widgetUpdater")
  public static void injectWidgetUpdater(DayOneAlarmReceiver instance,
      DayOneWidgetUpdater widgetUpdater) {
    instance.widgetUpdater = widgetUpdater;
  }
}
