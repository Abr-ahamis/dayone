package com.dayone.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DayOneDao_Impl implements DayOneDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StreakStateEntity> __insertionAdapterOfStreakStateEntity;

  private final EntityInsertionAdapter<CalendarEntryEntity> __insertionAdapterOfCalendarEntryEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfClearHistory;

  public DayOneDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStreakStateEntity = new EntityInsertionAdapter<StreakStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `streak_state` (`id`,`habitName`,`emoji`,`colorHex`,`startEpochDay`,`currentStreak`,`longestStreak`,`totalCompleted`,`streakBreaks`,`verseIndex`,`lastUpdateEpochMillis`,`lastNoonUpdateEpochDay`,`lastMorningEpochDay`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StreakStateEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getHabitName());
        statement.bindString(3, entity.getEmoji());
        statement.bindString(4, entity.getColorHex());
        statement.bindLong(5, entity.getStartEpochDay());
        statement.bindLong(6, entity.getCurrentStreak());
        statement.bindLong(7, entity.getLongestStreak());
        statement.bindLong(8, entity.getTotalCompleted());
        statement.bindLong(9, entity.getStreakBreaks());
        statement.bindLong(10, entity.getVerseIndex());
        statement.bindLong(11, entity.getLastUpdateEpochMillis());
        statement.bindLong(12, entity.getLastNoonUpdateEpochDay());
        statement.bindLong(13, entity.getLastMorningEpochDay());
      }
    };
    this.__insertionAdapterOfCalendarEntryEntity = new EntityInsertionAdapter<CalendarEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `calendar_history` (`epochDay`,`state`,`streakValue`,`updatedAtEpochMillis`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEntryEntity entity) {
        statement.bindLong(1, entity.getEpochDay());
        final String _tmp = __converters.fromDayState(entity.getState());
        statement.bindString(2, _tmp);
        statement.bindLong(3, entity.getStreakValue());
        statement.bindLong(4, entity.getUpdatedAtEpochMillis());
      }
    };
    this.__preparedStmtOfClearHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM calendar_history";
        return _query;
      }
    };
  }

  @Override
  public Object upsertState(final StreakStateEntity state,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStreakStateEntity.insert(state);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertEntry(final CalendarEntryEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCalendarEntryEntity.insert(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearHistory(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearHistory.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<StreakStateEntity> observeState() {
    final String _sql = "SELECT * FROM streak_state WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"streak_state"}, new Callable<StreakStateEntity>() {
      @Override
      @Nullable
      public StreakStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitName = CursorUtil.getColumnIndexOrThrow(_cursor, "habitName");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfStartEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochDay");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfTotalCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCompleted");
          final int _cursorIndexOfStreakBreaks = CursorUtil.getColumnIndexOrThrow(_cursor, "streakBreaks");
          final int _cursorIndexOfVerseIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "verseIndex");
          final int _cursorIndexOfLastUpdateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdateEpochMillis");
          final int _cursorIndexOfLastNoonUpdateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNoonUpdateEpochDay");
          final int _cursorIndexOfLastMorningEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMorningEpochDay");
          final StreakStateEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpHabitName;
            _tmpHabitName = _cursor.getString(_cursorIndexOfHabitName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpStartEpochDay;
            _tmpStartEpochDay = _cursor.getLong(_cursorIndexOfStartEpochDay);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final int _tmpTotalCompleted;
            _tmpTotalCompleted = _cursor.getInt(_cursorIndexOfTotalCompleted);
            final int _tmpStreakBreaks;
            _tmpStreakBreaks = _cursor.getInt(_cursorIndexOfStreakBreaks);
            final int _tmpVerseIndex;
            _tmpVerseIndex = _cursor.getInt(_cursorIndexOfVerseIndex);
            final long _tmpLastUpdateEpochMillis;
            _tmpLastUpdateEpochMillis = _cursor.getLong(_cursorIndexOfLastUpdateEpochMillis);
            final long _tmpLastNoonUpdateEpochDay;
            _tmpLastNoonUpdateEpochDay = _cursor.getLong(_cursorIndexOfLastNoonUpdateEpochDay);
            final long _tmpLastMorningEpochDay;
            _tmpLastMorningEpochDay = _cursor.getLong(_cursorIndexOfLastMorningEpochDay);
            _result = new StreakStateEntity(_tmpId,_tmpHabitName,_tmpEmoji,_tmpColorHex,_tmpStartEpochDay,_tmpCurrentStreak,_tmpLongestStreak,_tmpTotalCompleted,_tmpStreakBreaks,_tmpVerseIndex,_tmpLastUpdateEpochMillis,_tmpLastNoonUpdateEpochDay,_tmpLastMorningEpochDay);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getState(final Continuation<? super StreakStateEntity> $completion) {
    final String _sql = "SELECT * FROM streak_state WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StreakStateEntity>() {
      @Override
      @Nullable
      public StreakStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitName = CursorUtil.getColumnIndexOrThrow(_cursor, "habitName");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfStartEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochDay");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfTotalCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCompleted");
          final int _cursorIndexOfStreakBreaks = CursorUtil.getColumnIndexOrThrow(_cursor, "streakBreaks");
          final int _cursorIndexOfVerseIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "verseIndex");
          final int _cursorIndexOfLastUpdateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdateEpochMillis");
          final int _cursorIndexOfLastNoonUpdateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNoonUpdateEpochDay");
          final int _cursorIndexOfLastMorningEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMorningEpochDay");
          final StreakStateEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpHabitName;
            _tmpHabitName = _cursor.getString(_cursorIndexOfHabitName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpStartEpochDay;
            _tmpStartEpochDay = _cursor.getLong(_cursorIndexOfStartEpochDay);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final int _tmpTotalCompleted;
            _tmpTotalCompleted = _cursor.getInt(_cursorIndexOfTotalCompleted);
            final int _tmpStreakBreaks;
            _tmpStreakBreaks = _cursor.getInt(_cursorIndexOfStreakBreaks);
            final int _tmpVerseIndex;
            _tmpVerseIndex = _cursor.getInt(_cursorIndexOfVerseIndex);
            final long _tmpLastUpdateEpochMillis;
            _tmpLastUpdateEpochMillis = _cursor.getLong(_cursorIndexOfLastUpdateEpochMillis);
            final long _tmpLastNoonUpdateEpochDay;
            _tmpLastNoonUpdateEpochDay = _cursor.getLong(_cursorIndexOfLastNoonUpdateEpochDay);
            final long _tmpLastMorningEpochDay;
            _tmpLastMorningEpochDay = _cursor.getLong(_cursorIndexOfLastMorningEpochDay);
            _result = new StreakStateEntity(_tmpId,_tmpHabitName,_tmpEmoji,_tmpColorHex,_tmpStartEpochDay,_tmpCurrentStreak,_tmpLongestStreak,_tmpTotalCompleted,_tmpStreakBreaks,_tmpVerseIndex,_tmpLastUpdateEpochMillis,_tmpLastNoonUpdateEpochDay,_tmpLastMorningEpochDay);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CalendarEntryEntity>> observeHistory(final long start, final long end) {
    final String _sql = "SELECT * FROM calendar_history WHERE epochDay BETWEEN ? AND ? ORDER BY epochDay ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendar_history"}, new Callable<List<CalendarEntryEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStreakValue = CursorUtil.getColumnIndexOrThrow(_cursor, "streakValue");
          final int _cursorIndexOfUpdatedAtEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAtEpochMillis");
          final List<CalendarEntryEntity> _result = new ArrayList<CalendarEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntryEntity _item;
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final DayState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.toDayState(_tmp);
            final int _tmpStreakValue;
            _tmpStreakValue = _cursor.getInt(_cursorIndexOfStreakValue);
            final long _tmpUpdatedAtEpochMillis;
            _tmpUpdatedAtEpochMillis = _cursor.getLong(_cursorIndexOfUpdatedAtEpochMillis);
            _item = new CalendarEntryEntity(_tmpEpochDay,_tmpState,_tmpStreakValue,_tmpUpdatedAtEpochMillis);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getHistory(final long start, final long end,
      final Continuation<? super List<CalendarEntryEntity>> $completion) {
    final String _sql = "SELECT * FROM calendar_history WHERE epochDay BETWEEN ? AND ? ORDER BY epochDay ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CalendarEntryEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStreakValue = CursorUtil.getColumnIndexOrThrow(_cursor, "streakValue");
          final int _cursorIndexOfUpdatedAtEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAtEpochMillis");
          final List<CalendarEntryEntity> _result = new ArrayList<CalendarEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntryEntity _item;
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final DayState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.toDayState(_tmp);
            final int _tmpStreakValue;
            _tmpStreakValue = _cursor.getInt(_cursorIndexOfStreakValue);
            final long _tmpUpdatedAtEpochMillis;
            _tmpUpdatedAtEpochMillis = _cursor.getLong(_cursorIndexOfUpdatedAtEpochMillis);
            _item = new CalendarEntryEntity(_tmpEpochDay,_tmpState,_tmpStreakValue,_tmpUpdatedAtEpochMillis);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllHistory(final Continuation<? super List<CalendarEntryEntity>> $completion) {
    final String _sql = "SELECT * FROM calendar_history ORDER BY epochDay ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CalendarEntryEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStreakValue = CursorUtil.getColumnIndexOrThrow(_cursor, "streakValue");
          final int _cursorIndexOfUpdatedAtEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAtEpochMillis");
          final List<CalendarEntryEntity> _result = new ArrayList<CalendarEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntryEntity _item;
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final DayState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.toDayState(_tmp);
            final int _tmpStreakValue;
            _tmpStreakValue = _cursor.getInt(_cursorIndexOfStreakValue);
            final long _tmpUpdatedAtEpochMillis;
            _tmpUpdatedAtEpochMillis = _cursor.getLong(_cursorIndexOfUpdatedAtEpochMillis);
            _item = new CalendarEntryEntity(_tmpEpochDay,_tmpState,_tmpStreakValue,_tmpUpdatedAtEpochMillis);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEntry(final long epochDay,
      final Continuation<? super CalendarEntryEntity> $completion) {
    final String _sql = "SELECT * FROM calendar_history WHERE epochDay = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, epochDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CalendarEntryEntity>() {
      @Override
      @Nullable
      public CalendarEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStreakValue = CursorUtil.getColumnIndexOrThrow(_cursor, "streakValue");
          final int _cursorIndexOfUpdatedAtEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAtEpochMillis");
          final CalendarEntryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final DayState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.toDayState(_tmp);
            final int _tmpStreakValue;
            _tmpStreakValue = _cursor.getInt(_cursorIndexOfStreakValue);
            final long _tmpUpdatedAtEpochMillis;
            _tmpUpdatedAtEpochMillis = _cursor.getLong(_cursorIndexOfUpdatedAtEpochMillis);
            _result = new CalendarEntryEntity(_tmpEpochDay,_tmpState,_tmpStreakValue,_tmpUpdatedAtEpochMillis);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
