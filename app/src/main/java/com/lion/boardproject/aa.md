```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
        <variable
            name="rowReplyViewModel"
            type="com.lion.boardproject.viewmodel.RowReplyViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="10dp"
        android:transitionGroup="true">

        <TextView
            android:id="@+id/textViewRowCommentNickName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@={rowReplyViewModel.textViewRowCommentNickNameText}"
            android:textAppearance="@style/TextAppearance.AppCompat.Medium" />

        <TextView
            android:id="@+id/textViewRowCommentText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@={rowReplyViewModel.textViewRowCommentTextText}"
            android:textAppearance="@style/TextAppearance.AppCompat.Large" />

        <TextView
            android:id="@+id/textViewRowCommentDate"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@={rowReplyViewModel.textViewRowCommentDateText}"
            android:textAlignment="textEnd" />

    </LinearLayout>

</layout>

```