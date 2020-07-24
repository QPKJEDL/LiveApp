/*                                                                            
  Copyright (c) 2014-2019, GoBelieve     
    All rights reserved.		    				     			
 
  This source code is licensed under the BSD-style license found in the
  LICENSE file in the root directory of this source tree. An additional grant
  of patent rights can be found in the PATENTS file in the same directory.
*/


package com.beetle.bauhinia.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.beetle.bauhinia.db.message.File;

import java.beans.PropertyChangeEvent;
import com.beetle.bauhinia.db.IMessage;
import com.beetle.imkit.R;

public class MessageFileView extends MessageContentView {

    public MessageFileView(Context context) {
        super(context);
        int contentLayout = R.layout.chat_content_file;
        inflater.inflate(contentLayout, this);
    }

    public void setMessage(IMessage msg) {
        super.setMessage(msg);
        File fileMsg = (File) msg.content;

        String filename = fileMsg.filename;
        ImageView imageView = (ImageView)findViewById(R.id.image);
        if (filename.endsWith(".doc") || filename.endsWith("docx")) {
            imageView.setImageResource(R.drawable.word);
        } else if (filename.endsWith(".xls") || filename.endsWith(".xlsx")) {
            imageView.setImageResource(R.drawable.excel);
        } else if (filename.endsWith(".pdf")) {
            imageView.setImageResource(R.drawable.pdf);
        } else {
            imageView.setImageResource(R.drawable.file);
        }


        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(fileMsg.filename);

        TextView contentView = (TextView)findViewById(R.id.descreption);
        contentView.setText(formatSize(fileMsg.size));

        requestLayout();
    }


    private String formatSize(int size) {
        if (size < 1024) {
            return String.format("%d字节", size);
        } else if (size < 1024*1024){
            return String.format("%.1fKB",  size*1.0/1024);
        } else {
            return String.format("%.1fMB", size*1.0/(1024*1024));
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);

    }
}
