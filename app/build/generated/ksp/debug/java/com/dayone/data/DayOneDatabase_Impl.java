package com.dayone.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DayOneDatabase_Impl extends DayOneDatabase {
  private volatile DayOneDao _dayOneDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `streak_state` (`id` INTEGER NOT NULL, `habitName` TEXT NOT NULL, `emoji` TEXT NOT NULL, `colorHex` TEXT NOT NULL, `startEpochDay` INTEGER NOT NULL, `currentStreak` INTEGER NOT NULL, `longestStreak` INTEGER NOT NULL, `totalCompleted` INTEGER NOT NULL, `streakBreaks` INTEGER NOT NULL, `verseIndex` INTEGER NOT NULL, `lastUpdateEpochMillis` INTEGER NOT NULL, `lastNoonUpdateEpochDay` INTEGER NOT NULL, `lastMorningEpochDay` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `calendar_history` (`epochDay` INTEGER NOT NULL, `state` TEXT NOT NULL, `streakValue` INTEGER NOT NULL, `updatedAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`epochDay`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8fd3fad7c2ecd52c21615a76677d17f0')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `streak_state`");
        db.execSQL("DROP TABLE IF EXISTS `calendar_history`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsStreakState = new HashMap<String, TableInfo.Column>(13);
        _columnsStreakState.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("habitName", new TableInfo.Column("habitName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("emoji", new TableInfo.Column("emoji", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("colorHex", new TableInfo.Column("colorHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("startEpochDay", new TableInfo.Column("startEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("currentStreak", new TableInfo.Column("currentStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("longestStreak", new TableInfo.Column("longestStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("totalCompleted", new TableInfo.Column("totalCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("streakBreaks", new TableInfo.Column("streakBreaks", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("verseIndex", new TableInfo.Column("verseIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("lastUpdateEpochMillis", new TableInfo.Column("lastUpdateEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("lastNoonUpdateEpochDay", new TableInfo.Column("lastNoonUpdateEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreakState.put("lastMorningEpochDay", new TableInfo.Column("lastMorningEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStreakState = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStreakState = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStreakState = new TableInfo("streak_state", _columnsStreakState, _foreignKeysStreakState, _indicesStreakState);
        final TableInfo _existingStreakState = TableInfo.read(db, "streak_state");
        if (!_infoStreakState.equals(_existingStreakState)) {
          return new RoomOpenHelper.ValidationResult(false, "streak_state(com.dayone.data.StreakStateEntity).\n"
                  + " Expected:\n" + _infoStreakState + "\n"
                  + " Found:\n" + _existingStreakState);
        }
        final HashMap<String, TableInfo.Column> _columnsCalendarHistory = new HashMap<String, TableInfo.Column>(4);
        _columnsCalendarHistory.put("epochDay", new TableInfo.Column("epochDay", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarHistory.put("state", new TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarHistory.put("streakValue", new TableInfo.Column("streakValue", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarHistory.put("updatedAtEpochMillis", new TableInfo.Column("updatedAtEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCalendarHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCalendarHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCalendarHistory = new TableInfo("calendar_history", _columnsCalendarHistory, _foreignKeysCalendarHistory, _indicesCalendarHistory);
        final TableInfo _existingCalendarHistory = TableInfo.read(db, "calendar_history");
        if (!_infoCalendarHistory.equals(_existingCalendarHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "calendar_history(com.dayone.data.CalendarEntryEntity).\n"
                  + " Expected:\n" + _infoCalendarHistory + "\n"
                  + " Found:\n" + _existingCalendarHistory);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "8fd3fad7c2ecd52c21615a76677d17f0", "e21c15308ceab64e1fb6794fd223449d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "streak_state","calendar_history");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `streak_state`");
      _db.execSQL("DELETE FROM `calendar_history`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DayOneDao.class, DayOneDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DayOneDao dao() {
    if (_dayOneDao != null) {
      return _dayOneDao;
    } else {
      synchronized(this) {
        if(_dayOneDao == null) {
          _dayOneDao = new DayOneDao_Impl(this);
        }
        return _dayOneDao;
      }
    }
  }
}
