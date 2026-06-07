package com.dayone.widget;

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
public final class ExpandedDayOneWidgetProvider_MembersInjector implements MembersInjector<ExpandedDayOneWidgetProvider> {
  private final Provider<DayOneWidgetUpdater> updaterProvider;

  public ExpandedDayOneWidgetProvider_MembersInjector(
      Provider<DayOneWidgetUpdater> updaterProvider) {
    this.updaterProvider = updaterProvider;
  }

  public static MembersInjector<ExpandedDayOneWidgetProvider> create(
      Provider<DayOneWidgetUpdater> updaterProvider) {
    return new ExpandedDayOneWidgetProvider_MembersInjector(updaterProvider);
  }

  @Override
  public void injectMembers(ExpandedDayOneWidgetProvider instance) {
    injectUpdater(instance, updaterProvider.get());
  }

  @InjectedFieldSignature("com.dayone.widget.ExpandedDayOneWidgetProvider.updater")
  public static void injectUpdater(ExpandedDayOneWidgetProvider instance,
      DayOneWidgetUpdater updater) {
    instance.updater = updater;
  }
}
