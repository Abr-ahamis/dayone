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
public final class CompactDayOneWidgetProvider_MembersInjector implements MembersInjector<CompactDayOneWidgetProvider> {
  private final Provider<DayOneWidgetUpdater> updaterProvider;

  public CompactDayOneWidgetProvider_MembersInjector(
      Provider<DayOneWidgetUpdater> updaterProvider) {
    this.updaterProvider = updaterProvider;
  }

  public static MembersInjector<CompactDayOneWidgetProvider> create(
      Provider<DayOneWidgetUpdater> updaterProvider) {
    return new CompactDayOneWidgetProvider_MembersInjector(updaterProvider);
  }

  @Override
  public void injectMembers(CompactDayOneWidgetProvider instance) {
    injectUpdater(instance, updaterProvider.get());
  }

  @InjectedFieldSignature("com.dayone.widget.CompactDayOneWidgetProvider.updater")
  public static void injectUpdater(CompactDayOneWidgetProvider instance,
      DayOneWidgetUpdater updater) {
    instance.updater = updater;
  }
}
