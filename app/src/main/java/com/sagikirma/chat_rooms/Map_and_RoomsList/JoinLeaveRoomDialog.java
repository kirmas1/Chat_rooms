package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sagikirma.chat_rooms.R;


public class JoinLeaveRoomDialog extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogOkClick(int location);
    }

    NoticeDialogListener mListener;
    Boolean join;
    int location;

    public static JoinLeaveRoomDialog newInstance(NoticeDialogListener n) {
        JoinLeaveRoomDialog joinLeaveRoomDialog = new JoinLeaveRoomDialog();
        joinLeaveRoomDialog.mListener = n;
        return joinLeaveRoomDialog;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        join = getArguments().getBoolean("T join F leave");
        location = getArguments().getInt("location");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_join_leave_room_dialog, null);
        builder.setView(v);

        TextView tv = (TextView) v.findViewById(R.id.join_leave_dialog_text);
        Button ok_button = (Button) v.findViewById(R.id.join_leave_dialog_ok_button);
        Button cancel_button = (Button) v.findViewById(R.id.join_leave_dialog_cancel_button);


        if (join) {
            tv.setText(R.string.join_room);
        } else tv.setText(R.string.leave_room);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogOkClick(location);
                dismiss();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }
}



